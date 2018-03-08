package com.example.tom.fitas_led;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.fields_serie;
import com.example.tom.fitas_led.Model.fields_trecho;
import com.example.tom.fitas_led.Model.fields_treino;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 07/05/2017.
 */

public class Treino_Raia_ListAdapter extends BaseAdapter implements ListAdapter {


    private List<fields_treino> list = new ArrayList<fields_treino>();
    public List<fields_treino> treinos_marcados = new ArrayList<fields_treino>();
    private Context context;
    private String TAG = "TREINO_RAIA_LISTADAPTER";

    private int serie_atual = 0;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);



    public List<fields_treino> treinos_a_enviar = new ArrayList<>();


    public void set_marcados(List<fields_treino> treinos_marcados) {

        this.treinos_marcados = treinos_marcados;


    }




    public Treino_Raia_ListAdapter(List<fields_treino> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.treino_raia_item, null);
        }


        //Handle TextView and display string from your list
        final TextView ItemText = (TextView) view.findViewById(R.id.raias_item_string);
        final CheckBox check = (CheckBox) view.findViewById(R.id.checkBox);
        final Button show = (Button) view.findViewById(R.id.show_btn);
        final Dialog dialog = new Dialog(context);
        final DBHandler db = new DBHandler(context);


        ItemText.setText(list.get(position).name);

        if(treinos_a_enviar.contains(list.get(position))){

            check.setChecked(true);

        }



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "OnOKClick");

                if(check.isChecked()){
                    Log.d(TAG, "MARCOU");

                    Boolean multiplo = false;
                    List<fields_serie> series = db.getSeries(list.get(position).id_treino);


                    for(int i = 0; i < series.size(); ++i){

                        if(series.get(i).distancia % db.getTamanho().get("nome") == 0){
                            multiplo = true;
                        }


                    }
                    if(multiplo)
                        treinos_a_enviar.add(list.get(position));
                    else {
                        check.setChecked(false);

                        final Toast toast = Toast.makeText(context, "DISTANCIA NAO MULTIPLA, EDITE", Toast.LENGTH_SHORT);

                        toast.show();

                    }

                }
                else{
                    Log.d(TAG, "DESMARCOU");

                    treinos_a_enviar.remove(list.get(position));

                }

                if(treinos_a_enviar.size() > 0) {


                    for (int i = 0; i < treinos_a_enviar.size(); i++) {
                        Log.d(TAG, "LISTA ITEM ==" + treinos_a_enviar.get(i).name);
                    }
                }

                else{
                    Log.d(TAG, "LISTA VAZIA");
                }


            }

        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ShowBTn");


                serie_atual = 0;
                dialog.setContentView(R.layout.dialog_show_treino);


                final TextView treino = (TextView) dialog.findViewById(R.id.treino_nome);
                final TextView serie = (TextView) dialog.findViewById(R.id.serie_numero);
                final TextView repeticoes = (TextView) dialog.findViewById(R.id.text_repeticoes);
                final TextView distancia = (TextView) dialog.findViewById(R.id.text_distancia);
                final TextView descanso = (TextView) dialog.findViewById(R.id.text_desca);
                final TextView intervalo = (TextView) dialog.findViewById(R.id.text_intervalo);
                final TextView tempo = (TextView) dialog.findViewById(R.id.text_tempo);
                ImageButton exit = (ImageButton) dialog.findViewById(R.id.exit_btn);
                final TextView show_trechos = (TextView) dialog.findViewById(R.id.show_trechos);
                final ImageButton next = (ImageButton) dialog.findViewById(R.id.next_serie);
                final ImageButton previous = (ImageButton) dialog.findViewById(R.id.previous_serie);
                final Toast toast = Toast.makeText(context , "Treino nao Possui Series", Toast.LENGTH_SHORT);

                treino.setText(list.get(position).name);

                dialog.show();


                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);
                        dialog.dismiss();

                    }
                });

                final List<fields_serie> series = db.getSeries(list.get(position).id_treino);

                if(series.size() == 0){

                    Log.d(TAG, "TREINO NAO POSSUI SERIES");
                    toast.show();

                    dialog.dismiss();



                }

                else{

                    serie.setText("Serie Atual -> " + Integer.toString(serie_atual));
                    repeticoes.setText(Float.toString(series.get(serie_atual).repeticoes));
                    distancia.setText(Float.toString(series.get(serie_atual).distancia));
                    descanso.setText(Float.toString(series.get(serie_atual).d_entre_repeticoes));
                    intervalo.setText(Float.toString(series.get(serie_atual).d_entre_series));
                    tempo.setText(Float.toString(1));

                    if(serie_atual - 1 < 0){
                        previous.setVisibility(View.INVISIBLE);
                    }
                    if(serie_atual + 1 >= series.size()){
                        next.setVisibility(View.INVISIBLE);

                    }
                }


                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);
                        previous.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        serie_atual ++;

                        if(serie_atual - 1 < 0){
                            previous.setVisibility(View.INVISIBLE);
                        }
                        if(serie_atual + 1 >= series.size()){
                            next.setVisibility(View.INVISIBLE);
                        }


                        serie.setText("Serie Atual -> " + Integer.toString(serie_atual));
                        repeticoes.setText(Float.toString(series.get(serie_atual).repeticoes));
                        distancia.setText(Float.toString(series.get(serie_atual).distancia));
                        descanso.setText(Float.toString(series.get(serie_atual).d_entre_repeticoes));
                        intervalo.setText(Float.toString(series.get(serie_atual).d_entre_series));
                        tempo.setText("1");//Float.toString(series.get(serie_atual).tempo));


                    }
                });

                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);
                        previous.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        serie_atual --;


                        if(serie_atual - 1 < 0){
                            previous.setVisibility(View.INVISIBLE);
                        }
                        else if(serie_atual + 1 >= series.size()){
                            next.setVisibility(View.INVISIBLE);

                        }

                        serie.setText("Serie Atual -> " + Integer.toString(serie_atual));
                        repeticoes.setText(Float.toString(series.get(serie_atual).repeticoes));
                        distancia.setText(Float.toString(series.get(serie_atual).distancia));
                        descanso.setText(Float.toString(series.get(serie_atual).d_entre_repeticoes));
                        intervalo.setText(Float.toString(series.get(serie_atual).d_entre_series));
                        tempo.setText("1");

                    }
                    });


                    show_trechos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog dialog2 = new Dialog(context);
                            dialog2.setContentView(R.layout.dialog_add_trecho);

                            List<fields_trecho> trechos_ida = db.getTrechos(series.get(serie_atual).id_serie).get(0);
                            List<fields_trecho> trechos_volta = db.getTrechos(series.get(serie_atual).id_serie).get(1);


                            final ListView lView_ida = (ListView) dialog2.findViewById(R.id.list_ida);
                            final ListView lView_volta = (ListView) dialog2.findViewById(R.id.list_volta);
                            final CheckBox ida_volta = (CheckBox) dialog2.findViewById(R.id.ida_volta);
                            final ImageButton add_volta = (ImageButton) dialog2.findViewById(R.id.add_volta);
                            ImageButton add_ida = (ImageButton) dialog2.findViewById(R.id.add_ida);
                            ImageButton save_btn = (ImageButton) dialog2.findViewById(R.id.save_btn);

                            final Lista_TrechosIdaArrayAdapter list_ida_adapter = new Lista_TrechosIdaArrayAdapter(trechos_ida, dialog2.getContext(), db.getTamanho().get("nome"));
                            final Lista_TrechosVoltaArrayAdapter list_volta_adapter = new Lista_TrechosVoltaArrayAdapter(trechos_volta, dialog2.getContext(), db.getTamanho().get("nome"));
                            list_ida_adapter.turn_button_invisible();
                            list_volta_adapter.turn_button_invisible();


                            lView_ida.setAdapter(list_ida_adapter);
                            lView_volta.setAdapter(list_volta_adapter);




                            ida_volta.setVisibility(View.INVISIBLE);
                            add_ida.setVisibility(View.INVISIBLE);
                            add_volta.setVisibility(View.INVISIBLE);
                            save_btn.setVisibility(View.INVISIBLE);



                            dialog2.show();
                        }
                    });
            }

        });





        return view;
    }

}

