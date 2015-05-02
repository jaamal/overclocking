package compressionservice.runner;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.state.ITaskStatesStorage;
import compressionservice.runner.state.TaskStateModel;
import dataContracts.statistics.RunParamKeys;

public class TaskRunner implements ITaskRunner {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 32, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
    
    private ITaskStatesStorage statesStorage;
    private IWorker worker;

    public TaskRunner(
            ITaskStatesStorage statesStorage,
            IWorker worker) {
        this.statesStorage = statesStorage;
        this.worker = worker;
    }

    @Override
    public boolean isAvailable() {
        return threadPoolExecutor.getActiveCount() == 0;
    }

    @Override
    public TaskStateModel run(final IRunParams runParams) {
        if (!isAvailable())
            throw new TaskRunnerException("Compression runner is busy at now.");
        
        if (!runParams.contains(RunParamKeys.AlgorithmType))
            throw new TaskRunnerException("Unable to run compression algorithm since algorithm type parameter was not passed.");
        
        final UUID requestId = statesStorage.registerNew();
        try {
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        worker.process(requestId, runParams);
                        statesStorage.setComplete(requestId);
                    }
                    catch (Exception e) {
                        statesStorage.setFailed(requestId, e.getMessage());
                    }
                }
            });
            return statesStorage.getState(requestId);
        }
        catch (Exception e) {
            return statesStorage.setFailed(requestId, e.getMessage());
        }
    }
}
