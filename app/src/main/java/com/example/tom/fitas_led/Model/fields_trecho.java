package com.example.tom.fitas_led.Model;

/** CREATED BY TOMÁS QUEIROZ 24/04
 *  DESCRIPTION: MODEL DE TRECHO PARA O DB UTILIZADA PRA INSERIR NA TABELA. NAO HA ID PORQUE
 *  SERA ADICIONADO AUTOMATICAMENTE PELO BANCO (PRIMARY KEY)
 */

public class fields_trecho {

    public float tempo, tamanho;
    public int id_trecho;

    public fields_trecho(int id_trecho, float tempo, float tamanho){
        this.tempo = tempo;
        this.tamanho = tamanho;
        this.id_trecho = id_trecho;
    }
}
