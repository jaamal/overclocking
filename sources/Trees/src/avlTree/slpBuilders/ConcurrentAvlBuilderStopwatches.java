package avlTree.slpBuilders;

public class ConcurrentAvlBuilderStopwatches {
    public final Stopwatch totalStopwatch = new Stopwatch();
    public final Stopwatch findingLayersStopwatch = new Stopwatch();
    public final Stopwatch processingLayersStopwatch = new Stopwatch();
    public final Stopwatch mergeNeighbouringTreesStopwatch = new Stopwatch();
    public final Stopwatch minimizeTreeStopwatch = new Stopwatch();

    public final Stopwatch waitProcessingLayersStopwatch = new Stopwatch();
    public final Stopwatch waitMergeNeighbouringTreesStopwatch = new Stopwatch();


    public void printTimes() {
        System.out.println("Total Time: " + totalStopwatch.getElapsedSeconds());
        System.out.println("Finding layers. Time: " + findingLayersStopwatch.getElapsedSeconds());
        System.out.println("Processing layers. Time: " + processingLayersStopwatch.getElapsedSeconds());
        System.out.println("Wait processing layers. Time: " + waitProcessingLayersStopwatch.getElapsedSeconds());
        System.out.println("Merge neighbouring. Time: " + mergeNeighbouringTreesStopwatch.getElapsedSeconds());
        System.out.println("Wait merge neighbouring. Time: " + waitMergeNeighbouringTreesStopwatch.getElapsedSeconds());
        System.out.println("Minimize tree. Time: " + minimizeTreeStopwatch.getElapsedSeconds());
    }
}

