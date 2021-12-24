import java.io.*;
import java.util.*;
import java.awt.Point;

public class Naloga7{
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);

            String line = "";
            int n = Integer.parseInt(br.readLine());

            String[] temp;
            int v = 0;
            int[][] sorted = new int[n][];
            int[][] orig = new int[n][];
            HashMap<Integer, HashMap<Integer, Povezava>> adj = new HashMap<>();
            HashMap<Integer, Integer> prev = new HashMap<>();
            HashSet<Povezava> povezave = new HashSet<>();
            
            for(int i = 0; i < n && (line = br.readLine()) != null ; i++){
                temp = line.split(",");
                sorted[i] = new int[temp.length];
                orig[i] = new int[temp.length];
                for(int j = 0; j < temp.length; j++){
                    sorted[i][j] = Integer.parseInt(temp[j]);
                    orig[i][j] = Integer.parseInt(temp[j]);
                    if(Integer.parseInt(temp[j]) > v)
                        v = Integer.parseInt(temp[j]);
                }
                Arrays.sort(sorted[i]);
            }

            for(int i = 0; i < v; i++){
                adj.put(i + 1, new HashMap<>());   
                prev.put(i + 1, 0);    
            }

            for(int i = 0; i < orig.length ; i++){
                for(int j = 0; j < orig[i].length ; j++){
                    for(int k = j + 1; k < orig[i].length ; k++){
                        if(!adj.get(orig[i][j]).keySet().contains(orig[i][k])){
                            prev.put(orig[i][j], prev.get(orig[i][j]) + 1);
                            prev.put(orig[i][k], prev.get(orig[i][k]) + 1); 
                        }
                        if(k != j){
                            int diff = Math.abs(contains(orig[i], orig[i][k]) - contains(orig[i], orig[i][j]));

                            if(!povezave.contains(new Povezava(orig[i][k], orig[i][j]))){
                                povezave.add(new Povezava(orig[i][j], orig[i][k]));
                                povezave.add(new Povezava(orig[i][k], orig[i][j]));
                                adj.get(orig[i][j]).put(orig[i][k], new Povezava(diff, i, false, orig[i][j], orig[i][k]));
                                adj.get(orig[i][k]).put(orig[i][j], new Povezava(diff, i, false, orig[i][k], orig[i][j]));
                            }
                            else{
                                if(diff < adj.get(orig[i][j]).get(orig[i][k]).razdalja){
                                    adj.get(orig[i][j]).put(orig[i][k], new Povezava(diff, i, false, orig[i][j], orig[i][k]));
                                    adj.get(orig[i][k]).put(orig[i][j], new Povezava(diff, i, false, orig[i][k], orig[i][j]));
                                }
                            }

                        }
                    }
                } 
            }

            temp = br.readLine().split(",");
            int start = Integer.parseInt(temp[0]);
            int finish = Integer.parseInt(temp[1]);

            prev.put(start, 0);

            int stPrestop = stPrestopov(sorted, start, finish);
            p.println(stPrestop);

            if(stPrestop == -1){
                p.println(-1);
                p.println(-1);
            }
            else{

                Node node = steviloPostaj(prev, adj, v, start, finish);
                int minPostaj = node.razdalja;
                int prestopov = node.prestopov;

                p.println(minPostaj);

                if(prestopov == stPrestop){
                    p.println(1);
                }
                else{
                    p.println(0);
                }

            }          
            
            

            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("cel program : " + (endTime - startTime));

    }

    public static Node steviloPostaj(HashMap<Integer, Integer> stPrev, HashMap<Integer, 
        HashMap<Integer, Povezava>> adj, int numNodes, int start, int finish){
        

        Queue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(start, 0, 0, stPrev.get(start), null));

        Node[] dist = new Node[numNodes];
        for(int i = 0; i < dist.length; i++)
            dist[i] = new Node(i + 1, Integer.MAX_VALUE, Integer.MAX_VALUE);

        
        dist[start - 1] = new Node(start, 0, 0, 0);
        HashSet<Integer> settled = new HashSet<>(); 

        while(!pq.isEmpty()){
            Node current = pq.remove();
            int id = current.id;
            int razdalja = current.razdalja;
            int prestopov = current.prestopov;
            int prev = current.prev; // stevilo nodeov, ki imajo current node za soseda
            Povezava prejsnja = current.prejsnja;

            if(prev <= 0){
                settled.add(id);
            }
            
            if(current.compareTo(dist[id - 1]) == -1){
                dist[id - 1].prestopov = prestopov;
                dist[id - 1].razdalja = razdalja;
            }

            for(int i : adj.get(id).keySet()){
                Povezava p = adj.get(id).get(i);
                Povezava p1 = adj.get(i).get(id);
                if(!p.visited){
                    p.visited = true;
                    p1.visited = true;
                    stPrev.put(p.f, stPrev.get(p.f) - 1);
                    if(prejsnja != null){
                        pq.add(new Node(p.f, razdalja + p.razdalja, prestopov + prestop(prejsnja.s, prejsnja.f, p.f, adj), 
                                                                                                        stPrev.get(p.f) - 1, p));
                    }
                    else{
                        pq.add(new Node(p.f, razdalja + p.razdalja, prestopov, stPrev.get(p.f) - 1, p));
                    }
                }

            }

        }

        return dist[finish - 1];
    }

    public static int contains(int[] arr, int val){
        for(int i = 0; i < arr.length; i++)
            if(arr[i] == val)
                return i;

        return -1;

    }

    public static int prestop(int prev, int current, int next, HashMap<Integer, HashMap<Integer, Povezava>> adj){

        
        Povezava a = adj.get(prev).get(current);
        Povezava b = adj.get(current).get(next);

        if(a.linija == b.linija)
            return 0;
        return 1;
    }

 


    public static boolean seka(int[] a, int[] b) {
        int i = 0, j = 0;
        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) 
                return true;
            if (a[i] < b[j])
                i++; 
            else 
                j++;
        }
        return false;
    }

    
    public static int stPrestopov(int[][] poti, int start, int finish) {

        if (start == finish) 
            return 0;

        int n = poti.length;

        List<List<Integer>> graph = new ArrayList();
        for (int i = 0; i < n; i++) {
            Arrays.sort(poti[i]);
            graph.add(new ArrayList());
        }
        Set<Integer> seen = new HashSet();
        Set<Integer> targets = new HashSet();
        Queue<Point> queue = new ArrayDeque();

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (seka(poti[i], poti[j])) {
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }             

        for (int i = 0; i < n; i++) {
            if (Arrays.binarySearch(poti[i], start) >= 0) {
                seen.add(i);
                queue.offer(new Point(i, 0));
            }
            if (Arrays.binarySearch(poti[i], finish) >= 0)
                targets.add(i);
        }


        while (!queue.isEmpty()) {
            Point info = queue.poll();
            int node = info.x;
            int depth = info.y; 

            if (targets.contains(node))
                return depth;
            
            for (Integer nei: graph.get(node)) {
                if (!seen.contains(nei)) {
                    seen.add(nei);
                    queue.offer(new Point(nei, depth + 1));
                }
            }
        }

        return -1;
    }
}

class Node implements Comparable<Node>{
    int id;
    int razdalja;
    int prestopov;
    int prev;
    Povezava prejsnja;

    public Node(int id, int razdalja, int prestopov, int prev){
        this.id = id;
        this.razdalja = razdalja;
        this.prestopov = prestopov;
        this.prev = prev;
    }

    public Node(int id, int razdalja, int prestopov){
        this.id = id;
        this.razdalja = razdalja;
        this.prestopov = prestopov;
    }

    public Node(int id, int razdalja, int prestopov, int prev, Povezava prejsnja){
        this.id = id;
        this.razdalja = razdalja;
        this.prestopov = prestopov;
        this.prev = prev;
        this.prejsnja = prejsnja;
    }

    @Override
    public int compareTo(Node node){
        if(this.razdalja < node.razdalja)
            return -1;
        else if(this.razdalja > node.razdalja)
            return 1;
        else{
            if(this.prestopov < node.prestopov)
                return -1;
            else if(this.prestopov > node.prestopov)
                return 1;
            else 
                return 0;
        }

    }
}

class Povezava implements Comparable<Povezava>{
    int razdalja;
    boolean visited;
    int s;
    int f;
    int linija;

    public Povezava(int razdalja, int linija, boolean visited, int s, int f){
        this.razdalja = razdalja;
        this.linija = linija;
        this.visited = visited;
        this.s = s;
        this.f = f;
    }

    public Povezava(int s, int f){
        this.s = s;
        this.f = f;
    }

    @Override
    public int compareTo(Povezava povezava){
        if(this.razdalja < povezava.razdalja)
            return -1;
        else if(this.razdalja > povezava.razdalja)
            return 1;
        else{
            return 0;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
 
        if (!(o instanceof Povezava)) {
            return false;
        }
         
        Povezava c = (Povezava) o;
           
        return this.s == c.s ? this.f == c.f : false;
    }

    @Override
    public int hashCode() {
        return this.s * 11 + this.f * 13;
    }
}

