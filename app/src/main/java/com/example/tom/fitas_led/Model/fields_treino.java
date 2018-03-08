package com.example.tom.fitas_led.Model;

/** CREATED BY TOMÁS QUEIROZ 24/04
 *  DESCRIPTION: MODEL DE SERIES PARA O DB UTILIZADA PRA INSERIR NA TABELA. NAO HA ID PORQUE
 *  SERA ADICIONADO AUTOMATICAMENTE PELO BANCO (PRIMARY KEY)
 */

public class fields_treino {

    public String name;
    public int id_treino;

    /***************TODOS OS TEMPOS ESTAO EM SEGUNDOS COMO UNIDADE*****************/


    public fields_treino(int id_treino, String name){
        this.name = name;
        this.id_treino = id_treino;
    }



}
