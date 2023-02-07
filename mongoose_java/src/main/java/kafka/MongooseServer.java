package kafka;

import java.net.*;
import java.io.*;

public class MongooseServer {

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

    private static class Server implements Runnable {
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
                            System.out.println("Message from Client: " + inData);
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
