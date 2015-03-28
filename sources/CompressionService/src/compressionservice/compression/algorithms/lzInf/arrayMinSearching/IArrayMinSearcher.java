package compressionservice.compression.algorithms.lzInf.arrayMinSearching;

/**
 * The interface for getting minimum number in
 * segment of array.
 */
public interface IArrayMinSearcher
{
    /**
     * Calculate the minimum number in segment
     *
     * @param startIndex the start position of segment
     * @param endIndex   the position AFTER end of segment
     * @return the minimum number in array from position
     *         startIndex to endIndex - 1.
     */
    public long getMin(long startIndex, long endIndex);
    void dispose();
}
