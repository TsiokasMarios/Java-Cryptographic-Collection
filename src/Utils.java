import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;

public class Utils {
    public static StringBuilder convertToHex(byte[] text){
        StringBuilder msg = new StringBuilder();
        for (byte b : text) {
            msg.append(Integer.toHexString(0xff & b));
        }
        return msg;
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

    public static String keyToHex(Key secretKey){
        StringBuffer hexKey = new StringBuffer();
        for (int i=0;i<secretKey.getEncoded().length;i++)
            hexKey.append(Integer.toHexString(0xFF & secretKey.getEncoded()[i]));
        return hexKey.toString();
    }
}
