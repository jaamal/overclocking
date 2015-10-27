package suffixTree;

public class SuffixTreeBuilder implements ISuffixTreeBuilder
{
    private String text;
    private Node root;
    
    public SuffixTreeBuilder() {
        this.text = "";
        this.root = new Node();
    }
    
    public void append(char symbol) {
        for (int i = 0; i < text.length(); i++) {
            extendSuffix(text.substring(i), symbol);
        }
        
        if (root.findEdge(symbol) == null) {
            root.addEdge(Edge.create(String.valueOf(symbol), new Node()));
        }
        else {
            Edge edge = root.findEdge(symbol);
            if (edge.label.length() > 1) {
                Node middleNode = new Node();
                middleNode.addEdge(Edge.create(edge.label.substring(1), edge.to));
                Edge prefixEdge = Edge.create(String.valueOf(symbol), middleNode);
                root.changeEdge(edge, prefixEdge);
            }
        }
        
        text += symbol;
    }
    
    private void extendSuffix(String suffix, char symbol) {
        int suffixCurrentPosition = 0;
        Node currentNode = root;
        
        while (suffixCurrentPosition < suffix.length()) {
            Edge edge = currentNode.findEdge(suffix.charAt(suffixCurrentPosition));
            if (edge == null)
                throw new RuntimeException(String.format("We expect to read suffix %s but it was not found at tree.", suffix));
            
            if (edge.label.length() == suffix.length() - suffixCurrentPosition) {
                if (edge.to.isLeaf()) {
                    edge.extendLabel(symbol);
                }
                else {
                    currentNode = edge.to;
                    edge = currentNode.findEdge(symbol);
                    if (edge == null) {
                        currentNode.addEdge(Edge.create(String.valueOf(symbol), new Node()));
                    }
                }
                return;
            }
            else if (edge.label.length() < suffix.length() - suffixCurrentPosition) {
                suffixCurrentPosition += edge.label.length();
                currentNode = edge.to;
            }
            else {
                if (edge.label.charAt(suffix.length() - suffixCurrentPosition) != symbol) {
                    Node middleNode = new Node();
                    middleNode.addEdge(Edge.create(String.valueOf(symbol), new Node()));
                    middleNode.addEdge(Edge.create(edge.label.substring(suffix.length() - suffixCurrentPosition), edge.to));
                    Edge prefixEdge = Edge.create(edge.label.substring(0, suffix.length() - suffixCurrentPosition), middleNode);
                    currentNode.changeEdge(edge, prefixEdge);
                }
                return;
            }
        }
        
    }

    @Override
    public ISuffixTree toSuffixTree()
    {
        return new SuffixTree(root);
    }
}
