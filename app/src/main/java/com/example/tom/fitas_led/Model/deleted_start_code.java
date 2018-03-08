package com.example.tom.fitas_led.Model;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 06/05/2017.
 */

public class deleted_start_code {
    //ANTES ON CREATE
/*

    private EditText distancia,descanso,repeticao,tempo, intervalo;
    private TextView text_log;
    private Button button;

    private int i,treino_escolhido;
    private Context context = this;

    private List<fields_serie_id> series;
    private int serie_atual;

     private DBHandler dbhandler;
    private Spinner spinner;
    private int NUMERO_TREINOS;


    //////DEPOIS ON CREATE


    /*treino_escolhido = -1;
        dbhandler = new DBHandler(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        distancia = (EditText) findViewById(R.id.distancia);
        descanso = (EditText) findViewById(R.id.descanso);
        repeticao = (EditText) findViewById(R.id.repeticoes);
        tempo = (EditText) findViewById(R.id.tempo);
        intervalo = (EditText) findViewById(R.id.intervalo);
        button = (Button) findViewById(R.id.send_button);
        text_log = (TextView) findViewById(R.id.text_log);
        ImageButton next = (ImageButton) findViewById(R.id.next);
        ImageButton previous = (ImageButton) findViewById(R.id.previous);



        /***************CONFIGURACAO DOS EDIT TEXT***********************/
/*
    repeticao.setCursorVisible(false);
    repeticao.setLongClickable(false);
    repeticao.setClickable(false);
    repeticao.setFocusable(false);
    repeticao.setSelected(false);
    repeticao.setKeyListener(null);
    repeticao.setBackgroundResource(android.R.color.transparent);


    distancia.setCursorVisible(false);
    distancia.setLongClickable(false);
    distancia.setClickable(false);
    distancia.setFocusable(false);
    distancia.setSelected(false);
    distancia.setKeyListener(null);
    distancia.setBackgroundResource(android.R.color.transparent);


    descanso.setCursorVisible(false);
    descanso.setLongClickable(false);
    descanso.setClickable(false);
    descanso.setFocusable(false);
    descanso.setSelected(false);
    descanso.setKeyListener(null);
    descanso.setBackgroundResource(android.R.color.transparent);



    intervalo.setCursorVisible(false);
    intervalo.setLongClickable(false);
    intervalo.setClickable(false);
    intervalo.setFocusable(false);
    intervalo.setSelected(false);
    intervalo.setKeyListener(null);
    intervalo.setBackgroundResource(android.R.color.transparent);



    tempo.setCursorVisible(false);
    tempo.setLongClickable(false);
    tempo.setClickable(false);
    tempo.setFocusable(false);
    tempo.setSelected(false);
    tempo.setKeyListener(null);
    tempo.setBackgroundResource(android.R.color.transparent);






    /*******************************CONFIGURACAO DE TOAST*********************************/
/*


    final Toast toast = Toast.makeText(context , "ADICIONE UM TREINO ANTES", Toast.LENGTH_SHORT);
    final Toast toast2 = Toast.makeText(context , "ACABARAM AS SERIES", Toast.LENGTH_SHORT);

    /************ADICIONA TREINOS PRESENTES NO BANCO A UM SPINNER*************/
 /*   final List<String> lista_spinner = new ArrayList<String>();
    lista_spinner.add("Escolha seu treino");
    List<fields_treino> treinos = new ArrayList<>();
    treinos = dbhandler.getAllTrains();


    if(dbhandler.getTrainCount() != 0) {
        /***********LENDO QUANTOS TREINOS TEM NO BANCO**************/
  /*      Log.d(TAG, "Numero de treinos no banco" + dbhandler.getTrainCount());
        for (i = 0; i < dbhandler.getTrainCount(); ++i) {
            lista_spinner.add(treinos.get(i).name);
            Log.d(TAG, "Nome treino == " + treinos.get(i).name);
        }

        /**********VARIAVEL COM O NUMERO DE TREINOS NO BANCO ANTES DE SALVAR ALGUMA COISA*****************/
   //     NUMERO_TREINOS = i;


        /************ADICIONANDO ITENS AO SPINNER*************/
   /*     ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item, lista_spinner);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0) {
                    fields_treino treino = dbhandler.getTreino(parent.getItemAtPosition(position).toString());
                    series = dbhandler.getSeries(treino.id_treino);
                    serie_atual = 0;

                    repeticao.setText(Float.toString(series.get(serie_atual).repeticoes));
                    distancia.setText(Float.toString(series.get(serie_atual).distancia));
                    descanso.setText(Float.toString(series.get(serie_atual).descanso));
                    intervalo.setText(Float.toString(series.get(serie_atual).intervalo));
                    tempo.setText(Float.toString(series.get(serie_atual).tempo));

                    text_log.setText("Dados da serie " + serie_atual + 1);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*********************** VERIFICAR IMPLEMENTAÇÃO E CORRIGIR PARA NOVA ESTRUTURA DE BANCO - TRECHOS*******************/
      /*  serie_atual = 0;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serie_atual + 1 < series.size()){

                    serie_atual += 1;
                    repeticao.setText(Float.toString(series.get(serie_atual).repeticoes));
                    distancia.setText(Float.toString(series.get(serie_atual).distancia));
                    descanso.setText(Float.toString(series.get(serie_atual).descanso));
                    intervalo.setText(Float.toString(series.get(serie_atual).intervalo));
                    tempo.setText(Float.toString(series.get(serie_atual).tempo));


                }
                else{

                    serie_atual = 0;
                    repeticao.setText(Float.toString(series.get(serie_atual).repeticoes));
                    distancia.setText(Float.toString(series.get(serie_atual).distancia));
                    descanso.setText(Float.toString(series.get(serie_atual).descanso));
                    intervalo.setText(Float.toString(series.get(serie_atual).intervalo));
                    tempo.setText(Float.toString(series.get(serie_atual).tempo));
                    toast2.show();


                }



            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serie_atual >= 1){

                    serie_atual -= 1;
                    repeticao.setText(Float.toString(series.get(serie_atual).repeticoes));
                    distancia.setText(Float.toString(series.get(serie_atual).distancia));
                    descanso.setText(Float.toString(series.get(serie_atual).descanso));
                    intervalo.setText(Float.toString(series.get(serie_atual).intervalo));
                    tempo.setText(Float.toString(series.get(serie_atual).tempo));



                }
                else{

                    serie_atual = 0;
                    repeticao.setText(Float.toString(series.get(serie_atual).repeticoes));
                    distancia.setText(Float.toString(series.get(serie_atual).distancia));
                    descanso.setText(Float.toString(series.get(serie_atual).descanso));
                    intervalo.setText(Float.toString(series.get(serie_atual).intervalo));
                    tempo.setText(Float.toString(series.get(serie_atual).tempo));
                    toast2.show();


                }



            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });




    }


    else{
        toast.show();
        finish();
    }


}




    private fields_treino get_Treino(String name){
        Log.d(TAG, "on TReino escolhido:");

        return dbhandler.getTreino(name);




    }



*/
}
