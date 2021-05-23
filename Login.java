import java.util.*;
import java.io.*;
/**
 * Write a description of class Login here.
 *
 * @author (Brayden Anderson)
 * @version (a version number or a date)
 */
public class Login{
    private static ArrayList<String> activeUsers = new ArrayList<String>(); // any user who is logged in
    private static ArrayList<String> allUsers = new ArrayList<String>(); // all users with acccounts
    private static String user;
    protected static String password;
    //private static customScanner scan = new customScanner();
    /**
     * Method displaySolarLogo
     *
     * @return Logo
     */
    public static String displaySolarLogo(){
        String Logo = "SOLAR";
        System.out.println(" #####  ####### #          #    ######  ");
        System.out.println("#     # #     # #         # #   #     #"); 
        System.out.println("#       #     # #        #   #  #     #");
        System.out.println(" #####  #     # #       #     # ###### "); 
        System.out.println("      # #     # #       ####### #   #   ");
        System.out.println("#     # #     # #       #     # #    #  ");
        System.out.println(" #####  ####### ####### #     # #     #  ");   
        System.out.println("==========================================");
        System.out.println("Copyright 2021");    
        return Logo;
    }

    /**
     * Method loginPage
     *
     */
    public static void loginPage() {
        
        displaySolarLogo();
        System.out.println("Welcome to Solar!");
        System.out.println("========================================");
        System.out.println("Version: " + Setup.getVersion());
        mainBody.setNewMessage("Welcome to Solar, Version: " + Setup.getVersion());
        System.out.println("Console: ");
        if(mainBody.Messages.size() > 0){
            int size = mainBody.Messages.size();
            size--;
            String time;
            if(mainBody.getTimeSet() == true){
                time = mainBody.getLastTime();
            }else{
                time = "";
            }
            System.out.println(mainBody.Messages.get(size ) + time); 
        }
        if(activeUsers.size()>1){
            System.out.println("Type \"Swi\" to view and switch to an active user");
            System.out.println();
        }
        System.out.println("Username: ");
        String Username =  customScanner.nextLine();
        String user = getUser();
        if(user.equals("Null")){
            System.out.println("Password: ");
        }else{
            if(user.equals(Username)){
                mainBody.setNewMessage("[System]: Switch User ABORTED, User chose Active User");
                mainBody.mainMenu();
            }else{
                System.out.println("Password: ");
            }
        }
        String Password =  customScanner.nextLine();
        
        if(Username.equals("test") && Password.equals("testing")){
            if(!activeUsers.contains(Username)){
                activeUsers.add(Username);
            }
            setUser(Username);
            mainBody.mainMenu();
        }else if(Username.equals("swi") || Username.equals("SWI")){
            switchUserController();
        }else if(Username.equals("quit")){
            System.exit(1);
        }else if(Username.equals("reload") || Username.equals("Reload")){
            mainBody.setNewMessage("[System]: Reloading Login Page...");
            System.out.println(mainBody.getLastMessage());
            Setup.createProgramDir(Setup.getPathLetter() + Setup.getBaseDir());
            loginPage();
        }else if(Username.equals("Restart") || Username.equals("restart")){
            mainBody.setNewMessage("[System]: Restarting Application..."); 
            System.out.println(mainBody.getLastMessage()); 
            Setup.setTax(Setup.getTax());
            System.out.println(mainBody.getLastMessage());
            boolean success = setUser(getUser());
            if(success){
                mainBody.setNewMessage("[System]: Setting User to Original User");
            }else{
                mainBody.setNewMessage("[System]: Failed to set User");
            }
            System.out.println(mainBody.getLastMessage());
            Setup.setPath("Null");
            if(success){
                mainBody.setNewMessage("[System]: Setting path to null");
            }else{
                mainBody.setNewMessage("[System]: Failed to set path");
            }
            System.out.println(mainBody.getLastMessage());
            Setup.autoSearchForDir(); 
        }else{
            validateUserSignIn(Username, Password);
        }
    }

    /**
     * Method removeActiveUser
     *
     * @param user A parameter
     * @return success 
     */
    public static boolean removeActiveUser(String user){
        activeUsers.remove(user);
        boolean success = activeUsers.contains(user);
        if(success){
            mainBody.setNewMessage("[System]: Successfully removed " + user + " from active user list");
            return success;
        }else{
            mainBody.setNewMessage("[Warning]: Failed to remove " + user + " from active users list");
            return !success;
        }
    }
    public static boolean changePass(){
        String path = Setup.getPath();
        System.out.println("ChangePass:");
        System.out.println("========================================");
        if(user.equals("test") || user.equals("admin")){
            System.out.println("Username: ");
            String username = customScanner.nextLine();
            if(username.equals("admin")){
                mainBody.setNewMessage("[System]: Transferring to Change Admin Password Function");
                changeAdminPass();
                return true;
            }else if(username.equals("test")){
                mainBody.setNewMessage("[Warning]: You Cannot Change Debug Password");
                return false;
            }else{
                System.out.println("New Password: ");
                String password = customScanner.nextLine();
                path = path + "\\Users/" + username + ".txt";
                File file = new File(path);
                if(file.exists()){
                    try {
                        mainBody.setNewMessage("[System]: Changing Password for User: " + username);
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(password);
                        bw.close();
                        return true;
                    } catch (Exception e) {
                        mainBody.setNewMessage("[Warning]: " + e.toString());
                        return false;
                    }
                }else{
                    mainBody.setNewMessage("[Warning]: Failed to find User, Unable to change Password");
                    return false;
                }
            }
        }else{
            System.out.println("User: " + user);
            System.out.println("Old Password: ");
            String tempOldPassword = customScanner.nextLine();
            System.out.println("New Password: ");
            String newPassword = customScanner.nextLine();
            System.out.println("Confirm New Password: ");
            String confirmNewPass = customScanner.nextLine();
            path = path + "\\Users/" + user + ".txt";
            String oldPass = "Null";
            File file = new File(path);
            if(file.exists()){
                int line = 0;
                try {
                    BufferedReader in = new BufferedReader(new FileReader(new File(path)));
                    for(String x= in.readLine(); x != null; x= in.readLine()){
                        mainBody.setNewMessage("[System]: Reading Password on line: " + line);
                        oldPass = x;
                    }
                    in.close();
                    if(tempOldPassword.equals(oldPass)){
                        if(confirmNewPass.equals(newPassword)){
                            mainBody.setNewMessage("[System]: Changing Password for User: " + user);
                            //change Pass
                            FileWriter fw = new FileWriter(file.getAbsoluteFile());
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(newPassword);
                            bw.close();
                            return true;
                        }else{
                            mainBody.setNewMessage("[Warning]: Password Confirmation does not match Password");
                            return false;
                        }
                    }else{
                        mainBody.setNewMessage("[Warning]: Old Password does not match Password we have on File.");
                        return false;
                    }
                } catch (Exception e) {
                    mainBody.setNewMessage("[Warning]: " + e.toString());
                    return false;
                }   
            }else{
                mainBody.setNewMessage("[Warning]: Failed to find User Account");
                return false;
            }
        }
    }

    public static boolean changeAdminPass(){
        String path = Setup.getPath();
        System.out.println("ChangePass:");
        System.out.println("========================================");
        if(!user.equals("test")){
            mainBody.setNewMessage("[Warning]: Invalid User, Cannot change Pass");
            return false;
        }else{
            path = path + "\\Users/admin.txt";
            File file = new File(path);
            if(file.exists()){
                System.out.println("New Password: ");
                String password = customScanner.nextLine();
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(password);
                    bw.close();
                    mainBody.setNewMessage("[System]: Successfully updated Admin Password");
                    return true;
                } catch (Exception e) {
                    mainBody.setNewMessage("[Warning]: " + e.toString());
                    return false;
                }
                //change Password
            }else{
                mainBody.setNewMessage("[Warning]: Unable to locate admin file");
                return false;
            }
        }
    }
    /**
     * Method validateUserSignIn
     * Validates Username and Password
     * @param Username A parameter
     * @param Password A parameter
     */
    public static void validateUserSignIn(String Username, String Password){
        if(Username.equals("test") && Password.equals("testing")){
            if(!activeUsers.contains(Username)){
                activeUsers.add(Username);
            }
            setUser(Username);
            mainBody.mainMenu();
        }else{
            String path = Setup.getPath();
            path = path + "\\Users/" + Username + ".txt";
            System.out.println(path);
            File file = new File(path);
            if(!file.exists()){
                mainBody.setNewMessage("[Warning]: Username or Password is incorrect");
                loginPage();
            }else{
                try{
                    String password;
                    int line = 0;
                    BufferedReader in = new BufferedReader(new FileReader(new File(path)));
                    for(String x= in.readLine(); x != null; x= in.readLine()){
                        line++;
                        password = x;
                        if(Password.equals(password)){
                            if(!activeUsers.contains(Username)){
                                activeUsers.add(Username);
                                mainBody.setNewMessage("[System]: Obtained Password on line: " + line);
                            }
                            setUser(Username);
                            mainBody.setNewMessage("[System]: " + Username + " Successfully Logged On!");
                            mainBody.mainMenu();
                        }else{
                            System.out.println("[Warning]: Username or Password is incorrect");
                            loginPage();
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                    System.out.println(e);
                    loginPage();
                }
            }
        }
    }

    /**
     * Method switchUserController
     * Controls active users, allows users to switch between other users, must use original password of account you are switching to.
     */
    public static void switchUserController(){
        
        int allActiveUsers = activeUsers.size();
        int j =1;
        if(allActiveUsers > 1){// if the activeusers array has more than 1 value stored do this
            System.out.println("Active Users: ");
            System.out.println("========================================");
            if(!activeUsers.contains("Login Screen")){
                activeUsers.add("Login Screen");
            }
            for(int i = 0; i < activeUsers.size(); i++){
                System.out.println(j + " " + activeUsers.get(i));//find and print the indexes of activeUsers
                j++;
            }
            int activeUser = customScanner.nextInt();
            activeUser--;
            String User = activeUsers.get(activeUser);
            String oldUser = getUser();
            if(User.equals("Login Screen")){
                activeUsers.remove(activeUser);
                if(activeUsers.contains(User)){
                    activeUsers.remove(activeUser);
                }
                loginPage();
            }
            if(User.equals(oldUser)){
                mainBody.setNewMessage("[System]: Switch User ABORTED, User chose Active User");
                mainBody.mainMenu();
            }else{
                System.out.println("Password: ");
                String Password =  customScanner.nextLine();
                Password =  customScanner.nextLine();
                mainBody.setNewMessage("[System]: Attempting to log in User");
                System.out.println(mainBody.getLastMessage());
                
                validateUserSignIn(User, Password);
            }
        }else{
            mainBody.setNewMessage("[System]: No other users are active, Sending user back to Login Page");
            loginPage();
        }
    }

    /**
     * Method getUser
     *
     * @return Current User
     */
    public static String getUser() {
        return user;
    }

    /**
     * Method setUser
     * sets the current active user
     * @param User A parameter
     * @return success of setting active user
     */
    public static boolean setUser(String User) {
        user = User;
        boolean success = false;
        if (User.equals(user)) {
            success = true;
        }else{
            success = false;
        }
        return success;
    }

    /**
     * Method readFilesToUsersArraylist
     * reads Username File Names into an Arraylist
     */
    public static void readFilesToUsersArraylist() {
        String Path = Setup.getPath();
        allUsers.clear();
        File[] files = new File(Path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                allUsers.add(name);
            }
        }
        for(int i = 0; i < activeUsers.size(); i ++){
            System.out.println(activeUsers.get(i));
        }
    }
}