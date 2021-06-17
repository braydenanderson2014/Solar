import java.util.*;
//import java.math.*;
import java.text.*;
/**
 * Write a description of class POS here.
 *
 * @author (Brayden Anderson)
 * @version (Base Version: Beta 1.0.1, Snapshot 3xWav-7)
 */
public class POS{
    private static double Savings = 0;//$amount saved
    private static ArrayList<String> itemOnInvoice = new ArrayList<String>();//Items on Current invoice
    private static ArrayList<Double> pricesForInvoice = new ArrayList<Double>();//Prices for current invoice (in same order as itemsOnInvoice Array[])
    private static ArrayList<Boolean> isItemDiscounted = new ArrayList<Boolean>();
    private static ArrayList<Double> origPrices = new ArrayList<Double>();
    private static ArrayList<Double> invoiceSavings = new ArrayList<Double>();//all invoice savings
    //private static ArrayList<Double> pOff = new ArrayList<Double>();
    //private static ArrayList<String> allItemsSold = new ArrayList<String>();//List of all items sold from all invoices
    //private static ArrayList<Double> allPricesSold = new ArrayList<Double>();//List of all prices for items sold on all invoices (Same order as allItemsSold Array[])
    //private static int invoiceNum = invoice.invoiceNumGenerator();//gets a invoice number from invoice class to use for a receipt
    private static double Subtotal;//invoice subtotal
    private static DecimalFormat df = new DecimalFormat("0.00");//Decimal Formatter... converts decimals from 0.000000+ to 0.00 format
    private static double origSubtotal;//original Subtotal Before Discount
    private static double pfullInvoiceDiscount = 0; // Holds invoice level discount amount Percentage
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
    /**
     * Shows all items on invoice including discounts
     * @return
     */
    public static String viewItemsOnInvoice(){
        if(itemOnInvoice.size() > 0){
            Login.displaySolarLogo();
            System.out.println();
            System.out.println("Items On Invoice:");
            System.out.println("========================================");
            for(int i = 0; i < itemOnInvoice.size(); i++){
                if(isItemDiscounted.get(i) == true){
                    System.out.println("Item: \"" + itemOnInvoice.get(i) + "\" [Discounted] Original Price: " + origPrices.get(i) + "$ Current Price: " + pricesForInvoice.get(i) + "$");
                }else{
                    System.out.println("Item: \"" + itemOnInvoice.get(i) + "\" Price: " + pricesForInvoice.get(i) + "$");
                }
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
        String user = Login.getUser(); //Get Current User Logged 
        System.out.println("Welcome: " + user);
        System.out.println("========================================");
        System.out.println("[CAT]: Categories"); //[Menu item code] item name
        System.out.println("[MAN]: Manual Entry");
        System.out.println("[APP]: Apply Discount");
        if(user.equals("admin") || user.equals("test")){
            System.out.println("[RIT]: Return items");
        }
        System.out.println("[REM]: Remove Item/Discount");
        System.out.println("[VII]: View Items on Invoice");
        System.out.println("[SIL]: Save Invoice for later");
        System.out.println("[CLS]: Clear Sales Data");
        System.out.println("[TOT]: Total");
        System.out.println("[RET]: Return to Main Menu");
        System.out.println();
        if(itemOnInvoice.size() > 0){
            Subtotal = 5;
            //updateSubtotalDiscount();
            applyFullInvoicePToItem();
            updateSubtotal();
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
                discountMenu();
                POSMenu();
                
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
            case "rem":
                removeMenu();
            break;
            case "vii":
                viewItemsOnInvoice();
                System.out.println("Press Enter to Continue");
                String Enter = customScanner.nextLine();
                mainBody.setNewMessage("[System]: User Pressed: " + Enter);
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
                    isItemDiscounted.clear();
                    origPrices.clear();
                    Savings = 0;
                    Subtotal = 0;
                    origTotal = 0;
                    pfullInvoiceDiscount = 0;
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
    }
    private static void removeMenu() {
        if(itemOnInvoice.size() == 0){
            mainBody.setNewMessage("[System]: No Items to Remove");
            POSMenu();
        }
        boolean hasDiscount;
        for (int i = 0; i < isItemDiscounted.size(); i++) {
            if(isItemDiscounted.get(i) == false){
                 hasDiscount = false;
                 mainBody.setNewMessage("[System]: No Discounts Exist, Discount Status:" + hasDiscount);
                 break;
            }
        }
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Remove Menu:");
        System.out.println("========================================");
        System.out.println("[DIS]: Remove a Discount");
        System.out.println("[ITM]: Remove an Item");
        System.out.println("[RET]: Return to POS Menu");
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
        String option = customScanner.nextLine().toLowerCase();
        switch(option){
            case "dis":
                removeDiscount();
            break;

            case "itm":
                removeItem();
            break;

            case "ret":
                POSMenu();
            break;

            default:
                mainBody.setNewMessage("[Warning]: Invalid Option, Try again");
                removeMenu();
            break;
        }
    }
    private static void removeItem() {
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Remove an Item: ");
        System.out.println("========================================");
        int item = 1;
        for (int i = 0; i < itemOnInvoice.size(); i++) {
            System.out.println(item + " Item \"" + itemOnInvoice.get(i) + "\" Price: " + pricesForInvoice.get(i));
            item++;
        }
        System.out.println("Item to Remove: ");
        try {
            int itemToRemove = customScanner.nextInt();
            itemToRemove--;
            itemOnInvoice.remove(itemToRemove);
            pricesForInvoice.remove(itemToRemove);
            origPrices.remove(itemToRemove);
            isItemDiscounted.remove(itemToRemove);
            invoiceSavings.remove(itemToRemove);
            mainBody.setNewMessage("[System]: Successfully removed Item");
            POSMenu();
        } catch (Exception e) {
            mainBody.setNewMessage("[Warning]: " + e.toString());
            removeMenu();
        }
    }
    private static void removeDiscount() {
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Remove a Discount: ");
        System.out.println("========================================");
        int item = 1;
        for (int i = 0; i < itemOnInvoice.size(); i++) {
            if(isItemDiscounted.get(i) == true){
                System.out.println(item + " "  + itemOnInvoice.get(i) + " Original Price: " + origPrices.get(i) + " Current Price: " + pricesForInvoice.get(i));
                indexes.add(i);
                item++;
            }
        }
        System.out.println("Discount to remove: ");
        try {
            int discountToRemove = customScanner.nextInt();
            discountToRemove--;
            int index = indexes.get(discountToRemove);
            pricesForInvoice.set(index, origPrices.get(index));
            isItemDiscounted.set(index, false);
            invoiceSavings.set(index, 0.00);
            mainBody.setNewMessage("[System]: Successfully Removed Discount");
            removeMenu();
        } catch (Exception e) {
            mainBody.setNewMessage("[Warning]: " + e.toString());
            removeMenu();
        }
    }
    /**
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
                double Price = customScanner.nextDouble();
                addItem("Fridge", Price);
                categories();
            }else if(option2.equals("mix")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Mixer: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Mixer", Price);
                categories();
            }else if(option2.equals("mic")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Microwave: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Microwave", Price);
                categories();
            }else if(option2.equals("dis")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Dishwasher: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Dishwasher", Price);
                categories();
            }else if(option2.equals("toa")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Toaster: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Toaster", Price);
                categories();
            }else if(option2.equals("ove")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Oven, Range, Cooktop: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Oven, Range, Cooktop", Price);
                categories();
            }else if(option2.equals("ble")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Blender: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
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
                    double Price = customScanner.nextDouble();
                    addItem("Gaming Laptop", Price);
                    categories();
                }else if(option3.equals("nor")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Normal Laptops: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
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
                    double Price = customScanner.nextDouble();
                    addItem("Gaming Desktop", Price);
                    categories();
                }else if(option3.equals("nor")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Normal Desktop: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
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
                double Price = customScanner.nextDouble();
                addItem("Server", Price);
                categories();
            }else if(option2.equals("tel")){
                //telephone
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Telephones: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Telephones", Price);
                categories();
            }else if(option2.equals("tev")){
                //TV
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Televison (TV): ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
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
                    double Price = customScanner.nextDouble();
                    addItem("CPU", Price);
                    categories();
                }else if(option3.equals("gpu")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Graphical Processing Unit (GPU): ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("GPU", Price);
                    categories();
                }else if(option3.equals("ram")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Random Access Memory (RAM MODULE): ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("RAM Module", Price);
                    categories();
                }else if(option3.equals("pow")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Power Supply: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("Power Supply", Price);
                    categories();
                }else if(option3.equals("mot")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Motherboard: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("MotherBoard", Price);
                    categories();
                }else if(option3.equals("the")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Thermal Paste: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("Thermal Paste", Price);
                    categories();
                }else if(option3.equals("fan")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Fans: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("Fans", Price);
                    categories();
                }else if(option3.equals("cas")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Case: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("Computer Case", Price);
                    categories();
                }else if(option3.equals("mon")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Monitor: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("Computer Monitor", Price);
                    categories();
                }else if(option3.equals("mou")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Mouse: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
                    addItem("Computer Mouse", Price);
                    categories();
                }else if(option3.equals("key")){
                    Login.displaySolarLogo();
                    System.out.println();
                    System.out.println("Computer Keyboard: ");
                    System.out.println("==========================================");
                    System.out.println("Price: ");
                    double Price = customScanner.nextDouble();
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
                    double Price = customScanner.nextDouble();
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
                double Price = customScanner.nextDouble();
                addItem("Couch", Price);
                categories();
            }else if(option2.equals("cha")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Chair: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Chair", Price);
                categories();
            }else if(option2.equals("tab")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Table: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Tables", Price);
                categories();
            }else if(option2.equals("ben")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Bench: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
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
                double Price = customScanner.nextDouble();
                addItem("Pictures/Picture Frames", Price);
                categories();
            }else if(option2.equals("kni")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Knick Knacks: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("General Knick Knacks", Price);
                categories();
            }else if(option2.equals("stu")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Stuffed Animals: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
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
                double Price = customScanner.nextDouble();
                addItem("Pot", Price);
                categories();
            }else if(option2.equals("pan")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Pans: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Pan", Price);
                categories();
            }else if(option2.equals("cut")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Cutting Board: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
                addItem("Cutting Board", Price);
                categories();
            }else if(option2.equals("sil")){
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Silverware: ");
                System.out.println("==========================================");
                System.out.println("Price: ");
                double Price = customScanner.nextDouble();
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
        //double taxP = Setup.getTax();//Tax Percentage
        //double taxD = Setup.getTax()/100;//Tax as a Decimal
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
        updateSubtotal();
        System.out.println("Press Enter to Continue");
        String Enter = customScanner.nextLine();
        mainBody.setNewMessage("[System]: User Pressed " + Enter);
        calcAmountR();
        paymentMenu();
    }
    public static double applyFullInvoicePToItem(){
        mainBody.setNewMessage("[System]: Applying Invoice % off Discount to item");
        double discounts = pfullInvoiceDiscount * 100; // converts decimal to percent
        discounts = 100 - discounts;//100 - the percent
        discounts = discounts/100;//converts percent to decimal
        if(pfullInvoiceDiscount > 0){
            for (int i = 0; i < isItemDiscounted.size(); i++) {
                if(isItemDiscounted.get(i) == false){
                    isItemDiscounted.set(i, true);
                    double price = origPrices.get(i);
                    double itemTotal = price * discounts;
                    double placeHolder = pricesForInvoice.get(i) - itemTotal;
                    pricesForInvoice.set(i, placeHolder);
                    invoiceSavings.set(i, invoiceSavings.get(i) + itemTotal);
                }else{
                    mainBody.setNewMessage("[Warning]: Item: " + itemOnInvoice.get(i) + " Is already Discounted, Cannot apply Invoice % Off Discount to item.");
                }
            }
        }else{
            mainBody.setNewMessage("[System]: No Invoice discounts to apply to item");
        }
        return 0.00;
    }
    public static double updateSubtotal(){
        mainBody.setNewMessage("[System]: Updating Subtotal...");
        double taxP = Setup.getTax();//Tax as a Percentage
        double taxD = Setup.getTax()/100;//Tax as a Decimal
        origSubtotal = 0;
        double discountPrices = 0;
       // double discounts = 0;
        for (int i = 0; i < itemOnInvoice.size(); i++) {
            origSubtotal = origSubtotal + origPrices.get(i);//populates subtotal
        }
        origTotal = origSubtotal;
        for (int i = 0; i < itemOnInvoice.size(); i++){
            discountPrices = discountPrices + pricesForInvoice.get(i);
        }
        Subtotal = origSubtotal - discountPrices;
        setTotal(discountPrices);
        System.out.println("Subtotal: " + df.format(discountPrices) + "$");//Subtotal
        System.out.println("Original total: " + origSubtotal + "$");
        System.out.println("Discounts: " + Subtotal + "$");
        System.out.println("Tax%: " + taxP + "%" + " Tax Amount: " + df.format(origTotal * taxD) + "$");//Tax Percentage and then the $ Amount of tax
        origTotal = discountPrices + (origTotal * taxD); //Dollar amount of tax Calculated
        //origTotal = origTotal + discountPrices;//Total amount
        //origTotal = origTotal - Subtotal;
        System.out.println("Total: $" + df.format(origTotal));
        System.out.println();
        return 0.00;
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
        //double amountT = 0;
        System.out.println("Payment Options");
        System.out.println("========================================");
        System.out.println("Amount Remaining: " + df.format(getAmountR()) + "$");
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
            //double total = 0;
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
            mainBody.setNewMessage("[System]: User Pressed: " + enter);
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
            mainBody.setNewMessage("[System]: User Pressed: " + Enter);
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
        double cashP = customScanner.nextDouble();
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
        double checkP = customScanner.nextDouble();
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
        double cardP = customScanner.nextDouble();
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
            double manualPrice = customScanner.nextDouble();
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
        invoiceSavings.add(0.00);
        origPrices.add(price);
        isItemDiscounted.add(false);
        if(itemOnInvoice.contains(item) && pricesForInvoice.contains(price)){
            mainBody.setNewMessage("[System]: " + item + " $" + price);
        }else if(itemOnInvoice.contains(item) && !pricesForInvoice.contains(price)){
            mainBody.setNewMessage("[Warning]: Failed to add Price of $" + price + " to " + item + ", Removing item...");
            itemOnInvoice.remove(item);
        }else if(!itemOnInvoice.contains(item) && pricesForInvoice.contains(price)){
            mainBody.setNewMessage("[Warning]: Failed to add Item: " + item + ", removing price of $" + price);
            pricesForInvoice.remove(price);
        }else{
            mainBody.setNewMessage("[Warning]: Failed to add Item: " + item + ", and Failed to add price: $" + price);
        }
        return item;
    }
    public static void discountMenu(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Apply Discount");
        System.out.println("========================================");
        System.out.println("Would you like to Apply Discount to a specific item or total invoice?");
        System.out.println("1. Sigle item");
        System.out.println("2. Total Invoice");
        System.out.println("3. Cancel and return to POS Menu");
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
        int selection = customScanner.nextInt();
        switch(selection){
            case 1:
            int item = 0;
            int choice = 0;
            Login.displaySolarLogo();
            System.out.println("Which Item: ");
            for(int i = 0; i < itemOnInvoice.size(); i++){
                item ++;
                System.out.println(item + ". " + itemOnInvoice.get(i) + " $" + pricesForInvoice.get(i));
            }
            System.out.println("Choice: ");
            try{
                choice = customScanner.nextInt();
                choice--;
                System.out.println("Item Selected: " + itemOnInvoice.get(choice) + " $" + pricesForInvoice.get(choice));
            }catch(Exception e){
                mainBody.setNewMessage("[Warning]: Invalid Option");
                e.printStackTrace();
                mainBody.setNewMessage("[Warning]: " + e.toString());
                POSMenu();
            }
            singleItemDiscount(choice);
            break;

            case 2:
            invoiceLevelDiscount();
            break;

            case 3:
                POSMenu();
            break;

            default:
                mainBody.setNewMessage("[Warning]: Invalid Option, Try again");
                discountMenu();
            break;
        }
    }
    public static void singleItemDiscount(int index){
        double dAmountOff;
        double pAmountOff;
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Single Item Discount: ");
        System.out.println("==========================================");
        System.out.println("1. $off");
        System.out.println("2. %off");
        System.out.println("Choice: ");
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
        int choice = customScanner.nextInt();
        switch (choice){
            case 1:
            //$off
                Login.displaySolarLogo();
                System.out.println();
                System.out.println("Single Item Discount ($ Amount Off): ");
                System.out.println("==========================================");
                System.out.println("$ Amount Off: ");
                dAmountOff = customScanner.nextDouble();
                double priceToDiscount = pricesForInvoice.get(index);
                double discounted = priceToDiscount - dAmountOff;
                invoiceSavings.set(index, dAmountOff);
                isItemDiscounted.set(index, true);
                pricesForInvoice.set(index, discounted);
                POSMenu();;
            break;

            case 2:
            //%off
                Login.displaySolarLogo();  
                System.out.println();
                System.out.println("Single Item Discount (% Amount Off): ");
                System.out.println("==========================================");
                System.out.println("Please write Percent Off as a Percentage");
                System.out.println("% Amount Off: ");
                pAmountOff = customScanner.nextDouble();
                if(pAmountOff < 0 || pAmountOff > 100){
                    mainBody.setNewMessage("[Warning]: Percent cannot be greater than 100 or less then 0");
                    POSMenu();
                }else{
                    double discountD = 100 - pAmountOff;
                    if(discountD < 0){
                       discountD =  discountD * (-1);
                    }else if(discountD > 100){
                        mainBody.setNewMessage("[Warning]: Invalid option, try again.");
                        discountMenu();
                    }
                    discountD = pAmountOff/100; //converts to a decimal
                    priceToDiscount = pricesForInvoice.get(index);//get the price to discount
                    discounted = priceToDiscount * discountD;//calculation 
                    dAmountOff = priceToDiscount - discounted;//$amountoff is = priceToDiscount - the discount
                    invoiceSavings.set(index, discounted);
                    pricesForInvoice.set(index, dAmountOff);
                    isItemDiscounted.set(index, true);
                    POSMenu();                
                }
            break;

            default:
                mainBody.setNewMessage("[Warning]: Invalid Option, try again");
                System.out.println(mainBody.getLastMessage());
                singleItemDiscount(index);
            break;
        }
    }
    public static double invoiceLevelDiscount(){
        Login.displaySolarLogo();
        System.out.println();
        System.out.println("Invoice Level Discount: ");
        System.out.println("==========================================");
        System.out.println("1. $off");
        System.out.println("2. %off");
        System.out.println("Choice: ");
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
        int choice = customScanner.nextInt();        
        switch(choice){
            case 1:
                Login.displaySolarLogo();
                System.out.println("Invoice Level Discount ($ Amount Off): ");
                System.out.println("==========================================");
                System.out.println("$ Amount Off: ");
                double dAmountOff = customScanner.nextDouble();
                addItem("Invoice Level Discount", (dAmountOff * -1));
                int index = invoiceSavings.size();
                index--;
                invoiceSavings.set(index, dAmountOff);
                POSMenu();
            break;

            case 2:
                Login.displaySolarLogo();
                System.out.println("Invoice Level Discount (% Amount Off): ");
                System.out.println("==========================================");
                System.out.println("% Amount Off: ");
                double pAmountOff = customScanner.nextDouble();
                dAmountOff = pAmountOff/100;
                pfullInvoiceDiscount = dAmountOff;
                invoiceSavings.add(0.00);
                itemOnInvoice.add("Invoice Level Discount");
                pricesForInvoice.add(0.00);
                origPrices.add(0.00);
				isItemDiscounted.add(true);
                POSMenu();
            break;

            default:
            break;
        }
        return 0.0;
    }
    /**
     * Method addDiscount
     * adds discount to item or invoice
     * @return The return value
     */
    

    /**
     * Method updateSubtotalDiscount
     *
     * @return subtotal discount
     */
}
