package com.example.tom.fitas_led;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.tom.fitas_led.Model.fields_treino;

/**
 * Created by Tom on 03/03/2017.
 */

public class SendMessageAsync extends AsyncTask <String, String, String> {

    private              String     COMMAND     = ""      ;
    private              TCPClient  tcpClient                        ;
    private              Handler    mHandler                         ;
    private static final String     TAG         = "SendMessageAsync";
    private              String     ip_add      = "192.168.4.1";            //IP_DEFAULT = 192.168.4.1
    public AsyncResponse delegate = null;


    /********SEND MESSAGE ASYNC CONSTRUCTOR**************/
    public SendMessageAsync(Handler mHandler, String comando, AsyncResponse delegate){
        this.COMMAND += comando;
        this.mHandler = mHandler;

        this.delegate = delegate;

    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public void comecar(){

        this.execute();
    }

    public void cancelar(){

        if(tcpClient != null)
            tcpClient.stopClient();

        this.cancel(true);

    }





    /*******FUNCAO CHAMADA COM METODO .EXECUTE()*******/
    @Override
    protected String doInBackground(String... params) {

        Log.d(TAG, "In do in background");



        try {
            tcpClient = new TCPClient(mHandler,
                    COMMAND,
                    ip_add,
                    new TCPClient.MessageCallback() {
                        @Override
                        public void callbackMessageReceiver(String message) {
                            publishProgress(message);

                        }
                    });

        } catch (NullPointerException e) {
            Log.d(TAG, "Caught null pointer exception");
            e.printStackTrace();
        }


        if(!isCancelled()) {
            tcpClient.run();
        }

        if(isCancelled()){
            tcpClient.stopClient();
            Log.d(TAG, "Cancelado");
        }

         return tcpClient.mensagem;

    }


    /********FUNCAO CHAMADA COM METODO PUBLISH PROGRESS*////////////////
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "In progress update, values: " + values.toString());
        if(values[0] != null) {

        }
        else{
            tcpClient.sendMessage("wrong");
          //  mHandler.sendEmptyMessage(MainActivity.ERROR);
            tcpClient.stopClient();
        }
    }


    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Log.d(TAG, "In on post execute");


        Log.d(TAG, "RESULTADO == " + result);
        delegate.processFinish(result);
    }
}

