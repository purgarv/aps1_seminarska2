import java.io.*;
import java.util.*;

public class Naloga6{
    public static StringBuilder str = new StringBuilder();
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);

            String line = br.readLine();

            String[] exp = line.replace("(", "( ").replace(")", " )").split(" ");

            String [] out = infixtopostfix(exp).toArray(new String[0]);
            
            
            Node r = buildTree(out);

            printPreorder(r);
            
            p.print(str.substring(0,str.length()-1));
            p.print("\n");
            p.print(depth(r));


            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }

    }
    
    public static Node buildTree(String[] exp){
        Stack<Node> s = new Stack();
        Node t1, t2, temp;


        for(int i = 0; i < exp.length; i++){
            switch(exp[i]){
                case "NOT":
                    temp = new Node(exp[i]);
                    t1 = s.pop();
                    temp.left = t1;
                    s.push(temp);
                    break;

                case "AND":
                case "OR":
                    temp = new Node(exp[i]);
    
                    t2 = s.pop();
                    t1 = s.pop();
    
                    temp.left = t1;
                    temp.right = t2;
    
                    s.push(temp);
                    break;

                default:
                    s.push(new Node(exp[i]));
                    break;
                 
            }
        }

        return s.pop();
    }
    
    public static LinkedList<String> infixtopostfix(String[] exp){
        Stack<String> s = new Stack();
        LinkedList<String> out = new LinkedList();
        int j = 0;


        for(int i = 0; i < exp.length; i++){

            if(exp[i].contains("(") || exp[i].equals("NOT")){
                s.push(exp[i]);
            }
            else if(exp[i].contains(")")){
                                
                while(!s.peek().equals("(")){
                    out.add(s.pop());
                    j++;
                }
                
                s.pop();

            }
            else if(Character.isLowerCase(exp[i].charAt(0)) || exp[i].equals("TRUE") || exp[i].equals("FALSE")){
                out.add(exp[i]);
                j++;
            }
            else if(exp[i].equals("AND") || exp[i].equals("OR")){

                while(!s.isEmpty() && priority(exp[i]) <= priority(s.peek()) ) {
                    out.add(s.pop());
                    j++;
                }
                
                s.push(exp[i]);
            }
        }
            
        System.out.println();

        while(!s.isEmpty()){
            
            out.add(s.pop());
            j++;
            
        }

        return out;
    }
    

    public static int priority(String a){
        switch(a){
            case "OR":
                return 0;
            case "AND":
                return 1;
            case "NOT":
                return 2;
            default: 
                return -1;
        }
    }

    public static void printPreorder(Node node){

        if (node == null)
            return;
 
        str.append(node.data).append(",");
        printPreorder(node.left);
        printPreorder(node.right);

    }

    public static int depth(Node node){
        if (node == null)
            return 0;
        else{
            int lDepth = depth(node.left);
            int rDepth = depth(node.right);

            if (lDepth > rDepth)
                return (lDepth + 1);
             else
                return (rDepth + 1);
        }
    }

}

class Node{
    String data;
    Node left, right;

    public Node(String data){
        this.data = data;
        this.left = null;
        this.right = null;
    }
    public Node(){}
}
