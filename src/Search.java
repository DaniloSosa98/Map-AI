import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Search {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int argSize = args.length;
        if (argSize != 2){
            System.err.println("Usage: java Search inputFile outputFile");
            System.exit(0);
        }
        String start = "", end = "", inputF = "", outputF = "";
        if(args[0].equals("-")){
            System.out.println("Enter the cities:");
            start = sc.nextLine();
            end = sc.nextLine();
        }else{
            inputF = args[0];
            try {
                FileReader in = new FileReader("./"+inputF);
                BufferedReader bufferedReader = new BufferedReader(in);
                start = bufferedReader.readLine();
                end = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                System.err.println("File not found: "+inputF);
                System.exit(0);
            } catch (IOException e) {
                System.exit(0);
            }
        }
        boolean stdOut= false;
        if(args[1].equals("-")){stdOut = true;}
        HashMap<String, Node> nodes = new HashMap<>();
        try {
            FileReader cities = new FileReader("./city.dat");
            BufferedReader bufferedReader = new BufferedReader(cities);
            String line;

            while((line = bufferedReader.readLine()) != null){
                StringTokenizer st = new StringTokenizer(line);
                Node n = new Node(st.nextToken(), st.nextToken(), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
                nodes.put(n.getName(), n);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: city.dat");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Graph map = new Graph(nodes);
        try {
            FileReader edges = new FileReader("./edge.dat");
            BufferedReader bufferedReader = new BufferedReader(edges);
            String line;
            int count = 0;
            while((line = bufferedReader.readLine()) != null){
                if(count >= 2){
                    StringTokenizer st = new StringTokenizer(line);
                    map.addEdge(nodes, st.nextToken(), st.nextToken());
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: edge.dat");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
