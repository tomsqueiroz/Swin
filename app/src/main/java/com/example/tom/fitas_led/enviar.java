package com.example.tom.fitas_led;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.tom.fitas_led.Model.fields_treino;

/**
 * Created by Tom on 10/03/2017.
 */

public class enviar {

    final String TAG = "LOOOOOOOG";
    final String SSID = "bruno";
    //final SendMessageAsync send;
    final Handler mHandler;

    //INICIA TENTATIVA DE SE CONCTAR A WIFI//
    final wifi w;

    public enviar(fields_treino treino, Context context){

        final Bundle bundle = new Bundle();

        mHandler = new Handler(context.getMainLooper());
        //send = new SendMessageAsync(mHandler, treino);
        w = new wifi(SSID, null, context, 0);



        /*//NAO ESTA CONECTADO EM NENHUMA WIFI - CONEXAO NAO FUNCIONARA
                else if(!w.get_connected_status()){
                    Log.d(TAG, "Nao esta conectado em wifi");
                    text_log.setText("Por favor conecte-se a wifi");
                    w.connect();
                }
                */

        //INICIAR TAREFA ASINCRONA RESPONSAVEL POR ENVIAR DADOS VIA TCP//
        Log.d(TAG, "Enviando antes executar async");
        //send.execute();
       // Log.d(TAG, send.getStatus().toString());

    }

}
