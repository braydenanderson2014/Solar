import java.util.*;
import java.io.*;
/**
 * Write a description of class mainBody here.
 *
 * @author (Brayden Anderson)
 * @version (a version number or a date) 
 */
public class mainBody{
    public static ArrayList<String> Messages = new ArrayList<String>();
    public static ArrayList<String> changeLog = new ArrayList<String>();
    /**
     * mainBody Constructor
     * Setup Menu
     */
    public mainBody(){
        Scanner scan = new Scanner(System.in); 
        Login.displaySolarLogo();
        System.out.println("Welcome to Solar!");
        System.out.println("========================================");
        System.out.println("1. Start Setup");
        System.out.println("2. Quit Program");
        System.out.println("Console: ");
        setNewMessage("[System]: Please Start Setup to Proceed");
        System.out.println(getLastMessage());
        String selection = scan.nextLine();
        if(selection.equals("1")){
            setNewMessage("[USER]: " + selection);
            Setup.startSetup();
        }else if(selection.equals("2")){
            System.exit(1);
        }else{
            scan.close();
            new mainBody();
        }
    }

    /**
     * Method setNewMessage
     *
     * @param message message to set into systemlog
     * @return useless return value... not needed
     */
    public static boolean setNewMessage(String message){
        boolean success;
        Messages.add(message);
        if(Messages.contains(message)){
            success = true;
            return success;
        } else {
            success = false;
            return success;
        }
    }

    /**
     * Method getMessageSize
     *
     * @return Message Arraylist size
     */
    public static int getMessageSize(){
        return Messages.size();
    }

    /**
     * Method getLastMessage
     *
     * @return Returns last Message in Messages index
     */
    public static String getLastMessage(){
        int size = Messages.size();
        size--;
        return Messages.get(size);
    }

    /**
     * Method getLastMessageNum
     *
     * @return returns Messages size
     */
    public static int getLastMessageNum(){
        int size = Messages.size();
        size--;
        return size;
    }

    /**
     * Method removeLastMessage
     *
     * @param size index to remove
     * @return index removed
     */
    public static int removeLastMessage(int size){
        Messages.remove(size);
        return size;
    }

    /**
     * Method mainMenu
     * Main Menu
     */
    public static void mainMenu(){
        Scanner scan = new Scanner(System.in);
        String user = Login.getUser();
        System.out.println("Welcome: " + user);
        System.out.println("========================================");
        System.out.println("[POS]: POS Menu");
        System.out.println("[SET]: Settings Menu");
        System.out.println("[SFI]: Search for an Invoice");
        System.out.println("[VSM]: View System Messages");
        System.out.println("[VCL]: View ChangeLog");
        if(user.equals("admin") || user.equals("test")){   
            System.out.println("[DI]:  Delete an Invoice");
            System.out.println("[RFR]: Run Final Report");
            System.out.println("[CNA]: Create new Account"); 
            System.out.println("[DEA]: Delete an Existing Account");
        }
        if(!user.equals("test")){
            System.out.println("[RAB]: Report a Bug");
        }
        if(user.equals("test")){
            System.out.println("[CAP]: Change Admin Password");
            System.out.println("[CV]:  Change Version");
            System.out.println("[NOT]: Notepad");
            System.out.println("[BUG]: Bug Report");
        }
        System.out.println("[OFF]: Log off");
        System.out.println("[SWI]: Switch User");
        System.out.println("Console: ");
        if(Messages.size() > 0){
            int size = Messages.size();
            size--;
            System.out.println(Messages.get(size)); 
        }
        String selection = scan.nextLine().toLowerCase();
        switch(selection){
            case "pos":
            POS.POSMenu();
            break;

            case "set":
            Setup.Settings();
            break;

            case "sfi":
            //search for invoice
            mainMenu();
            break;

            case "vsm":
            viewSystemMessages();
            String plsContinue = scan.nextLine();
            int size = Messages.size();
            size--;
            Messages.remove(size);
            mainMenu();
            break;

            case "rab":
            if(!user.equals("test")){
                //report a bug
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "di":
            if(user.equals("admin") || user.equals("test")){
                //delete an invoice
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "rfr":
            if(user.equals("admin") || user.equals("test")){
                //run final report
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "cna":
            if(user.equals("admin") || user.equals("test")){
                createNewAccount();
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "dea":
            if(user.equals("admin") || user.equals("test")){
                deleteAnAccount();
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu(); 
            break;

            case "cap":
            if(user.equals("test")){
                //change admin password
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "cv":
            if(user.equals("test")){
                //change version
                changeVersion();
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "not":
            if(user.equals("test")){
                //notepad
                mainBody.setNewMessage("[System]: [CRE]: Only 1 Notebook can exist at one time");
                debugNotepad.autoLoadDebugNotepad();

                debugNotepad.Notepad();
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "bug":
            if(user.equals("test")){
                //bug report
            }else{
                setNewMessage("[Warning]: you do not have the proper permissions");
            }
            mainMenu();
            break;

            case "off":
            setNewMessage("[System]: " + user + "Logged off successfully");
            Login.removeActiveUser(user);
            Login.setUser("");
            Login.loginPage();
            break;

            case "swi":
            setNewMessage("[System]: Swapping User");
            Login.switchUserController();
            break;

            case "vcl":
            String path = Setup.getPath() + "\\Settings/ChangeLog.txt";
            File file = new File(path);
            if(file.exists()){
                viewChangeLog();
            }else{
                setNewMessage("[Warning]: No Change Log Exists, Please Contact Developer");
            }
            mainMenu();
            break;

            default:
            scan.close();
            setNewMessage("[Warning]: Invalid Option: Please make sure you have the proper privelages");
            mainMenu();
            break;
        }

    }

    /**
     * Method viewChangeLog
     * NEEDS TO BE REWRITTEN... READ FROM FILE INSTEAD OF ARRAYLIST
     * Displays changelog
     * @return Useless value
     */
    public static String viewChangeLog(){
        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < changeLog.size(); i++){
            System.out.println("[*] " + changeLog.get(i));
        }
        System.out.println("[System]: Press Enter to Continue");
        String Enter = scan.nextLine();
        scan.close();
        return "Success";
    }

    /**
     * Method changeVersion
     *
     *Change Version of Program and saves Version to file
     * @return Useless Value
     */
    public static String changeVersion(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Old Version: " + Setup.getVersion());
        System.out.println("New Version: ");
        String Version = scan.nextLine();
        Setup.setVersion(Version);
        String path = Setup.getPath() + "\\Settings/Version.txt";
        File file = new File(path);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Version);
            bw.close();
            setNewMessage("[System]: Version: " + Version + " was successfully updated"); 
            setNewMessage("[System]: Auto opening ChangeLog Updater");
            changeLog.clear();
            updateChangeLog();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
            setNewMessage("[Warning]: Unable to Write to File");
        }
        scan.close();
        return "VERSION";
    }

    /**
     * Method updateChangeLog
     * updates changelog, saves to file
     * @return Useless value
     */
    public static String updateChangeLog(){
        Scanner scan = new Scanner(System.in);
        String path = Setup.getPath() + "\\Settings/ChangeLog.txt";
        String Version = Setup.getVersion();
        File file = new File(path);
        if(file.exists()){
            try{
                System.out.println("[System]: Task Started...([ChangeLog Updater])");
                setNewMessage("[System]: Task Started...([ChangeLog Updater])");
                System.out.println("ChangeLog: ");
                System.out.println("========================================");
                for(int i = 0; i < changeLog.size(); i++){
                    System.out.println("[*] " + changeLog.get(i));
                }
                System.out.println("[ChangeLog Updater]: Type \"cancel\" to cancel, Type \"Done\" when Finished");
                System.out.println("Note: ");
                String newNote = scan.nextLine().toLowerCase();
                if(newNote.equals("cancel")){
                    setNewMessage("[Warning]: Developer Did not Update Change Log");
                    scan.close();
                    return "Canceled";
                }else if(newNote.equals("done")){
                    setNewMessage("[ChangeLog Updater]: Generating ChangeLog");
                    System.out.println("[ChangeLog Updater]: Generating ChangeLog");
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("ChangeLog: \r\n");
                    bw.write("========================================\r\n");
                    bw.write("Version: " + Setup.getVersion() + "\r\n");
                    for(int i = 0; i < changeLog.size(); i++){
                        bw.write(changeLog.get(i) + "\r\n");
                    }
                    bw.close();
                    setNewMessage("[ChangeLog Updater]: ChangeLog Generated");
                    scan.close();
                    return "Success";
                }else{
                    changeLog.add(newNote);
                    updateChangeLog();
                }
                scan.close();
                return "Failed";
            }catch(Exception e){
                setNewMessage("[Warning]: Failed to start Task...([ChangeLog Updater])");
                scan.close();
                return "Failed";
            }
        }else{
            setNewMessage("[Warning]: Change Log Missing, Running Setup Repair...");
            System.out.println("[Warning]: Change Log Missing, Running Setup Repair...");
            System.out.println("[SystemRecovery]: Starting SetupRepair... ");
            setNewMessage("[SystemRecovery]: Starting SetupRepair... ");
            System.out.println("[SystemRecovery]: Checking File Structure...");
            setNewMessage("[SystemRecovery]: Checking File Structure...");
            System.out.println("[SystemRecovery]: 1 Issue Detected...");
            setNewMessage("[SystemRecovery]: 1 Issue Detected...");
            System.out.println("[SystemRecovery]: Rectifying Issue...");
            setNewMessage("[SystemRecovery]: Rectifying Issue...");
            try{
                file.createNewFile();
                System.out.println("[SystemRecovery]: Issue Solved... Restarting ChangeLog Updater...");
                setNewMessage("[SystemRecovery]: Issue Solved... Restarting ChangeLog Updater...");
                System.out.println("[System]: Closing down task...([SystemRecovery])");
                setNewMessage("[System]: Closing down task...([SystemRecovery])");
                System.out.println("[System]: ([SystemRecovery]) Closed, Restarting ChangeLog Updater...");
                setNewMessage("[System]: ([SystemRecovery]) Closed, Restarting ChangeLog Updater...");
                scan.close();
                updateChangeLog();
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
                setNewMessage("[SystemRecovery ERROR]: Failed to Repair File");
                System.out.println("[SystemRecovery ERROR]: Failed to Repair File");
                scan.close();
                return "Failed";
            }
        }
        return "h";
    }

    /**
     * Method viewSystemMessages
     * Displays System Messages
     * @return useless value
     */
    public static String viewSystemMessages(){
        System.out.println("System Messages: ");
        System.out.println("========================================");
        setNewMessage("[System]: Press Enter To Continue");
        for(int i = 0; i < Messages.size(); i++){
            System.out.println("[*] " + Messages.get(i));
        }
        return "hello";
    }
    //admin region
    /**
     * Method createNewAccount
     * Creates New Accounts (new Files)
     * @return success of account creation
     */
    public static boolean createNewAccount(){
        Scanner scan = new Scanner(System.in);
        String path = Setup.getPath();
        boolean success = false;
        System.out.println("New User: ");
        String newUser = scan.nextLine();
        path = path + "\\Users/" + newUser + ".txt";
        File file = new File(path);
        if(!file.exists()){
            try{
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Default");
                bw.close();
                setNewMessage("[System]: Password = Default");
                success = true;
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
                setNewMessage("[System]: Failed to create User account");
            }
        }
        scan.close();
        return success;
    }

    /**
     * Method deleteAnAccount
     * deletes account of choosing
     * @return account deletion success
     */
    public static boolean deleteAnAccount(){
        Scanner scan = new Scanner(System.in);
        String path = Setup.getPath();//get standard program path
        boolean success = false;//needed a value;
        System.out.println("Account: ");
        String Account = scan.nextLine();//account name
        path = path + "\\Users/" + Account + ".txt";//path to account file
        File file = new File(path);
        if(file.exists()){//tests to see if file exists, if it does, delete account
            setNewMessage("[System]: Deleting account...");
            if(file.delete()){//delete file
                setNewMessage("[System]: Account Deleted!");
                success = true;
            }else{
                setNewMessage("[Warning]: Failed to Delete Account");
            }
        }else{
            setNewMessage("[Warning]: Account Not Found");
        }
        scan.close();
        return success;
    }
    //Region: DEBUG MODE

    /**
     * Method main
     *
     * @param args A parameter
     */
    public static void main(String[] args) {
        new mainBody();
    }
}