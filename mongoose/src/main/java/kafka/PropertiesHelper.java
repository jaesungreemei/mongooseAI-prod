package kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {

    public static Properties getProperties() throws Exception {
        Properties props = null;
        
        try (InputStream input = ProducerApp.class.getClassLoader().getResourceAsStream("config.properties")) {
            props = new Properties();
            if (input == null) {
                throw new Exception("Unable to find config.properties");
            }
            props.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }
    
}
