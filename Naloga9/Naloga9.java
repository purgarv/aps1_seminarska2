import java.io.*;
import java.util.*;


public class Naloga9{
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            HashMap<Povezava, Integer> povezave = new HashMap<>();
            HashSet<Integer> nodes = new HashSet<>();

            String[] temp = br.readLine().split(",");
            int n = Integer.parseInt(temp[0]);
            int m = Integer.parseInt(temp[1]);

            

            String line;



            for(int i = 0; i < n && (line = br.readLine()) != null; i++){
                temp = line.split(",");

                povezave.put(new Povezava( Integer.parseInt(temp[0]), Integer.parseInt(temp[1]) ), 0 ); // povezava (a,b) , stevilo voznikov
                nodes.add(Integer.parseInt(temp[0]));
                nodes.add(Integer.parseInt(temp[1]));
                
            }

            int v = nodes.size();

            ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>(v);

            for (int i = 0; i <= v; i++) {
                adj.add(new ArrayList<Integer>());
            }

            for(Povezava pov : povezave.keySet()){

                adj.get(pov.v1  - 1).add(pov.v2);
                adj.get(pov.v2 - 1).add(pov.v1);

            }


           
            for(int i = 0; i < m && (line = br.readLine()) != null; i++){
                temp = line.split(",");

                Povezava key = new Povezava(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));

                if(povezave.containsKey(key)){

                    povezave.put(key, povezave.get(key) + Integer.parseInt(temp[2]));

                }
                else{

                    LinkedList<Integer> path = shortestPath(adj, Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), v);
                    
                    for(int j = 0; j < path.size() - 1; j++){

                        int v1 = path.get(j);
                        int v2 = path.get(j + 1);

                        Povezava key1 = new Povezava(v1, v2);

                        povezave.put(key1, povezave.get(key1) + Integer.parseInt(temp[2]));
                        

                    }
                    
                }
                
            }
            LinkedList<Povezava> najdrazje = new LinkedList<>();
            int max = 0;

            for(Povezava pov : povezave.keySet()){
                int value = povezave.get(pov);

                if(value > max){
                    max = value;
                    najdrazje.clear();
                    najdrazje.add(pov);
                }
                else if(value == max){
                    najdrazje.add(pov);
                }

            }

            for(Povezava pov : najdrazje){
                p.println(pov.v1 + "," + pov.v2);
            }            

            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }
    }

    private static LinkedList<Integer> shortestPath(ArrayList<ArrayList<Integer>> adj, int s, int dest, int v){
        int pred[] = new int[v];
        int dist[] = new int[v];
 
        if (BFS(adj, s, dest, v, pred, dist) == false) {
            return null;
        }
 
        
        LinkedList<Integer> path = new LinkedList<>();
        int crawl = dest - 1;
        path.add(crawl + 1);
        while (pred[crawl] != -1) {
            path.add(pred[crawl] + 1);
            crawl = pred[crawl];
        }

        return path;
    }

    private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int s, int d, int v, int pred[], int dist[]){
        LinkedList<Integer> queue = new LinkedList<Integer>();

        boolean visited[] = new boolean[v];

        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        visited[s - 1] = true;
        dist[s - 1] = 0;
        queue.add(s - 1);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                if (visited[adj.get(u).get(i) - 1] == false) {

                    visited[adj.get(u).get(i) - 1] = true;
                    dist[adj.get(u).get(i) - 1] = dist[u] + 1;
                    pred[adj.get(u).get(i) - 1] = u;
                    queue.add(adj.get(u).get(i) - 1);

                    if (adj.get(u).get(i) == d)
                        return true;
                }
            }
        }
        return false;
    }

}

class Povezava{
    int v1;
    int v2;

    public Povezava(int v1, int v2){
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public boolean equals(Object o){

        if (o == this) {
            return true;
        }
        if (!(o instanceof Povezava)) {
            return false;
        }

        Povezava two = (Povezava) o;

        if( (this.v1 == two.v1 && this.v2 == two.v2) || (this.v1 == two.v2 && this.v2 == two.v1) )
            return true;
        return false;

    }

    @Override
    public int hashCode() {
        final int prime = 7727;
        int result = 1;
        result = prime * result + v1 * result + v2 * result;  
        return result;
    }

}