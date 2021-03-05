
import java.util.*;
import java.math.*;
import java.text.*;
/**
 * Write a description of class POS here.
 *
 * @author (Brayden Anderson)
 * @version (Base Version: ALPHA V2.1.4, Snapshot 3xWav-3)
 */
public class POS{
    private static double Savings = 0;//$amount saved
    private static ArrayList<String> itemOnInvoice = new ArrayList<String>();
    private static ArrayList<Double> pricesForInvoice = new ArrayList<Double>();
    private static ArrayList<String> allItemsSold = new ArrayList<String>();
    private static ArrayList<Double> allPricesSold = new ArrayList<Double>();
    private static ArrayList<Double> invoiceSavings = new ArrayList<Double>();//all invoice savings
    private int invoiceNum = invoice.invoiceNumGenerator();
    private static double Subtotal;//invoice subtotal
    private static DecimalFormat df = new DecimalFormat("0.00");
    /**
     * POS Constructor
     *
     */
    public POS(){
        POSMenu();
    }

    /**
     * Method POSMenu
     * POS Menu
     */
    public static void POSMenu(){
        Scanner scan = new Scanner(System.in);
        String user = Login.getUser();
        System.out.println("Welcome: " + user);
        System.out.println("========================================");
        System.out.println("[CAT]: Categories");
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
        double savings=0;
        Subtotal = 0;
        for(int j = 0; j < invoiceSavings.size(); j++){
            savings = savings + invoiceSavings.get(j);
        }
        for(int i = 0; i <pricesForInvoice.size(); i++){
            Subtotal = Subtotal + pricesForInvoice.get(i);
        }
        //Subtotal = Subtotal - savings;
        if(Subtotal == 0){
            System.out.println("Subtotal: $" + df.format(0.00));
        }else if(Subtotal >0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }else if(Subtotal <0){
            System.out.println("Subtotal: $" + df.format(Subtotal));
        }

        if(invoiceSavings.size()>0){
            System.out.println("Savings: $" + savings * (-1));
        }
        System.out.println("Console: ");
        int messageSize = mainBody.getMessageSize();
        if(messageSize > 0){
            messageSize --;
            System.out.println(mainBody.getLastMessage());
        }
        String option = scan.nextLine().toLowerCase();
        if(option.equals("ret")){
            mainBody.mainMenu();        
        }else if(option.equals("cat")){
            //categories
            categories();
        }else if(option.equals("man")){
            //manual entry
            manualEntry();
        }else if(option.equals("app")){
            //apply coupon
            if(Subtotal ==0){
                mainBody.setNewMessage("[System]: In order to apply discount, subtotal cannot be 0$");
                POSMenu();
            }else if(Subtotal < 0){
                mainBody.setNewMessage("[System]: In order to apply discount, subtotal cannot be negative");
                POSMenu();
            }else{
                addDiscount();            
                POSMenu();
            }
        }else if(option.equals("rit") && user.equals("test") || user.equals("admin")){
            //return items
            mainBody.setNewMessage("[System]: This Feature is not yet Available");
            POSMenu();
        }else if(option.equals("vii")){
            mainBody.setNewMessage("[System]: This Feature is not yet Available");
            POSMenu();
        }else if(option.equals("sil")){
            //save invoice for later (Learn XML or JSON)
            mainBody.setNewMessage("[System]: This Feature is not yet Available");
            POSMenu();
        }else if(option.equals("cls")){
            // clear sales data
            mainBody.setNewMessage("[System]: Are You Sure you want To Clear Sales Data?");
            System.out.println(mainBody.getLastMessage());
            String answer = scan.nextLine().toLowerCase();
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
        }else if(option.equals("tot")){
            //Total
            mainBody.setNewMessage("[System]: This Feature is not yet Available");
            POSMenu();
        }else{
            mainBody.setNewMessage("[Warning]: Invalid Option: Please make sure you have the proper permissions");
            POSMenu();
        }
        scan.close();
    }

    /**
     * Method categories
     * Categories for POS items
     * Menu Logic is not neccessarily in the same order as the Menu Items
     */
    private static void categories(){
        mainBody.setNewMessage("[System]: This Feature is not yet Completed");
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
        int messageSize = mainBody.getMessageSize();
        if(messageSize > 0){
            messageSize --;
            System.out.println(mainBody.getLastMessage());
        }
        Scanner scan = new Scanner(System.in);
        String option = scan.nextLine().toLowerCase().trim();
        if(option.equals("ret")){
            POSMenu();
        }else if(option.equals("app")){
            System.out.println("[FRI]: Fridge");
            System.out.println("[MIC]: Microwave");
            System.out.println("[DIS]: Dishwasher");
            System.out.println("[TOA]: Toaster");
            System.out.println("[OVE]: Oven, Range, Cooktop");
            System.out.println("[BLE]: Blender");
            System.out.println("[MIX]: Mixer");
            System.out.println("[RET]: Return to Menu");
            String option2 = scan.nextLine();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("fri")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Fridge", Price);
                categories();
            }else if(option2.equals("mix")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Mixer", Price);
                categories();
            }else if(option2.equals("mic")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Microwave", Price);				
                categories();
            }else if(option2.equals("dis")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Dishwasher", Price);				
                categories();
            }else if(option2.equals("toa")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Toaster", Price); 				
                categories();
            }else if(option2.equals("ove")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Oven, Range, Cooktop", Price); 				
                categories();
            }else if(option2.equals("ble")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Blender", Price);				
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("com")){
            System.out.println("[LAP]: Laptops");
            System.out.println("[DES]: Desktops");
            System.out.println("[TEL]: Telephones");
            System.out.println("[TEV]: Television");
            System.out.println("[HAR]: Hardware");
            System.out.println("[SOF]: Software");
            System.out.println("[RET]: Return to Menu");
            String option2 = scan.nextLine();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("lap")){
                System.out.println("Laptops");				        
                System.out.println("========================================");
                System.out.println("[GAM]: Gaming Laptops");
                System.out.println("[NOR]: Normal Laptops");
                System.out.println("[RET]: Return to Menu");
                String option3 = scan.nextLine();
                if(option3.equals("ret")){
                    categories();	
                }else if(option3.equals("gam")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Gaming Laptop", Price);				
                    categories();
                }else if(option3.equals("nor")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Normal Laptop", Price);				
                    categories();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                    categories();
                }
            }else if(option2.equals("des")){
                System.out.println("Desktop");				        
                System.out.println("========================================");
                System.out.println("[GAM]: Gaming Desktop");
                System.out.println("[NOR]: Normal Desktop");
                System.out.println("[RET]: Return to Menu");
                String option3 = scan.nextLine();
                if(option3.equals("ret")){
                    categories();	
                }else if(option3.equals("gam")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Gaming Desktop", Price);				
                    categories();
                }else if(option3.equals("nor")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Normal Desktop", Price);				
                    categories();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                    categories();
                }
            }else if(option2.equals("tel")){
                //telephone
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Telephones", Price);
                categories();
            }else if(option2.equals("tev")){
                //TV
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("TV's", Price);
                categories();
            }else if(option2.equals("har")){
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
                String option3 = scan.nextLine().toLowerCase();
                if(option3.equals("ret")){										
                    mainBody.setNewMessage("[System]: User Canceled Category Selection");
                    categories();
                }else if(option3.equals("cpu")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("CPU", Price);
                    categories();
                }else if(option3.equals("gpu")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("GPU", Price);
                    categories();
                }else if(option3.equals("ram")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("RAM Module", Price);
                    categories();
                }else if(option3.equals("pow")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Power Supply", Price);
                    categories();
                }else if(option3.equals("mot")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("MotherBoard", Price);
                    categories();
                }else if(option3.equals("the")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Thermal Paste", Price);
                    categories();
                }else if(option3.equals("fan")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Fans", Price);
                    categories();
                }else if(option3.equals("cas")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Case", Price);
                    categories();
                }else if(option3.equals("mon")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Monitor", Price);
                    categories();
                }else if(option3.equals("mou")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Mouse", Price);
                    categories();
                }else if(option3.equals("key")){
                    System.out.println("Price: ");
                    double Price = scan.nextDouble();
                    addItem("Computer Keyboard", Price);
                    categories();
                }else{
                    mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                    categories();
                }
            }else if(option2.equals("sof")){
                System.out.println("[RET]: Return");
                System.out.println("Name of Program: ");
                String name = scan.nextLine();
                if(name.equals("ret") || name.equals("RET")){
                    mainBody.setNewMessage("[System]: User Canceled Category Selection");
                    categories();
                }else{
                    double Price = scan.nextDouble();
                    addItem(name, Price);
                    categories();
                }
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("fur")){
            System.out.println("[COU]: Couch");
            System.out.println("[CHA]: Chair");
            System.out.println("[TAB]: Tables");
            System.out.println("[BEN]: Benches");

            System.out.println("[RET]: Return to Menu");
            String option2 = scan.nextLine();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("cou")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Couch", Price);
                categories();
            }else if(option2.equals("cha")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Chair", Price);
                categories();
            }else if(option2.equals("tab")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Tables", Price);
                categories();
            }else if(option2.equals("ben")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Bench", Price);
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("dec")){
            System.out.println("[PIC]: Pictures/Picture Frames");
            System.out.println("[KNI]: Knick Knacks");
            System.out.println("[STU]: Stuffed Animals");
            System.out.println("[RET]: Return to Menu");
            String option2 = scan.nextLine().toLowerCase();
            if(option2.equals("ret")){
                categories();
            }else if(option2.equals("pic")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Pictures/Picture Frames", Price);
                categories();
            }else if(option2.equals("kni")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("General Knick Knacks", Price);
                categories();
            }else if(option2.equals("stu")){
                System.out.println("Price: ");
                double Price = scan.nextDouble();
                addItem("Stuffed Animals", Price);
                categories();
            }else{
                mainBody.setNewMessage("[System]: Invalid Option, Functionality may not be available yet.");
                categories();
            }
        }else if(option.equals("kit")){
            System.out.println("[POT]: Pots");
            System.out.println("[PAN]: Pan");
            System.out.println("[Cut]: Cutting Board");
            System.out.println("[SIL]: Silverware");
            System.out.println("[RET]: Return to Menu");
            String option2 = scan.nextLine();
            if(option2.equals("ret")){
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
        scan.close();
        POSMenu();
    }

    /**
     * Method manualEntry
     * Manual Item input for POS
     */
    private static void manualEntry(){
        System.out.println("Type \"[CAT]\" to go to Categories");
        System.out.println("Manual Entry: ");
        Scanner scan = new Scanner(System.in);
        String manualEntry = scan.nextLine();
        if(manualEntry.equals("cat")){
            scan.close();
            categories();
        }else if(manualEntry.equals("ret")){
            scan.close();
            POSMenu();
        }else{
            System.out.println("Price: ");
            double manualPrice = scan.nextDouble();
            scan.close();
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
        Scanner scan = new Scanner(System.in);
        System.out.println("Apply Discount");
        System.out.println("========================================");
        System.out.println("Would you like to Apply Discount to a specific item or total invoice?");
        System.out.println("1. Sigle item");
        System.out.println("2. Total Invoice");
        int selection = scan.nextInt();
        switch(selection){
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
                choice = scan.nextInt();
                choice--;
                System.out.println("Item Selected: " + itemOnInvoice.get(choice) + " $" + pricesForInvoice.get(choice));
            }catch(Exception e){
                mainBody.setNewMessage("[System ERROR]: Invalid Option");
                e.printStackTrace();
                mainBody.setNewMessage("[System ERROR]: " + e.toString());
                POSMenu();
            }
            System.out.println("1. $off");
            System.out.println("2. %off");
            System.out.println("Choice: ");
            int choices = 0;
            choices = scan.nextInt();
            if(choices == 1){
                System.out.println("$ Amount Off: ");
                choice--;
                dAmountOff = scan.nextDouble();
                dAmountOff = dAmountOff;
                double placeHolder = choice;
                placeHolder = placeHolder - dAmountOff;
                pricesForInvoice.set(choice, placeHolder);
                itemOnInvoice.set(choice, itemOnInvoice.get(choice) + "*Discounted*");
                invoiceSavings.add(dAmountOff * (-1));
                mainBody.setNewMessage("[System]: Discount Applied to " + itemOnInvoice.get(choice));
                scan.close();
                POSMenu();
            }else if(choices == 2){
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
            double subtotal = 0;
            break;
            default:
            mainBody.setNewMessage("[System]: Invalid Option, try again");
            POSMenu();
            break;
        }

        System.out.println("1. $ off");
        System.out.println("2. % off");
        //Scanner scan = new Scanner(System.in);
        int option = scan.nextInt();
        if(option == 1){
            System.out.println("$: ");
            double dAmountOff = scan.nextDouble();
            dAmountOff = dAmountOff * -1;
            addItem("Discount", 0);
            Savings = Savings + dAmountOff;
            invoiceSavings.add(Savings);
            return dAmountOff;
        }else if(option == 2){
            System.out.println("%: ");
            double subtotal;
            double percentOff = scan.nextDouble();//% off
            subtotal = 0;
            double savingsAmount;//$amount off based of percentage
            for(int i = 0; i < pricesForInvoice.size(); i++){
                subtotal = subtotal + pricesForInvoice.get(i);//getting value for subtotal
            }
            System.out.println("Subtotal: " + subtotal);
            df.format(percentOff);
            System.out.println("Percent: " + percentOff);
            percentOff = percentOff / 100;
            System.out.println("Percent: " + percentOff);
            savingsAmount = subtotal * percentOff;
            System.out.println("Savings: " + savingsAmount);
            Savings = (Savings + savingsAmount);
            System.out.println("Savings: " + savingsAmount);
            invoiceSavings.add(Savings);
            addItem("Discount", 0);
            return subtotal;
        }else{
            mainBody.setNewMessage("Invalid Option");
            return 0.00;
        }
    }

    /**
     * Method updateSubtotalDiscount
     *
     * @return subtotal discount
     */
    public static boolean updateSubtotalDiscount(){
        return true;
    }
}