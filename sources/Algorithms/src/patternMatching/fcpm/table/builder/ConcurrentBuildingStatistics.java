package patternMatching.fcpm.table.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;

public final class ConcurrentBuildingStatistics implements IConcurrentBuildingStatistics {
    private final List<Long> mapBegins = new ArrayList<>();
    private final List<Long> mapEnds = new ArrayList<>();
    private final List<Long> reduceBegins = new ArrayList<>();
    private final List<Long> reduceEnds = new ArrayList<>();
    private final List<StopWatch> batchesWatches;
    private long beginTime;

    public ConcurrentBuildingStatistics(int batchesCount) {
        this.batchesWatches = new ArrayList<>(batchesCount);
        for (int batchIndex = 0; batchIndex < batchesCount; ++batchIndex) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            stopWatch.suspend();
            batchesWatches.add(stopWatch);
        }
    }

    @Override
    public void begin() {
        beginTime = getCurrentTime();
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    @Override
    public void beginSplit() {
        mapBegins.add(getCurrentTime());
    }

    @Override
    public void beginBatch(int batchIndex) {
        batchesWatches.get(batchIndex).resume();
    }

    @Override
    public void endBatch(int batchIndex) {
        batchesWatches.get(batchIndex).suspend();
    }

    @Override
    public void endSplit() {
        mapEnds.add(getCurrentTime());
    }

    @Override
    public void beginMerge() {
        reduceBegins.add(getCurrentTime());
    }

    @Override
    public void endMerge() {
        reduceEnds.add(getCurrentTime());
    }

    @Override
    public void end() {
        long endTime = getCurrentTime();
        System.out.println(String.format("Total time is %s", endTime - beginTime));
        int totalMapReduces = reduceBegins.size();
        long totalMapExecutionTime = 0;
        long totalReduceExecutionTime = 0;
        long maxMapExecutionTime = 0;
        long minMapExecutionTime = Long.MAX_VALUE;
        long maxReduceExecutionTime = 0;
        long minReduceExecutionTime = Long.MAX_VALUE;
        for(int i = 0; i < totalMapReduces; ++i) {
            long mapExecutionTime = mapEnds.get(i) - mapBegins.get(i);
            totalMapExecutionTime += mapExecutionTime;
            long reduceExecutionTime = reduceEnds.get(i) - reduceBegins.get(i);
            totalReduceExecutionTime += reduceExecutionTime;
            maxMapExecutionTime = Math.max(maxMapExecutionTime, mapExecutionTime);
            minMapExecutionTime = Math.min(minMapExecutionTime, mapExecutionTime);
            maxReduceExecutionTime = Math.max(maxReduceExecutionTime, reduceExecutionTime);
            minReduceExecutionTime = Math.min(minReduceExecutionTime, reduceExecutionTime);
        }
        long avgMapExecutionTime = totalMapExecutionTime / totalMapReduces;
        long avgReduceExecutionTime = totalReduceExecutionTime / totalMapReduces;
        System.out.println(String.format("Split: total=%s, avg=%s, min=%s, max=%s Merge: total=%s, avg=%s, min=%s, max=%s", totalMapExecutionTime, avgMapExecutionTime, minMapExecutionTime, maxMapExecutionTime, totalReduceExecutionTime, avgReduceExecutionTime, minReduceExecutionTime, maxReduceExecutionTime));
        for(int batchIndex = 0; batchIndex < batchesWatches.size(); ++batchIndex) {
            StopWatch stopWatch = batchesWatches.get(batchIndex);
            stopWatch.stop();
            System.out.println(String.format("Batch %s with time %s", batchIndex, stopWatch.toString()));
        }
    }
}
