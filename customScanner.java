import java.util.Scanner;
/**
 * Write a description of class customScanner here.
 *
 * @author (Brayden Anderson)
 * @version (Base Version: Beta 1.0.1, Snapshot 3xWav-7)
 */
public class customScanner
{
    public static Scanner scan;
   
    public customScanner(){
         scan = new Scanner(System.in);
    }
    public static Scanner getScanner(){
        return scan;
    }
    public static double nextDouble(){
        double d = scan.nextDouble();
        return d;
    }
    
    public static String nextLine(){
        String s = scan.nextLine();
        return s;
    }
    
    public static int nextInt(){
        int i = scan.nextInt();
        return i;
    }
    
    public int close(int b){
        scan.close();
        try{
            b = System.in.available();
        }catch(Exception e){
            mainBody.setNewMessage(e.toString());
            e.printStackTrace();
            b = 0;
        }
        return b;
    }
}
