import java.io.*;
import java.util.*;

public class Naloga8{
    public static void main(String[] args) {
        
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            HashMap<Integer, Node> nodes = new HashMap<>();
            HashSet<Integer> leftRight = new HashSet<>();  

            String line = "";
            int n = Integer.parseInt(br.readLine());
            int[] ids = new int[n];   
            String[] temp;

            for(int i = 0; i < n && (line = br.readLine()) != null; i++){
                temp = line.split(",");

                leftRight.add(Integer.parseInt(temp[2]));
                leftRight.add(Integer.parseInt(temp[3]));

                ids[i] = Integer.parseInt(temp[0]);

                nodes.put(Integer.parseInt(temp[0]), 
                new Node(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]) ) );  // id, node(value)
            }
            int start = 0;

            for(int i = 0; i < ids.length; i++){
                if(!leftRight.contains(ids[i])){
                    start = ids[i];
                    break;
                }
            }

            
            Node root = buildTree(start, nodes);

            
            dolociX(root, 0);
            dolociY(root, 0);

            p.print(print(root));
            

            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }


    }
    public static Node buildTree(int start, HashMap<Integer, Node> nodes){
        Queue<Node> q = new LinkedList<>();

        Node root = nodes.get(start);
        q.add(root);

        while(!q.isEmpty()){
            Node current = q.remove();

            if(current.idl != -1){
                current.left = nodes.get(current.idl);
                q.add(current.left);
            }
            if(current.idd != -1){
                current.right = nodes.get(current.idd);
                q.add(current.right);
            }

        }

        return root;
    }

    public static int dolociX(Node node, int x){

        if(node == null)
            return x;
        
        
        int left = dolociX(node.left, x);

        node.x = left;
        
        int right = dolociX(node.right, left + 1);
        
        return right;

    }

    public static void dolociY(Node node, int y){

        if(node == null)
            return;

        node.y = y;

        dolociY(node.left, y + 1);
        dolociY(node.right, y + 1);

    }

    public static String print(Node root){
        StringBuilder a = new StringBuilder();

        Queue<Node> q = new LinkedList<>();

        q.add(root);
        
        while (q.size() > 0){
            Node n = q.remove();
            a.append(n.v).append(",").append(n.x).append(",").append(n.y).append("\n");

            if (n.left !=null)
                q.add(n.left);
            
            if (n.right !=null)
                q.add(n.right);
        }

        return a.toString();

    }
}

class Node{
    int id;
    int v;
    int idl;
    int idd;
    Node left;
    Node right;

    int x;
    int y;

    public Node(int id, int v, Node left, Node right){

        this.id = id;
        this.v = v;
        this.left = left;
        this.right = right;

    }


    public Node(int id, int v, int idl, int idd){
        this.id = id;
        this.v = v;
        this.idl = idl;
        this.idd = idd;
    }
    

    public Node(int id){
        this.id = id;
    }

}