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
        System.out.println("copyright 2021");    
        return Logo;
    }

    /**
     * Method loginPage
     *
     */
    public static void loginPage() {
        Scanner scan = new Scanner(System.in);
        displaySolarLogo();
        System.out.println("Welcome to Solar!");
        System.out.println("========================================");
        System.out.println("Version: " + Setup.getVersion());
        mainBody.setNewMessage("Welcome to Solar, Version: " + Setup.getVersion());
        System.out.println("Type \"Switch\" to view and switch to an active user");
        System.out.println("Username: ");
        String Username = scan.nextLine();
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
        String Password = scan.nextLine();
        scan.close();
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
                System.out.println("[Warning]: Username or Password is incorrect");
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
        Scanner scan = new Scanner(System.in);
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
            int activeUser = scan.nextInt();
            activeUser--;
            String User = activeUsers.get(activeUser);
            String oldUser = getUser();
            if(User.equals("Login Screen")){
                activeUsers.remove(activeUser);
                if(activeUsers.contains(activeUser)){
                    activeUsers.remove(activeUser);
                }
                loginPage();
            }
            if(User.equals(oldUser)){
                mainBody.setNewMessage("[System]: Switch User ABORTED, User chose Active User");
                mainBody.mainMenu();
            }else{
                System.out.println("Password: ");
                String Password = scan.nextLine();
                Password = scan.nextLine();
                mainBody.setNewMessage("[System]: Attempting to log in User");
                System.out.println(mainBody.getLastMessage());
                scan.close();
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