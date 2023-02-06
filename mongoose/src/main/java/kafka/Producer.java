package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {
    private static final Logger log = LoggerFactory.getLogger( Producer.class.getSimpleName() );

    public static void main(String[] args) {

        /* ----------------------------------------- */
        // (Step 1) Create Producer Properties, Producer Object\
        /*
         * - KEY_SERIALIZER_CLASS_CONFIG: key serializer for producer
         * - VALUE_SERIALIZER_CLASS_CONFIG: value serializer for producer
         */
        /* ----------------------------------------- */
        Properties properties = new Properties();

        String bootstrapServers = "localhost:9092";

        // Connect to localhost
        properties.setProperty( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );

        // Security Protocol (eg. Conduktor Playground)
        // properties.setProperty( "bootstrap.servers", "..." );
        // properties.setProperty( "security.protocol", "SASL_SSL" );
        // properties.setProperty( "sasl.jaas.config", "..." );
        // properties.setProperty( "sasl.mechanism", "PLAIN" );

        // Serializer: how the producer serializes keys and values
        properties.setProperty( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
        properties.setProperty( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        /* ----------------------------------------- */
        // (Step 2) Send Data
        /* 
         * - Sticky Partitioner: improve performance by batching messages (when messages are sent rapidly)
         *      - partitioner.class = null
         * 
         * - Asynchronous Operation: use a callback function that gets triggered when response is received from Kafka broker
         *      - onCompletion() executes every time a record is sent successfully or an exception is thrown.
         */
        /* ----------------------------------------- */

        for (int i = 0; i < 10; i++) {
            
            String topic = "plc_topic";
            String value = "Hello World " + i;
            String key = "id_" + i;
         
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
            
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e == null) {
                        log.info("Received new metadata/ \n" +
                                 "Topic: " + metadata.topic() + "\n" +
                                 "Key: " + producerRecord.key() + "\n" +
                                 "Partition: " + metadata.partition() + "\n" +
                                 "Offset: " + metadata.offset() + "\n" +
                                 "Timestamp: " + metadata.timestamp());
                    } else {
                        log.error("Error while producing ", e);
                    }

                }
            });

            /* Optional: default partitioner behavior can be simulated
             *  - properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());
             */
            // try {
            //     Thread.sleep(2000);
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }}
        }
        

        /* ----------------------------------------- */
        // (Step 3) Flush and Close the Producer
        /* ----------------------------------------- */
        // Flush Data (Synchronous): block until all data is sent and received by Kafka.
        producer.flush();
        producer.close();

    }
}
