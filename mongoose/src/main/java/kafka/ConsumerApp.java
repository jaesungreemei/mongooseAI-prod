package kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class ConsumerApp extends AbstractKafkaApp {
    
    private final Logger log = Logger.getLogger( ConsumerApp.class.getName() );
    
    private final int TIME_OUT_MS = 5000;
    private KafkaConsumer<String, String> kafkaConsumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ConsumerApp() throws Exception {
    }

    /* ----------------------------------------------------------------------------------------- */

    public void run(String topicName, Integer numberOfRecords) throws Exception {
        Properties props = PropertiesHelper.getProperties();

        Optional<Integer> recs = Optional.ofNullable(numberOfRecords);
        Integer numOfRecs = recs.orElseGet( () -> Integer.parseInt(props.getProperty("max.poll.records")) );
        props.setProperty("max.poll.records", String.valueOf(numOfRecs));

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(
            Collections.singleton(new TopicPartition(topicName, 0))
        );

        int i = numOfRecs;
        while (i > 0) {
            ConsumerRecords<String, String> records = consumer.poll( Duration.ofMillis(TIME_OUT_MS) );
            i = records.count();

            if (i == 0) {
                log.info(MessageHelper.getSimpleJSONObject("No records retrieved.").toJSONString());
                break;
            }

            for (ConsumerRecord<String, String> consumerRecord : records) {
                String source = ConsumerApp.class.getName();
                String key = consumerRecord.key();
                String value = consumerRecord.value();
                String partition = String.valueOf(consumerRecord.partition());
                String offset = String.valueOf(consumerRecord.offset());
                String timestamp = String.valueOf(consumerRecord.timestamp());
                
                JSONObject obj = MessageHelper.getConsumerJSONObject(source, topicName, key, value, partition, offset, timestamp);
                log.info(obj.toJSONString());

                i--;
            }
        }

        consumer.close();

    }

    /* ----------------------------------------------------------------------------------------- */

    private void close() throws Exception {
        if (this.getKafkaConsumer() == null) {
            log.info(MessageHelper.getSimpleJSONObject("The internal consumer is NULL"));
        } else {
            log.info(MessageHelper.getSimpleJSONObject("Closing consumer..."));
            this.getKafkaConsumer().close();
        }
    }

    /* ----------------------------------------------------------------------------------------- */

    public void runAlways(String topicName) throws Exception {
        
        try {
            getKafkaConsumer().subscribe(Arrays.asList(topicName));

            while (!closed.get()) {
                ConsumerRecords<String, String> records = getKafkaConsumer().poll( Duration.ofMillis(TIME_OUT_MS) );
                
                if (records.count() == 0) {
                    log.info(MessageHelper.getSimpleJSONObject("No records retrieved."));
                } else {
                    for (ConsumerRecord<String, String> consumerRecord : records) {
                        String source = ConsumerApp.class.getName();
                        String key = consumerRecord.key();
                        String value = consumerRecord.value();
                        String partition = String.valueOf(consumerRecord.partition());
                        String offset = String.valueOf(consumerRecord.offset());
                        String timestamp = String.valueOf(consumerRecord.timestamp());
                        
                        JSONObject obj = MessageHelper.getConsumerJSONObject(source, topicName, key, value, partition, offset, timestamp);
                        
                        System.out.println(obj.toJSONString());
                        log.info(obj.toJSONString());
                    }
                }
            }

        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        }
    }

    /* ----------------------------------------------------------------------------------------- */
    
    public void shutdown() throws Exception {
        closed.set(true);
        log.info(MessageHelper.getSimpleJSONObject("Shutting down consumer..."));
        getKafkaConsumer().wakeup();
    }

    public KafkaConsumer<String, String> getKafkaConsumer() throws Exception {
        if(this.kafkaConsumer == null) {
            Properties props = PropertiesHelper.getProperties();
            this.kafkaConsumer = new KafkaConsumer<>(props);
        }
        return this.kafkaConsumer;
    }

    /* ----------------------------------------------------------------------------------------- */

}
