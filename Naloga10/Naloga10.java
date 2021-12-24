import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Naloga10{
    public static void main(String[] args) {
        //long startTime = System.currentTimeMillis();
        //long vmesni2 = 0;
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            
            int n = Integer.parseInt(br.readLine());

            ArrayList<Razdalja> distances = new ArrayList<>(n*(n+1)/2);
            HashMap<Integer, Tocka> tocke = new HashMap<>();

            String line;

            String[] val;

            for(int i = 0; (line = br.readLine()) != null && i < n; i++){

                val = line.split(",");
                tocke.put(i + 1, new Tocka(i + 1, Double.parseDouble(val[0]), Double.parseDouble(val[1]), i + 1));
                
            }

            int k = Integer.parseInt(line); // zahtevano stevilo clusterjev
            int currentClusters = n;

            for(int i = 1; i <= n; i++){ // izracun zacetnih razdalj
                for(int j = i + 1; j <= n; j++){
                    distances.add( new Razdalja(tocke.get(i).id, tocke.get(j).id, distance(tocke.get(i), tocke.get(j)) ) ); 
                }        
            }

            Collections.sort(distances);

            //long vmesni = System.currentTimeMillis();
            //System.out.println("init zacetne matrike : " + (vmesni - startTime));

            while(currentClusters > k){ 
                boolean found = false;

                for(int i = 0; i < distances.size() && !found; i++){
                    Tocka t1 = tocke.get(distances.get(i).t1);
                    Tocka t2 = tocke.get(distances.get(i).t2);

                    if(t1.clusterID != t2.clusterID){
                        found = true;
                        for(Tocka t : tocke.values()){
                            if(t.clusterID == t1.clusterID)
                                tocke.put(t.id, new Tocka(t.id, t.x, t.y, t2.clusterID));
                        }

                    }
                }

                currentClusters--;
            }

            //vmesni2 = System.currentTimeMillis();
            //System.out.println("main del : " + (vmesni2 - vmesni));

            // print clusters
            int currentCluster = tocke.get(1).clusterID;
            int[] seen = new int[k];
            int firstOut = 1;
            boolean first = true;
            StringBuilder str = new StringBuilder();

            for(int i = 0; i < k; i++){
                for(int j = firstOut; j <= tocke.size(); j++){
                    Tocka t = tocke.get(j);
                    if(t.clusterID == currentCluster && !contains(seen, currentCluster)){
                        str.append(t.id).append(",");
                    }
                    else if(t.clusterID != currentCluster && !contains(seen, t.clusterID) && first){
                        firstOut = t.id;
                        first = false;
                    }
                }
                seen[i] = currentCluster;
                first = true;
                currentCluster = tocke.get(firstOut).clusterID;
                str.setLength(str.length() - 1);
                str.append("\n");
            }

            p.print(str);
            
            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }
        //long endTime = System.currentTimeMillis();
        //System.out.println("print clustrov : " + (endTime - vmesni2));
        //System.out.println("cel program : " + (endTime - startTime));
    }


    public static double distance(Tocka t1, Tocka t2){
        return Math.sqrt( (t1.x - t2.x) * (t1.x - t2.x) + (t1.y - t2.y) * (t1.y - t2.y) );
    }

    public static boolean contains(int[] arr, int el){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == el)
                return true;
        }
        return false;
    }
}

class Razdalja implements Comparable<Razdalja>{
    int t1;
    int t2;
    Double razdalja;

    public Razdalja(int t1, int t2, Double razdalja){
        this.t1 = t1;
        this.t2 = t2;
        this.razdalja = razdalja;
    }


    @Override
    public int compareTo(Razdalja r) {
        return compare(this.razdalja, r.razdalja);
    }

    public static int compare (Double r1, Double r2) {
        return r1 < r2 ? -1 : r1 == r2 ? 0 : 1;
    }

}


class Tocka implements Comparable<Tocka>{

    double x;
    double y;
    int id;
    int clusterID;

    public Tocka(int id, double x, double y, int clusterID){

        this.x = x;
        this.y = y;
        this.id = id;
        this.clusterID = clusterID;

    }

    @Override
    public int compareTo(Tocka o) {
        return compare(this.id, o.id);
    }

    public static int compare (int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

}
