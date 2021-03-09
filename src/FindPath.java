/**
 * This class contains all the algorithms (BFS, DFS, A*), as well as
 * other helper methods to setup each run and output the results.
 * @author Danilo Sosa (dgs5678)
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.sqrt;

public class FindPath {
    private HashMap<String,Node> map;
    private String start, destination;

    public FindPath(HashMap<String, Node> graph, String start, String destination) {
        this.map = graph;
        this.start = start;
        this.destination = destination;
    }

    /**
     * This method receives 2 neighboring nodes which latitude and longitude values
     * are used to calculate the distance between them. We do this by using the given
     * formula from the project write-up.
     * @param n1
     * @param n2
     * @return
     */
    public double sld(Node n1, Node n2) {
        double lat1, lat2, lon1, lon2;
        lat1 = n1.getLat();
        lat2 = n2.getLat();
        lon1 = n1.getLon();
        lon2 = n2.getLon();
        double dist = sqrt( (lat1-lat2)*(lat1-lat2) + (lon1-lon2)*(lon1-lon2) ) * 100;
        return dist;
    }
    /**
     * The method goes through each node in the HashMap and sorts the
     * node's neighbors by ascending alphabetical order for BFS.
     */
    @SuppressWarnings("unchecked")
    public void prepBFS(){
        for (String n : map.keySet()) {
            Collections.sort(map.get(n).getNeighbors(), new ascending());
        }
    }
    /**
     * The method goes through each node in the HashMap and sorts the
     * node's neighbors by descending alphabetical order for DFS.
     */
    @SuppressWarnings("unchecked")
    public void prepDFS(){
        for (String n : map.keySet()) {
            Collections.sort(map.get(n).getNeighbors(), new descending());
        }
    }

    /**
     * This method was used to compare Nodes instead of strings
     * however after a change I decided to keep it even though it compares
     * exactly like Collections.sort
     */
    class ascending implements Comparator<String> {
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    /**
     * Instead of running a reverse on an ascending ordered list,
     * this comparator basically saves up a step. Instead of traversing
     * twice we do it once.
     */
    class descending implements Comparator<String> {
        public int compare(String a, String b) {
            return b.compareTo(a);
        }
    }

    /**
     * This method is used to traceback the route.
     * It does it by receiving the destination node and going
     * through the parent nodes until the current node is
     * the start node.
     * @param node
     * @return
     */
    public ArrayList<String> getRoute(String node){
        ArrayList<String> route = new ArrayList<>();
        route.add(node); //add destination node to route
        String curr = node; //store current node
        if (node.equals(start)){//if the destination node is the start node
            return route;
        }
        curr = map.get(curr).getParent(); //set current as the parent of the last current
        route.add(curr);//add current node to the route
        while(!curr.equals(start)){//repeat same lines from above until we get to the start node
            curr = map.get(curr).getParent();
            route.add(curr);
        }
        Collections.reverse(route);//since we are getting the route backwards, we reverse it
        return route;
    }

    /**
     * This BFS is based off the pseudocode from the Week03-Search slides
     * However, instead of having a list of closed nodes, we set their
     * visited boolean as true.
     */
    public void BFS(){
        Queue<Node> open = new LinkedList<>();
        prepBFS();//call method to sort neighbors in asc order
        open.add(map.get(start)); //initialize the open queue
        while(!open.isEmpty()){//while there are states remaining
            Node X = open.poll();//remove leftmost state from open, call it X
            if (X.getCity().equals(destination)){//if X is the goal
                return;//return success
            }else{
                X.setVisited(true);//mark X as visited
                ArrayList<String> neighbors = X.getNeighbors();//generate children of X
                for (String neighbor : neighbors) {//go through all children
                    if (neighbor.equals(destination)) {//if any child of X is goal then return
                        map.get(neighbor).setParent(X.getCity());//used to be able to traceback route
                        return;
                    }
                    else if(!map.get(neighbor).isVisited()){//if child is not visited
                        map.get(neighbor).setParent(X.getCity());//to traceback route
                        map.get(neighbor).setVisited(true);//set child as visited
                        open.add(map.get(neighbor));//put remaining children on right end of open
                    }
                }
            }
        }
    }
    /**
     * This prints the results of BFS to stdout with the exact format of
     * the sample outputs.
     */
    public void outputBFS(){
        double distance = 0;
        System.out.println();
        System.out.println("Breadth-First Search Results: ");
        ArrayList <String> route = getRoute(destination);
        for(int i = 0; i < route.size()-1; i++){
            System.out.println(route.get(i));
            distance += sld(map.get(route.get(i)), map.get(route.get(i+1)));
        }
        System.out.println(route.get(route.size()-1));
        System.out.println("That took " + (route.size()-1) + " hops to find.");
        System.out.println("Total distance = " + Math.round(distance) +" miles.");
        reset();
    }
    /**
     * This receives the output file name and writes to it.
     * If it already exists it will overwrite it, if does not exist it will be
     * created.
     * @param file
     */
    public void outputBFS(String file){
        double distance = 0;
        try {
            FileWriter fw = new FileWriter("./"+file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write("Breadth-First Search Results: ");
            bw.newLine();
            ArrayList <String> route = getRoute(destination);
            for(int i = 0; i < route.size()-1; i++){
                bw.write(route.get(i)+"\n");
                distance += sld(map.get(route.get(i)), map.get(route.get(i+1)));
            }
            bw.write(route.get(route.size()-1)+"\n");
            bw.write("That took " + (route.size()-1) + " hops to find.\n");
            bw.write("Total distance = " + Math.round(distance) +" miles.\n");
            bw.close();
            reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This DFS is based off the pseudocode from the Week03-Search slides
     * However, instead of having a list of closed nodes, we set their
     * visited boolean as true.
     */
    public void DFS(){
        Stack<Node> open = new Stack<>();
        prepDFS();//call method to sort neighbors in des order
        open.add(map.get(start));//initialize the open stack
        while(!open.isEmpty()){//while there are states remaining
            Node X = open.pop();//remove leftmost state from open, call it X
            if(X.getCity().equals(destination)){//if X is the goal
                return;//return success
            }else{
                X.setVisited(true);//mark X as visited
                ArrayList<String> neighbors = X.getNeighbors();//generate children of X
                for (String neighbor : neighbors) {//go through all children
                    if(neighbor.equals(destination)){//if any child of X is goal then return
                        map.get(neighbor).setParent(X.getCity());//used to be able to traceback route
                        return;
                    }else if(!map.get(neighbor).isVisited()){//if child is not visited
                        map.get(neighbor).setParent(X.getCity());//to traceback route
                        map.get(neighbor).setVisited(true);//set child as visited
                        open.add(map.get(neighbor));//put remaining children on left end of open
                    }
                }
            }
        }
    }
    /**
     * This prints the results of DFS to stdout with the exact format of
     * the sample outputs.
     */
    public void outputDFS(){
        double distance = 0;
        System.out.println();
        System.out.println("Depth-First Search Results:");
        ArrayList <String> route = getRoute(destination);
        for(int i = 0; i < route.size()-1; i++){
            System.out.println(route.get(i));
            distance += sld(map.get(route.get(i)), map.get(route.get(i+1)));
        }
        System.out.println(route.get(route.size()-1));
        System.out.println("That took " + (route.size()-1) + " hops to find.");
        System.out.println("Total distance = " + Math.round(distance) + " miles.");
        reset();
    }
    /**
     * This receives the output file name and writes to it.
     * Since the file was created and written-to by BFS output, the DFS
     * output will be appended.
     * @param file
     */
    public void outputDFS(String file){
        double distance = 0;
        try {
            FileWriter fw = new FileWriter("./"+file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.newLine();
            bw.write("Depth-First Search Results: ");
            bw.newLine();
            ArrayList <String> route = getRoute(destination);
            for(int i = 0; i < route.size()-1; i++){
                bw.write(route.get(i)+"\n");
                distance += sld(map.get(route.get(i)), map.get(route.get(i+1)));
            }
            bw.write(route.get(route.size()-1)+"\n");
            bw.write("That took " + (route.size()-1) + " hops to find.\n");
            bw.write("Total distance = " + Math.round(distance) +" miles.\n");
            bw.close();
            reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is similar to what we do in the output of each algorithm,
     * we are finding g(n) = distance from the start node to the current node
     * @param node
     * @return
     */
    public double routeWeight(String node){
        double weight = 0;
        ArrayList<String> route = getRoute(node);
        for(int i = 0; i < route.size()-1; i++){
            weight += sld(map.get(route.get(i)), map.get(route.get(i+1)));
        }
        return weight;
    }

    /**
     * Like the other prep methods for BFS and DFS, this one sorts
     * the neighbors by their f(n) = estimated total cost of path through n to goal
     * @param routes
     * @param node
     * @return
     */
    public ArrayList<Node> prepAstar(ArrayList<Node> routes, Node node){
        int size = routes.size();
        for(int i = 0; i < routes.size(); i++){
            if(node.getSld() < routes.get(i).getSld()){
                routes.add(i, node);
                break;
            }
        }
        if(routes.size() == size)
            routes.add(node);
        return routes;
    }

    /**
     * We didn't get an A* pseudocode I tried to follow a similar
     * format as the one we had for BFS and DFS.
     */
    public void Astar(){
        ArrayList<Node> open = new ArrayList<>();
        //Start by calculating the f(n) of the start node
        map.get(start).setSld(routeWeight(start) + sld(map.get(destination), map.get(start)));
        open = prepAstar(open, map.get(start));//initialize the open list
        while(!open.isEmpty()){//while there are states remaining
            Node X = open.get(0);//get leftmost state from open, call it X
            open.remove(0);//remove leftmost state from open
            if(X.getCity().equals(destination)){//if X is the goal
                return;//return success
            }else {
                X.setVisited(true);//mark X as visited
                ArrayList<String> neighbors = X.getNeighbors();//generate children of X
                for (String neighbor : neighbors) {//go through all children
                    if (neighbor.equals(destination)) {//if any child of X is goal then return
                        map.get(neighbor).setParent(X.getCity());//used to be able to traceback route
                        return;
                    }
                    else if(!map.get(neighbor).isVisited()){//if child is not visited
                        map.get(neighbor).setParent(X.getCity());//to traceback route
                        map.get(neighbor).setVisited(true);//set child as visited
                        map.get(start).setSld(routeWeight(neighbor) + sld(map.get(destination), map.get(neighbor)));//update f(n)
                        open = prepAstar(open, map.get(neighbor));//put remaining children on left end of open

                    }
                }
            }
        }
    }

    /**
     * This prints the results of A* to stdout with the exact format of
     * the sample outputs.
     */
    public void outputAstar(){
        double distance = 0;
        System.out.println();
        System.out.println("A* Search Results: ");
        ArrayList <String> path = getRoute(destination);
        for(int i = 0; i < path.size()-1; i++){
            System.out.println(path.get(i));
            distance += sld(map.get(path.get(i)), map.get(path.get(i+1)));
        }
        System.out.println(path.get(path.size()-1));
        System.out.println("That took " + (path.size()-1) + " hops to find.");
        System.out.println("Total distance = " + Math.round(distance) +" miles.");
    }
    /**
     * This receives the output file name and writes to it.
     * Since the file was created and written-to by BFS output, the A*
     * output will be appended.
     * @param file
     */
    public void outputAstar(String file){
        double distance = 0;
        try {
            FileWriter fw = new FileWriter("./"+file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.newLine();
            bw.write("A* Search Results: ");
            bw.newLine();
            ArrayList <String> route = getRoute(destination);
            for(int i = 0; i < route.size()-1; i++){
                bw.write(route.get(i)+"\n");
                distance += sld(map.get(route.get(i)), map.get(route.get(i+1)));
            }
            bw.write(route.get(route.size()-1)+"\n");
            bw.write("That took " + (route.size()-1) + " hops to find.\n");
            bw.write("Total distance = " + Math.round(distance) +" miles.\n");
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method sets all nodes visited boolean as false.
     * It is called after BFS and DFS.
     */
    public void reset(){
        for (String n : map.keySet()) {
            map.get(n).setVisited(false);
        }
    }
}
