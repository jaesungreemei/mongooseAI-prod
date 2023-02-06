package kafka;

import java.net.*;
import java.util.logging.Logger;

import java.io.*;

public class TestServer {

    private static final Logger log = Logger.getLogger( TestServer.class.getName() );

    /* ---------------------------------------------------------------------- */
    public static void main(String[] args) throws InterruptedException {
        /*
         * Main:
         *  - create and start server thread instance
         */
        
        Thread serverThread = new Thread( new Server() );
        serverThread.start();
    }

    /* ---------------------------------------------------------------------- */

    protected static class Server implements Runnable {
        /*
         * Runnable Interface:
         *  - implement Server on Thread using the Runnable interface
         *  - private --> so that the Server implementation is only accessible within the declared class
         *  - static --> a static nested class can be instantiated without creating an object of the outer class
         */

        @Override
        public void run() {
            /*
             * Override the run() function in the interface for Server implementation.
             */

            ServerSocket serverSocket = null;
            int port = 4444;

            String inData = null;
            String source = TestServer.class.getName();

            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Waiting for connection...");

                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.format("Connected to Port: %d. " , port);

                        BufferedReader inputReader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
                        inData = inputReader.readLine();

                        if(inData != null) {
                            // JSONParser parser = new JSONParser();
                            // JSONObject parsedData = (JSONObject) parser.parse(inData);
                            
                            // // ProducerApp
                            // key = parsedData.get("metric_category");
                            // getProducer().send();

                            log.info("[" + source + "] " + inData);
                        }
                        
                    } catch (SocketTimeoutException ste) {
                        ste.printStackTrace();
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

    /* ---------------------------------------------------------------------- */
}
