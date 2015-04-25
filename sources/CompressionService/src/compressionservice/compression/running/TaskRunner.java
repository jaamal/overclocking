package compressionservice.compression.running;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.ICompressionStatesKeeper;
import compressionservice.compression.parameters.IRunParams;

import dataContracts.statistics.CompressionRunKeys;

public class TaskRunner implements ITaskRunner {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 32, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
    
    private ICompressionStatesKeeper compressionStatesKeeper;
    private IWorker worker;

    public TaskRunner(
            ICompressionStatesKeeper compressionStatesKeeper,
            IWorker worker) {
        this.compressionStatesKeeper = compressionStatesKeeper;
        this.worker = worker;
    }

    @Override
    public boolean isAvailable() {
        return threadPoolExecutor.getActiveCount() == 0;
    }

    @Override
    public CompressionRunnerState run(final IRunParams runParams) {
        if (!isAvailable())
            throw new TaskRunnerException("Compression runner is busy at now.");
        
        if (!runParams.contains(CompressionRunKeys.AlgorithmType))
            throw new TaskRunnerException("Unable to run compression algorithm since algorithm type parameter was not passed.");
        
        final CompressionRunnerState result = compressionStatesKeeper.registerNew();
        try {
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        worker.process(runParams);
                        compressionStatesKeeper.setComplete(result.requestId);
                    }
                    catch (Exception e) {
                        compressionStatesKeeper.setFailed(result.requestId, e.getMessage());
                    }
                }
            });
            return result;
        }
        catch (Exception e) {
            return compressionStatesKeeper.setFailed(result.requestId, e.getMessage());
        }
    }
}
