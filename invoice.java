import java.util.*;
import java.io.*;
/**
 * Write a description of class invoice here.
 *
 * @author (Brayden Anderson)
 * @version (a version number or a date)
 */
public class invoice{  
    private static ArrayList<Integer> invoices = new ArrayList<Integer>();
    private static int lastInvoice;
    /**
     * Method addInvoice
     *
     * @param invoiceNum A number that gets added to invoices Arraylist
     * @return tests to see if invoiceNum was added to invoices
     */
    public static boolean addInvoice(int invoiceNum){
        invoices.add(invoiceNum);
        if(invoices.contains(invoiceNum)){
            mainBody.setNewMessage("[System]: Successfully Generated Invoice Number"); 
            return true;
        }else{
            mainBody.setNewMessage("[Warning]: Failed to Generate Invoice Number");
            return false;
        }
    }

    /**
     * Method invoiceNumGenerator
     * Generates Invoice Number
     * @return Generated Invoice Number
     */
    public static int invoiceNumGenerator(){
        Random ran = new Random();
        int invoiceNum = ran.nextInt(9999999);
        if(invoices.contains(invoiceNum)){
            invoiceNumGenerator();
        }else{
            mainBody.setNewMessage("[System]: Assigning Invoice Number " + invoiceNum);
            addInvoice(invoiceNum);
        }
        return invoiceNum;
    }

    /**
     * Method getLastInvoice
     *
     * @return Last index of invoices Arraylist
     */
    public static int getLastInvoice(){
        int size = invoices.size();
        size--;
        int invoiceNum = invoices.get(size);
        return invoiceNum;
    }

    /**
     * Method generateInvoiceReceipt
     *
     * @return The return value
     */
    public static boolean generateInvoiceReceipt(){
        return false;
    }
}