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
        extendSuffix("", symbol);
        
        text += symbol;
    }
    
    private void extendSuffix(String suffix, char symbol) {
        MovementResult movementResult = move(root, suffix);
        if (movementResult.currentNode.isLeaf()) {
            if (movementResult.lastPassedEdge == null) {
                movementResult.currentNode.addEdge(Edge.create(String.valueOf(symbol), new Node()));
            }
            else {
                movementResult.lastPassedEdge.extendLabel(symbol);
            }
        }
        else {
            if (movementResult.suffixTailLength == 0) {
                Edge edge = movementResult.currentNode.findEdge(symbol);
                if (edge == null) {
                    movementResult.currentNode.addEdge(Edge.create(String.valueOf(symbol), new Node()));
                } else {
                    if (edge.label.length() > 1) {
                        Node middleNode = new Node();
                        middleNode.addEdge(Edge.create(edge.label.substring(1), edge.to));
                        Edge prefixEdge = Edge.create(String.valueOf(symbol), middleNode);
                        movementResult.currentNode.changeEdge(edge, prefixEdge);
                    }
                }
            }
            else {
                Edge edge = movementResult.currentNode.findEdge(suffix.charAt(suffix.length() - movementResult.suffixTailLength));
                if (edge.label.charAt(movementResult.suffixTailLength) != symbol) {
                    Node middleNode = new Node();
                    middleNode.addEdge(Edge.create(String.valueOf(symbol), new Node()));
                    middleNode.addEdge(Edge.create(edge.label.substring(movementResult.suffixTailLength), edge.to));
                    Edge prefixEdge = Edge.create(edge.label.substring(0, movementResult.suffixTailLength), middleNode);
                    movementResult.currentNode.changeEdge(edge, prefixEdge);
                }
            }
        }
    }
    
    private MovementResult move(Node from, String suffix) {
        if (suffix == null || suffix.length() == 0)
            return new MovementResult(from, null, suffix.length());
        
        int suffixCurrentPosition = 0;
        Node currentNode = from;
        Edge lastPassedEdge = null;
        
        while (!currentNode.isLeaf() && suffixCurrentPosition < suffix.length()) {
            lastPassedEdge = currentNode.findEdge(suffix.charAt(suffixCurrentPosition));
            if (lastPassedEdge == null)
                throw new RuntimeException(String.format("We expect to read suffix %s but it was not found at tree.", suffix));

            if (lastPassedEdge.label.length() <= suffix.length() - suffixCurrentPosition) {
                suffixCurrentPosition += lastPassedEdge.label.length();
                currentNode = lastPassedEdge.to;
            }
            else {
                break;
            }
        }
        return new MovementResult(currentNode, lastPassedEdge, suffix.length() - suffixCurrentPosition);
    }

    @Override
    public ISuffixTree toSuffixTree()
    {
        return new SuffixTree(root);
    }
    
    private class MovementResult {
        
        public final Node currentNode;
        public final Edge lastPassedEdge;
        public final int suffixTailLength;
        
        public MovementResult(
                Node currentNode,
                Edge lastPassedEdge,
                int suffixTailLength) {
            this.currentNode = currentNode;
            this.lastPassedEdge = lastPassedEdge;
            this.suffixTailLength = suffixTailLength;
        }
    }
}
