package compressionservice.compression.running;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.ICompressionStatesKeeper;
import compressionservice.compression.parameters.IRunParams;

import dataContracts.AlgorithmType;
import dataContracts.statistics.CompressionRunKeys;

public class CompressionRunner implements ICompressionRunner {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 32, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
    private ITypedCompressionRunner[] runners;
    private ICompressionStatesKeeper compressionStatesKeeper;

    public CompressionRunner(ITypedCompressionRunner[] runners, ICompressionStatesKeeper compressionStatesKeeper) {
        this.runners = runners;
        this.compressionStatesKeeper = compressionStatesKeeper;
    }

    @Override
    public boolean isAvailable() {
        return threadPoolExecutor.getActiveCount() == 0;
    }

    @Override
    public CompressionRunnerState run(final IRunParams runParams) {
        if (!isAvailable())
            throw new CompresionRunnerException("Compression runner is busy at now.");
        
        if (!runParams.contains(CompressionRunKeys.AlgorithmType))
            throw new CompresionRunnerException("Unable to run compression algorithm since algorithm type parameter was not passed.");
        
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, CompressionRunKeys.AlgorithmType);
        for (int i = 0; i < runners.length; i++) {
            if (runners[i].getAlgorithmType() == algorithmType) {
                final ITypedCompressionRunner runner = runners[i];
                final CompressionRunnerState result = compressionStatesKeeper.registerNew();
                
                CheckParamsResult checkParamsResult = runners[i].checkAndRefillParams(runParams);
                if (checkParamsResult.state == CheckParamsResult.State.failed)
                    return compressionStatesKeeper.setFailed(result.requestId, result.message);
                
                try {
                    threadPoolExecutor.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                runner.run(runParams);
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
        throw new CompresionRunnerException(String.format("Runner for algorithm type %s was not found.", runParams.toString()));
    }

}
