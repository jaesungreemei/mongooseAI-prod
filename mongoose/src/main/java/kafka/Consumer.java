package kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {
    private static final Logger log = LoggerFactory.getLogger( Producer.class.getSimpleName() );

    public static void main(String[] args) {

        /* ----------------------------------------- */
        // (Step 1) Create Consumer Properties, Consumer Object
        /*
         * - KEY_DESERIALIZER_CLASS_CONFIG: must be the same as key serializer from Producer
         * - VALUE_DESERIALIZER_CLASS_CONFIG: must be the same as value serializer from Producer
         * - GROUP_ID_CONFIG: consumer group name
         * - AUTO_OFFSET_RESET_CONFIG: none/earliest/latest
         *      - none:        if no previous offset is found, do not even start
         *      - earliest:    read from the very beginning of the topic
         *      - latest:      read only from the "now" of the topic
         * - PARTITION_ASSIGNMENT_STRATETGY_CONFIG: RangeAssignor/RoundRobin/StickyAssignor/CooperativeStickyAssignor
         *      - RangeAssignor: assign partitions on a per-topic basis (can lead to imbalance)
         *      - RoundRobin: assign partitions across all topics in round-robin fashion, optimal balance
         *      - StickyAssignor: balanced like roundRobin, and then minimizees partition movements when consumer join / leaves the group in order to minimize movements (Cooperative Rebalance)
         *      - CooperativeStickyAssignor: rebalance strategy is identical to StickyAssignor, but supports cooperative rebalances and consumers can keep on consuming from topic
         */
        /* ----------------------------------------- */
        Properties properties = new Properties();

        String bootstrapServers = "localhost:9092";
        String groupId = "plc_group";
        String topic = "plc_topic";
        String auto_offset_reset = "earliest";

        // Connect to localhost
        properties.setProperty( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        properties.setProperty( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName() );
        properties.setProperty( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName() );
        properties.setProperty( ConsumerConfig.GROUP_ID_CONFIG, groupId );
        properties.setProperty( ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, auto_offset_reset );
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        // Security Protocol (eg. Conduktor Playground)
        // properties.setProperty( "bootstrap.servers", "..." );
        // properties.setProperty( "security.protocol", "SASL_SSL" );
        // properties.setProperty( "sasl.jaas.config", "..." );
        // properties.setProperty( "sasl.mechanism", "PLAIN" );

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        /* ----------------------------------------- */
        // (Step 2) Graceful Shutdown
        /*
         * - ensure that we have code in place to respond to termination signals
         * - consumer.wakeup(): the next time we do a consumer.poll(), it calls an exception, allowing us to leave the while(true) loop.
         */
        /* ----------------------------------------- */

        // Get a reference to the current thread
        final Thread mainThread = Thread.currentThread();

        // Add the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("[SHUTDOWN HOOK] Detected a shutdown, exit by calling consumer.wakeup()...");
                consumer.wakeup();

                // Join the main thread, allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /* ----------------------------------------- */
        // (Step 3) Subscribe Consumer to topics
        /*
         * - Poll for New Data:
         *      - Get as many records as you can
         *      - If there is no reply, wait for 100ms
         *      - If no reply, go to next line of code, records become empty collection
         * 
         * - Exception to topic subscription:
         *      - wakeup() Exception: consumer.poll() at some point will throw an expected WakeupException
         *      - Shutdown Hook: consumer.close()
         */
        /* ----------------------------------------- */
        try {
            consumer.subscribe(Arrays.asList(topic));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll( Duration.ofMillis(1000) );
                for (ConsumerRecord<String, String> record: records) {
                    log.info( "Key: " + record.key() + ", Value: " + record.value() );
                    log.info( "Partition: " + record.partition() + ", Offset: " + record.offset() );
                }
            }

        } catch (WakeupException e) {
            log.info("Wake up exception!");
        } catch (Exception e) {
            log.error("Unexpected exception");
        } finally {
            consumer.close();   // this will also commit the offsets if need be
            log.info("The consumer is now gracefully closed.");
        }

    }
}
