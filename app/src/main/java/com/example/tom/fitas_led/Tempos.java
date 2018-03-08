package com.example.tom.fitas_led;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tom.fitas_led.Model.Tempos_State;

/**
 * Created by Tom on 09/07/2017.
 */

public class Tempos implements Runnable {

    private View v;
    private int tempo_atual;
    private long tempo_total;
    private int indice;

    private Object mPauseLock;
    public boolean mPaused;


    public void onPause() {
        synchronized (mPauseLock) {
            mPaused = true;
            mPauseLock.notifyAll();
        }
    }

    public void onResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }

    public void setTempos(View v, int tempo_atual, int tempo_total){

        this.v = v;
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        progressBar.setProgress(tempo_atual);
        progressBar.setMax(tempo_total);


    }





    public Tempos(View v, int tempo_atual, int tempo_total, boolean mPaused, int indice) {
        this.v = v;
        this.tempo_atual = tempo_atual;
        this.tempo_total = tempo_total;
        this.indice = indice;

        mPauseLock = new Object();
        this.mPaused = mPaused;

    }

    @Override
    public void run() {

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        progressBar.setMax((int) tempo_total);
        progressBar.setProgress(tempo_atual);


        if(tempo_atual == 0 || tempo_atual > tempo_total){
            this.onPause();
        }

            while (tempo_atual < tempo_total) {


                synchronized (mPauseLock) {
                    while (mPaused) {
                        try {
                            mPauseLock.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }


                        try {
                            Thread.sleep(1000);
                            tempo_atual ++;
                        } catch (InterruptedException e) {
                            return;
                        } catch (Exception e) {
                            return;
                        }
                        progressBar.setProgress(tempo_atual);


            }

            mPaused = true;
            ((Tempos_State) v.getContext().getApplicationContext()).setState(true, indice);


            tempo_atual = 0;
            tempo_total = 1500000000;

            progressBar.setProgress(tempo_atual);


            final Intent loading_activity = new Intent(v.getContext(), Loading_Raias.class);
            Bundle pacote = new Bundle();
            pacote.putString("origem", "1");
            loading_activity.putExtra("origem", pacote);
            ((Activity) v.getContext()).finish();
            v.getContext().startActivity(loading_activity);


    }
}
