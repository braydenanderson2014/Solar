import java.util.*;
//import java.math.*;
import java.text.*;
/**
 * Write a description of class POS here.
 *
 * @author (Brayden Anderson)
 * @version (Base Version: ALPHA V2.1.4, Snapshot 3xWav-6)
 */
public class POS{
    private static double Savings = 0;//$amount saved
    private static ArrayList<String> itemOnInvoice = new ArrayList<String>();//Items on Current invoice
    private static ArrayList<Double> pricesForInvoice = new ArrayList<Double>();//Prices for current invoice (in same order as itemsOnInvoice Array[])
    // private static ArrayList<String> allItemsSold = new ArrayList<String>();//List of all items sold from all invoices
    // private static ArrayList<Double> allPricesSold = new ArrayList<Double>();//List of all prices for items sold on all invoices (Same order as allItemsSold Array[])
    private static ArrayList<Double> invoiceSavings = new ArrayList<Double>();//all invoice savings
    //private static int invoiceNum = invoice.invoiceNumGenerator();//gets a invoice number from invoice class to use for a receipt
    private static double Subtotal;//invoice subtotal
    private static DecimalFormat df = new DecimalFormat("0.00");//Decimal Formatter... converts decimals from 0.000000+ to 0.00 format
    private static customScanner scan = new customScanner(); //Gets an instance of customScanner class which implements the Scanner class(Scanner scan = new Scanner(System.in);)
    private static double origSubtotal;//original Subtotal Before Discount
    private static double fullInvoiceDiscount; // Holds invoice level discount amount Percentage
    private static double origTotal = 0;// original Total
    private static double amountT = 0;//Amount Tendered
    private static double amountR = 0;//Amount Remaining
    /**
     * POS Constructor
     *
     */
    public POS(){//new instance of POS class
        POSMenu();//goto POSMenu Function
    }
    public static String viewItemsOnInvoice(){
        int num = 1;
            if(itemOnInvoice.size() > 0){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Items On Invoice:");
                System.out.println("========================================");
                for(int i = 0; i < itemOnInvoice.size(); i++){
                    System.out.println("Item: \"" + itemOnInvoice.get(i) + "\" Price: " + pricesForInvoice.get(i) + "$");
                    num++;
                }
            }else{
                mainBody.setNewMessage("[System]: No Items On Invoice");
                System.out.println(mainBody.getLastMessage());
            }
        return "";
    }
    /**
     * Method POSMenu
     * POS Menu
     */
    public static void POSMenu(){
        Login.displaySolarLogo();
        System.out.println("POS Menu:");
        System.out.println();
        String user = Login.getUser(); //Get Current User Logged in
        System.out.println("Welcome: " + user);
        System.out.println("========================================");
        System.out.println("[CAT]: Categories"); //[Menu item code] item name
        System.out.println("[MAN]: Manual Entry");
        System.out.println("[APP]: Apply Discount");
        if(user.equals("admin") || user.equals("test")){
            System.out.println("[RIT]: Return items");
        }
        System.out.println("[VII]: View Items on Invoice");
        System.out.println("[SIL]: Save Invoice for later");
        System.out.println("[CLS]: Clear Sales Data");
        System.out.println("[TOT]: Total");
        System.out.println("[RET]: Return to Main Menu");
        System.out.println();
        double savings = 0; //reset savings amount fo recalculation
        Subtotal = 0; //reset Subtotal amount
        origSubtotal = 0; //reset origSubtotal amount
        for(int j = 0; j < invoiceSavings.size(); j++){
            savings = savings + invoiceSavings.get(j);
        }
        for(int i = 0; i < pricesForInvoice.size(); i++){
            Subtotal = Subtotal + pricesForInvoice.get(i);
            origSubtotal = origSubtotal + pricesForInvoice.get(i);
        }
        //Subtotal = Subtotal - savings;
        if(Subtotal == 0){
            System.out.println("Subtotal: $" + df.format(0.00));
        }else if(Subtotal > 0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }else if(Subtotal < 0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }
        double tempSavingsHolder = 0;
        if(invoiceSavings.size() > 0){
            for(int i = 0; i < invoiceSavings.size(); i++){
                tempSavingsHolder = tempSavingsHolder + invoiceSavings.get(i);//Temporaily holds the savings amount
            }
            if(tempSavingsHolder > 0){
                System.out.println("Savings: $" + savings * (-1));
            }
        }
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
        String option = customScanner.nextLine().toLowerCase();
        switch (option) {
            case "ret":
                mainBody.mainMenu();
            break;
            case "cat":
                categories();
            break;
            case "man":
                manualEntry();
            break;
            case "app":
                if(Subtotal == 0){
                    mainBody.setNewMessage("[System]: In order to apply discount, subtotal cannot be 0$");
                    POSMenu();
                }else if(Subtotal < 0){
                    mainBody.setNewMessage("[System]: In order to apply discount, subtotal cannot be negative");
                    POSMenu();
                }else{
                    addDiscount();
                    POSMenu();
                }
            break;
            case "rit":
                if(!user.equals("test") || !user.equals("admin")){
                    mainBody.setNewMessage("[System]: This is an Administrative Feature, Please check your Permissions");
                    POSMenu();
                }else{
                    mainBody.setNewMessage("[System]: This Feature is not yet Available");
                    POSMenu();
                }
            break;
            case "vii":
                viewItemsOnInvoice();
                System.out.println("Press Enter to Continue");
                String Enter = customScanner.nextLine();
                POSMenu();
            break;
            case "sil":
                mainBody.setNewMessage("[System]: This Feature is not yet Available");
                POSMenu();
            break;
            case "cls":
                mainBody.setNewMessage("[System]: Are You Sure you want To Clear Sales Data?");
                System.out.println(mainBody.getLastMessage());
                String answer = customScanner.nextLine().toLowerCase();
                if(answer.equals("y") || answer.equals("yes")){
                    pricesForInvoice.clear();
                    itemOnInvoice.clear();
                    invoiceSavings.clear();
                    Savings = 0;
                    mainBody.setNewMessage("[System]: Sales Data Cleared");
                    POSMenu();
                }else if(answer.equals("n") || answer.equals("no")){
                    mainBody.setNewMessage("[System]: No Sales Data Cleared");
                    POSMenu();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option");
                    POSMenu();
                }
            case "tot":
                mainBody.setNewMessage("[System]: Calculating Total...");
                Total();
            default:
                mainBody.setNewMessage("[Warning]: Invalid Option: Please make sure you have the proper permissions");
                POSMenu();
            break;
        }
    }/**
     * Method categories
     * Categories for POS items
     * Menu Logic is not neccessarily in the same order as the Menu Items
     */
    private static void categories(){
        Login.displaySolarLogo();
        System.out.println();
        mainBody.setNewMessage("[System]: Welcome to the Categories Page, Please select a categorey");
        System.out.println("Categories: ");
        System.out.println("========================================");
        System.out.println("[APP]: Appliances");
        System.out.println("[COM]: Computer And Electronics");
        System.out.println("[FUR]: Furniture");
        System.out.println("[DEC]: Decor");
        System.out.println("[KIT]: General Kithen Items (NON APPLIANCE)");
        System.out.println("[MAN]: Manual Entry");
        System.out.println("[RET]: Return");
        System.out.println();
        System.out.println("Console: ");
        Subtotal = 0;
        for(int i = 0; i < pricesForInvoice.size(); i++){
            Subtotal = Subtotal + pricesForInvoice.get(i);
        }
        if(Subtotal == 0){
            System.out.println("Subtotal: $" + df.format(0.00));
        }else if(Subtotal > 0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }else if(Subtotal < 0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }
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
        String option = customScanner.nextLine().toLowerCase().trim();
        if(option.equals("ret")){
            POSMenu();
        }else if(option.equals("app")){
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Appliances: ");
            System.out.println("==========================================");
            System.out.println("[FRI]: Fridge");
            System.out.println("[MIC]: Microwave");
            System.out.println("[DIS]: Dishwasher");
            System.out.println("[TOA]: Toaster");
            System.out.println("[OVE]: Oven, Range, Cooktop");
            System.out.println("[BLE]: Blender");
            System.out.println("[MIX]: Mixer");
            System.out.println("[RET]: Return to Menu");
            System.out.println();
            Subtotal = 0;
            for(int i = 0; i < pricesForInvoice.size(); i++){
                Subtotal = Subtotal + pricesForInvoice.get(i);
            }
            if(Subtotal == 0){
                System.out.println("Subtotal: $" + df.format(0.00));
            }else if(Subtotal > 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }else if(Subtotal < 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }
            String option2 = customScanner.nextLine();
            if(option2.equals("ret")){
                 categories();
            }else if(option2.equals("fri")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Fridge: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Fridge", Price);
                categories();
            }else if(option2.equals("mix")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Mixer: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Mixer", Price);
                categories();
            }else if(option2.equals("mic")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Microwave: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Microwave", Price);
                categories();
            }else if(option2.equals("dis")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Dishwasher: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Dishwasher", Price);
                categories();
            }else if(option2.equals("toa")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Toaster: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Toaster", Price);
                categories();
            }else if(option2.equals("ove")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Oven, Range, Cooktop: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Oven, Range, Cooktop", Price);
                categories();
            }else if(option2.equals("ble")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Blender: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Blender", Price);
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("com")){
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Computer and Electronics: ");
            System.out.println("==========================================");
            System.out.println("[LAP]: Laptops");
            System.out.println("[DES]: Desktops");
            System.out.println("[SER]: Server");
            System.out.println("[TEL]: Telephones");
            System.out.println("[TEV]: Television");
            System.out.println("[HAR]: Hardware");
            System.out.println("[SOF]: Software");
            System.out.println("[RET]: Return to Menu");
            System.out.println();
            Subtotal = 0;
            for(int i = 0; i < pricesForInvoice.size(); i++){
                Subtotal = Subtotal + pricesForInvoice.get(i);
            }
            if(Subtotal == 0){
                System.out.println("Subtotal: $" + df.format(0.00));
            }else if(Subtotal > 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }else if(Subtotal < 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }
            String option2 = customScanner.nextLine();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("lap")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Laptops");
                System.out.println("========================================");
                System.out.println("[GAM]: Gaming Laptops");
                System.out.println("[NOR]: Normal Laptops");
                System.out.println("[RET]: Return to Menu");
                System.out.println();
                Subtotal = 0;
                for(int i = 0; i < pricesForInvoice.size(); i++){
                    Subtotal = Subtotal + pricesForInvoice.get(i);
                }
                if(Subtotal == 0){
                    System.out.println("Subtotal: $" + df.format(0.00));
                }else if(Subtotal > 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }else if(Subtotal < 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }
                String option3 = customScanner.nextLine();
                if(option3.equals("ret")){
                    categories();
                }else if(option3.equals("gam")){
                    Login.displaySolarLogo();
                System.out.println();
                System.out.println("Gaming Laptops: ");
                System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Gaming Laptop", Price);
                    categories();
                }else if(option3.equals("nor")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Normal Laptops: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Normal Laptop", Price);
                    categories();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                    categories();
                }
            }else if(option2.equals("des")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Desktop");
                System.out.println("========================================");
                System.out.println("[GAM]: Gaming Desktop");
                System.out.println("[NOR]: Normal Desktop");
                System.out.println("[RET]: Return to Menu");
                System.out.println();
                Subtotal = 0;
                for(int i = 0; i < pricesForInvoice.size(); i++){
                    Subtotal = Subtotal + pricesForInvoice.get(i);
                }
                if(Subtotal == 0){
                    System.out.println("Subtotal: $" + df.format(0.00));
                }else if(Subtotal > 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }else if(Subtotal < 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }
                String option3 = customScanner.nextLine();
                if(option3.equals("ret")){
                    categories();
                }else if(option3.equals("gam")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Gaming Desktop: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Gaming Desktop", Price);
                    categories();
                }else if(option3.equals("nor")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Normal Desktop: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Normal Desktop", Price);
                    categories();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                    categories();
                }
            }else if(option2.equals("ser")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Server:");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Server", Price);
                categories();
            }else if(option2.equals("tel")){
                //telephone
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Telephones: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Telephones", Price);
                categories();
            }else if(option2.equals("tev")){
                //TV
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Televison (TV): ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Television (TV)", Price);
                categories();
            }else if(option2.equals("har")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Hardware: ");
                System.out.println("==========================================");
                System.out.println("[CPU]: Central Processing Unit (Processor)");
                System.out.println("[GPU]: Graphics Processing Unit (Graphics Card)");
                System.out.println("[RAM]: Random Access Memory_Module (Ram stick/Memory module)");
                System.out.println("[POW]: Power Supply");
                System.out.println("[MOT]: Mother Board");
                System.out.println("[THE]: Thermal Paste");
                System.out.println("[FAN]: Fans");
                System.out.println("[CAS]: Cases");
                System.out.println("[MON]: Monitor");
                System.out.println("[MOU]: Mouse");
                System.out.println("[KEY]: Keyboard");
                System.out.println("[RET]: Return");
                System.out.println();
                Subtotal = 0;
                for(int i = 0; i < pricesForInvoice.size(); i++){
                    Subtotal = Subtotal + pricesForInvoice.get(i);
                }
                if(Subtotal == 0){
                    System.out.println("Subtotal: $" + df.format(0.00));
                }else if(Subtotal > 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }else if(Subtotal < 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }
                String option3 = customScanner.nextLine().toLowerCase();
                if(option3.equals("ret")){
                    mainBody.setNewMessage("[System]: User Canceled Category Selection");
                    categories();
                }else if(option3.equals("cpu")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Central Processing Unit (CPU): ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("CPU", Price);
                    categories();
                }else if(option3.equals("gpu")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Graphical Processing Unit (GPU): ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("GPU", Price);
                    categories();
                }else if(option3.equals("ram")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Random Access Memory (RAM MODULE): ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("RAM Module", Price);
                    categories();
                }else if(option3.equals("pow")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Power Supply: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Power Supply", Price);
                    categories();
                }else if(option3.equals("mot")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Motherboard: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("MotherBoard", Price);
                    categories();
                }else if(option3.equals("the")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Thermal Paste: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Thermal Paste", Price);
                    categories();
                }else if(option3.equals("fan")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Fans: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Fans", Price);
                    categories();
                }else if(option3.equals("cas")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Case: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Case", Price);
                    categories();
                }else if(option3.equals("mon")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Monitor: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Monitor", Price);
                    categories();
                }else if(option3.equals("mou")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Mouse: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Mouse", Price);
                    categories();
                }else if(option3.equals("key")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Keyboard: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Keyboard", Price);
                    categories();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                    categories();
                }
            }else if(option2.equals("sof")){//fix
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Software: ");
                System.out.println("==========================================");
                System.out.println("[RET]: Return");
                System.out.println();
                Subtotal = 0;
                for(int i = 0; i < pricesForInvoice.size(); i++){
                    Subtotal = Subtotal + pricesForInvoice.get(i);
                }
                if(Subtotal == 0){
                    System.out.println("Subtotal: $" + df.format(0.00));
                }else if(Subtotal > 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }else if(Subtotal < 0){
                    System.out.println("Subtotal: $" + df.format(Subtotal));
                }
                System.out.println("Name of Program: ");
                String name = customScanner.nextLine();
                if(name.equals("ret") || name.equals("RET")){
                    mainBody.setNewMessage("[System]: User Canceled Category Selection");
                    categories();
                }else{
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Software: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem(name, Price);
                    categories();
                }
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("fur")){
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Furniture: ");
            System.out.println("==========================================");
            System.out.println("[COU]: Couch");
            System.out.println("[CHA]: Chair");
            System.out.println("[TAB]: Tables");
            System.out.println("[BEN]: Benches");
            System.out.println("[RET]: Return to Menu");
            System.out.println();
            Subtotal = 0;
            for(int i = 0; i < pricesForInvoice.size(); i++){
                Subtotal = Subtotal + pricesForInvoice.get(i);
            }
            if(Subtotal == 0){
                System.out.println("Subtotal: $" + df.format(0.00));
            }else if(Subtotal > 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }else if(Subtotal < 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }
            String option2 = customScanner.nextLine();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("cou")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Couch: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Couch", Price);
                categories();
            }else if(option2.equals("cha")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Chair: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Chair", Price);
                categories();
            }else if(option2.equals("tab")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Table: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Tables", Price);
                categories();
            }else if(option2.equals("ben")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Bench: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Bench", Price);
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("dec")){
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Decor: ");
            System.out.println("==========================================");
            System.out.println("[PIC]: Pictures/Picture Frames");
            System.out.println("[KNI]: Knick Knacks");
            System.out.println("[STU]: Stuffed Animals");
            System.out.println("[RET]: Return to Menu");
            System.out.println();
            Subtotal = 0;
            for(int i = 0; i < pricesForInvoice.size(); i++){
                Subtotal = Subtotal + pricesForInvoice.get(i);
            }
            if(Subtotal == 0){
                System.out.println("Subtotal: $" + df.format(0.00));
            }else if(Subtotal > 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }else if(Subtotal < 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }
            String option2 = customScanner.nextLine().toLowerCase();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("pic")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Pictures/Picture Frames: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Pictures/Picture Frames", Price);
                categories();
            }else if(option2.equals("kni")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Knick Knacks: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("General Knick Knacks", Price);
                categories();
            }else if(option2.equals("stu")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Stuffed Animals: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Stuffed Animals", Price);
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("kit")){
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Kitchen Items (Non Appliance): ");
            System.out.println("==========================================");
            System.out.println("[POT]: Pots");
            System.out.println("[PAN]: Pan");
            System.out.println("[CUT]: Cutting Board");
            System.out.println("[SIL]: Silverware");
            System.out.println("[RET]: Return to Menu");
            System.out.println();
            Subtotal = 0;
            for(int i = 0; i < pricesForInvoice.size(); i++){
                Subtotal = Subtotal + pricesForInvoice.get(i);
            }
            if(Subtotal == 0){
                System.out.println("Subtotal: $" + df.format(0.00));
            }else if(Subtotal > 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }else if(Subtotal < 0){
                System.out.println("Subtotal: $" + df.format(Subtotal));
            }
            String option2 = customScanner.nextLine();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("pot")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Pots: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Pot", Price);
                categories();
            }else if(option2.equals("pan")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Pans: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Pan", Price);
                categories();
            }else if(option2.equals("cut")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Cutting Board: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Cutting Board", Price);
                categories();
            }else if(option2.equals("sil")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Silverware: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Silverware", Price);
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("man")){
            manualEntry();
        }else{
            mainBody.setNewMessage("[System]: Invalid Option, try again");
            categories();
        }
    }
    public static double setTotal(double setTotal){
        origTotal = setTotal;
        return origTotal;
    }
    public static double getTotal(){
        return origTotal;
    }
    public static void Total(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Total: ");
        System.out.println("==========================================");
        double total = 0;
        double taxP = Setup.getTax();//Tax Percentage
        double taxD = Setup.getTax()/100;//Tax as a Decimal
        if(Subtotal < 0){
            mainBody.setNewMessage("[Warning]: Total Cannot be Negative");
            POSMenu();
        }else if(Subtotal == 0){
            mainBody.setNewMessage("[Warning]: Total is 0$, Would you like to process Invoice or Cancel?");
            System.out.println(mainBody.getLastMessage());
            System.out.println("1. [PRO]: Process Invoice");
            System.out.println("2. [CAN]: Cancel");
            System.out.println("Selection: ");
            String selection = customScanner.nextLine();
            selection.toLowerCase();
            if(selection.equals("1") || selection.equals("pro")){
                //create invoice
                mainBody.setNewMessage("[System]: This Feature is not Yet Available...");
                POSMenu();
            }else if(selection.equals("can") || selection.equals("2")){
                mainBody.setNewMessage("[System]: User Cancelled Payment");
                POSMenu();
            }else{
                mainBody.setNewMessage("[Warning]: Invalid option selected...");
                POSMenu();
            }
        }
        viewItemsOnInvoice();
        System.out.println();
        System.out.println("Subtotal: " + Subtotal + "$");//Subtotal
        System.out.println("Discounts: " + df.format(Savings) + "$");//Discounts
        setTotal(Subtotal);
        System.out.println("Tax%: " + taxP + "%" + " Tax Amount: " + df.format(origTotal * taxD) + "$");//Tax Percentage and then the $ Amount of tax
        origTotal = origTotal * taxD; //Dollar amount of tax Calculated
        origTotal = origTotal + Subtotal;//Total amount
        System.out.println("Total: " + df.format(origTotal));
        System.out.println();
        df.format(origTotal);
        setTotal(origTotal);
        calcAmountR();
        paymentMenu();
    }
    public static double calcAmountR(){// amount CT = Amount Currently Tendered
        amountR = origTotal - amountT;
        return amountR;
    }

    public static double getAmountR(){
        return amountR;
    }
    public static void paymentMenu(){
        Login.displaySolarLogo();
        System.out.println();
        double origTotal = getTotal();
        double amountT = 0;
        System.out.println("Payment Options");
        System.out.println("========================================");
        System.out.println("Amount Remaining: " + df.format(getAmountR()));
        System.out.println("[CAS]: Cash Payment");
        System.out.println("[CHE]: Check Payment");
        System.out.println("[CAR]: Card Payment");
        System.out.println("[CAN]: Cancel Payment");
        System.out.println("[TOT]: Show Total");
        String option = customScanner.nextLine();
        option.toLowerCase();
        //boolean success = updateTenderedAmount(amountT);
        if(option.equals("cas")){
            cashPayment();
        }else if(option.equals("che")){
            checkPayment();
        }else if(option.equals("car")){
            cardPayment();
        }else if(option.equals("tot")){
            double total = 0;
            double taxP = Setup.getTax();//Tax Percentage
            double taxD = Setup.getTax()/100;//Tax as a Decimal
            viewItemsOnInvoice();
            System.out.println("Subtotal: " + Subtotal + "$");//Subtotal
            System.out.println("Discounts: " + df.format(Savings) + "$");//Discounts
            setTotal(Subtotal);
            System.out.println("Tax%: " + taxP + "%" + " Tax Amount: " + df.format(origTotal * taxD) + "$");//Tax Percentage and then the $ Amount of tax
            origTotal = origTotal * taxD; //Dollar amount of tax Calculated
            origTotal = origTotal + Subtotal;//Total amount
            System.out.println("Total: " + df.format(origTotal));
            System.out.println("Press Enter to Continue");
            String enter = customScanner.nextLine();
            paymentMenu();
        }else if(option.equals("can")){
            POSMenu();
        }else{
            mainBody.setNewMessage("[Warning]: Improper Selection, try again");
            System.out.println(mainBody.getLastMessage());
            paymentMenu();
        }
    }
    public static void calcChange(){// total amount, amount paid
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Appliances: ");
        System.out.println("==========================================");
        if(amountT == origTotal){
            System.out.println("No Change Due");
        }else{
            double amountD = amountT - origTotal;//amount due
            System.out.println("Change due: " + df.format(amountD) + "$");
            System.out.println("Press Enter To Continue");
            String Enter = customScanner.nextLine();
        }
        pricesForInvoice.clear();
        itemOnInvoice.clear();
        invoiceSavings.clear();
        Savings = 0;
        amountR = 0;
        amountT = 0;
        mainBody.setNewMessage("[System]: Invoice Processed");
        POSMenu();
        //TO DO:
        //INVOICE
    }
    public static void cashPayment(){
        Login.displaySolarLogo();
        System.out.println();   
        System.out.println("Cash: ");
        System.out.println("========================================");
        System.out.println("Amount Remaining: " + df.format(getAmountR()));
        System.out.println("Type 0 to Return to Payment Menu");
        System.out.println("Amount To Tender: ");
        double cashP = scan.nextDouble();
        if(cashP == 0){
            paymentMenu();
        }else if(cashP > 0){
            boolean Success = updateTenderedAmount(cashP);
            if(Success){
                mainBody.setNewMessage("[System]: Success!");
                System.out.println(mainBody.getLastMessage());
                calcChange();
            }else{
                cashPayment();
            }
        }else if(cashP < 0){
            mainBody.setNewMessage("[Warning]: Amount Cannot Be Under 0$");
            cashPayment();
        }
    }
    public static void checkPayment(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Check: ");
        System.out.println("========================================");
        System.out.println("Amount Remaining: " + df.format(getAmountR()));
        System.out.println("Type 0 to Return to Payment Menu");
        double checkP = scan.nextDouble();
        if(checkP == 0){
            paymentMenu();
        }else if(checkP > 0){
            boolean Success = updateTenderedAmount(checkP);
            if(Success){
                mainBody.setNewMessage("[System]: Success!");
                System.out.println(mainBody.getLastMessage());
                calcChange();
            }
        }else if(checkP < 0){
            mainBody.setNewMessage("[Warning]: Amount Cannot be Under 0$");
            checkPayment();
        }
    }
    public static void cardPayment(){
        Login.displaySolarLogo();
        System.out.println();    
        System.out.println("Card: ");
        System.out.println("========================================");
        System.out.println("Amount Remaining: " + df.format(getAmountR()));
        System.out.println("Type 0 to Return to Payment Menu");
        double cardP = scan.nextDouble();
        if(cardP == 0){
            paymentMenu();
        }else if(cardP > 0){
            boolean Success = updateTenderedAmount(cardP);
            if(Success){
                mainBody.setNewMessage("[System]: Success!");
                System.out.println(mainBody.getLastMessage());
                calcChange();
            }
        }else if(cardP < 0){
            mainBody.setNewMessage("[Warning]: Amount Cannot be Under 0$");
            cardPayment();
        }
    }
    public static boolean updateTenderedAmount(double amountToT){
        amountT = amountT + amountToT;
        calcAmountR();
        System.out.println("Amount Tendered: " + amountT);
        double Total = getTotal();
        if(amountT >= Total){
            mainBody.setNewMessage("[System]: Processing Total");
            System.out.println(mainBody.getLastMessage());
            return true;
        }else{
            return false;
        }
    }
    /**
     * Method manualEntry
     * Manual Item input for POS
     */
    private static void manualEntry(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Manual Entry: ");
        System.out.println("==========================================");
        System.out.println("Type \"[CAT]\" to go to Categories");
        Subtotal = 0;
        for(int i = 0; i < pricesForInvoice.size(); i++){
            Subtotal = Subtotal + pricesForInvoice.get(i);
        }
        if(Subtotal == 0){
            System.out.println("Subtotal: $" + df.format(0.00));
        }else if(Subtotal > 0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }else if(Subtotal < 0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }
        System.out.println("Manual Entry: ");
        String manualEntry = customScanner.nextLine();
        if(manualEntry.equals("cat")){
            categories();
        }else if(manualEntry.equals("ret")){
            POSMenu();
        }else{
            System.out.println("Price: ");
            double manualPrice = scan.nextDouble();
            addItem(manualEntry, manualPrice);
            POSMenu();
        }
    }

    /**
     * Method addItem
     *
     * @param item A parameter
     * @param price A parameter
     * @return item value/ Useless Value
     */
    public static String addItem(String item, double price){
        itemOnInvoice.add(item);
        pricesForInvoice.add(price);
        if(itemOnInvoice.contains(item) && pricesForInvoice.contains(price)){
            mainBody.setNewMessage("[System]: " + item + " $" + price);
        }else if(itemOnInvoice.contains(item) && !pricesForInvoice.contains(price)){
            mainBody.setNewMessage("[System ERROR]: Failed to add Price of $" + price + " to " + item + ", Removing item...");
            itemOnInvoice.remove(item);
        }else if(!itemOnInvoice.contains(item) && pricesForInvoice.contains(price)){
            mainBody.setNewMessage("[System ERROR]: Failed to add Item: " + item + ", removing price of $" + price);
            pricesForInvoice.remove(price);
        }else{
            mainBody.setNewMessage("[System ERROR]: Failed to add Item: " + item + ", and Failed to add price: $" + price);
        }
        return item;
    }

    /**
     * Method addDiscount
     * adds discount to item or invoice
     * @return The return value
     */
    public static double addDiscount(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Apply Discount");
        System.out.println("========================================");
        System.out.println("Would you like to Apply Discount to a specific item or total invoice?");
        System.out.println("1. Sigle item");
        System.out.println("2. Total Invoice");
        int selection = customScanner.nextInt();
        switch(selection){//single item discount
            case 1:
            int item = 0;//numbers for items
            int choice = 0;//user choice
            double dAmountOff;//dollar amount off
            double pAmountOff;//percent off
            System.out.println("Which Item: ");
            for(int i = 0; i < itemOnInvoice.size(); i++){
                item++;
                System.out.println(item + ". " + itemOnInvoice.get(i) + " $" + pricesForInvoice.get(i));//Print items and their respective prices
            }
            System.out.println("Choice: ");
            try{
                choice = customScanner.nextInt();
                choice--;
                System.out.println("Item Selected: " + itemOnInvoice.get(choice) + " $" + pricesForInvoice.get(choice));
            }catch(Exception e){
                mainBody.setNewMessage("[System ERROR]: Invalid Option");
                e.printStackTrace();
                mainBody.setNewMessage("[System ERROR]: " + e.toString());
                POSMenu();
            }
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Single Item Discount: ");
            System.out.println("==========================================");
            System.out.println("1. $off");
            System.out.println("2. %off");
            System.out.println("Choice: ");
            int choices = 0;
            choices = customScanner.nextInt();
            if(choices == 1){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Single Item Discount ($ Amount Off): ");
                System.out.println("==========================================");
                System.out.println("$ Amount Off: ");
                dAmountOff = scan.nextDouble();//The Amount off
                //dAmountOff = dAmountOff;
                double placeHolder = choice;
                placeHolder = placeHolder - dAmountOff;
                pricesForInvoice.set(choice, placeHolder);
                itemOnInvoice.set(choice, itemOnInvoice.get(choice) + "*Discounted*");
                invoiceSavings.add(dAmountOff * (-1));
                mainBody.setNewMessage("[System]: Discount Applied to " + itemOnInvoice.get(choice));
                POSMenu();
            }else if(choices == 2){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Single Item Discount (% off): ");
                System.out.println("==========================================");
                mainBody.setNewMessage("[System]: Please write as a % and not a decimal");
                double chosenOption;
                String chosenStringValue = itemOnInvoice.get(choice);
                chosenOption = pricesForInvoice.get(choice);//simplified code
                System.out.println(mainBody.getLastMessage());
                System.out.println("% Off: ");
                pAmountOff = scan.nextDouble();//percent off as stated by User
                if(pAmountOff > 100 || pAmountOff < 0){
                    mainBody.setNewMessage("[System]: Invalid Percentage, try again");
                    POSMenu();
                }else{
                    pAmountOff = 100 - pAmountOff;
                    pAmountOff = pAmountOff / 100;//converts to a decimal
                    double placeHolder = chosenOption * pAmountOff;//takes the value of the6 chosen item and multiplies it by the decimal form of the percent
                    double discountedAmount = chosenOption - placeHolder;//takes the value of the chosen item and subtracts the placeHolder variable from it.
                    invoiceSavings.add(discountedAmount * (-1));
                    itemOnInvoice.set(choice, chosenStringValue + "*Discounted*");
                    pricesForInvoice.set(choice, placeHolder);//edits the original price.
                    mainBody.setNewMessage("[System]: Discount Applied to " + itemOnInvoice.get(choice));
                    POSMenu();
                }
            }
            break;
            case 2:
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Invoice Level Discount: ");
            System.out.println("==========================================");
            double subtotal = 0;
            if(fullInvoiceDiscount > 0){
                mainBody.setNewMessage("[System]: A Invoice Level Discount was already applied.");
                System.out.println(mainBody.getLastMessage());
            }else if(fullInvoiceDiscount < 0){
                mainBody.setNewMessage("[System]: Warning, Invoice Level Discount cannot be Negative! Setting Discount to 0");
                fullInvoiceDiscount = 0;
                POSMenu();
            }else{
                System.out.println("1. $ off");
                System.out.println("2. % off");
                int option = customScanner.nextInt();
                if(option == 1){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Invoice Level Discount ($ off): ");
                    System.out.println("==========================================");
                    System.out.println("$: ");
                    dAmountOff = scan.nextDouble();
                    dAmountOff = dAmountOff * -1;
                    addItem("Discount", 0);
                    Savings = Savings + dAmountOff;
                    invoiceSavings.add(Savings);
                    return dAmountOff;
                }else if(option == 2){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Invoice Level Discount (% off): ");
                    System.out.println("==========================================");
                    System.out.println("%: ");
                    double percentOff = scan.nextDouble();//% off
                    subtotal = 0;
                    double savingsAmount;//$amount off based of percentage
                    for(int i = 0; i < pricesForInvoice.size(); i++){
                        subtotal = subtotal + pricesForInvoice.get(i);//getting value for subtotal
                    }
                    System.out.println("Subtotal: " + subtotal);//displays subtotal
                    df.format(percentOff);//formats percent to 0.00 appearance
                    System.out.println("Percent: " + percentOff);//displays percent off decimal
                    percentOff = percentOff / 100; //converts percent to decimal
                    System.out.println("Percent: " + percentOff);
                    savingsAmount = subtotal * percentOff; // savings calc
                    System.out.println("Savings: " + savingsAmount);// savings amount displays
                    Savings = (Savings + savingsAmount);
                    System.out.println("Savings: " + savingsAmount);
                    invoiceSavings.add(Savings);// adds savings to total savings
                    addItem("Discount", 0);
                    return subtotal;
                }else{
                    mainBody.setNewMessage("Invalid Option");
                    return 0.00;
                }
                    }
                    break;
                    default:
                    mainBody.setNewMessage("[System]: Invalid Option, try again");
                    POSMenu();
                    break;
                }

        return 0.00;
    }

    /**
     * Method updateSubtotalDiscount
     *
     * @return subtotal discount
     */
    public static boolean updateSubtotalDiscount(){
        return true;
        //not implemented yet
    }
}
