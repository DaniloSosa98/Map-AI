import java.util.Scanner;

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
        }
        boolean stdOut= false;
        if(args[1].equals("-")){stdOut = true;}
    }
}
