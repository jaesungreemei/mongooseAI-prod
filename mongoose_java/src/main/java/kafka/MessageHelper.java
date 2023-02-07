package kafka;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.json.simple.JSONObject;

public class MessageHelper {
    
    private static Properties props;

    /* ----------------------------------------------------------------------------------------- */

    public static String getRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /* ----------------------------------------------------------------------------------------- */

    public static JSONObject getConsumerJSONObject(String source, String topic, String key, String value, String partition, String offset, String timestamp) throws Exception {
        HashMap<String, Object> consumerDetails = new HashMap<String, Object>();

        String bootstrapServers = PropertiesHelper.getProperties().getProperty("bootstrap.servers");
        consumerDetails.put("bootstrapServers", bootstrapServers);
        consumerDetails.put("source", source);
        consumerDetails.put("topic", topic);
        consumerDetails.put("key", key);
        consumerDetails.put("value", value);
        consumerDetails.put("partition", partition);
        consumerDetails.put("offset", offset);
        consumerDetails.put("timestamp", timestamp);

        JSONObject obj = new JSONObject(consumerDetails);
        return obj;
    }

    /* ----------------------------------------------------------------------------------------- */

    public static JSONObject getMessageLogEntryJSON(String source, String topic, String key, String message) throws Exception {
        HashMap<String, Object> LogDetails = new HashMap<String, Object>();
        
        String bootstrapServers = PropertiesHelper.getProperties().getProperty("bootstrap.servers");
        LogDetails.put("bootstrapServers", bootstrapServers);
        LogDetails.put("source", source);
        LogDetails.put("topic", topic);
        LogDetails.put("key", key);
        LogDetails.put("message", message);

        JSONObject obj = new JSONObject(LogDetails);
        return obj;

    }

    /* ----------------------------------------------------------------------------------------- */

    public static JSONObject getSimpleJSONObject(String message) throws Exception {
        HashMap<String, Object> SimpleDetails = new HashMap<String, Object>();
        
        SimpleDetails.put("message", message);

        JSONObject obj = new JSONObject(SimpleDetails);
        return obj;
    }

    /* ----------------------------------------------------------------------------------------- */

    protected static Properties getProperties() throws Exception {
        if (props == null) {
            props = PropertiesHelper.getProperties();
        }

        return props;
    }

    /* ----------------------------------------------------------------------------------------- */

}
