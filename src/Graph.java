import java.util.*;

import static java.lang.Math.sqrt;

public class Graph {
    private HashMap<String,LinkedList<Node>> adj;

    // Constructor
    Graph(HashMap<String,Node> nodes) {
        adj = new HashMap<>();
        for (Map.Entry n: nodes.entrySet()) {
            adj.put((String)n.getKey(), new LinkedList<>());
        }
    }

    // Function to add an edge into the graph
    void addEdge(HashMap<String, Node> nodes, String n1, String n2) {
        double lat1, lat2, lon1, lon2;
        Node temp1 = nodes.get(n1);
        Node temp2 = nodes.get(n2);
        lat1 = temp1.getLat();
        lat2 = temp2.getLat();
        lon1 = temp1.getLon();
        lon2 = temp2.getLon();
        double dist = sqrt( (lat1-lat2)*(lat1-lat2) + (lon1-lon2)*(lon1-lon2) ) * 100;
        temp1.setDist(dist);
        temp2.setDist(dist);
        adj.get(n1).add(temp2);
        adj.get(n2).add(temp1);
    }
@SuppressWarnings("unchecked")
    void showAdj(){
        for (Map.Entry n : adj.entrySet()) {
            System.out.println();
            System.out.println(n.getKey() + " neighbors:");
            for (Node node: (LinkedList<Node>)n.getValue()) {
                System.out.println(node.getName());
            }
        }
    }
}
