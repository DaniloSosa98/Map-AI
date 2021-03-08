public class Node {
    private String name, state;
    private double lat, lon, dist, sld;

    public Node() {
    }

    public Node(String name, String state, double lat, double lon) {
        this.name = name;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
        //this.sld = sld;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getSld() {
        return sld;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public void setSld(double sld) {
        this.sld = sld;
    }
}
