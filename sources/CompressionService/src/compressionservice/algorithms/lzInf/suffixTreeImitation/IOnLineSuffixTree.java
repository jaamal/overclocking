package compressionservice.algorithms.lzInf.suffixTreeImitation;

public interface IOnLineSuffixTree extends AutoCloseable
{
    /**      
     * @return Current state suffix tree navigator
     */
    public ISuffixTreeNavigator getNavigator();

    /**
     * Update state of suffix tree by adding to current prefix some symbols
     * @param charsCount count of adding symbols
     */
    public void addCharsToPrefix(long charsCount);

    /**
     * Update state of suffix tree
     * @param charsCount
     */
    public void removeCharsFromPrefix(long charsCount);

    public void close();
}
