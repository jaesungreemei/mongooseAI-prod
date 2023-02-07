package kafka;

import java.util.Locale;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class App {

    // private static final int NUM_OF_RECORD = 10;
    private final Logger log = Logger.getLogger( App.class.getName() );

    /* ----------------------------------------------------------------------------------------- */

    public void MessageHandler(String topicName, ConsumerRecord<String, String> message) throws Exception {

        String source = App.class.getName();
        JSONObject obj = MessageHelper.getMessageLogEntryJSON(source, source, message.key(), message.value());
        
        System.out.println(obj.toJSONString());
        log.info(obj);
    }

    /* ----------------------------------------------------------------------------------------- */

    public static void main(String[] args) throws Exception {
        String mode = args[0];
        String topicName = args[1];
        String cliError = "ERROR: you need to declare the first parameter as Producer or Consumer, the second parameter is the topic name";
        int port = 0;       // Automatically find any free port

        if (args.length != 2) {
            System.out.println(cliError);
            return;
        }

        if (mode.toLowerCase(Locale.ROOT).equals("producer")) {
            System.out.println("Starting the Producer... \n");
            new ProducerAppServer().runAlways(topicName, port);

        } else if (mode.toLowerCase(Locale.ROOT).equals("consumer")) {
            System.out.println("Starting the Consumer... \n");
            new ConsumerApp().runAlways(topicName, port);

        } else {
            System.out.println(cliError);
        }

    }
}

