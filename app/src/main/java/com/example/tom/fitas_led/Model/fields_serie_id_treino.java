package com.example.tom.fitas_led.Model;

/**
 * Created by Tom on 30/08/2017.
 */

public class fields_serie_id_treino {

    public int id_treino, id_serie;
    public float repeticoes, distancia, d_entre_repeticoes, d_entre_series;

    public fields_serie_id_treino(int id_treino, int id_serie, float repeticoes, float distancia, float d_entre_repeticoes, float d_entre_series){

            this.id_treino = id_treino;
            this.id_serie = id_serie;
            this.repeticoes = repeticoes;
            this.distancia = distancia;
            this.d_entre_repeticoes = d_entre_repeticoes;
            this.d_entre_series = d_entre_series;



        }



}
