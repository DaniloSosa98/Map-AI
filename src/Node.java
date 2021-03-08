/**
 * This class is used to represent a city as a node in the map.
 * The parent is used to trace back the path.
 * Since it is an undirected graph loops/cycles
 * can be an issue, so in order to know if a node has been visited we have a boolean (it will reset to false
 * after each search).
 * The arraylist neighbors is the adjacency list for each node.
 * sld is the straight-line-distance heuristic we have seen in the slides.
 * @author Danilo Sosa (dgs5678)
 */

import java.util.ArrayList;

public class Node {
    private String city, state, parent;
    private double lat, lon, sld;
    private boolean visited;
    private ArrayList<String> neighbors;

    public Node() {
    }

    public Node(String name, String state, double lat, double lon) {
        this.city = name;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
        visited = false;
        neighbors = new ArrayList<>();
    }

    protected String getCity() {
        return city;
    }

    protected String getParent() {
        return parent;
    }

    protected double getLat() {
        return lat;
    }

    protected double getLon() {
        return lon;
    }

    protected double getSld() {
        return sld;
    }

    protected boolean isVisited() {
        return visited;
    }

    protected ArrayList<String> getNeighbors() {
        return neighbors;
    }

    protected void setSld(double sld) {
        this.sld = sld;
    }

    protected void setVisited(boolean visited) {
        this.visited = visited;
    }

    protected void setParent(String parent) {
        this.parent = parent;
    }
}
