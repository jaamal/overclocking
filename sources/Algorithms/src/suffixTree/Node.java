package suffixTree;

import java.util.HashMap;

public class Node
{
    HashMap<Character, Edge> edges;
    
    public Node() {
        edges = new HashMap<>();
    }
    
    public Edge addEdge(Edge edge) {
        edges.put(edge.label.charAt(0), edge);
        return edge;
    }
    
    public boolean isLeaf() {
        return edges.size() == 0;
    }
    
    public Edge findEdge(char symbol) {
        return edges.containsKey(symbol) 
                ? edges.get(symbol) 
                : null;
    }
    
    public void changeEdge(Edge from, Edge to) {
        char firstSymbol = from.label.charAt(0);
        if (!(edges.containsKey(firstSymbol) && edges.get(firstSymbol) == from)) 
            throw new RuntimeException(String.format("Fail to change edge since edge with label %s was not found.", from.label));
            
        edges.remove(firstSymbol);
        addEdge(to);
    }
}
