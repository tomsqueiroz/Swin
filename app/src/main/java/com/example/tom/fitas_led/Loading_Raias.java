package com.example.tom.fitas_led;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tom on 06/05/2017.
 */

public class Loading_Raias extends AppCompatActivity implements SendMessageAsync.AsyncResponse {


    private static final String TAG = "Loading_Raias";
    private Intent Start_Activity;
    private Bundle pacote;
    private boolean n_achou;
    private String origem = "";


    Handler mHandler = new Handler(Looper.myLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        Intent i = getIntent();

        final Context context = this;

        origem += i.getBundleExtra("origem").getString("origem");

        n_achou = true;

        final SendMessageAsync conexao = new SendMessageAsync(mHandler, "23", this);
        conexao.comecar();

        final Toast toast = Toast.makeText(this, "RAIAS NÃO ENCONTRADAS, CONECTE-SE", Toast.LENGTH_SHORT);

        Start_Activity = new Intent(getApplicationContext(), Start.class);

        pacote = new Bundle();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "Cancelar");

                if(n_achou) {
                    conexao.cancelar();
                    finish();
                    toast.show();
                }

            }
        }, 5000);

    }





    @Override
    public void processFinish(String output) {
        Log.d(TAG, "On Process Finish");



        if(!output.matches("")){



            //ArrayList<String> raias;

            n_achou = false;


            if(origem.matches("0")) {
                //PARSE DADOS PARA LISTA DE STRING DE RAIAS

                String[] separated_todas = output.split(";");
                //raias = new ArrayList<String>(Arrays.asList(separated_todas));

                /****SEPARA RAIAS DISPONIVEIS****/
                String[] separated_disponiveis = separated_todas[0].split(":");
                ArrayList<String> raias_disponiveis = new ArrayList<>(Arrays.asList(separated_disponiveis));

                for(int x = 0; x < raias_disponiveis.size(); ++x){
                    if(raias_disponiveis.get(x).matches("")){
                        raias_disponiveis.remove(x);
                    }



                }


                Log.d(TAG, "SPLIT TEST " + raias_disponiveis);


                pacote.putStringArrayList("raias", raias_disponiveis);

                Start_Activity.putExtra("raias", pacote);

                this.finish();

                startActivity(Start_Activity);


            }
            else if(origem.matches("1")){
                /****ACOMPANAMENTO - PEDIR TODAS AS RAIAS *****/

                Intent Acompanhamento_Activity = new Intent(getApplicationContext(), Acompanhamento_Activity.class);


                ArrayList<String> raias;
                String[] separated_todas = output.split(";");
                raias = new ArrayList<String>(Arrays.asList(separated_todas));
                if(raias.size() == 2){
                    raias.add("");
                }


                pacote.putStringArrayList("raias", raias);

                Acompanhamento_Activity.putExtra("raias", pacote);

                this.finish();

                startActivity(Acompanhamento_Activity);



            }

        }


    }
}
