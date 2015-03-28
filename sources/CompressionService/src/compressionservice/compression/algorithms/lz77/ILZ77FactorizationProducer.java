package compressionservice.compression.algorithms.lz77;

import compressingCore.dataAccess.IReadableCharArray;

public interface ILZ77FactorizationProducer
{
    void run(String statisticsId, IReadableCharArray source, int windowSize);
}
