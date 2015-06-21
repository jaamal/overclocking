package compressionservice.algorithms.lcaOnlineSlp;

import avlTree.slpBuilders.SLPBuilder;
import data.charArray.IReadableCharArray;

public interface ILCAOnlineCompressor
{
    SLPBuilder buildSLP(IReadableCharArray text);
}
