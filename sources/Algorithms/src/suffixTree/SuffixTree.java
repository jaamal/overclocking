package suffixTree;

public class SuffixTree implements ISuffixTree
{
    private final Node root;

    public SuffixTree(Node root) {
        this.root = root;
    }
    
    @Override
    public boolean contains(String substring)
    {
        int currentSuffixPosition = 0;
        
        Edge currentEdge = root.findEdge(substring.charAt(currentSuffixPosition));
        while (currentEdge != null) {
            if (currentEdge.label.length() >= substring.length() - currentSuffixPosition) {
                for (int i = 0; i < substring.length() - currentSuffixPosition; i++) {
                    if (currentEdge.label.charAt(i) != substring.charAt(currentSuffixPosition + i))
                        return false;
                }
                return true;
            }
            else {
                for (int i = 0; i < currentEdge.label.length(); i++) {
                    if (currentEdge.label.charAt(i) != substring.charAt(currentSuffixPosition + i))
                        return false;
                }
                currentSuffixPosition += currentEdge.label.length();
                currentEdge = currentEdge.to.findEdge(substring.charAt(currentSuffixPosition));
            }
        }
        return false;
    }
}
