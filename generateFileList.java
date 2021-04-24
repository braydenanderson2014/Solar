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
    public static ArrayList<String> contentOfFile = new ArrayList<String>();
    public static boolean fileList(String src, String destination){
        boolean success = false;
        File[] files = new File(src).listFiles();
        int i = 0;
        try{
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    file.getAbsolutePath();
                    mainBody.setNewMessage("[System]: Files array size: " + file.length());
                    System.out.println(mainBody.getLastMessage());
                    mainBody.setNewMessage("[System]: File:" + src + name);
                    System.out.println(mainBody.getLastMessage());
                    BufferedReader in = new BufferedReader(new FileReader(new File(src)));
                    int line = 0;
                    for(String x= in.readLine(); x != null; x= in.readLine()){
                        mainBody.setNewMessage("[System]: Reading from file on line: " + line);
                        System.out.println(mainBody.getLastMessage());
                        line++;
                        contentOfFile.add(x);
                    }
                    in.close();
                    //success = FileMovement.moveFile(src + "/" + name, destination + "/" + name);//FileMovement.moveFile(src+name, destination+name);//
                    //success = CopyFilesRecursively.moveFiles(src, destination);
                    listOfFiles.add(name);
                    System.out.println("[System]: listOfFiles Size: " + listOfFiles.size());
                    System.out.println("[System]: " + listOfFiles.get(i));
                    writeToFile(listOfFiles.get(i));
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
    public static boolean writeToFile(String Path){
        File file = new File(Path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (Exception e) {
                mainBody.setNewMessage("[Warning]: " + e.toString());
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[Warning]: Failed to Create file... Path: " + Path);
                System.out.println(mainBody.getLastMessage());
                return false;
            }
        }else{
            mainBody.setNewMessage("[System]: Path already defined");
            System.out.println(mainBody.getLastMessage());
        }
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            mainBody.setNewMessage("[System]: Writing to file... Path: " + Path);
            System.out.println(mainBody.getLastMessage());
            for(int i = 0; i < contentOfFile.size(); i++){
                bw.write(contentOfFile.get(i) + "\r\n");
            }
            bw.close();
            mainBody.setNewMessage("[System]: Successfully Wrote to File");
            System.out.println(mainBody.getLastMessage());
            return true;
        } catch (Exception e) {
            mainBody.setNewMessage("[Warning]: " + e.toString());
            System.out.println(mainBody.getLastMessage());
            mainBody.setNewMessage("[Warning]: Failed to Write to file... Path: " + Path);
            System.out.println(mainBody.getLastMessage());
            return false;
        }
    }
}
