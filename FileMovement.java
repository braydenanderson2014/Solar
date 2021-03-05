import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException; 
public class FileMovement {
    /**
     * Method moveFile
     *
     * @param src A parameter
     * @param dest A parameter
     * @return true or false based off of file movement success
     */
    public static boolean moveFile(String src, String dest ) {
        Path result = null;
        System.out.println(src);
        try {
            result = Files.move(Paths.get(src), Paths.get(dest));
            System.out.println(src);
        } catch (IOException e) {
            mainBody.setNewMessage("[System ERROR]: Exception while moving file: " + e.getMessage());
            return false;
        }
        if(result != null) {
            mainBody.setNewMessage("[System]: File moved successfully.");
            return true;
        }else{
            mainBody.setNewMessage("[System ERROR]: File movement failed.");
            return false;
        }
    }
}