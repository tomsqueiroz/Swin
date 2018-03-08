package com.example.tom.fitas_led.Model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 07/08/2017.
 */

public class Tempos_State extends Application {

    public List<Boolean> tempo_state = new ArrayList<>();


    public boolean getState(int i){


        return tempo_state.get(i);

    }

    public void setState(boolean state, int i){


        tempo_state.add(i, state);
    }




}
