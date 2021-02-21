import java.io.*;
import java.util.*;
/**
 * Write a description of class generateFileList here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class generateFileList
{
    public static ArrayList<String> listOfFiles = new ArrayList<String>();
    public static boolean fileList(String src, String destination){
        boolean success = false;
        File[] files = new File(src).listFiles();
        int i = 0;
        try{
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    System.out.println(src + name);
                    success = FileMovement.moveFile(src + "/" + name, destination + "/" + name);//FileMovement.moveFile(src+name, destination+name);//
                    listOfFiles.add(name);
                    System.out.println(listOfFiles.get(i));
                    i++;
                }
            }
            return success;
        }catch(NullPointerException e){
            mainBody.setNewMessage("[System ERROR]: " + e.toString());
            return false;
        }catch(Exception e){
            mainBody.setNewMessage("[System ERROR]: " + e.toString());
            return false;
        }
    }
}
