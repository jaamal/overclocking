package compressionservice.compression.algorithms.lcaOnlineSlp;

import avlTree.slpBuilders.SLPBuilder;
import compressingCore.dataAccess.IReadableCharArray;

public interface ILCAOnlineCompressor
{
    SLPBuilder buildSLP(IReadableCharArray text);
}
