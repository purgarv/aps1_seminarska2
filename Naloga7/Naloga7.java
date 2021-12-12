import java.io.*;
import java.util.*;

import java.awt.Point;

public class Naloga7{
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            StringBuilder str = new StringBuilder();


            String line = "";
            int n = Integer.parseInt(br.readLine());

            String[] temp;
            int[][] poti = new int[n][];
            int[][] orig = new int[n][];

            for(int i = 0; i < n && (line = br.readLine()) != null ; i++){
                temp = line.split(",");
                poti[i] = new int[temp.length];
                orig[i] = new int[temp.length];
                for(int j = 0; j < temp.length; j++){
                    poti[i][j] = Integer.parseInt(temp[j]);
                    orig[i][j] = Integer.parseInt(temp[j]);
                }
                Arrays.sort(poti[i]);
            }

            temp = br.readLine().split(",");
            int start = Integer.parseInt(temp[0]);
            int finish = Integer.parseInt(temp[1]);


            int stPrestop = stPrestopov(poti, start, finish);
            //System.out.println(stPrestop);
            p.println(stPrestop);

            
            if(stPrestop == -1){
                //System.out.println("-1");
                p.println(-1);
                //System.out.println("-1");
                p.println(-1);
            }
            else{

                Point min = stPostaj(orig, start, finish);
                int minPostaj = min.x;
                int prestopov = min.y;

                //System.out.println(minPostaj);
                p.println(minPostaj);

                if(prestopov == stPrestop){
                    //System.out.println("1");
                    p.println(1);
                }
                else{
                    //System.out.println("0");
                    p.println(0);
                }

            }          
            

            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }

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
    

    public static Point stPostaj(int[][] poti, int s, int t){
        int minPostaj = Integer.MAX_VALUE;
        int stPrestopov = 0;

        if (s == t) 
            return new Point(0, 0); // (stevilo postaj, stevilo prestopov)


        int n = poti.length;

        Stack<Point4> st = new Stack();
        Set<Point> seen = new HashSet();
        Set<Integer> targets = new HashSet();

        for (int i = 0; i < n; i++) {
            int index = Arrays.binarySearch(poti[i], s);
            if (index >= 0) {
                seen.add(new Point(i, index));
                st.push(new Point4(i, index, 0, 0)); // (linija, postaja, stevilo postaj, stevilo prestopov)
            }
            index = Arrays.binarySearch(poti[i], t);
            if (index >= 0){
                targets.add(i);
            }
        }

        int diff;

        while(!st.isEmpty()){
            Point4 info = st.pop();
            int i = info.x;
            int index = info.y;
            int postaj = info.z;
            int prestopov = info.w;

            if(targets.contains(i)){
                diff = Math.abs(Arrays.binarySearch(poti[i], t) - Arrays.binarySearch(poti[i], poti[i][index]));
                if(postaj + diff < minPostaj){
                    minPostaj = postaj + diff;
                    stPrestopov = prestopov;
                }
            }
            for(int j = 0; j < n; j++){
                int a = Arrays.binarySearch(poti[j], poti[i][index]);
                if(a >= 0){
                    for(int k = 0; k < poti[j].length; k++){
                        if(!seen.contains(new Point(j, k))){
                            seen.add(new Point(j,k));
                            if(j != i)
                                st.push(new Point4(j, k, postaj + Math.abs(k - a), prestopov + 1));
                            else
                                st.push(new Point4(j, k, postaj + Math.abs(k - a), prestopov));
                        }
                    }
                }
            }

        }
        
        if(minPostaj == Integer.MAX_VALUE)
            return new Point(-1, 0);
        else   
            return new Point(minPostaj, stPrestopov);
        
    }
}

class Point4{
    int x;
    int y;
    int z;
    int w;

    public Point4(int x, int y, int z, int w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

}
