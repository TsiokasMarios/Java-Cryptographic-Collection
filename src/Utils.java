import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
}
