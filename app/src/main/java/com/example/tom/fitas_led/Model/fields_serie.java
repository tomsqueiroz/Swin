package com.example.tom.fitas_led.Model;

/** CREATED BY TOMÁS QUEIROZ 24/04
 *  DESCRIPTION: MODEL DE SERIES PARA O DB UTILIZADA PRA INSERIR NA TABELA. NAO HA ID PORQUE
 *  SERA ADICIONADO AUTOMATICAMENTE PELO BANCO (PRIMARY KEY)
 */




public class fields_serie {

    public int id_serie;
    public float repeticoes, distancia, d_entre_repeticoes, d_entre_series;

    public fields_serie(int id_serie, float repeticoes, float distancia, float d_entre_repeticoes, float d_entre_series){

        this.id_serie = id_serie;
        this.repeticoes = repeticoes;
        this.distancia = distancia;
        this.d_entre_repeticoes = d_entre_repeticoes;
        this.d_entre_series = d_entre_series;



    }
}
