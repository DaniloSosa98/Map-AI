/**
 * Search is the main program in which inputs are managed either through files
 * or trough standard input. After getting all data from city.dat and edge.dat,
 * which is stored in a HashMap, we proceed to call the PathFind algorithms, which
 * are BFS, DFS and A*.
 * The output will either go to a file or to standard output.
 * @author Danilo Sosa (dgs5678)
 * */

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Search {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int argSize = args.length;
        //Check if the program was given 2 arguments
        if (argSize != 2){
            System.err.println("Usage: java Search inputFile outputFile");
            System.exit(0);
        }
        //Declare necessary variables to store input information
        String start = "", destination = "", inputF = "", outputF = "";
        //If the first argument is '-' the start and destination cities will come from stdin
        if(args[0].equals("-")){
            start = sc.nextLine();
            destination = sc.nextLine();
        }else{
            //Else the cities will come from an input file,name stored below
            inputF = args[0];
            //read file and if failed to do so an error message will print and program will be terminated
            try {
                FileReader in = new FileReader("./"+inputF);
                BufferedReader bufferedReader = new BufferedReader(in);
                start = bufferedReader.readLine();
                destination = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                System.err.println("File not found: "+inputF);
                System.exit(0);
            } catch (IOException e) {
                System.exit(0);
            }
        }
        //this boolean would let us know if the output would go to a file or to stdout
        boolean stdOut= false;
        if(args[1].equals("-")){
            stdOut = true;
        }else{
            outputF = args[1];
        }
        //HashMap to store cities, key = city name : value = city node
        HashMap<String, Node> nodes = new HashMap<>();
        //Now we proceed to read and store the cities from city.dat
        try {
            FileReader cities = new FileReader("./city.dat");
            BufferedReader bufferedReader = new BufferedReader(cities);
            String line;
            //go through every line and store city, state, latitude and longitude.
            while((line = bufferedReader.readLine()) != null){
                StringTokenizer st = new StringTokenizer(line);
                Node n = new Node(st.nextToken(), st.nextToken(), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
                nodes.put(n.getCity(), n);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: city.dat");
            System.exit(0);
        } catch (IOException e) {
            System.exit(0);
        }
        //Now we go through the edge.dat file to store the neighbors each city has
        try {
            FileReader edges = new FileReader("./edge.dat");
            BufferedReader bufferedReader = new BufferedReader(edges);
            String line;
            int count = 0;
            while((line = bufferedReader.readLine()) != null){
                if(count >= 2){ //we skip the first 2 lines since they are blank
                    StringTokenizer st = new StringTokenizer(line);
                    String n1 = st.nextToken();
                    String n2 = st.nextToken();
                    //since it is an undirected graph, if A con go to B, B can go to A
                    nodes.get(n1).getNeighbors().add(n2);
                    nodes.get(n2).getNeighbors().add(n1);
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: edge.dat");
            System.exit(0);
        } catch (IOException e) {
            System.exit(0);
        }
        //Check if start city exists, if it doesn't print error and exit
        if(nodes.get(start)==null){
            System.err.println("No such city: "+start);
            System.exit(0);
        }
        //Check if destination city exists, if it doesn't print error and exit
        if(nodes.get(destination)==null){
            System.err.println("No such city: "+destination);
            System.exit(0);
        }
        //Calling the searching algorithms
        FindPath fp = new FindPath(nodes, start, destination);
        if (stdOut){
            fp.BFS();
            fp.outputBFS();

            fp.DFS();
            fp.outputDFS();

            fp.Astar();
            fp.outputAstar();
        }else{
            File f = new File("./" + outputF);
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fp.BFS();
            fp.outputBFS(outputF);

            fp.DFS();
            fp.outputDFS(outputF);

            fp.Astar();
            fp.outputAstar(outputF);
        }
    }
}
