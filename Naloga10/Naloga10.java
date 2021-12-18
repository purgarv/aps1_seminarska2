import java.io.*;
import java.util.Collections;
import java.util.LinkedList;

public class Naloga10{
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long vmesni2 = 0;
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            
            int n = Integer.parseInt(br.readLine());
            LinkedList<Razdalja> clusters = new LinkedList<>();

            LinkedList<LinkedList<Double>> distances = new LinkedList<>();

            String line;

            String[] val = new String[2];

            for(int i = 0; (line = br.readLine()) != null && i < n; i++){

                val = line.split(",");

                distances.add(new LinkedList<Double>());

                clusters.add(new Razdalja(new Tocka(i + 1, Double.parseDouble(val[0]), Double.parseDouble(val[1]))));

            }

            int k = Integer.parseInt(line); // zahtevano stevilo clusterjev
            int currentClusters = clusters.size();

            for(int i = 0; i < clusters.size(); i++){ // izracun zacetnih razdalj
                for(int j = 0; j < i; j++){
                    distances.get(i).add(distance(clusters.get(i), clusters.get(j))); 
                }        
            }

            long vmesni = System.currentTimeMillis();
            System.out.println("init zacetne matrike : " + (vmesni - startTime));

            while(currentClusters > k){ 

                Double minDist = Double.MAX_VALUE;
                int index1 = 0;
                int index2 = 0;

                for(int i = 0; i < distances.size(); i++){
                    for(int j = 0; j < i; j++){

                        if(distances.get(i).get(j) < minDist){
                            minDist = distances.get(i).get(j);
                            index1 = j;
                            index2 = i;
                        }
                    }
                }

                Razdalja one = clusters.get(index1);
                Razdalja two = clusters.get(index2);

                Razdalja nov = prenesiTocke(two, one);

                clusters.remove(index2);
                clusters.remove(index1);

                clusters.add(nov);

                
                LinkedList<Double> newDist1 = new LinkedList<>();
                LinkedList<Double> newDist2 = new LinkedList<>();
                LinkedList<Double> newDist = new LinkedList<>();

                for(int i = index2 + 1; i < distances.size(); i++){
                    newDist1.add(distances.get(i).get(index2));
                    distances.get(i).remove(index2);
                }

                distances.get(index2).remove(index1);
                newDist1.addAll(0, distances.get(index2));


                for(int i = index1 + 1; i < distances.size(); i++){
                    if(i != index2){
                        newDist2.add(distances.get(i).get(index1));
                        distances.get(i).remove(index1);
                    }
                }
                newDist2.addAll(0, distances.get(index1));
                
                distances.remove(index2);
                distances.remove(index1);

                for(int i = 0; i < newDist1.size(); i++){
                    newDist.add(Math.min(newDist1.get(i), newDist2.get(i)));
                }

                distances.add(newDist);

                currentClusters--;
            }

            vmesni2 = System.currentTimeMillis();
            System.out.println("main del : " + (vmesni2 - vmesni));
        
            clusters = sort(clusters);

            StringBuffer str = new StringBuffer();

            for(int i = 0; i < clusters.size(); i++){
                int j = 0;
                for(; j < clusters.get(i).tocke.size() - 1; j++){
                    str.append(clusters.get(i).tocke.get(j).id).append(",");
                }
                str.append(clusters.get(i).tocke.get(j).id);
                str.append("\n");
            }

            p.print(str);
            
            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("urejanje clustrov : " + (endTime - vmesni2));
        System.out.println("cel program : " + (endTime - startTime));
    }

    public static LinkedList<Razdalja> sort(LinkedList<Razdalja> clusters){

        for(Razdalja c : clusters){
            Collections.sort(c.tocke);
        }
        Collections.sort(clusters);
        return clusters;
    }

    public static Razdalja sort(Razdalja c){

        Collections.sort(c.tocke);

        return c;
    }

    public static double distance(Tocka t1, Tocka t2){
        return Math.sqrt( (t1.x - t2.x) * (t1.x - t2.x) + (t1.y - t2.y) * (t1.y - t2.y) );
    }

    public static double distance(Razdalja c1, Razdalja c2){
        Double minDist = Double.MAX_VALUE;

        for(Tocka p1 : c1.tocke){
            for(Tocka p2 : c2.tocke){
                Double dist = distance(p1, p2);
                if(dist < minDist){
                    minDist = dist;
                }
            }
        }

        return minDist;
    }

    public static Razdalja prenesiTocke(Razdalja start, Razdalja cilj){

        for(Tocka t : start.tocke){
            cilj.tocke.add(t);
        }

        return cilj;
    }

}

class Cluster implements Comparable<Cluster>{

    LinkedList<Tocka> tocke = new LinkedList<Tocka>();

    public Cluster(LinkedList<Tocka> tocke){
        this.tocke = tocke;
    }

    public Cluster(Tocka tocka){
        this.tocke.add(tocka);
    }

    @Override
    public int compareTo(Cluster o) {
        return compare(this.tocke, o.tocke);
    }

    public static int compare (LinkedList<Tocka> a, LinkedList<Tocka> b) {
        return a.getFirst().id < b.getFirst().id ? -1 : 1;
    }



}


class Tocka implements Comparable<Tocka>{

    double x;
    double y;
    int id;

    public Tocka(int id, double x, double y){

        this.x = x;
        this.y = y;
        this.id = id;

    }

    @Override
    public boolean equals(Object o) {
 
        if (o == this) {
            return true;
        }
 
        if (!(o instanceof Tocka)) {
            return false;
        }
         
        Tocka c = (Tocka) o;
         
        return this.id == c.id;
    }

    @Override
    public int compareTo(Tocka o) {
        return compare(this.id, o.id);
    }

    public static int compare (int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

}
