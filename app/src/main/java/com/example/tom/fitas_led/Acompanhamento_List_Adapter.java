package com.example.tom.fitas_led;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.Tempos_State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 05/07/2017.
 */

public class Acompanhamento_List_Adapter extends BaseAdapter implements ListAdapter, SendMessageAsync.AsyncResponse{

    private Context context = null;
    private ArrayList<String> list = new ArrayList<String>();
    private int disponiveis;
    private int nao_disponiveis;
    private int desligadas;
    private boolean n_achou;

    private int tempo_atual;
    private int tempo_total;

    private View v;

    private Thread thread = null;


    private final android.os.Handler mHandler = new android.os.Handler(Looper.myLooper());
    private List<String> lista_progress = new ArrayList<>();

    public List<Tempos> tempos_th = new ArrayList<>();

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);



    private DBHandler db;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        db = new DBHandler(context);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.acompanhamento_list_item, null);
        }

        TextView listItemText = (TextView) view.findViewById(R.id.raia_name);
        ImageButton play = (ImageButton) view.findViewById(R.id.play);
        ImageButton pause = (ImageButton) view.findViewById(R.id.pause);
        ImageButton stop = (ImageButton) view.findViewById(R.id.delete);
        ImageButton power_on = (ImageButton) view.findViewById(R.id.power_on);
        ImageButton power_off = (ImageButton) view.findViewById(R.id.power_off);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        v = view;


        listItemText.setText("Raia " + Integer.toString(Integer.parseInt(list.get(position)) + 1));
            if(position < disponiveis ){


                power_on.setVisibility(View.VISIBLE);
                power_off.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
            }
            else if(position >= disponiveis && position < disponiveis + nao_disponiveis){

                power_on.setVisibility(View.INVISIBLE);
                power_off.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);

                this.tempo_atual = parse_atual(position);
                this.tempo_total = parse_final(position);

                for(int i = 0; i < list.size(); ++i){
                    tempos_th.add(null);
                }

                tempos_th.add(position, new Tempos(view, tempo_atual, tempo_total, ((Tempos_State) v.getContext().getApplicationContext()).getState(position), position));

                thread = new Thread(tempos_th.get(position));
                thread.start();

                ((Tempos_State) v.getContext().getApplicationContext()).setState(tempos_th.get(position).mPaused, position);


            }
            else {


                power_off.setVisibility(View.VISIBLE);
                power_on.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);

            }


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                if(!((Tempos_State) v.getContext().getApplicationContext()).getState(position)) {
                    ((Tempos_State) v.getContext().getApplicationContext()).setState(true, position);
                    send(list.get(position) + ":699");
                    tempos_th.get(position).onPause();
                }


            }
        });








        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Log.d("Acompanhamento", "Stop");
                send(list.get(position) + ":700" );
                thread.interrupt();
                progressBar.setProgress(0);
                progressBar.setMax(0);


                Intent loading_activity = new Intent(v.getContext(), Loading_Raias.class);
                Bundle pacote = new Bundle();
                pacote.putString("origem", "1");

                ((Tempos_State) v.getContext().getApplicationContext()).setState(true, position);

                loading_activity.putExtra("origem", pacote);
                ((Activity) v.getContext()).finish();
                v.getContext().startActivity(loading_activity);
                ((Activity) v.getContext()).overridePendingTransition(0, 0);


            }
        });


        power_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                /******FALTA PARTE EM QUE O SE PEDE PISCINA POR NOME*******/
                send(list.get(position) + ":" + Math.round(db.getTamanho().get("nome")));


                Intent loading_activity = new Intent(v.getContext(), Loading_Raias.class);
                Bundle pacote = new Bundle();
                pacote.putString("origem", "1");

                loading_activity.putExtra("origem", pacote);
                ((Activity) v.getContext()).finish();
                v.getContext().startActivity(loading_activity);
                ((Activity) v.getContext()).overridePendingTransition(0, 0);



            }
        });

        power_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                send(list.get(position) + ":" + Math.round(db.getTamanho().get("nome")));


                Intent loading_activity = new Intent(v.getContext(), Loading_Raias.class);
                Bundle pacote = new Bundle();
                pacote.putString("origem", "1");

                loading_activity.putExtra("origem", pacote);
                ((Activity) v.getContext()).finish();
                v.getContext().startActivity(loading_activity);
                ((Activity) v.getContext()).overridePendingTransition(0, 0);


            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                if(((Tempos_State) v.getContext().getApplicationContext()).getState(position)) {
                    ((Tempos_State) v.getContext().getApplicationContext()).setState(false, position);
                    send(list.get(position) + ":699");
                    tempos_th.get(position).onResume();
                }




            }
        });



        return view;

    }



    private int parse_atual(int position){

        String progress = lista_progress.get(position);

        String[] separated_progress = progress.split(",");

        return Math.round(Float.parseFloat(separated_progress[0]));



    }

    private int parse_final(int position){

        String progress = lista_progress.get(position);

        String[] separated_progress = progress.split(",");

        return Math.round(Float.parseFloat(separated_progress[1]));


    }


    public Acompanhamento_List_Adapter(Context context, ArrayList<String> list, int disponiveis, int nao_disponiveis, int desligadas, List<String> lista_progress){
        this.list = list;
        this.context = context;
        this.disponiveis = disponiveis;
        this.nao_disponiveis = nao_disponiveis;
        this.desligadas = desligadas;
        this.lista_progress = lista_progress;

    }

    @Override
    public void processFinish(String output) {

        n_achou = false;



    }


    public void send(String string){

        final SendMessageAsync send = new SendMessageAsync(mHandler, string, this);
        final Toast toast = Toast.makeText(context, "ERRO AO ENVIAR", Toast.LENGTH_SHORT);

        n_achou = true;


        send.comecar();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(n_achou) {

                    Log.d("SEND ", "NAO ACHOU");
                    send.cancelar();
                    //toast.show();
                }

            }
        }, 5000);





    }

}
