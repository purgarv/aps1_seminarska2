import java.io.*;
import java.util.HashMap;

public class Naloga10{
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            
            int n = Integer.parseInt(br.readLine());
            HashMap<Integer, Tocka> tocke = new HashMap<>();

            String line;

            String[] val = new String[2];


            for(int i = 0; (line = br.readLine()) != null && i < n; i++){
                
                val = line.split(",");
                tocke.put(i + 1, new Tocka(i + 1, Double.parseDouble(val[0]), Double.parseDouble(val[1])));

            }

            int stZahtevano = Integer.parseInt(line);
            int stSkupin = n;

            while(stSkupin > stZahtevano){



                stSkupin--;
            }
            



            System.out.println(n);
            System.out.println(stZahtevano);
            for(int i = 0; i < n; i++){
                
                System.out.println(tocke.get(i + 1).id + ", " + tocke.get(i + 1).x + ", " + tocke.get(i + 1).y + ", " + tocke.get(i + 1).skupina);

            }
            
            

            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }
    }

    public static int IDnajblizje(){

        //func ki returna id skupine, ki ji je tocka najblizja
        return -1;
    }
}

class Tocka{
    double x;
    double y;

    int id;
    int skupina;

    public Tocka(int id, double x, double y){

        this.x = x;
        this.y = y;
        this.id = id;
        this.skupina = -1;
    }

    public Tocka(int id, double x, double y, int skupina){

        this.x = x;
        this.y = y;
        this.id = id;
        this.skupina = skupina;
    }
}