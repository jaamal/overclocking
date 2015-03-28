package patternMatching.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger implements ILogger {

    private PrintWriter output;
    private int recursionDepth;
    private long localSearchCalls;
    private long localSearchTotalTime;
    private long localSearchStartTime;
    private int maxRecursionDepth;
    private long buildStartTime;
    private long nonEmptyLocalSearchCount;
    private long localSearchLeafs;
    private long recursiveLSCalls;
    private long startAddTime;
    private long totalAddTime;

    public Logger(String filename) {
        recursionDepth = 0;
        localSearchCalls = 0;
        maxRecursionDepth = 0;
        buildStartTime = 0;
        localSearchTotalTime = 0;
        localSearchStartTime = 0;
        nonEmptyLocalSearchCount = 0;
        localSearchLeafs = 0;
        recursiveLSCalls = 0;
        totalAddTime = 0;

        try {
            this.output = new PrintWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void logMessage(String message) {
        output.append(message + '\n');
        output.flush();
    }

    @Override
    public void logLSearchCall() {
        localSearchCalls++;
        localSearchStartTime = System.currentTimeMillis();
        if (recursionDepth != 0) throw new RuntimeException("The end of previous local search call was not logged");
    }

    @Override
    public void logLSearchEnd() {
        //logMessage("Local search recursion depth = " + recursionDepth);
        localSearchTotalTime += System.currentTimeMillis() - localSearchStartTime;
        if (recursionDepth > maxRecursionDepth) maxRecursionDepth = recursionDepth;
        recursionDepth = 0;
    }

    @Override
    public void logLSearchRecursiveCall() {
        recursionDepth++;
        recursiveLSCalls++;
    }

    @Override
    public void logBuildStart() {
        buildStartTime = System.currentTimeMillis();
        // logMessage("ArrayBasedTable find started at " + buildStartTime);
    }

    @Override
    public void logBuildEnd() {
        logMessage("Total building time " + (System.currentTimeMillis() - buildStartTime) + " ms");
        logMessage("Total number of LocalSearchExecutor calls " + localSearchCalls + recursiveLSCalls);
        logMessage("Max recursion depth = " + maxRecursionDepth);
        logMessage("Total LocalSearchExecutor execution time " + localSearchTotalTime + " ms");
        logMessage("Number of empty LocalSearchExecutor creations " + (localSearchCalls - nonEmptyLocalSearchCount));
        logMessage("Number of LocalSearchExecutor calls with a terminal text " + localSearchLeafs);
        logMessage("Total time spent on adding arpr in LS result " + totalAddTime + " ms");
    }

    @Override
    public void logNonEmptyLSearchCreation() {
        nonEmptyLocalSearchCount++;
    }

    @Override
    public void logLSearchLeaf() {
        localSearchLeafs++;
    }

    @Override
    public void logAddStart() {
        startAddTime = System.currentTimeMillis();
    }

    @Override
    public void logAddFinish() {
        totalAddTime += System.currentTimeMillis() - startAddTime;
    }
}
