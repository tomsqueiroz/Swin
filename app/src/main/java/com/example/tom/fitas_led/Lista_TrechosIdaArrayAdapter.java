package com.example.tom.fitas_led;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.fields_serie;
import com.example.tom.fitas_led.Model.fields_trecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Tom on 06/09/2017.
 */

public class Lista_TrechosIdaArrayAdapter extends BaseAdapter implements ListAdapter {


    private List<fields_trecho> list = new ArrayList<>();
    private List<Float> tamanhos = new ArrayList<>();
    private List<Float> tempos = new ArrayList<>();
    private Context context;
    private View view;
    private float tamanho_piscina;

    private int num_tamanhos = 0;

    private String[] numbers;

    private ViewHolder holder;

    public int InvisibleMode = 0;

    public void add_list(List<fields_trecho> trechos){

        for(int i = 0; i < trechos.size(); ++i){

            list.add(trechos.get(i));
            tamanhos.add(trechos.get(i).tamanho);
            tempos.add(trechos.get(i).tempo);

            holder.tempo_trecho.setText(String.valueOf(trechos.get(i).tempo));

            notifyDataSetChanged();
        }



    }

    public boolean is_data_ok(){

        float tamanho_total = 0;

        for(int i = 0; i < list.size(); ++i){

            if(list.get(i).tempo == 0){
                return false;
            }


        }
        for (int i = 0; i < list.size(); ++i){

            tamanho_total += list.get(i).tamanho;

        }

        if(tamanho_total != tamanho_piscina)
            return false;


        return true;
    }
    public void add(){

        list.add(new fields_trecho(-1,0,Float.parseFloat(numbers[0])));

        this.create_numbers();

        notifyDataSetChanged();


    }

    public List<fields_trecho> get_trechos (){

        return list;



    }

    public void create_numbers(){

        int modo = 0;
        float tam_max = 0;

        if(list != null){
            for(int i = 0 ; i < list.size(); ++i){
                if(list.get(i).tamanho > tamanho_piscina) {
                    modo = 1;

                    if(list.get(i).tamanho > tam_max)
                        tam_max = list.get(i).tamanho;


                }
            }
        }

        if(modo == 0){

            if(tamanho_piscina%2 != 0){

                int x = 0;
                numbers = new String[(int) ((tamanho_piscina/5)+1) ];
                num_tamanhos = (int) ((tamanho_piscina/5)+1);

                for(int i = 0; i < ((int) ((tamanho_piscina/5))); ++i){

                    if(tamanho_piscina/2 > (i)*5 && tamanho_piscina/2 < (i+1)*5){
                        numbers[i] = Float.toString(tamanho_piscina/2);
                        numbers[i+1] = Float.toString((i+1)*5);
                        x =1;
                    }
                    else{
                        if(x==0)
                            numbers[i] = Float.toString((i+1)*5) ;
                        if(x==1)
                            numbers[i+1] = Float.toString((i+1)*5) ;
                    }
                }
            }

            else{

                numbers = new String[(int) ((tamanho_piscina/5)) ];
                num_tamanhos = ((int) (tamanho_piscina/5));

                for(int i = 0; i < ((int) (tamanho_piscina/5)); ++i){
                    numbers[i] = Float.toString((i+1)*5);

                }


            }
        }

        if(modo == 1){

            numbers = new String[11];

            int x = 0;

            for(int i = 0; i < 10; ++i){

                if(i == 2){
                    numbers[i] = Float.toString((float) 12.5);
                    numbers[i+1] = Float.toString((i+1)*5);
                    x = 1;
                }
                else{
                    if(x==0)
                        numbers[i] = Float.toString((i+1)*5);
                    if(x==1)
                        numbers[i+1] = Float.toString((i+1)*5);
                }
            }

            num_tamanhos = 11;
        }
    }



    public Lista_TrechosIdaArrayAdapter(List<fields_trecho> list, Context context, float tamanho_piscina) {
        this.context = context;
        this.tamanho_piscina = tamanho_piscina;
        this.list = list;
        this.create_numbers();

    }


    public class ViewHolder{

        public ImageButton delete;
        NumberPicker np_tamanho;
        EditText tempo_trecho;

        public ViewHolder(View view){

            delete = (ImageButton) view.findViewById(R.id.delete_button);
            np_tamanho = (NumberPicker) view.findViewById(R.id.number_tamanho);
            tempo_trecho = (EditText) view.findViewById(R.id.tempo_trecho);

            if(InvisibleMode == 1) {
                delete.setVisibility(View.INVISIBLE);
                np_tamanho.setEnabled(false);
                tempo_trecho.setEnabled(false);
            }





        }
    }

    public void turn_button_invisible(){

        InvisibleMode = 1;

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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        Log.d("GETVIEW", "CHAMOU");


        if (convertView == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.novotrecho_list_item, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);

        }
        else{

            view = convertView;
            holder = (ViewHolder) view.getTag();

        }


        if(position < tamanhos.size() && tamanhos.size() > 0)
            tamanhos.remove(position);

        tamanhos.add(position, Float.parseFloat(numbers[0]));

        holder.np_tamanho.setClickable(true);
        holder.np_tamanho.setMinValue(0);
        holder.np_tamanho.setMaxValue(num_tamanhos -1);
        holder.np_tamanho.setDisplayedValues(numbers);




        if(list.size() != 0){

            for(int i = 0; i < list.size(); ++i){
                tempos.add(i, list.get(i).tempo);
            }


            if(tamanho_piscina%2 == 0)
                holder.np_tamanho.setValue((Math.round(list.get(position).tamanho) / 5) - 1);
            else
                holder.np_tamanho.setValue((Math.round(list.get(position).tamanho) / 5));

            holder.tempo_trecho.setText(String.valueOf(list.get(position).tempo));


        }




        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if(position < list.size())
                        list.remove(position);

                    if(position < tempos.size())
                        tempos.remove(position);

                    if(position < tamanhos.size())
                        tamanhos.remove(position);



                    notifyDataSetChanged();

                } catch (IndexOutOfBoundsException e) {

                    Log.d("TRECHOVOLTADAPTER", "ERRO");


                }

            }
        });

        holder.np_tamanho.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                if(position < tamanhos.size() && tamanhos.size() > 0)
                    tamanhos.remove(position);

                tamanhos.add(position,   Float.parseFloat(numbers[newVal]));

                fields_trecho trecho_novo = new fields_trecho(-1, list.get(position).tempo, Float.parseFloat(numbers[newVal]));


                list.remove(position);
                list.add(position, trecho_novo);

            }
        });

        holder.tempo_trecho.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().matches("")){

                    if(position < tempos.size() && tempos.size() > 0){
                        tempos.remove(position);
                    }

                    try{

                        tempos.add(position, Float.parseFloat(s.toString()));

                        fields_trecho trecho_novo = new fields_trecho(-1, Float.parseFloat(s.toString()) , list.get(position).tamanho);

                        list.remove(position);
                        list.add(position, trecho_novo);

                    }catch (IndexOutOfBoundsException e){

                            Log.d("TRECHOIDADAPTER", "ERRO");


                    }
                }
            }

        });




        notifyDataSetChanged();


        return view;

    }

}


