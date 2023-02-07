package kafka;

import org.apache.log4j.Logger;

public abstract class AbstractKafkaApp {
    /**
     * Instantiates a new Abstract class, KafkaApp.
     * This abstract class's constructor provides graceful
     * shutdown behavior for Kafka producers and consumers
     *
     */
    // @throws Exception the exception

    private final Logger log = Logger.getLogger( AbstractKafkaApp.class.getName() );

    public AbstractKafkaApp() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        log.info( MessageHelper.getSimpleJSONObject("Created the Shutdown Hook").toJSONString() );
    }

    public abstract void shutdown() throws Exception;

    public abstract void runAlways(String topicName, int port) throws Exception;

}
