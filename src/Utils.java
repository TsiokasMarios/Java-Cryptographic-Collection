import java.io.*;

public class Utils {
    public static String convertToHex(byte[] text){
        StringBuilder msg = new StringBuilder();
        for (byte b : text) {
            msg.append(Integer.toHexString(0xff & b));
        }
        return msg.toString();
    }

    public static void saveEncryptedText(String name,String text) throws FileNotFoundException{
        try
        {
            FileWriter myWriter = new FileWriter(name+".txt");
            myWriter.write(text);
            myWriter.close();
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
                message.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message " + message);
        return message.toString();
    }

    public static byte[] stringToBytes(String string){
        char[] chars = string.toCharArray();

        byte[] bytes = new byte[chars.length];

        for (int i =0; i < chars.length; i++){
            bytes[i] = (byte) chars[i];
        }
        return bytes;

    }
}
