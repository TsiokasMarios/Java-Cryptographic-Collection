import java.io.*;
import java.util.Scanner;

public class Utils {
    public static StringBuilder convertToHex(byte[] text){
        StringBuilder msg = new StringBuilder();
        for (byte b : text) {
            msg.append(Integer.toHexString(0xff & b));
        }
        return msg;
    }

    public Utils(String name, boolean append) throws FileNotFoundException{
        try
        {
            Scanner sc=new Scanner(System.in);         //object of Scanner class
            System.out.print("Enter the file name: ");
            name = sc.nextLine();
            FileOutputStream fos=new FileOutputStream(name, true);  // true for append mode
            System.out.print("Enter file content: ");
            String str=sc.nextLine()+"\n";      //str stores the string which we have entered
            byte[] b= str.getBytes();       //converts string into bytes
            fos.write(b);           //writes bytes into file
            fos.close();            //close the file
            System.out.println("file saved.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public static String extractMessage(String filePath){
        StringBuilder message = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // extract the message
                message.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message.toString();
    }
}
