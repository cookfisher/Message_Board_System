package message;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.put("admin","admins, concordia");
        properties.put("user", "encs");
        properties.storeToXML(new FileOutputStream("MEMBERSHIP.xml"),null);

    }

}
