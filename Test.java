import java.io.*; 
  
public class Test 
{ 
    public static boolean moveFile(String oldPath, String newPath) 
    { 
        File file = new File(oldPath); 
          
        // renaming the file and moving it to a new location 
        if(file.renameTo(new File(newPath))){ 
            // if file copied successfully then delete the original file 
            file.delete(); 
            System.out.println("File moved successfully"); 
            return true;
        }else{ 
            System.out.println("Failed to move the file"); 
            return false;
        } 
    } 
} 