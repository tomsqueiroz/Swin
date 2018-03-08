package com.example.tom.fitas_led;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

/**
 * Created by Tom on 09/05/2017.
 */

public class EditTreino extends AppCompatActivity implements Handler.Callback{

    private Context context = this;


    public final static String TAG = "Edit Activity";


    private List<fields_serie> lista_series;
    private fields_treino treino = null;
    private boolean selected_spinner = false;

    private Dialog dialog;
    private Dialog dialog2;




    /**********DECLARANDO BANCO DE DADOS SQL E HANDLER *************/
    DBHandler dbhandler;

    CustomListArrayAdapter adapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittreino);


        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        dialog2 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);



        /**********INSTANCIANDO E CRIANDO BANCO DE DADOS*********/
        dbhandler = new DBHandler(this);



        /*********DECLARANDO ATRIBUTOS A SEREM UTILIZADO PELA UI THREAD*****/

        Button add_serie = (Button) findViewById(R.id.add_serie);

        final Button save = (Button) findViewById(R.id.save);
        final Spinner spinner_treinos = (Spinner) findViewById(R.id.spinner_edittreino);


        /***************************DIALOG***************************/
        dialog.setContentView(R.layout.dialog_novo_treino);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        dialog2.setContentView(R.layout.dialog_add_trecho);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        final EditText distancia, d_entre_repeticoes, tempo, d_entre_series;
        final NumberPicker repeticoes;
        CheckBox dividir = (CheckBox) dialog.findViewById(R.id.trechos);
        distancia = (EditText) dialog.findViewById(R.id.distancia);
        d_entre_repeticoes = (EditText) dialog.findViewById(R.id.d_entre_repeticoes);
        tempo = (EditText) dialog.findViewById(R.id.tempo);
        d_entre_series = (EditText) dialog.findViewById(R.id.d_entre_series);
        repeticoes = (NumberPicker) dialog.findViewById(R.id.repeticoes);
        Button ok = (Button) dialog.findViewById(R.id.ok_button);
        final TextView tam_piscina = (TextView) dialog.findViewById(R.id.tamanho_piscina);
        final TextView text_multiplo = (TextView) dialog.findViewById(R.id.text_multiplo);








        /************ADICIONA TREINOS PRESENTES NO BANCO A UM SPINNER*************/
        final List<String> lista_spinner = new ArrayList<String>();
        lista_spinner.add("Escolha seu treino");
        List<fields_treino> treinos = dbhandler.getAllTrains();


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

                    if (position != 0) {
                        treino = dbhandler.getTreino(parent.getItemAtPosition(position).toString());

                        lista_series = dbhandler.getSeries(treino.id_treino);



                        adapter = new CustomListArrayAdapter(lista_series, context, treino.name);
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

                if(treino != null){

                    if(dbhandler.getSeries(treino.id_treino).isEmpty()){

                        dbhandler.deleteTrain(treino);

                        finish();

                    }
                }

                finish();

            }
        });



        //BOTAO NO DIALOG QUE ADICIONA DADOS DE SERIE A UMA LISTA, PARA QUE SEJAM ADICIONADAS NO FUTURO
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

                    serie = new fields_serie_id_treino(treino.id_treino ,-1,
                            (float) (repeticoes.getValue()), Float.parseFloat(distancia.getText().toString()),
                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                            Float.parseFloat(d_entre_series.getText().toString()));

                    fields_trecho trecho = new fields_trecho(-1, Float.parseFloat(tempo.getText().toString()), Float.parseFloat(distancia.getText().toString()));
                    List<fields_trecho> trechos = new ArrayList<fields_trecho>();
                    trechos.add(trecho);

                    //ADCIONA A SERIE COM APENAS UM TRECHO IGUAL PRA IDA E VOLTA COM A DISTANCIA IGUAL
                    dbhandler.addSerie(serie, trechos, null);

                    //LIMPA LISTA
                    lista_series.clear();


                    //PEGA AS NOVAS SERIES DO TREINO
                    lista_series = dbhandler.getSeries(treino.id_treino);

                    //INFLA O ADAPTADOR
                    adapter = new CustomListArrayAdapter(lista_series, context, treino.name);
                    ListView lView = (ListView)findViewById(R.id.list_series);
                    lView.setAdapter(adapter);


                    dialog.dismiss();
                }




            }

        });



        //BOTAO PARA ADICIONAR SERIES AO TREINO - LANÇA UM DIALOG
        add_serie.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clickou no Add_Serie");


                if(treino == null){
                    final Toast toast = Toast.makeText(context , "ESCOLHER NOME TREINO", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(dbhandler.getTrainCount() == 0){


                    tam_piscina.setText( "Tamanho Piscina -> " + Float.toString(dbhandler.getTamanho().get("nome")) + " m");
                    text_multiplo.setText("Multiplo de " +  Float.toString(dbhandler.getTamanho().get("nome")));
                    dialog.show();



                }
                else{


                    //tam_piscina.setText("Tamanho Piscina -> " +  Float.toString(dbhandler.getTamanho().get("nome")) + " m");
                    text_multiplo.setText("Multiplo de " +   Float.toString(dbhandler.getTamanho().get("nome")));
                    distancia.setText("");
                    d_entre_series.setText("");
                    tempo.setText("");
                    d_entre_series.setText("");
                    dialog.show();

                }



            }

        });



        dividir.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                if(distancia.getText().toString().matches("") || d_entre_repeticoes.getText().toString().matches("") ||
                        d_entre_series.getText().toString().matches("")){

                    final Toast toast = Toast.makeText(context , "DADOS INCOMPLETOS", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(Float.parseFloat(distancia.getText().toString()) % dbhandler.getTamanho().get("nome") != 0){


                    final Toast toast = Toast.makeText(context , "DISTANCIA NAO MULTIPLA", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {



                    tempo.setClickable(false);
                    tempo.setActivated(false);

                    dialog2.show();

                    final CheckBox ida_volta = (CheckBox) dialog2.findViewById(R.id.ida_volta);
                    final ImageButton add_volta = (ImageButton) dialog2.findViewById(R.id.add_volta);
                    ImageButton add_ida = (ImageButton) dialog2.findViewById(R.id.add_ida);
                    ImageButton save_btn = (ImageButton) dialog2.findViewById(R.id.save_btn);
                    final Lista_TrechosIdaArrayAdapter list_ida_adapter = new Lista_TrechosIdaArrayAdapter(new ArrayList<fields_trecho>(),dialog2.getContext(), dbhandler.getTamanho().get("nome"));
                    final Lista_TrechosVoltaArrayAdapter list_volta_adapter = new Lista_TrechosVoltaArrayAdapter(new ArrayList<fields_trecho>(), dialog2.getContext(), dbhandler.getTamanho().get("nome"));
                    final List<fields_trecho> lista_trechos_ida = new ArrayList<fields_trecho>();
                    final List<fields_trecho> lista_trechos_volta = new ArrayList<fields_trecho>();





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

                                    int id_serie = dbhandler.Search_serie((float) repeticoes.getValue(), Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()), trechos_ida, trechos_volta);


                                    fields_serie_id_treino serie = new fields_serie_id_treino(treino.id_treino, id_serie,
                                            (float) repeticoes.getValue(),
                                            Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()));


                                    dbhandler.updateSerie(serie, trechos_ida, trechos_volta);


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

                                    int id_serie = dbhandler.Search_serie((float) repeticoes.getValue(), Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()), trechos_ida, null);

                                    fields_serie_id_treino serie = new fields_serie_id_treino(treino.id_treino, id_serie,
                                            (float) repeticoes.getValue(),
                                            Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()));



                                    dbhandler.updateSerie(serie, trechos_ida, null);


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
    }






    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage: ");
        return false;
    }
}
