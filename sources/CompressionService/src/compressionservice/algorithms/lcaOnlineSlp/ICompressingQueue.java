package compressionservice.algorithms.lcaOnlineSlp;

public interface ICompressingQueue {

    void insertSymbol(long symbol);

    void postProcessingRemain();
}
