import java.io.*;

public class Naloga7{
    public static void main(String[] args) {
        try{

            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            PrintWriter p = new PrintWriter(args[1]);
            StringBuilder str = new StringBuilder();


            String line = br.readLine();


            







            br.close();
            p.close();
        }
        catch(Exception e){
            System.out.println("error");
        }

    }
}
