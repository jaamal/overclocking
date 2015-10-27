package suffixTree;

public class Edge
{
    public String label;
    public Node to;
    
    private Edge(String label, Node to) {
        this.label = label;
        this.to = to;
    }
    
    public void extendLabel(char symbol) {
        label += symbol;
    }
    
    public static Edge create(String label, Node to) {
        if (label == null || label.length() == 0)
            throw new RuntimeException("Fail to create Edge with empty label.");
        if (to == null)
            throw new RuntimeException("Fail to create Edge that leads to null Node.");
        
        return new Edge(label, to);
    }
    
}
