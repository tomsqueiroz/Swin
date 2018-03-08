package com.example.tom.fitas_led;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.fields_serie;
import com.example.tom.fitas_led.Model.fields_serie_id_treino;
import com.example.tom.fitas_led.Model.fields_trecho;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 06/05/2017.
 */

public class CustomListArrayAdapter extends BaseAdapter implements ListAdapter{


    private List<fields_serie> list = new ArrayList<fields_serie>();
    private Context context;
    private String nome_treino;
    private DBHandler dbhandler;

    private String[] numbers = new String[4];


    private CustomListArrayAdapter.ViewHolder holder;



    public CustomListArrayAdapter(List<fields_serie> list, Context context, String nome_treino) {
        this.list = list;
        this.context = context;
        this.nome_treino = nome_treino;
        dbhandler = new DBHandler(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public List<fields_serie> getSeries(){

        return list;

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


    public class ViewHolder{

        Dialog dialogx;
        Dialog dialogy;

        public ViewHolder(View view){

            dialogx = new Dialog(view.getContext(), android.R.style.Theme_Black_NoTitleBar);
            dialogx.setContentView(R.layout.dialog_add_trecho);
            dialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            dialogy = new Dialog(view.getContext(),android.R.style.Theme_Black_NoTitleBar);
            dialogy.setContentView(R.layout.dialog_novo_treino);
            dialogy.setTitle("Title...");
            dialogy.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        }



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.novotreino_list_item, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);

        }
        else{

            view = convertView;
            holder = (CustomListArrayAdapter.ViewHolder) view.getTag();

        }


        final EditText distancia, d_entre_repeticoes, tempo, d_entre_series;
        final NumberPicker repeticoes = (NumberPicker) holder.dialogy.findViewById(R.id.repeticoes);
        distancia = (EditText) holder.dialogy.findViewById(R.id.distancia);
        d_entre_repeticoes = (EditText) holder.dialogy.findViewById(R.id.d_entre_repeticoes);
        tempo = (EditText) holder.dialogy.findViewById(R.id.tempo);
        d_entre_series = (EditText) holder.dialogy.findViewById(R.id.d_entre_series);
        final Button ok = (Button) holder.dialogy.findViewById(R.id.ok_button);
        final TextView text_multiplo = (TextView) holder.dialogy.findViewById(R.id.text_multiplo);
        final CheckBox checkbox_trechos = (CheckBox) holder.dialogy.findViewById(R.id.trechos);



        parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ((ViewGroup) view).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);


        final TextView listItemText = (TextView)view.findViewById(R.id.raias_item_string);
        listItemText.setText(Integer.toString(position + 1));


        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button editbtn = (Button) view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                dbhandler.deleteSerie(dbhandler.getTreino(nome_treino).id_treino, list.get(position).id_serie);
                list.remove(position);
                notifyDataSetChanged();

            }
        });

        editbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                checkbox_trechos.setChecked(false);

                final List<fields_trecho> trechos_ida;
                final List<fields_trecho> trechos_volta;


                text_multiplo.setText("Multipla de " + Float.toString(dbhandler.getTamanho().get("nome")));

                trechos_ida = dbhandler.getTrechos(list.get(position).id_serie).get(0);
                trechos_volta = dbhandler.getTrechos(list.get(position).id_serie).get(1);

                repeticoes.setMinValue(1);
                repeticoes.setMaxValue(50);

                repeticoes.setValue((int) (list.get(position).repeticoes));
                distancia.setText(Float.toString(list.get(position).distancia));
                d_entre_repeticoes.setText(Float.toString(list.get(position).d_entre_repeticoes));
                d_entre_series.setText(Float.toString(list.get(position).d_entre_series));
                tempo.setClickable(false);



                //SE TIVER 1 TRECHO COM IDA IGUAL, NAO FOI DIVIDIDO EM TRECHO
                if (trechos_ida.size() == 1 && (trechos_volta == null || trechos_volta.size() == 0)) {

                    // TEMPO SERA O TEMPO DE CADA PISCINA VEZES O NUMERO DE PISCINAS
                    tempo.setText(Float.toString(trechos_ida.get(0).tempo));


                } else if (trechos_ida.size() > 1 || trechos_volta != null) {

                    tempo.setText("");
                    tempo.setClickable(false);

                }
                holder.dialogy.show();

                if (distancia.getText().toString().matches("") || d_entre_repeticoes.getText().toString().matches("") || d_entre_series.getText().toString().matches("")) {

                    final Toast toast = Toast.makeText(context, "DADOS INCOMPLETOS", Toast.LENGTH_SHORT);
                    toast.show();
                }

                    checkbox_trechos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (Float.parseFloat(distancia.getText().toString()) % dbhandler.getTamanho().get("nome") != 0) {

                                final Toast toast = Toast.makeText(context, "DISTANCIA NAO MULTIPLA", Toast.LENGTH_SHORT);
                                checkbox_trechos.setChecked(false);
                                toast.show();

                            } else {

                                final List<fields_trecho> trechos_ida = new ArrayList<>();
                                final List<fields_trecho> trechos_volta = new ArrayList<>();
                                final Lista_TrechosIdaArrayAdapter list_ida_adapter = new Lista_TrechosIdaArrayAdapter(trechos_ida, holder.dialogx.getContext(), dbhandler.getTamanho().get("nome"));
                                final Lista_TrechosVoltaArrayAdapter list_volta_adapter = new Lista_TrechosVoltaArrayAdapter(trechos_volta, holder.dialogx.getContext(), dbhandler.getTamanho().get("nome"));
                                final ImageButton save_button = (ImageButton) holder.dialogx.findViewById(R.id.save_btn);
                                final CheckBox ida_volta = (CheckBox) holder.dialogx.findViewById(R.id.ida_volta);
                                final ImageButton add_ida = (ImageButton) holder.dialogx.findViewById(R.id.add_ida);
                                final ImageButton add_volta = (ImageButton) holder.dialogx.findViewById(R.id.add_volta);
                                final List<fields_trecho> lista_trechos_ida = dbhandler.getTrechos(list.get(position).id_serie).get(0);
                                final List<fields_trecho> lista_trechos_volta = dbhandler.getTrechos(list.get(position).id_serie).get(1);
                                ListView lView = (ListView) holder.dialogx.findViewById(R.id.list_ida);
                                ListView l2View = (ListView) holder.dialogx.findViewById(R.id.list_volta);
                                lView.setAdapter(list_ida_adapter);
                                lView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                                lView.setItemsCanFocus(true);
                                l2View.setAdapter(list_volta_adapter);
                                holder.dialogx.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


                                /********MOSTRAR TRECHOS JA SALVOS NO BANCO PARA ESSA SERIE********/

                                //IDA == VOLTA E DIVIDIDA EM TRECHOS
                                if (lista_trechos_ida.size() > 1 && lista_trechos_volta == null) {

                                    ida_volta.setChecked(false);
                                    add_volta.setVisibility(View.INVISIBLE);

                                    for (int i = 0; i < lista_trechos_ida.size(); ++i) {

                                        trechos_ida.add(lista_trechos_ida.get(i));
                                        list_ida_adapter.create_numbers();
                                    }

                                    list_ida_adapter.notifyDataSetChanged();

                                }

                                //IDA != VOLTA
                                else if (lista_trechos_volta != null) {

                                    ida_volta.setChecked(true);
                                    add_volta.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < lista_trechos_ida.size(); ++i) {

                                        trechos_ida.add(lista_trechos_ida.get(i));
                                        list_ida_adapter.create_numbers();


                                    }
                                    for (int i = 0; i < lista_trechos_volta.size(); ++i) {

                                        trechos_volta.add(lista_trechos_volta.get(i));


                                    }


                                    list_ida_adapter.notifyDataSetChanged();
                                    list_volta_adapter.notifyDataSetChanged();


                                }


                                holder.dialogx.show();

                                save_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (lista_trechos_ida != null) {
                                            lista_trechos_ida.clear();
                                        } else {

                                            final List<fields_trecho> lista_trechos_ida = new ArrayList<fields_trecho>();

                                        }


                                        if (lista_trechos_volta != null) {
                                            lista_trechos_volta.clear();
                                        } else {

                                            final List<fields_trecho> lista_trechos_volta = new ArrayList<fields_trecho>();

                                        }


                                        if (ida_volta.isChecked()) { /********IDA != VOLTA***********/

                                            if (list_ida_adapter.is_data_ok() && list_volta_adapter.is_data_ok()) {

                                                List<fields_trecho> trechos_ida = list_ida_adapter.get_trechos();
                                                List<fields_trecho> trechos_volta = list_volta_adapter.get_trechos();

                                                fields_serie_id_treino serie = new fields_serie_id_treino(dbhandler.getTreino(nome_treino).id_treino, list.get(position).id_serie,
                                                        (float) repeticoes.getValue() - 1,
                                                        Float.parseFloat(distancia.getText().toString()),
                                                        Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                                        Float.parseFloat(d_entre_series.getText().toString()));


                                                dbhandler.updateSerie(serie, trechos_ida, trechos_volta);


                                                holder.dialogx.dismiss();
                                                holder.dialogy.dismiss();


                                            } else {

                                                final Toast toast = Toast.makeText(context, "SOMA DOS TRECHOS INDEVIDA", Toast.LENGTH_SHORT);
                                                toast.show();

                                            }

                                        } else {

                                            if (list_ida_adapter.is_data_ok()) {

                                                List<fields_trecho> trechos_ida = list_ida_adapter.get_trechos();


                                                fields_serie_id_treino serie = new fields_serie_id_treino(dbhandler.getTreino(nome_treino).id_treino, list.get(position).id_serie,
                                                        (float) repeticoes.getValue() -1 ,
                                                        Float.parseFloat(distancia.getText().toString()),
                                                        Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                                        Float.parseFloat(d_entre_series.getText().toString()));


                                                dbhandler.updateSerie(serie, trechos_ida, null);

                                                holder.dialogx.dismiss();
                                                holder.dialogy.dismiss();
                                            } else {

                                                final Toast toast = Toast.makeText(context, "SOMA DOS TRECHOS INDEVIDA", Toast.LENGTH_SHORT);
                                                toast.show();

                                            }
                                        }


                                    }
                                });

                                ida_volta.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (ida_volta.isChecked()) {              /*******IDA != VOLTA******/
                                            add_volta.setVisibility(View.VISIBLE);
                                        } else {   //IDA == VOLTA

                                            add_volta.setVisibility(View.INVISIBLE);
                                            trechos_volta.clear();

                                            list_volta_adapter.notifyDataSetChanged();

                                        }
                                    }
                                });

                                add_ida.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        list_ida_adapter.add();


                                    }
                                });

                                add_volta.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        list_volta_adapter.add();


                                    }
                                });


                                holder.dialogx.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                        holder.dialogy.dismiss();
                                        holder.dialogx.dismiss();


                                    }

                                });


                            }
                        }
                    });


                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (distancia.getText().toString().matches("") || d_entre_repeticoes.getText().toString().matches("") || d_entre_series.getText().toString().matches("") || tempo.getText().toString().matches("")) {

                                final Toast toast = Toast.makeText(context, "DADOS INCOMPLETOS", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {

                                int id_serie = dbhandler.Search_serie(list.get(position).repeticoes, list.get(position).distancia, list.get(position).d_entre_repeticoes, list.get(position).d_entre_series, trechos_ida, trechos_volta);
                                fields_serie_id_treino serie;

                                if(repeticoes.getValue() > 1)
                                    serie = new fields_serie_id_treino( dbhandler.getTreino(nome_treino).id_treino ,id_serie,
                                            (float) repeticoes.getValue()-1, Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()));
                                else
                                    serie = new fields_serie_id_treino( dbhandler.getTreino(nome_treino).id_treino ,id_serie,
                                            (float) repeticoes.getValue(), Float.parseFloat(distancia.getText().toString()),
                                            Float.parseFloat(d_entre_repeticoes.getText().toString()),
                                            Float.parseFloat(d_entre_series.getText().toString()));


                                fields_trecho trecho = new fields_trecho(-1, Float.parseFloat(tempo.getText().toString()), Float.parseFloat(distancia.getText().toString()));
                                List<fields_trecho> trechos = new ArrayList<>();
                                trechos.add(trecho);

                                //ADCIONA A SERIE COM APENAS UM TRECHO IGUAL PRA IDA E VOLTA COM A DISTANCIA IGUAL
                                dbhandler.updateSerie(serie, trechos, null);

                                //LIMPA LISTA
                                List<fields_serie> lista_series;


                                //PEGA AS NOVAS SERIES DO TREINO
                                list = dbhandler.getSeries(dbhandler.getTreino(nome_treino).id_treino);

                                //INFLA O ADAPTADOR
                                notifyDataSetChanged();


                                holder.dialogy.dismiss();
                            }
                        }
                    });
            }

        });
        return view;
    }

}
