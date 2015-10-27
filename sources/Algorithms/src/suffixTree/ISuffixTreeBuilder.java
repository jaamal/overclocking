package suffixTree;

public interface ISuffixTreeBuilder
{
    void append(char symbol);
    
    default void append(String str) {
        for (int i = 0; i < str.length(); i++) {
            append(str.charAt(i));
        }
    }
    
    ISuffixTree toSuffixTree();
}
