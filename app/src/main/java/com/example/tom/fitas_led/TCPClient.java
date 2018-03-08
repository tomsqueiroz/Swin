package com.example.tom.fitas_led;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Tom on 03/03/2017.
 */

public class TCPClient {

        private static final String            TAG             = "TCPClient"     ;
        private final        Handler           mHandler                          ;
        private              String            ipNumber, incomingMessage, command;
        BufferedReader    in                                ;
        PrintWriter       out                               ;
        private              MessageCallback   listener        = null            ;
        private              boolean           mRun            = false           ;
        private              int               porta           = 5000;
        private String MENSAGEM_RECEBIDA = "ç";
        private Socket socket;
        public String mensagem = "";



        /**
         * TCPClient class constructor, which is created in AsyncTasks after the button click.
         * @param mHandler Handler passed as an argument for updating the UI with sent messages
         * @param command  Command passed as an argument, e.g. "shutdown -r" for restarting computer
         * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
         * @param listener Callback interface object
         */
        public TCPClient(Handler mHandler, String command, String ipNumber, MessageCallback listener) {
            this.listener         = listener;
            this.ipNumber         = ipNumber;
            this.command          = command ;
            this.mHandler         = mHandler;
        }

        /**
         * Public method for sending the message via OutputStream object.
         * @param message Message passed as an argument and sent via OutputStream object.
         */
        public void sendMessage(String message){
            if (out != null && !out.checkError()) {
                mRun = true;
                out.print(message);
                out.flush();


                Log.d(TAG, "Sent Message: " + message);

            }
        }

        /**
         * Public method for stopping the TCPClient object ( and finalizing it after that ) from AsyncTask
         */
        public void stopClient(){
            Log.d(TAG, "Client stopped!");
            mRun = false;
            if(socket!= null) {
                try {
                    Log.d(TAG, "Socket Closed");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public void run() {

        mRun = true;

       // try {







            try {

                InetAddress serverAddress = InetAddress.getByName(ipNumber);
                Log.d(TAG, "Connecting...");


                socket = new Socket(serverAddress, porta);
                socket.setReuseAddress(true);
                Log.d(TAG, "SOCKET = " + socket.toString());


                // Create PrintWriter object for sending messages to server.
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //Create BufferedReader object for receiving messages from server.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(TAG, "In/Out created");
                while (mRun) {


                    this.sendMessage(command);

                    incomingMessage = in.readLine();
                    if (incomingMessage != null && listener != null) {

                        listener.callbackMessageReceiver(incomingMessage);

                    }
                    if (incomingMessage != null) {
                        mensagem = incomingMessage;
                        Log.d(TAG, "Received Message: " + incomingMessage);


                        if (!incomingMessage.contains(MENSAGEM_RECEBIDA)) {
                            out.flush();
                            out.close();
                            socket.close();
                            mRun = false;
                        }
                    }
                    incomingMessage = null;

                }

            } catch (UnknownHostException e) {

                Log.d(TAG, "Error do anus");


            } catch (IOException  e) {


                //Log.d(TAG, "Error di tito");
                //this.run();


            } catch (Exception e) {

                Log.d(TAG, "Error", e);

            } finally {
                try {
                    if(out != null && in != null && socket != null) {
                        out.flush();
                        out.close();
                        in.close();
                        socket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Log.d(TAG, "Socket Closed");
            }
/*
        } catch (Exception e) {

            Log.d(TAG, "Error", e);

        }
*/
    }






    public boolean isRunning() {
        return mRun;
    }
    public interface MessageCallback {
        public void callbackMessageReceiver(String message);
    }




}