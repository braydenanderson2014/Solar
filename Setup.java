import java.util.*;
import java.io.*;
/**
 * Write a description of class Setup here.
 *
 * @author (Brayden Anderson)
 * @version (a version number or a date)
 */
public class Setup{
    private static String path = "Null";
    private static String PATHLETTER = "C:";
    private static String baseDir = "";
    private static String oldPath;
    public static String defaultPath = "\\ThingsRememberedSLC\\Solar";
    private static String Version;
    public static int i = 5;
    private static ArrayList <String> dirPath = new ArrayList <String>();
    private static double Tax;
    private static customScanner scan = new customScanner();
    /**
     * Method startSetup
     * Starts program setup
     */
    public static void startSetup(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Setup:");
        System.out.println("==========================================");
        mainBody.setNewMessage("[System]: Starting Setup!...");
        System.out.println(mainBody.getLastMessage());
        if(path.equals("Null")){
            autoSearchForDir(); 
        }else{
            createProgramDir(path);
        }
    }
    public static boolean setBaseDir(String _baseDir){
        baseDir = _baseDir;
        return true;
    }
    public static String getBaseDir(){
        return baseDir;
    }
    public static boolean setPathLetter(String _pathLetter){
        PATHLETTER = _pathLetter + ":";
        return true;
    }
    public static String getPathLetter(){
        return PATHLETTER;
    }
    /**
     * Method getVersion
     *
     * @return The Version
     */
    public static String getVersion(){
        return Version;
    }

    /**
     * Method setVersion
     *
     * @param version A parameter
     * @return version
     */
    public static String setVersion(String version){
        Version = version;
        String path = getPath() + "\\Settings/" + "Version.txt";
        File file = new File(path);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Version);
            bw.close();
            mainBody.setNewMessage("[System]: Successfully Set Version in Version File");
        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e);
            mainBody.setNewMessage("[System ERROR]: Failed to Set Version in Version File");
        }
        return Version;
    }

    /**
     * Method setTax
     *
     * @param tax A parameter
     * @return Tax
     */
    public static double setTax(double tax){
        Tax = tax;
        String path = getPath() + "\\Settings/" + "TaxValuetxt";
        File file = new File(path);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String Conversion = String.valueOf(Tax);
            bw.write(Conversion);
            bw.close(); 
            mainBody.setNewMessage("[System]: Successfully Set Tax Value in Tax File");
        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e);
            mainBody.setNewMessage("[System ERROR]: Failed to Set Tax Value in Tax File");
        }
        return tax;
    }
    
    /**
     * Method getTax
     *
     * @return tax
     */
    public static double getTax(){
        return Tax;
    }
    public static String getOldPath(){
        return oldPath;
    }
    public static String setOldPath(String oldPath){
        Setup.oldPath = oldPath;
        return Setup.oldPath;
    }
    /**
     * Method getPath
     *
     * @return path directory
     */
    public static String getPath(){
        return path;
    }

    /**
     * Method setPath
     *
     * @param Path A parameter
     * @return path
     */
    public static String setPath(String Path){
        if(Path.equals("Default") || Path.equals("default")){
            path = defaultPath;
            return path;
        }else{
            path = Path;
            return path;
        }   
    }

    /**
     * Method setProgramDirectory
     *
     * @param oldPath A parameter
     * @return The New Path or Old Path
     */
    public static String setProgramDirectory(){
        Login.displaySolarLogo();
        System.out.println();
        setOldPath(getPath());
        String Path = "Default";
        System.out.println("Change Program Directory");
        System.out.println("========================================");
        System.out.println("Current Base Directory: " + Setup.oldPath);
        mainBody.setNewMessage("[System]: Please type out the Directory you would like to Change Solar's working Directory to, You do not need to include the slashed \"\\\" ");
        System.out.println(mainBody.getLastMessage());
        mainBody.setNewMessage("[System]: Press [ENTER] to submit each folder, Please type Which Drive Letter You would like to use");
        System.out.println(mainBody.getLastMessage());
        mainBody.setNewMessage("[System]: EXAMPLE \"C: [Enter] Users [ENTER] * (User Folder) [ENTER] * Documents [ENTER]  done [ENTER]\" ");
        System.out.println(mainBody.getLastMessage());
        mainBody.setNewMessage("[System]: Type \"Cancel\" to cancel and start over, Type \"Back\" to remove last directory ");
        System.out.println(mainBody.getLastMessage());
        String extraPath = "null";
        int size;
        dirPath.clear();
        String pathLetter =  customScanner.nextLine();
        String tempPath;
        while(!extraPath.equals("done") || !extraPath.equals("Done")){ 
            Login.displaySolarLogo();
            System.out.println();
            //here
            tempPath = completeManualDir(pathLetter + ":");
            mainBody.setNewMessage("[System]: Current Path Defined as: " + tempPath);
            System.out.println(mainBody.getLastMessage());
            extraPath =  customScanner.nextLine();  
            if(extraPath.equals("cancel") || extraPath.equals("Cancel")){
                dirPath.clear();
                mainBody.setNewMessage("[System]: User Cancelled Changes to the Program Directory");
                Settings();
            }else if(extraPath.equals("back") || extraPath.equals("Back")){
                size = dirPath.size();
                size--;
                dirPath.remove(size);
            }else if(extraPath.equals("done") || extraPath.equals("Done")){
                Login.displaySolarLogo();
                System.out.println();
                mainBody.setNewMessage("[System]: User Completed Program Directory Path");
                System.out.println(mainBody.getLastMessage());
                Path = completeManualDir(pathLetter + ":");
                oldPath = getPath();
                setOldPath(oldPath);
                setPath(Path);
                System.out.println("Would you like to move all data from previous stored location to selected Path? \"Y/N\"");
                String answer = customScanner.nextLine().toLowerCase();
                if(answer.equals("y") || answer.equals("yes")){
                    File oldFile = new File(oldPath).getAbsoluteFile();
                    File newPath = new File(Path).getAbsoluteFile();
                    if(!oldFile.exists()){
                        mainBody.setNewMessage("[Warning]: The old Path does not Exist!");
                        return Path;
                    }
                    if(!newPath.exists()){
                        mainBody.setNewMessage("[Warning]: The New Path does not Exist!");
                        newPath.mkdirs();
                    }
                    createProgramDir(Path);
                    System.out.println(oldPath);
                    System.out.println(Path);
                    // try{
                    boolean fileList = generateFileList.fileList(oldPath, Path);//CopyFilesRecursively.moveFiles(oldPath, Path);generateFileList.fileList(oldPath, Path);//CopyFilesRecursively.moveFiles(oldPath, Path);
                    if(fileList == true){
                        mainBody.setNewMessage("[System]: Successfully transfered Files");
                    }else{
                        mainBody.setNewMessage("[Warning]: Failed to Transfer Files");
                        setPath(oldPath);
                        Path = oldPath;
                        Login.loginPage();
                    }
                    // }catch(IOException e){
                    // mainBody.setNewMessage("[System ERROR]: " + e.toString());
                    // }//generateFileList.fileList(oldPath, Path);
                    return Path;
                }else if(answer.equals("n") || answer.equals("no")){
                    setPath(Path);
                    return Path;
                }else{
                    return oldPath;
                }
            }else if(!extraPath.equals("") || !extraPath.equals(" ")){
                dirPath.add(extraPath);
            } else{
                dirPath.add(extraPath);
            }
        }
        return Path;
    }

    /**
     * Method Settings
     * Settings Menu
     */
    public static void Settings(){
        Login.displaySolarLogo();
        System.out.println();
        String user = Login.getUser();
        System.out.println("Settings Menu");
        System.out.println("========================================");
        System.out.println("[CT]: Change Tax");
        if(!user.equals("test") || !user.equals("admin")){
            System.out.println("[CP]: Change Password");
        }
        if(user.equals("test") || user.equals("admin")){
            System.out.println("[CPD]: Change Program Directory");
        }
        System.out.println("[DTS]: Display Time Stamps");
        System.out.println("[RET]: Return to Main Menu");
        System.out.println();
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
            System.out.println(mainBody.Messages.get(size) + time); 
        }
        String settingsToChange = customScanner.nextLine().toLowerCase();
        switch(settingsToChange){
            case "ct":
            double tax = getTax();
            System.out.println("Current Tax Value: " + tax);
            System.out.println("New Tax Value: ");
            double TaxValue = scan.nextDouble();
            setTax(TaxValue);
            Settings();
            break;
            case "cp":
            if(!user.equals("test") || !user.equals("admin")){
                mainBody.setNewMessage("[System]: " + user + " is requesting a password change");
                Login.changePass();
                //change password logic
                Settings();
            }else{
                mainBody.setNewMessage("[Warning]: Invalid Option: Please make sure you have the proper privelages");
                Settings();
            }
            break;
            case "cpd":
            if(user.equals("test") || user.equals("admin")){
                mainBody.setNewMessage("[System]: " + user + " is Changing the Program Directory");
                //change program Directory
                path = setProgramDirectory();
                setPath(path);
                Settings();
            }else{
                mainBody.setNewMessage("[Warning]: Invalid Option: Please make sure you have the proper privelages");
                Settings();
            }
            break;
            case "dts":
            if(mainBody.getTimeSet() == true){
                mainBody.setTimeSet(false);
            }else if(mainBody.getTimeSet() == false){
                mainBody.setTimeSet(true);
            }
            mainBody.setNewMessage("[System]: Display Date/Time Stamps: " + mainBody.getTimeSet());
            String path = getPath() + "\\Settings/DisplayTimestamp.txt";
            File file = new File(path);
            if(file.exists()){
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    String boolToString = Boolean.toString(mainBody.getTimeSet());
                    bw.write(boolToString);
                    bw.close();
                    Settings();
                } catch (Exception e) {
                    mainBody.setNewMessage("[Warning]: Failed to save Setting to File");
                    Settings();
                }
            }else{
                mainBody.setNewMessage("[Warning]: Failed to find Setting File");
                Settings();
            }
            break;
            case "ret":
            mainBody.setNewMessage("[System]: Welcome to Solar, Version: "+ getVersion());
            mainBody.mainMenu();
            break;
            default:
            i--;
            mainBody.setNewMessage("[System]: You have " + i + " more attempts to choose an option before Auto Return Engages");
            if(i == 0){
                mainBody.setNewMessage("[System]: Warning, Auto Returning to Main Menu");
                i = 5;
                mainBody.mainMenu();
            }else{
                Settings();
            }
            break;
        }
    }

    /**
     * Method autoSearchForDir
     * Searches for a usable Directory
     */
    public static void autoSearchForDir() {// auto searches for a usable directory, then passes the directory to testDir
        // to see if it exists.
        String pathLetter=  "A";
        mainBody.setNewMessage("[System]: Searching for a directory");
        mainBody.clear();
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Program Directory:");
        System.out.println("==========================================");
        for (int i = 1; i < 26; i++) {
            switch (i) {
                case 1:
                pathLetter = "A";
                testDirBasic(pathLetter);
                break;
                case 2:
                pathLetter = "B";
                testDirBasic(pathLetter);
                break;
                case 3:
                mainBody.setNewMessage("[System]: Would you like to use drive \" C \" for Installation");
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[System]: Y/N?");
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[Warning]: Setup Halted, Awaiting User Response");
                System.out.println(mainBody.getLastMessage());
                String option;
                option =  customScanner.nextLine();
                option = option.toLowerCase();
                mainBody.setNewMessage("[USER]: " + option);
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[Warning]: Setup Resumed");
                System.out.println(mainBody.getLastMessage());
                if(option.equals("y")){
                    pathLetter = "C";
                    mainBody.setNewMessage("[System]: Please type out the Directory you would like to install System Files, You do not need to include the slashed \"\\\" ");
                    System.out.println(mainBody.getLastMessage());
                    mainBody.setNewMessage("[System]: EXAMPLE: \"Users\" _Enter_ \"* User *\" _Enter_ \"Documents\"_Enter_\"Done\"_Enter");
                    System.out.println(mainBody.getLastMessage());
                    mainBody.setNewMessage("[System]: Type \"Cancel\" to cancel and start over, Type \"Back\" to remove last directory ");
                    System.out.println(mainBody.getLastMessage());
                    String extraPath = "null";
                    int size;
                    String tempPath;
                    while(!extraPath.equals("done") || !extraPath.equals("Done")){ 
                        mainBody.clear();
                        Login.displaySolarLogo();
                        System.out.println();
                        System.out.println("Program Directory:");
                        System.out.println("==========================================");
                        tempPath = completeManualDir("C:");
                        mainBody.setNewMessage("[System]: Current Path Defined as: " + tempPath);
                        System.out.println(mainBody.getLastMessage());
                        extraPath =  customScanner.nextLine();  
                        if(extraPath.equals("cancel") || extraPath.equals("Cancel")){
                            dirPath.clear();
                            new mainBody();
                        }else if(extraPath.equals("back") || extraPath.equals("Back")){
                            size = dirPath.size();
                            size--;
                            dirPath.remove(size);
                        }else if(extraPath.equals("done") || extraPath.equals("Done")){
                            mainBody.setNewMessage("[System]: User Completed Program Directory Path");
                            System.out.println(mainBody.getLastMessage());
                            String temporaryPath = completeManualDir("");
                            setBaseDir(temporaryPath);
                            String Path = completeManualDir("C:");
                            createProgramDir(Path);
                            Login.loginPage();
                        }else if(!extraPath.equals("") || !extraPath.equals(" ")){
                            dirPath.add(extraPath);
                        } else{
                            dirPath.add(extraPath);
                        }
                    }
                }else if(option.equals("n")){
                    Login.displaySolarLogo();
                    System.out.println();
                    mainBody.setNewMessage("[System ERROR]: Skipping C: Drive");
                    System.out.println(mainBody.getLastMessage());
                }else{
                    mainBody.setNewMessage("[System ERROR]: Invalid Option, Restarting Setup...");
                    System.out.println(mainBody.getLastMessage());
                    autoSearchForDir();
                }
                break;
                case 4:
                pathLetter = "D";
                mainBody.setNewMessage("[Warning]: Drive \"D\" is usally used as a recovery drive");
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[System]: Would you like to use drive \" D \" for Installation");        
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[System]: Y/N?");
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[Warning]: Setup Halted, Awaiting User Response");
                System.out.println(mainBody.getLastMessage());
                String option2 = customScanner.nextLine();
                option2.toLowerCase();
                if(option2.equals("yes") || option2.equals("y")){
                    pathLetter = "D";
                    testDirBasic(pathLetter);
                }else{
                    mainBody.setNewMessage("[System]: Skipping drive \"D\" ");
                    System.out.println(mainBody.getLastMessage());
                }
                break;
                case 5:
                pathLetter = "E";
                testDirBasic(pathLetter);
                break;
                case 6:
                pathLetter = "F";
                testDirBasic(pathLetter);
                break;
                case 7:
                pathLetter = "G";
                testDirBasic(pathLetter);
                break;
                case 8:
                pathLetter = "H";
                testDirBasic(pathLetter);
                break;
                case 9:
                pathLetter = "I";
                testDirBasic(pathLetter);
                break;
                case 10:
                pathLetter = "J";
                testDirBasic(pathLetter);
                break;
                case 11:
                pathLetter = "K";
                testDirBasic(pathLetter);
                break;
                case 12:
                pathLetter = "L";
                testDirBasic(pathLetter);
                break;
                case 13:
                pathLetter = "M";
                testDirBasic(pathLetter);
                break;
                case 14:
                pathLetter = "N";
                testDirBasic(pathLetter);
                break;
                case 15:
                pathLetter = "O";
                testDirBasic(pathLetter);
                break;
                case 16:
                pathLetter = "P";
                testDirBasic(pathLetter);
                break;
                case 17:
                pathLetter = "Q";
                testDirBasic(pathLetter);
                break;
                case 18:
                pathLetter = "R";
                testDirBasic(pathLetter);
                break;
                case 19:
                pathLetter = "S";
                testDirBasic(pathLetter);
                break;
                case 20:
                pathLetter = "T";
                testDirBasic(pathLetter);
                break;
                case 21:
                pathLetter = "U";
                testDirBasic(pathLetter);
                break;
                case 22:
                pathLetter = "V";
                testDirBasic(pathLetter);
                break;
                case 23:
                pathLetter = "W";
                testDirBasic(pathLetter);
                break;
                case 24:
                pathLetter = "X";
                testDirBasic(pathLetter);
                break;
                case 25:
                pathLetter = "Y";
                testDirBasic(pathLetter);
                break;
                case 26:
                pathLetter = "Z";
                testDirBasic(pathLetter);
                break;
                default:
                mainBody.setNewMessage("[Warning]: No other Drives were Detected");
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[System]: Restarting Setup...");
                System.out.println(mainBody.getLastMessage());
                autoSearchForDir();
                break;
            }
        }
    }

    /**
     * Method completeManualDir
     *
     * @param Path A parameter
     * @return Pieces together the contents of dirPath as a normal Directory Path
     */
    public static String completeManualDir(String Path){
        int size = dirPath.size();
        mainBody.setNewMessage("[System]: Auto Completing directory path, Size: " + size);
        size--;
        for(int j = 0; j < dirPath.size(); j++){
            Path = Path +  "\\" + dirPath.get(j).trim();
        }
        mainBody.setNewMessage("[System]: Path Defined as " + Path);
        return Path;
    }

    /**
     * Method testDirBasic
     *
     * @param pathLetter A parameter
     * @return Success, if Directory exists, success == true; same as opposite.
     */
    public static boolean testDirBasic(String pathLetter){
        boolean success = false;
        File file = new File(pathLetter + ":");
        // If file doesn't exists, then create it
        if (!file.exists()) {
            if(success = false){
                mainBody.setNewMessage("[System]: Path Success: " + success);
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[System]: Failed to find Directory");
                System.out.println(mainBody.getLastMessage());
                success = false;
            }
            return success;
        }else{
            success = true;
            mainBody.setNewMessage("[System]: Success: " + success);
            System.out.println(mainBody.getLastMessage());
            mainBody.setNewMessage("[System]: Successfully found Directory");
            System.out.println(mainBody.getLastMessage());
            String path = pathLetter + ":";
            setPathLetter(path);
            createProgramDir(path);
            Login.loginPage();
            return success; 
        }
    }

    /**
     * Method createProgramDir
     * Directory and file Creator
     * @param Path A parameter
     */
    public static boolean createProgramDir(String Path) {
        Login.displaySolarLogo();
        System.out.println();
        try {
            String path;
            path = Path + defaultPath;
            setPath(path);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.exists()) {
                mainBody.setNewMessage("[System]: " + path + " was successfully created");
                System.out.println(mainBody.getLastMessage());
            } else {
                mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                System.out.println(mainBody.getLastMessage());
            }
            // above this comment is creating a default directory
            // below this comment is creating the invoices directory
            path = path + "\\Invoices/";
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.exists()) {
                mainBody.setNewMessage("[System]: " + path + " was successfully created");
                System.out.println(mainBody.getLastMessage());
            } else {
                mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                System.out.println(mainBody.getLastMessage());
            }
            // line break------------------------------------------------------//
            path = Path + defaultPath + "\\Reports/";
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.exists()) {
                mainBody.setNewMessage("[System]: " + path + " was successfully created");
                System.out.println(mainBody.getLastMessage());
            } else {
                mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                System.out.println(mainBody.getLastMessage());
            }
            // above this comment is creating a Reports directory
            // below this comment is creating a settings directory
            path = Path + defaultPath + "\\Settings/";
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.exists()) {
                mainBody.setNewMessage("[System]: " + path + " was successfully created");
                System.out.println(mainBody.getLastMessage());
            } else {
                mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                System.out.println(mainBody.getLastMessage());
            }
            // Settings files
            path = Path + defaultPath + "\\Settings/DisplayTimestamp.txt";
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                if (file.exists()) {
                    mainBody.setNewMessage("[System]: " + path + " was successfully created");
                    System.out.println(mainBody.getLastMessage());
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("true");
                    bw.close();
                    mainBody.setNewMessage("[System]: Successfully Set Setting for time");
                    System.out.println(mainBody.getLastMessage());
                } else {
                    mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                    System.out.println(mainBody.getLastMessage());
                }
            } else {
                mainBody.setNewMessage("[System]: " + path + " was successfully created");
                System.out.println(mainBody.getLastMessage());
            }
            boolean timeSet;
            BufferedReader in = new BufferedReader(new FileReader(new File(path)));
            int line = 0;
            for(String x= in.readLine(); x != null; x= in.readLine()){
                line++;
                mainBody.setNewMessage("[System]: Reading in Time Setting on Line: " + line);
                timeSet = Boolean.parseBoolean(x);
                mainBody.setTimeSet(timeSet);
            }
            in.close();
            path = Path + defaultPath + "\\Settings/ProgramDirectory.txt";
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                if (file.exists()) {
                    mainBody.setNewMessage("[System]: " + path + " was successfully created");
                    System.out.println(mainBody.getLastMessage());
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(Path + defaultPath);
                    bw.close();
                    mainBody.setNewMessage("[Notification]: Path written to file successfully");
                    System.out.println(mainBody.getLastMessage());
                } else {
                    mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                    System.out.println(mainBody.getLastMessage());
                }
            } else {
                mainBody.setNewMessage("[System]: " + path + " was successfully created");
                System.out.println(mainBody.getLastMessage());
            }
            path = Path + defaultPath + "\\Settings/" + "TaxValuetxt";
            file = new File(path);
            if(!file.exists()){
                file.createNewFile();
                if (file.exists()) {
                    mainBody.setNewMessage("[System]: " + path + " was successfully created");
                    System.out.println(mainBody.getLastMessage());
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("7.56");
                    bw.close();
                    mainBody.setNewMessage("[System]: Successfully Wrote Default Tax Value");
                    System.out.println(mainBody.getLastMessage());
                } else {
                    mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                    System.out.println(mainBody.getLastMessage());
                }
            }else if (file.exists()){
                mainBody.setNewMessage("[System]: Tax File Already Exists, Reading from file...");
                System.out.println(mainBody.getLastMessage());
            }
            in = new BufferedReader(new FileReader(new File(path)));
            line = 0;
            for(String x= in.readLine(); x != null; x= in.readLine()){
                line++;
                mainBody.setNewMessage("[System]: Converting Tax from a String to a double, Line: " + line);
                double conversion = Double.parseDouble(x);
                setTax(conversion);
            }
            in.close();
            mainBody.setNewMessage("[System]: Tax Value set");
            System.out.println(mainBody.getLastMessage());
            path = getPath() + "\\Settings/Version.txt";
            file = new File(path);
            if(!file.exists()){
                try{
                    file.createNewFile();
                    mainBody.setNewMessage("[System]: Version File Created, Applying Default Version...");
                    System.out.println(mainBody.getLastMessage());
                    String Version = "ALPHA TEST";
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(Version);
                    bw.close();
                    mainBody.setNewMessage("[System]: " + Version + " Written to File");
                    System.out.println(mainBody.getLastMessage());
                    setVersion(Version);
                }catch(Exception e){
                    e.printStackTrace();
                    mainBody.setNewMessage("[Warning]: Failed to create Version File");
                    System.out.println(mainBody.getLastMessage());
                }
            }else{
                mainBody.setNewMessage("[System]: Found Version File...");
                System.out.println(mainBody.getLastMessage());
                mainBody.setNewMessage("[System]: Reading Version File...");
                System.out.println(mainBody.getLastMessage());
                in = new BufferedReader(new FileReader(new File(path)));
                line = 0;
                for(String x= in.readLine(); x != null; x= in.readLine()){
                    line++;
                    setVersion(x);
                }
                mainBody.setNewMessage("[System]: Version set");
                System.out.println(mainBody.getLastMessage());
            }
            path = getPath();
            path = path + "\\debugNotepad/";
            file = new File(path);
            if(!file.exists()){
                file.mkdir();
                if(file.exists()){
                    mainBody.setNewMessage("[System]: " + path + " was successfully created");
                    System.out.println(mainBody.getLastMessage());
                }else{
                    mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                    System.out.println(mainBody.getLastMessage());
                }
            }
            path = getPath();
            // Admin File
            path = Path + defaultPath + "\\Users/";
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
                if (file.exists()) {
                    mainBody.setNewMessage("[System]: " + path + " was successfully created");
                    System.out.println(mainBody.getLastMessage());
                } else {
                    mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                    System.out.println(mainBody.getLastMessage());
                }
            }
            path = path + "admin.txt";
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                if (file.exists()) {
                    mainBody.setNewMessage("[System]: " + path + " was successfully created");
                    System.out.println(mainBody.getLastMessage());
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("adminPassword");
                    bw.close();
                    mainBody.setNewMessage("[Notification]: Admin Password written to file successfully");
                    System.out.println(mainBody.getLastMessage());
                } else {
                    mainBody.setNewMessage("[Warning]: Failed to create directory at " + path);
                    System.out.println(mainBody.getLastMessage());
                }
             
                //next Step in process
                Login.setUser("Null");
                return true;
            }else{
                Login.setUser("Null");
                return false;
                //next Step in Process
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }
}
