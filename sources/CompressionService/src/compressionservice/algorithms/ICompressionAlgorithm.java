package compressionservice.algorithms;

public interface ICompressionAlgorithm extends IAlgorithm
{
    byte[] getCompressedRepresentation();
}
