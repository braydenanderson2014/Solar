import java.util.*;
import java.io.*;
import java.io.IOException; 
import java.nio.file.*;
/**
 * Write a description of class debugNotepad here.
 *
 * @author (Brayden Anderson)
 * @version (a version number or a date)
 */
public class debugNotepad 
{
    public static ArrayList<String>debugNotepad = new ArrayList<String>();
    /**
     * Method autoLoadDebugNotepad
     * reads notepad file if it exists and adds contents into debugNotepad Arraylist
     * @return True or False based off abiltiy to load debug notepad into arraylist
     */
    public static boolean autoLoadDebugNotepad(){
        String path = Setup.getPath(); 
        path = path + "\\debugNotepad/Notepad.txt";
        File file = new File(path);
        if(file.exists()){
            try{
                BufferedReader in = new BufferedReader(new FileReader(new File(path)));
                int line = 0;
                for(String x= in.readLine(); x != null; x= in.readLine()){
                    line++;
                    debugNotepad.add(x);
                }
                in.close();
                return true;
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
                mainBody.setNewMessage("[Warning]: Unable to read from notepad, Some Notes may be missing");
                return false;
            }
        }else{
            mainBody.setNewMessage("[System]: No Notebook Exists, Please create one!");
            return false;
        }
    }

    /**
     * Method Notepad
     * Notepad Menu
     */
    public static void Notepad(){
        String User = Login.getUser();
        System.out.println(mainBody.getLastMessage());
        System.out.println("NOTEPAD: ");
        System.out.println("========================================");
        System.out.println("[CRE]: Create Notebook");
        System.out.println("[ADD]: Add a New Note");
        System.out.println("[VN]:  View Notebook");
        System.out.println("[DEL]: Delete a Note");
        System.out.println("[DAN]: Delete all Notes");
        System.out.println("[DEN]: Delete Notebook");
        System.out.println("[SAV]: Save Notes to File");
        System.out.println("[DNC]: Discard new Changes");
        System.out.println("[RET]: Return to Main Menu");
        System.out.println();
        System.out.println("Console: ");
        if(mainBody.Messages.size() > 0){
            int size = mainBody.Messages.size();
            size--;
            System.out.println(mainBody.Messages.get(size)); 
        }
        String option = customScanner.nextLine();
        if(option.equals("cre")){
            //Create Notebook 
            newNotebook();
            Notepad();
        }else if(option.equals("add")){
            //add a new note
            addNote();
            Notepad();
        }else if(option.equals("vn")){
            //View Notebook
            viewNotes();
            String Continue = customScanner.nextLine();
            Notepad();
        }else if(option.equals("del")){
            //Delete a Note
            deleteNote();
            Notepad();
        }else if(option.equals("dan")){
            //Delete all Notes
            deleteAllNotes();
            Notepad();
        }else if(option.equals("den")){
            //Delete Notebook
            try{
                deleteNotebook();
            }catch(IOException e){
                mainBody.setNewMessage("[System ERROR]: " + e.toString());
            }
            Notepad();
        }else if(option.equals("sav")){
            //Save Notes to File
            saveNotesToFile();
            Notepad();
        }else if(option.equals("dnc")){
            //discard new Changes
            deleteAllNotes();
            mainBody.mainMenu();
        }else if(option.equals("ret")){
            mainBody.setNewMessage("[System]: Main Menu");
            mainBody.mainMenu();
        }else{
            mainBody.setNewMessage("[Warning]: Invalid Option: Please make sure you have the proper privelages");
            Notepad();
        }
    }

    /**
     * Method newNotebook
     * Creates new Notebook
     * @return True or False based off abiltiy to create notebook
     */
    public static boolean newNotebook(){
        String path = Setup.getPath();
        path = path + "\\debugNotepad/Notepad.txt";
        File file = new File(path);
        if(!file.exists()){
            try{
                file.createNewFile();
                mainBody.setNewMessage("[System]: Successfully created Notepad at " + path);
                return true;
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
                mainBody.setNewMessage("[Warning]: Failed to Create Notepad");
                return false;
            }
        }else{
            mainBody.setNewMessage("[Warning]: A Notepad Already Exists!");
            return false;
        }
    }

    /**
     * Method addNote
     * Allows you to add a custom note to the notebook
     * @return useless value
     */
    public static boolean addNote(){
        System.out.println("New Note: ");
        String note = customScanner.nextLine();
        debugNotepad.add(note);
        mainBody.setNewMessage("[System]: Successfully added Note: " + note);     
        return true;
    }

    /**
     * Method viewNotes
     * Displays notes in notebook
     * @return Useless value
     */
    public static boolean viewNotes(){
        for(int i = 0; i<debugNotepad.size();i++){
            System.out.println("[*] " + debugNotepad.get(i));
        }
        System.out.println("Console: ");
        mainBody.setNewMessage("[System]: Press Enter To Continue");
        System.out.println(mainBody.getLastMessage());
        int message = mainBody.getLastMessageNum();
        mainBody.removeLastMessage(message);
        return true;
    }

    /**
     * Method deleteNote
     * allows you to delete a note off of the notebook
     * @return True or false based off abiltiy to delete a note
     */
    public static boolean deleteNote(){  
        int j = 1;
        for(int i = 0; i<debugNotepad.size();i++){
            System.out.println("[*]" + j + " " + debugNotepad.get(i));
            j++;
        }
        mainBody.setNewMessage("[System]: Type \"0\" to Return");
        System.out.println("Console: ");
        if(mainBody.Messages.size() > 0){
            int size = mainBody.Messages.size();
            size--;
            System.out.println(mainBody.Messages.get(size)); 
        }
        int itemToRemove = customScanner.nextInt();
        if(itemToRemove == 0){
            mainBody.setNewMessage("[Warning]: User Cancelled Deletion");
            return false;
        }else{
            itemToRemove--;
            String itemRemoved = debugNotepad.get(itemToRemove);
            debugNotepad.remove(itemToRemove);
            mainBody.setNewMessage("[System]: " + itemRemoved + " Removed Successfully");
            return true;
        }
    }

    /**
     * Method saveNotesToFile
     * saves contents of the notebook to notebook file.
     * @return true or false based off of ability to save notes to file
     */
    public static boolean saveNotesToFile(){
        String path = Setup.getPath();
        path = path + "\\debugNotepad/Notepad.txt";
        File file = new File(path);
        if(file.exists()){
            try{
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for(int i = 0; i<debugNotepad.size();i++){
                    bw.write(debugNotepad.get(i) + "\r\n");
                }
                bw.close();
                mainBody.setNewMessage("[System]: Successfully saved Notes to file");
                debugNotepad.clear();
                autoLoadDebugNotepad();
                return true;
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
                mainBody.setNewMessage("[Warning]: Failed to Save Notes to File");
                return false;
            }
        }else{
            mainBody.setNewMessage("[Warning]: Failed to find File");
            return false;
        }
    }

    /**
     * Method deleteAllNotes
     * deletes all notes in the notebook
     * @return useless value
     */
    public static boolean deleteAllNotes(){
        debugNotepad.clear();
        mainBody.setNewMessage("[System]: Successfully cleared all Notes");
        return true;
    }

    /**
     * Method deleteNotebook
     *
     * @return The return value
     */
    public static boolean deleteNotebook() throws IOException{
        String path = Setup.getPath();
        path = path + "\\debugNotepad/Notepad.txt";
        File file = new File(path);
        try{
            Files.deleteIfExists(Paths.get(path));
            if(file.exists()){
                mainBody.setNewMessage("[Warning]: Failed to Delete Notebook");
                return false;
            }else{
                mainBody.setNewMessage("[System]: Successfully Deleted Notebook");
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
            mainBody.setNewMessage("[Warning]: Failed to Delete Notebook");
            return false;
        }
    }
}
