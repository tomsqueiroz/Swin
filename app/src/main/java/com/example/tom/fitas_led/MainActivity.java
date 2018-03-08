package com.example.tom.fitas_led;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tom.fitas_led.Model.Tempos_State;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;


/** CREATED BY TOMÁS QUEIROZ 24/04
 *  DESCRIPTION: MAIN ACTIVITY -> MOSTRA AO USUARIO BOTOES DE COMECAR TREINO, CRIAR NOVO TREINO e EDITAR TREINO
 *  OBSERVATION: IMPLEMENTAR EDIT DO BANCO DE DADOS
 */


public class MainActivity extends AppCompatActivity implements SendMessageAsync.AsyncResponse{
    private final static String TAG = "MAIN_ACTIVITY";

    private Button start;
    private ImageButton config_button;
    private TextView novo_treino, editar_treino;
    private DBHandler dbhandler;

    private int MODO = 1;


    private final android.os.Handler mHandler = new android.os.Handler(Looper.myLooper());
    final Context context = this;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);


    @Override
    public void onBackPressed()
    {


        super.finishAffinity();

    }





    @Override
    public void onResume(){
        super.onResume();

        Log.d(TAG, "onResume");

    }



    @Override
    public void onStop() {
        super.onStop();


        Log.d(TAG, "onStop");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 8; ++i) {
            ((Tempos_State) this.getApplicationContext()).setState(true, i);
        }

        Log.d(TAG, "OnCreate");

        start = (Button) findViewById(R.id.start_button);
        novo_treino = (TextView) findViewById(R.id.novo_treino);
        config_button = (ImageButton) findViewById(R.id.config_button);
        editar_treino = (TextView) findViewById(R.id.editar_treino);


        Intent i = getIntent();


        dbhandler = new DBHandler(this);






        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                    .enableDumpapp(
                            Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(
                            Stetho.defaultInspectorModulesProvider(this))
                    .build());






        final Intent treino = new Intent(getApplicationContext(), Treino.class);
        final Intent edit_treino = new Intent(getApplicationContext(), EditTreino.class);
        final Intent loading_activity = new Intent(getApplicationContext(), Loading_Raias.class);
        final Intent acompanhamento_activity = new Intent(getApplicationContext(), Acompanhamento_Activity.class);
        Button acompanhamento = (Button) findViewById(R.id.acompanhamento);



        acompanhamento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                    Bundle pacote = new Bundle();
                    pacote.putString("origem", "1");

                    loading_activity.putExtra("origem", pacote);


                    startActivity(loading_activity);





            }
        });








        if(dbhandler.getTamanho() == null){
            final Dialog init_config = new Dialog(context);
            init_config.setContentView(R.layout.dialog_init_guide);
            final EditText tamanho = (EditText) init_config.findViewById(R.id.tamanho);
            ImageButton save_btn = (ImageButton) init_config.findViewById(R.id.save_btn);
            init_config.show();


            save_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                        if(tamanho.getText().toString().matches("")){

                            final Toast toast = Toast.makeText(context , "DADOS INCOMPLETOS", Toast.LENGTH_SHORT);
                            toast.show();


                        }

                        else{
                            /******** FALTA ADICIONAR PARTE EM QUE PEDE NOME DA PISCINA *********/


                            dbhandler.addTamanho(Integer.parseInt(tamanho.getText().toString()), "nome");


                            init_config.dismiss();

                        }
                }
            });
        }



        //BOTAO LEVA A ACTIVITY DE LER TREINO DO BANCO E PREPARAR PARA ENVIO
        start.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                Log.d(TAG, "Clickou no Start");
                if(dbhandler.getTrainCount() == 0){
                    final Toast toast = Toast.makeText(context , "ADICIONE UM TREINO", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {


                    Bundle pacote = new Bundle();
                    pacote.putString("origem", "0");

                    loading_activity.putExtra("origem", pacote);

                    startActivity(loading_activity);
                }
            }

        });


        //BOTAO LEVA A ACTIVITY RESPONSAVEL POR CRIAR NOVOS TREINOS
        novo_treino.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*
                Dialog d = new Dialog(context);
                Rect displayRectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

// inflate and adjust layout
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.time_picker_own, null);
                layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
                layout.setMinimumHeight((int)(displayRectangle.height() * 0.5f));


                d.setContentView(layout);
                d.show();
*/

                v.startAnimation(buttonClick);
                Log.d(TAG, "Clickou em Novo treino");
                startActivity(treino);

            }


        });

        //BOTAO LEVA A ACTIVITY RESPONSAVEL POR EDITAR TREINOS JA EXISTENTES
        editar_treino.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Log.d(TAG, "Clickou em Editar treino");
                if(dbhandler.getTrainCount() == 0){
                    final Toast toast = Toast.makeText(context , "ADICIONE UM TREINO", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    startActivity(edit_treino);
                }
            }


        });



        //BOTAO MOSTRA DIALOG PARA CONFIGURACAO DO TAMANHO DA PISCINA
        config_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Log.d(TAG, "Clickou em Config");

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_config);



                final Toast toast = Toast.makeText(context , "Dados incompletos", Toast.LENGTH_SHORT);
                final EditText tamanho_piscina = (EditText) dialog.findViewById(R.id.tamanho_piscina);
                final TextView editar_tamanho = (TextView) dialog.findViewById(R.id.editar_tamanho);
                ImageButton save_tamanho = (ImageButton) dialog.findViewById(R.id.save_tamanho);
                ImageButton exit = (ImageButton)    dialog.findViewById(R.id.exit);



                if (dbhandler.getTamanhoCount() > 0){
                    Log.d(TAG, "TAmanho == " + dbhandler.getTamanho().get("nome") );
                    editar_tamanho.setText("Tamanho atual -> " + dbhandler.getTamanho().get("nome") + " m");


                }
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }


                });




                //BOTAO FECHA DIALOG BOX E SALVA CONFIGURACAO
                save_tamanho.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tamanho_piscina.getText().toString().matches("")){
                            Log.d(TAG, "Opcoes incompletas");
                            toast.show();

                        }
                        else{

                            /******** FALTA ADICIONAR PARTE EM QUE PEDE NOME DA PISCINA *********/


                            dbhandler.addTamanho(Integer.parseInt(tamanho_piscina.getText().toString()), "nome");

                            //send(tamanho_piscina.getText().toString());

                            Toast toast2 = Toast.makeText(context , "Tamanho da piscina " + tamanho_piscina.getText().toString() + " m", Toast.LENGTH_SHORT);
                            toast2.show();


                            editar_tamanho.setText("Tamanho atual -> " + tamanho_piscina.getText().toString() + " m");
                            tamanho_piscina.setText("");





                            //dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }


        });
    }

    public void send(String string){

        new SendMessageAsync(mHandler, string, this);

    }




    @Override
    public void processFinish(String output) {
        Log.d(TAG, "Enviou");
    }
}
