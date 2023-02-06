package kafka;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.Logger;

import java.net.*;
import java.io.*;

public class ProducerAppServer extends AbstractKafkaApp {
    
    private final Logger log = Logger.getLogger( ProducerAppServer.class.getName() );

    private KafkaProducer<String, String> kafkaProducer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ProducerAppServer() throws Exception {
    }

    /* ----------------------------------------------------------------------------------------- */

    public void runAlways(String topicName) throws Exception {
        ServerSocket serverSocket = null;
        int port = 4444;

        String inData = null;
        String key = null;
        
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Waiting for connection...");

                Socket clientSocket = serverSocket.accept();
                System.out.format("Connected to Port: %d. " , port);

                while (true) {
                    try {
                        BufferedReader inputReader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
                        inData = inputReader.readLine();

                        if(inData != null) {
                            JSONParser parser = new JSONParser();
                            JSONObject parsedData = (JSONObject) parser.parse(inData);
                            
                            // ProducerApp
                            // key = String.valueOf(parsedData.get("metric_category"));
                            this.send(topicName, key, inData);
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        }
    }

    /* ----------------------------------------------------------------------------------------- */

    protected void send(String topicName, String key, String message) throws Exception {
        String source = ProducerAppServer.class.getName();
        JSONObject obj = MessageHelper.getMessageLogEntryJSON(source, topicName, key, message);
        
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, message);
        log.info(obj.toJSONString());
        getProducer().send(producerRecord);
    }

    /* ----------------------------------------------------------------------------------------- */

    private KafkaProducer<String, String> getProducer() throws Exception {
        if(this.kafkaProducer == null) {
            Properties props = PropertiesHelper.getProperties();
            this.kafkaProducer = new KafkaProducer<>(props);
        }
        return this.kafkaProducer;
    }

    /* ----------------------------------------------------------------------------------------- */

    public void shutdown() throws Exception {
        closed.set(true);
        log.info( MessageHelper.getSimpleJSONObject("Shutting down Producer...").toJSONString() );
        getProducer().close();
    }

    /* ----------------------------------------------------------------------------------------- */

}
