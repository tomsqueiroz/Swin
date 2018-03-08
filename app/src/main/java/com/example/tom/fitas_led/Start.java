package com.example.tom.fitas_led;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.tom.fitas_led.Model.fields_serie;
import com.example.tom.fitas_led.Model.fields_trecho;
import com.example.tom.fitas_led.Model.fields_treino;

import java.util.ArrayList;
import java.util.List;

/** CREATED BY TOMÁS QUEIROZ 24/04
 *  DESCRIPTION: ACTIVITY CHAMADA PELA MAIN -> RESPONSÁVEL PELA COMUNICAÇÃO COM uC E POR SELECIONAR
 *               QUAL TREINO SERÁ ENVIADO A FITA.
 *  OBSERVATION: IMPLEMENTAR PARTE DE SELEÇÃO E VISUALIZAÇÃO DAS SERIES DENTRO DE UM TREINO
 */

public class Start extends AppCompatActivity  implements SendMessageAsync.AsyncResponse {


    private String TAG = "Start Activity";

    private Intent main_intent;
    private Bundle pacote;
    final Context context = this;
    private DBHandler db;
    private List<fields_treino> list = new ArrayList<fields_treino>();
    private List<fields_serie> lista_series = new ArrayList<>();

    private final android.os.Handler mHandler = new android.os.Handler(Looper.myLooper());


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("conectado", "conectado");
        startActivity(i);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);


        db = new DBHandler(this);
        main_intent = getIntent();
        pacote = main_intent.getBundleExtra("raias");
        final ArrayList<String> raias = pacote.getStringArrayList("raias");
        final Intent loading_raias = new Intent(getApplicationContext(), Loading_Raias.class);





        Log.d(TAG, raias.toString());

        final Raias_List_Adapter adapter = new Raias_List_Adapter(raias, context);
        ListView lView = (ListView) findViewById(R.id.raias_list);
        lView.setAdapter(adapter);


        Button send = (Button) findViewById(R.id.send_button);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String treino = "";

                for(int i = 0; i < raias.size(); ++i){



                    list = adapter.map_raias.get(raias.get(i));

                    if(list == null){
                        Log.d(TAG, "Nada para a raia " + raias.get(i));


                    }
                    else if(!list.isEmpty()){

                        parse(list, raias.get(i));

                    }
                }

                Bundle pacote = new Bundle();
                pacote.putString("origem", "1");

                loading_raias.putExtra("origem", pacote);

                ((Activity) context).finish();
                startActivity(loading_raias);

            }
        });





    }


    void parse(List<fields_treino> list, String raia){




        String treino_completo = "";


        for(int i = 0; i < list.size(); i++){

            if(i == 0)
                treino_completo += raia + ":";


            List<fields_serie> series = db.getSeries(list.get(i).id_treino);


            treino_completo += parse_send(series, raia);
        }

       StringBuilder sb = new StringBuilder(treino_completo);
       sb.deleteCharAt(treino_completo.length()-1);
       SendMessageAsync conexao = new SendMessageAsync(mHandler, sb.toString(), this);
       conexao.comecar();


    }


    String parse_send(List<fields_serie> series, String raia){
        String series_enviar = "";

        float tempo;

        for (int i = 0; i < series.size(); ++i) {




            tempo = 1;

            series_enviar += Math.round(series.get(i).repeticoes) + ":" + Math.round(series.get(i).distancia) + ":"
                    + series.get(i).d_entre_repeticoes + ":" + parse_trechos(series.get(i).id_serie) + ">" + series.get(i).d_entre_series;

            if (i != series.size() - 1)
                series_enviar += ";";

            if (i == series.size() - 1)
                series_enviar += "-";


        }


        return series_enviar;



    }


    private String parse_trechos (int id_serie){

        List <fields_trecho> trecho_ida = db.getTrechos(id_serie).get(0);
        List <fields_trecho> trechos_volta = db.getTrechos(id_serie).get(1);

        String trechos_enviar = "";

        for(int i = 0; i < trecho_ida.size(); ++i){

            trechos_enviar += trecho_ida.get(i).tamanho;

            if(i != trecho_ida.size() - 1)
                trechos_enviar += ":";

            if(i == trecho_ida.size() - 1)
                trechos_enviar += "/";
        }

        for(int i = 0; i < trecho_ida.size(); ++i){

            trechos_enviar += trecho_ida.get(i).tempo;

            if(i != trecho_ida.size() - 1)
                trechos_enviar += ":";

            if(i == trecho_ida.size() - 1 && trechos_volta.size()!= 0)
                trechos_enviar += "|";
        }

        for(int i = 0; i < trechos_volta.size(); ++i){

            trechos_enviar += trechos_volta.get(i).tamanho;

            if(i != trechos_volta.size() - 1)
                trechos_enviar += ":";

            if(i == trechos_volta.size() - 1)
                trechos_enviar += "/";
        }

        for(int i = 0; i < trechos_volta.size(); ++i){

            trechos_enviar += trechos_volta.get(i).tempo;

            if(i != trechos_volta.size() - 1)
                trechos_enviar += ":";

        }

        return trechos_enviar;
    }



    @Override
    public void processFinish(String output) {
        Log.d(TAG, "ENVIOU");
    }
}



