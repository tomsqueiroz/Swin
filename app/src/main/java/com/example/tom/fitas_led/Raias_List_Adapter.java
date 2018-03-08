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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tom.fitas_led.Model.fields_treino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 06/05/2017.
 */

public class Raias_List_Adapter  extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();

    public Map<String, List<fields_treino>> map_raias = new HashMap<>();


    private Context context;
    private String TAG = "RAIAS_LIST_ADAPTER";

    private DBHandler db;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);



    public Raias_List_Adapter(ArrayList<String> list, Context context) {
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
        db = new DBHandler(context);


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.raias_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.raias_item_string);
        listItemText.setText("Raia " + Integer.toString(Integer.parseInt(list.get(position)) + 1));

        //Handle buttons and show onClickListeners
        ImageButton trainbtn = (ImageButton) view.findViewById(R.id.train_btn);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_treino_raia);
        final Treino_Raia_ListAdapter adapter = new Treino_Raia_ListAdapter(db.getAllTrains(), dialog.getContext());


        trainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                v.startAnimation(buttonClick);



                ListView lView = (ListView) dialog.findViewById(R.id.treinos_raia_list);
                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);

                lView.setAdapter(adapter);


                adapter.set_marcados(adapter.treinos_a_enviar);

                dialog.show();

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d(TAG, "OnOKClick");

                        dialog.dismiss();

                        if(adapter.treinos_a_enviar.size() > 0) {

                            for (int i = 0; i < adapter.treinos_a_enviar.size(); i++) {
                                Log.d(TAG, "TREINOS A ENVIAR ==" + adapter.treinos_a_enviar.get(i).name);
                            }

                            map_raias.put(list.get(position), adapter.treinos_a_enviar);




                        }

                        else{
                            Log.d(TAG, "LISTA VAZIA");
                        }


                    }

                });
            }
        });

        return view;
    }

}