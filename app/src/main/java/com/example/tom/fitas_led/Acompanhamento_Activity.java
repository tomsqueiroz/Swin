package com.example.tom.fitas_led;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.Tempos_State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tom on 06/06/2017.
 */

public class Acompanhamento_Activity extends AppCompatActivity implements SendMessageAsync.AsyncResponse{

    private static final String TAG = "Acompanhamento_Activity";

    private final android.os.Handler mHandler = new android.os.Handler(Looper.myLooper());


    private boolean n_achou;

    private String[] separated_disponiveis = null;
    private String[] separated_nao_disponiveis = null;
    private String[] separated_desligadas = null;
    private String[] raias_tempos = null;
    private List<String> tempos = new ArrayList<>();
    private ArrayList<String> raias_disponiveis = null;
    private ArrayList<String> raias_nao_disponiveis = null;
    private ArrayList<String> raias_desligadas = null;
    private int disponiveis;
    private int nao_disponiveis;
    private int desligadas;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> list_geral = new ArrayList<String>();



    final Context context = this;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);



    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acompanhamento);

        Log.d(TAG, "OnCreate");

        ImageButton play_geral = (ImageButton) findViewById(R.id.play_geral);
        ImageButton pause_geral = (ImageButton) findViewById(R.id.pause_geral);
        ImageButton stop_geral = (ImageButton) findViewById(R.id.stop_geral);


        Intent i = getIntent();
        Bundle pacote = new Bundle();

        pacote = i.getBundleExtra("raias");
        final ArrayList<String> raias = pacote.getStringArrayList("raias");

        switch (raias.size()){

            case 1:
                separated_disponiveis = raias.get(0).split(":");
                raias_disponiveis = new ArrayList<String>(Arrays.asList(separated_disponiveis));
                disponiveis = raias_disponiveis.size();

                list.addAll(raias_disponiveis);

                break;

            case 2:
                separated_disponiveis = raias.get(0).split(":");
                raias_disponiveis = new ArrayList<String>(Arrays.asList(separated_disponiveis));
                disponiveis = raias_disponiveis.size();
                list.addAll(raias_disponiveis);


                separated_nao_disponiveis = raias.get(1).split(":");
                raias_nao_disponiveis = new ArrayList<String>(Arrays.asList(separated_nao_disponiveis));
                nao_disponiveis = raias_nao_disponiveis.size();
                list.addAll(raias_nao_disponiveis);
                list_geral.addAll(raias_nao_disponiveis);

                break;

            case 3:

                separated_disponiveis = raias.get(0).split(":");
                raias_disponiveis = new ArrayList<String>(Arrays.asList(separated_disponiveis));

                for(int x = 0; x < raias_disponiveis.size(); ++x){

                    if(!raias_disponiveis.get(x).matches("")){
                        list.add(raias_disponiveis.get(x));
                        tempos.add("");

                    }
                }
                disponiveis = list.size();



                separated_nao_disponiveis = raias.get(1).split(":");
                raias_nao_disponiveis = new ArrayList<String>(Arrays.asList(separated_nao_disponiveis));


                for(int x = 0; x < raias_nao_disponiveis.size(); ++x) {



                    if (!raias_nao_disponiveis.get(x).matches("")) {

                        raias_tempos = raias_nao_disponiveis.get(x).split(",");

                        list.add(raias_tempos[0]);
                        list_geral.add(raias_tempos[0]);

                        tempos.add(raias_tempos[1] + "," + raias_tempos[2]);

                    }
                }

                nao_disponiveis = list.size() - disponiveis;






                separated_desligadas = raias.get(2).split(":");
                raias_desligadas = new ArrayList<String>(Arrays.asList(separated_desligadas));



                for(int x = 0; x < raias_desligadas.size(); ++x) {

                    if (!raias_desligadas.get(x).matches("")) {
                        list.add(raias_desligadas.get(x));
                        tempos.add("");
                    }
                }

                desligadas = list.size() - disponiveis - nao_disponiveis;


                break;
        }

        for(int x = 0; x < list.size(); ++x){

            if(list.get(x).matches("")){
               list.remove(x);
            }
        }

        for(int x = 0; x < list_geral.size(); ++ x){

            if(list_geral.get(x).matches("")){
                list_geral.remove(x);
            }


        }



        final Acompanhamento_List_Adapter adapter = new Acompanhamento_List_Adapter(context, list, disponiveis, nao_disponiveis, desligadas, tempos);
        ListView lView = (ListView) findViewById(R.id.raias_list);
        lView.setAdapter(adapter);



        pause_geral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);


                String playzao = "";

                for(int i = 0; i < list_geral.size(); ++i){

                    playzao += list_geral.get(i);

                    if(i == list_geral.size() - 1)
                        playzao+= ":699";
                    else
                        playzao += ",";


                }

                for(int i = 0 ; i < 8; ++i){
                    ((Tempos_State) v.getContext().getApplicationContext()).setState(true, i);
                }



                send(playzao);

                final Toast toast = Toast.makeText(context , "PAUSE GERAL", Toast.LENGTH_SHORT);

                toast.show();



                for(int i = 0; i < adapter.tempos_th.size(); ++i){

                    if(adapter.tempos_th.get(i) != null) {
                        adapter.tempos_th.get(i).onPause();
                    }

                    ((Tempos_State) v.getContext().getApplicationContext()).setState(true, i);

                }



            }

        });



        stop_geral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                String playzao = "";

                for(int i = 0; i < list_geral.size(); ++i){

                    playzao += list_geral.get(i);

                    if(i == list_geral.size() - 1)
                        playzao+= ":700";
                    else
                        playzao += ",";


                }

                for(int i = 0 ; i < 8; ++i){
                    ((Tempos_State) v.getContext().getApplicationContext()).setState(true, i);
                }


                send(playzao);


                final Toast toast = Toast.makeText(context , "STOP GERAL", Toast.LENGTH_SHORT);

                toast.show();

                final Intent loading_activity = new Intent(v.getContext(), Loading_Raias.class);
                Bundle pacote = new Bundle();
                pacote.putString("origem", "1");
                loading_activity.putExtra("origem", pacote);
                ((Activity) context).finish();
                v.getContext().startActivity(loading_activity);



            }

        });


        play_geral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                String playzao = "";

                for(int i = 0; i < list_geral.size(); ++i){

                    playzao += list_geral.get(i);

                    if(i == list_geral.size() - 1)
                        playzao+= ":699";
                    else
                        playzao += ",";

                }


                for(int i = 0 ; i < 8; ++i){
                    ((Tempos_State) v.getContext().getApplicationContext()).setState(false, i);
                }





                send(playzao);

                final Toast toast = Toast.makeText(context , "PLAY GERAL", Toast.LENGTH_SHORT);

                toast.show();


                for(int i = 0; i < adapter.tempos_th.size(); ++i){

                    if(adapter.tempos_th.get(i) != null) {
                        adapter.tempos_th.get(i).onResume();
                    }

                    ((Tempos_State) v.getContext().getApplicationContext()).setState(false, i);

                }


            }
        });












    }


    @Override
    protected void onStop(){
        super.onStop();


        Log.d(TAG, "onStop");


    }

    @Override
    protected void onResume(){
        super.onResume();


        Log.d(TAG, "onResume");


    }


    @Override
    public void processFinish(String output) {
        Log.d(TAG, "ENviou");
        n_achou = false;


    }



    public void send(String string){

        final SendMessageAsync send = new SendMessageAsync(mHandler, string, this);
        final Toast toast = Toast.makeText(this, "ERRO AO ENVIAR", Toast.LENGTH_SHORT);

        n_achou = true;


        send.comecar();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "Cancelar");

                if(n_achou) {

                    send.cancelar();
                    toast.show();

                }

            }
        }, 5000);

    }



}
