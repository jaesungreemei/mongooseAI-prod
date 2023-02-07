package kafka;

import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.Logger;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;

public class ProducerAppServer extends AbstractKafkaApp {
    
    private final Logger log = Logger.getLogger( ProducerAppServer.class.getName() );

    private KafkaProducer<String, String> kafkaProducer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ProducerAppServer() throws Exception {
    }

    /* ----------------------------------------------------------------------------------------- */

    public void runAlways(String topicName, int port) throws Exception {
        
        ServerSocket serverSocket = null;
        String inData = null;
        String key = null;
        
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Waiting for connection on port: " + serverSocket.getLocalPort());

                Socket clientSocket = serverSocket.accept();
                System.out.format("Connected to Port: %d. " , port);

                while (true) {
                    try {
                        BufferedReader inputReader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
                        inData = inputReader.readLine();

                        if(inData != null) {
                            JSONParser parser = new JSONParser();
                            JSONObject parsedData = (JSONObject) parser.parse(inData);
                            
                            // Use "machine_id" as key for partition (i.e. every machine has its own partition).
                            key = String.valueOf(parsedData.get("machine_id"));
                            
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
    // private JSONObject convertHour(JSONObject parsedData) {
    //     JSONObject outData = new JSONObject();
    //     String dt = parsedData.get("dt");

    //     DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");



    //     return outData;
    // }

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
