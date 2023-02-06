package kafka;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;

import org.apache.log4j.Logger;

public class ProducerApp extends AbstractKafkaApp {
    
    private final Logger log = Logger.getLogger( ProducerApp.class.getName() );

    private KafkaProducer<String, String> kafkaProducer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ProducerApp() throws Exception {
    }

    /* ----------------------------------------------------------------------------------------- */

    public void run(String topicName, int numberOfMessages) throws Exception {
        String key;
        String message;
        int i = 0;

        while (i <= numberOfMessages) {
            key = UUID.randomUUID().toString();
            message = MessageHelper.getRandomString();
            this.send(topicName, key, message);
            i++;
            Thread.sleep(1000);
        }
        this.shutdown();
    }

    /* ----------------------------------------------------------------------------------------- */

    public void runAlways(String topicName) throws Exception {
        String key;
        String message;

        while (true) {
            key = UUID.randomUUID().toString();
            message = MessageHelper.getRandomString();
            this.send(topicName, key, message);
            Thread.sleep(1000);
        }
    }

    /* ----------------------------------------------------------------------------------------- */

    private String topicName;
    private void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    private String getTopicName() {
        return this.topicName;
    }

    /* ----------------------------------------------------------------------------------------- */

    protected void send(String topicName, String key, String message) throws Exception {
        String source = ProducerApp.class.getName();
        JSONObject obj = MessageHelper.getMessageLogEntryJSON(source, topicName, key, message);
        
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, message);
        log.info(obj.toJSONString());
        getProducer().send(producerRecord);

        // getProducer().send(producerRecord, new Callback() {
            
        //     @Override
        //     public void onCompletion(RecordMetadata metadata, Exception e) {
        //         String partition;
        //         String offset;
        //         String timestamp;

        //         if (e == null) {
        //             partition = String.valueOf(metadata.partition());
        //             offset = String.valueOf(metadata.offset());
        //             timestamp = String.valueOf(metadata.timestamp());

        //             obj = MessageHelper.getProducerJSONObject(source, topicName, key, partition, offset, timestamp);
        //             log.info(obj.toJSONString());

        //         } else {
        //             log.error("Error while producing ", e);
        //         }   
        //     }

        // });
    }

    private KafkaProducer<String, String> getProducer() throws Exception {
        if(this.kafkaProducer == null) {
            Properties props = PropertiesHelper.getProperties();
            this.kafkaProducer = new KafkaProducer<>(props);
        }
        return this.kafkaProducer;
    }

    public void shutdown() throws Exception {
        closed.set(true);
        log.info( MessageHelper.getSimpleJSONObject("Shutting down Producer...").toJSONString() );
        getProducer().close();
    }

}
