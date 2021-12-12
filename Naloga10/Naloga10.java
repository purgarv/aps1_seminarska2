import java.io.*;

public class Naloga10{
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            
            int n = Integer.parseInt(br.readLine());
            Tocka[] tocke = new Tocka[n]; 

            String line;

            String[] val = new String[2];


            for(int i = 0; (line = br.readLine()) != null && i < n; i++){
                
                val = line.split(",");
                tocke[i] = new Tocka(i + 1, Double.parseDouble(val[0]), Double.parseDouble(val[1]));

            }

            int stSkupin = Integer.parseInt(line);

            for(int i = 0; i < stSkupin; i++){
                
                tocke[i].skupina = i + 1;

            }

            
            System.out.println(n);
            System.out.println(stSkupin);
            for(int i = 0; i < n; i++){
                
                System.out.println(tocke[i].id + ", " + tocke[i].x + ", " + tocke[i].y + ", " + tocke[i].skupina);

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
}