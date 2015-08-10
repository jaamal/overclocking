package compressionservice.algorithms.lcaOnlineSlp;

import data.charArray.IReadableCharArray;
import productEnumerator.ProductEnumerator;

public interface ILCAOnlineCompressor
{
    ProductEnumerator buildSLP(IReadableCharArray text);
}
