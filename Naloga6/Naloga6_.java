import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class Naloga6_ {

    public static class Node {
        private String vrednost;
        private Node levo;
        private Node desno;

        public Node(String vrednost) {
            this.vrednost = vrednost;
            this.levo = null;
            this.desno = null;
        }
    }

    public static Node ustvari_novega(String str) {
        Node nov = new Node(str);
        nov.levo = null;
        nov.desno = null;
        return nov;
    }

    public static Node drevo(String[] izraz) {
        Stack<Node> stack_for_nodes = new Stack<>();
        Stack<String> stack_for_strings = new Stack<>();
        Node t, t1, t2;

        for (int i = 0; i < izraz.length; i++) {
            //push ( v stack_for_strings
            if (izraz[i].equals("("))
                stack_for_strings.add(izraz[i]);
                
            //dodaj "poddrevo" kot del drevesa
            else if (izraz[i].equals(")")) {
                while (!stack_for_strings.isEmpty() && !stack_for_strings.peek().equals("(")) {
                    t = ustvari_novega(stack_for_strings.peek());
                    stack_for_strings.pop();

                    t1 = stack_for_nodes.peek();
                    stack_for_nodes.pop();

                    t2 = stack_for_nodes.peek();
                    stack_for_nodes.pop();

                    t.levo = t2;
                    t.desno = t1;

                    stack_for_nodes.add(t);
                    System.out.println(t.vrednost);
                }
                stack_for_strings.pop();
            }

            else if (izraz[i].equals("AND") || izraz[i].equals("OR")) {
                while (!stack_for_strings.isEmpty() && !stack_for_strings.peek().equals("(")) {
                    t = ustvari_novega(stack_for_strings.peek());
                    stack_for_strings.pop();

                    t1 = stack_for_nodes.peek();
                    stack_for_nodes.pop();

                    t2 = stack_for_nodes.peek();
                    stack_for_nodes.pop();

                    t.levo = t2;
                    t.desno = t1;
                    
                    stack_for_nodes.add(t);
                    System.out.println(t.vrednost);
                }
                stack_for_strings.push(izraz[i]);
            }

            else if (izraz[i].equals("NOT")) {
                while (!stack_for_strings.isEmpty() && !stack_for_strings.peek().equals("(")) {
                    t = ustvari_novega(stack_for_strings.peek());
                    stack_for_strings.pop();

                    t1 = stack_for_nodes.peek();
                    stack_for_nodes.pop();

                    t.levo = t1;
                    
                    stack_for_nodes.add(t);
                    System.out.println(t.vrednost);
                }
                stack_for_strings.push(izraz[i]);
            }

            //push operande v stack_for_nodes
            else {
                t = ustvari_novega(izraz[i]);
                stack_for_nodes.add(t);
                System.out.println(t.vrednost);
            }
        }
        t = stack_for_nodes.peek();
        return t;
    }

    public static String izraz_preorder = "";

    public static void preorder(Node root) {
        if (root != null) {
            if (izraz_preorder.length() >= 1)
                izraz_preorder += ",";
            izraz_preorder += root.vrednost;
            preorder(root.levo);
            preorder(root.desno);
        }
    }

    public static String vrni(Node root) {
        preorder(root);
        return izraz_preorder;
    }

    public static int visina_drevesa(Node root) {
        if (root == null)
            return 0;
        else {
            int visina_levega = visina_drevesa(root.levo);
            int visina_desnega = visina_drevesa(root.desno);

            if (visina_levega > visina_desnega)
                return visina_levega+1;
            else 
                return visina_desnega+1;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String vrstica;
        vrstica = br.readLine();
        vrstica = "(" + vrstica;
        vrstica += ")";
        
        //dodam presledke pred in po ( in ), da bom lahko potem splitala po presledkih
        String zamenjaj1 = vrstica.replace("(", "( ");
        String zamenjaj2 = zamenjaj1.replace(")", " )");
        String[] izraz = zamenjaj2.split(" ");

        Node root = drevo(izraz);

        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
        bw.write(vrni(root));
        bw.newLine();
        bw.write(String.valueOf(visina_drevesa(root)));
        bw.flush();
        bw.close();
    }
}