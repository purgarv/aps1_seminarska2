import java.io.*;

public class Naloga6{
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            StringBuilder str = new StringBuilder();


            String line = br.readLine();

            String[] l = line.split(" ");



            







            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }

    }
}

class Node{
    String data;
    Node left,right;

    public Node(String data){
        this.data = data;
        left = right = null;
    }

    void printPreorder(Node node, ){

        if (node == null)
            return ;
 
        /* first print data of node */
        System.out.print(node.data + ",");
 
        /* then recur on left subtree */
        printPreorder(node.left, );
 
        /* now recur on right subtree */
        printPreorder(node.right, );
    }

}