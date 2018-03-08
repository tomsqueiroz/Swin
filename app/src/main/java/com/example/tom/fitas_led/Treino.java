package com.example.tom.fitas_led;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.fields_serie;
import com.example.tom.fitas_led.Model.fields_serie_id_treino;
import com.example.tom.fitas_led.Model.fields_trecho;
import com.example.tom.fitas_led.Model.fields_treino;

import java.util.ArrayList;
import java.util.List;

/** CREATED BY TOMÁS QUEIROZ 24/04
 *  DESCRIPTION: ACTIVITY CHAMADA PELA MAIN ACTIVITY PARA CRIAR NOVOS TREINOS
 *  OBSERVATION:
 */

public class Treino extends AppCompatActivity implements Handler.Callback{

    public final static String TAG = "Treino_Activity";
    private Context context = this;


    private List<fields_serie> lista_series;
    private boolean selected_spinner = false;

    CustomListArrayAdapter adapter = null;


    /**********DECLARANDO BANCO DE DADOS SQL E HANDLER *************/
    DBHandler dbhandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino);






        /**********INSTANCIANDO E CRIANDO BANCO DE DADOS*********/
        dbhandler = new DBHandler(this);



        /*********DECLARANDO ATRIBUTOS A SEREM UTILIZADO PELA UI THREAD*****/
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        final Dialog dialog2 = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        final Button add_serie = (Button) findViewById(R.id.add_serie);
        final EditText text_nome = (EditText) findViewById(R.id.nome);
        final Button save = (Button) findViewById(R.id.save);
        final Spinner spinner_treinos = (Spinner) findViewById(R.id.spinner_edittreino);


        /***************************DIALOG***************************/
        dialog.setContentView(R.layout.dialog_novo_treino);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        dialog2.setContentView(R.layout.dialog_add_trecho);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        final EditText distancia, d_entre_repeticoes, tempo, d_entre_series;
        final NumberPicker repeticoes = (NumberPicker) dialog.findViewById(R.id.repeticoes);
        final CheckBox dividir = (CheckBox) dialog.findViewById(R.id.trechos);
        distancia = (EditText) dialog.findViewById(R.id.distancia);
        d_entre_repeticoes = (EditText) dialog.findViewById(R.id.d_entre_repeticoes);
        tempo = (EditText) dialog.findViewById(R.id.tempo);
        d_entre_series = (EditText) dialog.findViewById(R.id.d_entre_series);
        Button ok = (Button) dialog.findViewById(R.id.ok_button);
        final TextView text_multiplo = (TextView) dialog.findViewById(R.id.text_multiplo);
        final Button b1 = (Button) dialog.findViewById(R.id.button);
        final Button b2 = (Button) dialog.findViewById(R.id.button2);
        final Button b3 = (Button) dialog.findViewById(R.id.button3);




        /***********************DIALOG OWN TIMEPICKER********************/
        final Dialog d = new Dialog(context);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.time_picker_own, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.5f));



        final List<Integer> ja_clickados = new ArrayList<>();




        /************ADICIONA TREINOS PRESENTES NO BANCO A UM SPINNER*************/
        final List<String> lista_spinner = new ArrayList<String>();
        lista_spinner.add("Escolha seu treino");
        List<fields_treino> treinos = new ArrayList<>();
        treinos = dbhandler.getAllTrains();


        if(dbhandler.getTrainCount() != 0) {
            /***********LENDO QUANTOS TREINOS TEM NO BANCO**************/
            Log.d(TAG, "Numero de treinos no banco" + dbhandler.getTrainCount());
            for (int i = 0; i < dbhandler.getTrainCount(); ++i) {
                lista_spinner.add(treinos.get(i).name);
                Log.d(TAG, "Nome treino == " + treinos.get(i).name);
            }

            /************ADICIONANDO ITENS AO SPINNER*************/
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, lista_spinner);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            spinner_treinos.setAdapter(dataAdapter);

            spinner_treinos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    if (position != 0 && !ja_clickados.contains(position)) {

                        ja_clickados.add(position);
                        fields_treino treino = dbhandler.getTreino(parent.getItemAtPosition(position).toString());
                        lista_series = dbhandler.getSeries(treino.id_treino);

                        adapter = new CustomListArrayAdapter(lista_series, context, parent.getItemAtPosition(position).toString());
                        ListView lView = (ListView) findViewById(R.id.list_series);
                        lView.setAdapter(adapter);

                        selected_spinner = true;

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //BOTAO RESPONSAVEL POR SALVAR TREINO NO BANCO
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(text_nome.getText().toString().matches("")){ /* ADICIONAR CONDICAO PARA VER SE TEM ALGUMA SERIE NO ADAPTER, SE NAO TIVER APAGAR O TREINO)*/
                    final Toast toast = Toast.makeText(context , "DADOS INCOMLETOS", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(dbhandler.getTreino(text_nome.getText().toString()) == null){


                    if(adapter == null){

                        finish();

                    }
                    else if(adapter.getCount() == 0){

                        finish();


                    }
                    else{

                        dbhandler.addTrain(text_nome.getText().toString());
                        List<fields_serie> series = adapter.getSeries();

                        for(int i = 0; i < series.size(); ++i){

                            fields_serie_id_treino serie = new fields_serie_id_treino(dbhandler.getTreino(text_nome.getText().toString()).id_treino,
                                    series.get(i).id_serie, series.get(i).repeticoes, series.get(i).distancia, series.get(i).d_entre_repeticoes, series.get(i).d_entre_series);


                            dbhandler.addSerie(serie,dbhandler.getTrechos(serie.id_serie).get(0), dbhandler.getTrechos(serie.id_serie).get(1));
                        }
                    }




                    finish();


                }
                else if(dbhandler.getSeries(dbhandler.getTreino(text_nome.getText().toString()).id_treino).isEmpty()){

                    fields_treino treino = new fields_treino(dbhandler.getTreino(text_nome.getText().toString()).id_treino, text_nome.getText().toString());

                    dbhandler.deleteTrain(treino);

                    finish();

                }


                else {
                    finish();
                }

            }
        });



        //SE O CLIQUE OCORRER NO BOTAO OK, SIGNIFICA QUE O TREINADOR NAO DIVIDIU EM TRECHOS ENTAO SALVAR NO BANCO COM APENAS UM TRECHO COM IDA IGUAL A VOLTA
        //FUNCAO EH CHAMADA APENAS AO CRIAR A SERIE
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            fields_serie_id_treino serie;

            if(distancia.getText().toString().matches("") || d_entre_repeticoes.getText().toString().matches("") || tempo.getText().toString().matches("")
                    || d_entre_series.getText().toString().matches("")){

                final Toast toast = Toast.makeText(context , "DADOS INCOMPLETOS", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(Float.parseFloat(distancia.getText().toString()) % dbhandler.getTamanho().get("nome") != 0){


                final Toast toast = Toast.makeText(context , "DISTANCIA NAO MULTIPLA", Toast.LENGTH_SHORT);
                toast.show();

            }
            else{

                if(repeticoes.getValue() == 1)
                    serie = new fields_serie_id_treino( dbhandler.getTreino(text_nome.getText().toString()).id_treino ,-1,
                            (float) repeticoes.getValue(), Float.parseFloat(distancia.getText().toString()),
                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                            Float.parseFloat(d_entre_series.getText().toString()));
                else
                    serie = new fields_serie_id_treino( dbhandler.getTreino(text_nome.getText().toString()).id_treino ,-1,
                            (float) repeticoes.getValue()-1, Float.parseFloat(distancia.getText().toString()),
                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                            Float.parseFloat(d_entre_series.getText().toString()));

                fields_trecho trecho = new fields_trecho(-1, Float.parseFloat(tempo.getText().toString()), Float.parseFloat(distancia.getText().toString()));
                List<fields_trecho> trechos = new ArrayList<fields_trecho>();
                trechos.add(trecho);

                //ADCIONA A SERIE COM APENAS UM TRECHO IGUAL PRA IDA E VOLTA COM A DISTANCIA IGUAL
                dbhandler.addSerie(serie, trechos, null);


                //PEGA AS NOVAS SERIES DO TREINO
                lista_series = dbhandler.getSeries(dbhandler.getTreino(text_nome.getText().toString()).id_treino);

                //INFLA O ADAPTADOR
                CustomListArrayAdapter adapter = new CustomListArrayAdapter(lista_series, context, text_nome.getText().toString());
                ListView lView = (ListView)findViewById(R.id.list_series);
                lView.setAdapter(adapter);


                dialog.dismiss();



            }

            selected_spinner = true;




            }

        });

        dividir.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                if(distancia.getText().toString().matches("") || d_entre_repeticoes.getText().toString().matches("") ||
                        d_entre_series.getText().toString().matches("")){

                    final Toast toast = Toast.makeText(context , "DADOS INCOMPLETOS", Toast.LENGTH_SHORT);
                    dividir.setChecked(false);
                    toast.show();
                }
                else if(Float.parseFloat(distancia.getText().toString()) % dbhandler.getTamanho().get("nome") != 0){


                    final Toast toast = Toast.makeText(context , "DISTANCIA NAO MULTIPLA", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {



                    tempo.setClickable(false);
                    tempo.setActivated(false);



                    final CheckBox ida_volta = (CheckBox) dialog2.findViewById(R.id.ida_volta);
                    final ImageButton add_volta = (ImageButton) dialog2.findViewById(R.id.add_volta);
                    ImageButton add_ida = (ImageButton) dialog2.findViewById(R.id.add_ida);
                    ImageButton save_btn = (ImageButton) dialog2.findViewById(R.id.save_btn);
                    final Lista_TrechosIdaArrayAdapter list_ida_adapter = new Lista_TrechosIdaArrayAdapter(new ArrayList<fields_trecho>(), dialog2.getContext(), dbhandler.getTamanho().get("nome"));
                    final Lista_TrechosVoltaArrayAdapter list_volta_adapter = new Lista_TrechosVoltaArrayAdapter(new ArrayList<fields_trecho>(), dialog2.getContext(), dbhandler.getTamanho().get("nome"));
                    ListView lView = (ListView) dialog2.findViewById(R.id.list_ida);
                    ListView l2View = (ListView) dialog2.findViewById(R.id.list_volta);
                    lView.setAdapter(list_ida_adapter);
                    lView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    lView.setItemsCanFocus(true);
                    l2View.setAdapter(list_volta_adapter);
                    dialog2.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



                    final List<fields_trecho> lista_trechos_ida = new ArrayList<fields_trecho>();
                    final List<fields_trecho> lista_trechos_volta = new ArrayList<fields_trecho>();

                    add_volta.setVisibility(View.INVISIBLE);

                    dialog2.show();


                    ida_volta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (ida_volta.isChecked()) {              /*******IDA != VOLTA******/
                                add_volta.setVisibility(View.VISIBLE);
                            } else {   //IDA == VOLTA
                                add_volta.setVisibility(View.INVISIBLE);

                            }

                        }
                    });

                    add_volta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            list_volta_adapter.add();

                        }


                    });



                    add_ida.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            list_ida_adapter.add();


                        }
                    });

                    save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(ida_volta.isChecked()){ /********IDA != VOLTA***********/

                                if(list_ida_adapter.is_data_ok() && list_volta_adapter.is_data_ok()){

                                    List<fields_trecho> trechos_ida = list_ida_adapter.get_trechos();
                                    List<fields_trecho> trechos_volta = list_volta_adapter.get_trechos();

                                    fields_serie_id_treino serie = new fields_serie_id_treino(dbhandler.getTreino(text_nome.getText().toString()).id_treino, -1,
                                            (float) repeticoes.getValue()-1,
                                            Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()));


                                    dbhandler.addSerie(serie, trechos_ida, trechos_volta);

                                    List<fields_serie> series = dbhandler.getSeries(dbhandler.getTreino(text_nome.getText().toString()).id_treino);

                                    CustomListArrayAdapter lista_series_adapter = new CustomListArrayAdapter(series, context, text_nome.getText().toString());
                                    ListView lView = (ListView) findViewById(R.id.list_series);
                                    lView.setAdapter(lista_series_adapter);


                                    selected_spinner = true;


                                    dialog2.dismiss();
                                    dialog.dismiss();


                                }
                                else{

                                    final Toast toast = Toast.makeText(context , "SOMA DOS TRECHOS INDEVIDA", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            }
                            else{

                                if(list_ida_adapter.is_data_ok()){

                                    List<fields_trecho> trechos_ida = list_ida_adapter.get_trechos();


                                    fields_serie_id_treino serie = new fields_serie_id_treino(dbhandler.getTreino(text_nome.getText().toString()).id_treino, -1,
                                                    (float) repeticoes.getValue()-1,
                                                    Float.parseFloat(distancia.getText().toString()),
                                                    Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                                    Float.parseFloat(d_entre_series.getText().toString()));



                                    dbhandler.addSerie(serie, trechos_ida, null);

                                    List<fields_serie> series = dbhandler.getSeries(dbhandler.getTreino(text_nome.getText().toString()).id_treino);

                                    CustomListArrayAdapter lista_series_adapter = new CustomListArrayAdapter(series, context, text_nome.getText().toString());
                                    ListView lView = (ListView) findViewById(R.id.list_series);
                                    lView.setAdapter(lista_series_adapter);

                                    selected_spinner = true;


                                    dialog2.dismiss();
                                    dialog.dismiss();

                                }
                                else{

                                    final Toast toast = Toast.makeText(context , "SOMA DOS TRECHOS INDEVIDA", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            }
                        }
                    });

                }





            }
        });






                    //BOTAO PARA ADICIONAR SERIES AO TREINO - LANÇA UM DIALOG
                    add_serie.setOnClickListener(new View.OnClickListener() {



                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Clickou no Add_Serie");



                            text_multiplo.setText("Multipla de " +  Float.toString(dbhandler.getTamanho().get("nome")));
                            distancia.setText("");
                            d_entre_repeticoes.setText("");
                            tempo.setText("");
                            d_entre_series.setText("");


                            repeticoes.setMinValue(1);
                            repeticoes.setMaxValue(50);




                            if(text_nome.getText().toString().matches("")){
                                final Toast toast = Toast.makeText(context , "ESCOLHER NOME TREINO", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else if(dbhandler.getTrainCount() == 0){

                                dbhandler.addTrain(text_nome.getText().toString());

                                dialog.show();
                                text_nome.setEnabled(false);
                                text_nome.setClickable(false);



                            }
                            else if(dbhandler.getTreino(text_nome.getText().toString())!= null && !selected_spinner){

                                final Toast toast = Toast.makeText(context , "TREINO JA EXISTE", Toast.LENGTH_SHORT);
                                toast.show();

                            }


                            else{

                                dbhandler.addTrain(text_nome.getText().toString());
                                distancia.setText("");
                                d_entre_repeticoes.setText("");
                                tempo.setText("");
                                d_entre_series.setText("");
                                dialog.show();
                                text_nome.setEnabled(false);
                                text_nome.setClickable(false);

                             }
            }

        });



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                d.setContentView(layout);
                Button save_time = (Button) d.findViewById(R.id.save_time);
                final TextView texto_d_entre_repeticoes = (TextView) dialog.findViewById(R.id.texto_d_entre_repeticoes);
                final NumberPicker np_minutos = (NumberPicker) d.findViewById(R.id.np_minutos);
                final NumberPicker np_segundos = (NumberPicker) d.findViewById(R.id.np_segundos);
                final NumberPicker np_dsegundos = (NumberPicker) d.findViewById(R.id.np_dsegundos);


                np_minutos.setMinValue(0);
                np_minutos.setMaxValue(60);


                np_segundos.setMinValue(0);
                np_segundos.setMaxValue(60);


                np_dsegundos.setMinValue(0);
                np_dsegundos.setMaxValue(100);


                np_minutos.setValue(0);
                np_segundos.setValue(0);
                np_dsegundos.setValue(0);

                d.show();


                save_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        b1.setVisibility(View.INVISIBLE);
                        texto_d_entre_repeticoes.setText(Integer.toString(np_minutos.getValue()) + ":" + Integer.toString(np_segundos.getValue()) + ":" + Integer.toString(np_dsegundos.getValue()));
                        d.dismiss();

                    }
                });

                texto_d_entre_repeticoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String [] numbers_parsed = texto_d_entre_repeticoes.getText().toString().split(":");

                        String minutos = numbers_parsed[0];
                        String segundos = numbers_parsed[1];
                        String dsegundos = numbers_parsed[2];

                        np_minutos.setValue(Integer.parseInt(minutos));
                        np_segundos.setValue(Integer.parseInt(segundos));
                        np_dsegundos.setValue(Integer.parseInt(dsegundos));

                        d.show();



                    }
                });






            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.setContentView(layout);
                d.show();


            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.setContentView(layout);
                d.show();


            }
        });



    }



    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage: ");
        return false;
    }
}
