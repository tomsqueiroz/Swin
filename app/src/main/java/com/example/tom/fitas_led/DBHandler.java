package com.example.tom.fitas_led;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tom.fitas_led.Model.fields_serie;
import com.example.tom.fitas_led.Model.fields_serie_id_treino;
import com.example.tom.fitas_led.Model.fields_trecho;
import com.example.tom.fitas_led.Model.fields_treino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** CREATED BY TOMÁS QUEIROZ 28/08  -- VERSAO 2
 *  DESCRIPTION: CLASSE RESPONSAVEL POR TRATAR TODA INTERACAO COM O BANCO (SQLITE)
 *  OBSERVATION: IDA ESTÁ IGUAL A VOLTA (ATUALIZAR EM PRÓXIMAS EDIÇÕES) / OnUpgrade NAO IMPLEMENTADA
 *               DECIDIR SOBRE QUANTIDADE DE TAMANHOS DE PISCINA PASSIVEIS DE ARMAZENAMENTO NO BANCO (ATUAL = 1)
 *               IMPLEMENTAR OPCAO DE DELETAR TREINO/SERIE...
 */



public class DBHandler extends SQLiteOpenHelper {

    private static final boolean SUCESSO = true;
    private static final String TAG = "DBHANDLER";

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "SWIN";

    // TREINOS table name
    private static final String TABLE_TREINOS = "TREINOS";

    //SERIE table name
    private static final String TABLE_SERIES = "SERIES";

    //PISCINA table name
    private static final String TABLE_PISCINAS= "PISCINAS";

    //TRECHOS table name
    private static final String TABLE_TRECHOS = "TRECHOS";


    //TABELAS AUXILIARES
    private static final String TABLE_TREINO_SERIE = "TREINO_SERIE";
    private static final String TABLE_SERIE_IDA = "SERIE_IDA";
    private static final String TABLE_SERIE_VOLTA = "SERIE_VOLTA";



    // TREINOS Table Columns names
    private static final String KEY_ID_TREINO = "ID_TREINO";
    private static final String KEY_NOME_TREINO = "NOME_TREINO";


    //SERIE Table Columns names
    private static final String KEY_ID_SERIE = "ID_SERIE";
    private static final String KEY_REPETICOES= "REPETICOES";
    private static final String KEY_DISTANCIA = "DISTANCIA";
    private static final String KEY_D_ENTRE_REPETICOES = "D_ENTRE_REPETICOES";
    private static final String KEY_D_ENTRE_SERIES = "D_ENTRE_SERIES";


    //TRECHOS Table Columns names
    private static final String KEY_ID_TRECHO = "ID_TRECHO";
    private static final String KEY_TAMANHO_TRECHO = "TAMANHO_TRECHO";
    private static final String KEY_TEMPO_TRECHO = "TEMPO_TRECHO";


    //PISCINAS Table Columns
    private static final String KEY_TAMANHO_PISCINA = "TAMANH0_PISCINA";
    private static final String KEY_NOME_PISCINA = "NOME_PISCINA";
    private static final String KEY_ID_PISCINA = "ID_PISCINA";


    /**********************CLASSE CONSTRUTORA**************************/
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    /**********************CLASSE CHAMADA AUTOMATICAMENTE CASO NAO HAJA UM BANCO UP**************************/
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "OnCreate");

        String TREINOS = "CREATE TABLE " + TABLE_TREINOS + " (" + KEY_ID_TREINO + " INTEGER PRIMARY KEY AUTOINCREMENT, "  + KEY_NOME_TREINO  + " VARCHAR(50));";

        String SERIES = "CREATE TABLE " + TABLE_SERIES + " ("
                + KEY_ID_SERIE + " INTEGER NOT NULL PRIMARY KEY, "
                + KEY_REPETICOES + " REAL, "
                + KEY_DISTANCIA + " REAL, "
                + KEY_D_ENTRE_REPETICOES + " REAL, "
                + KEY_D_ENTRE_SERIES + " REAL); ";

        String TREINO_SERIE = "CREATE TABLE " + TABLE_TREINO_SERIE + " ("
                + KEY_ID_TREINO + " INTEGER, "
                + KEY_ID_SERIE + " INTEGER, "
                + " PRIMARY KEY ("
                + KEY_ID_TREINO + "," + KEY_ID_SERIE + ") ,"
                + "FOREIGN KEY (" + KEY_ID_TREINO + ")" + " REFERENCES " +  TABLE_TREINOS + " (" + KEY_ID_TREINO + "), "
                + "FOREIGN KEY (" + KEY_ID_SERIE + ") REFERENCES " +  TABLE_SERIES + " (" + KEY_ID_SERIE +  "));";


        String TRECHOS = "CREATE TABLE " + TABLE_TRECHOS  + " (" + KEY_ID_TRECHO + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_TAMANHO_TRECHO + " REAL," + KEY_TEMPO_TRECHO + " REAL );";

        String SERIE_IDA = "CREATE TABLE " + TABLE_SERIE_IDA + " ("
                + KEY_ID_SERIE + " INTEGER, "
                + KEY_ID_TRECHO + " INTEGER, "
                + " PRIMARY KEY ("
                + KEY_ID_SERIE + "," + KEY_ID_TRECHO + ") ,"
                + "FOREIGN KEY (" + KEY_ID_SERIE + ")" + " REFERENCES " +  TABLE_SERIES + " (" + KEY_ID_SERIE + "), "
                + "FOREIGN KEY (" + KEY_ID_TRECHO + ") REFERENCES " +  TABLE_TRECHOS + " (" + KEY_ID_TRECHO +  "));";

        String SERIE_VOLTA = "CREATE TABLE " + TABLE_SERIE_VOLTA + " ("
                + KEY_ID_SERIE + " INTEGER, "
                + KEY_ID_TRECHO + " INTEGER, "
                + " PRIMARY KEY ("
                + KEY_ID_SERIE + "," + KEY_ID_TRECHO + ") ,"
                + "FOREIGN KEY (" + KEY_ID_SERIE + ")" + " REFERENCES " +  TABLE_SERIES + " (" + KEY_ID_SERIE + "), "
                + "FOREIGN KEY (" + KEY_ID_TRECHO + ") REFERENCES " +  TABLE_TRECHOS + " (" + KEY_ID_TRECHO +  "));";


        String PISCINAS = "CREATE TABLE " + TABLE_PISCINAS + " (" + KEY_ID_PISCINA + " INTEGER PRIMARY KEY, " +  KEY_TAMANHO_PISCINA + " REAL, " + KEY_NOME_PISCINA + " VARCHAR(50));";





        //METODOS RESPONSAVEIS POR EXECUTAR COMANDOS SQL CONTIDOS NAS STRINGS ACIMA
        db.execSQL(TREINOS);
        db.execSQL(SERIES);
        db.execSQL(TRECHOS);
        db.execSQL(PISCINAS);
        db.execSQL(TREINO_SERIE);
        db.execSQL(SERIE_IDA);
        db.execSQL(SERIE_VOLTA);




        Log.d(TAG, "Created TABLES");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //A IMPLEMENTAR

    }

    /*********************************TRATAMENTO DA TABLE TREINOS*****************************/


    // ADDING NEW TRAIN
    public boolean addTrain(String name) {


        Log.d(TAG, "AddTrain");


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(KEY_NOME_TREINO, name); // NOME DO TREINO


        // Inserting Row
        db.insert(TABLE_TREINOS, null, values);
        db.close(); // Closing database connection
        return SUCESSO;

    }


    // RETORNA NULL CASO NAO HAJA TREINOS NO BANCO
    public fields_treino getTreino(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TREINOS + " WHERE " + KEY_NOME_TREINO + " =?", new String[] {name});
        fields_treino treino = null;


        if (cursor != null) {
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                treino = new fields_treino(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
            }
            cursor.close();
            return treino;
        }
        else{
            cursor.close();
            return null;

        }
    }


    //Getting all Train
    public List<fields_treino> getAllTrains(){
        List<fields_treino> lista_treinos = new ArrayList<fields_treino>();

        String selectquery = "SELECT * FROM " + TABLE_TREINOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);

        if(cursor.moveToFirst()){
            do{
                fields_treino treino = new fields_treino(Integer.parseInt(cursor.getString(0)), cursor.getString(1));


                lista_treinos.add(treino);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista_treinos;
    }


    //Getting train count
    public int getTrainCount(){
        int x;
        String countQuery = "SELECT * FROM " + TABLE_TREINOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        x = cursor.getCount();
        cursor.close();

        return x;

    }


    //Deleting a train
    public void deleteTrain(fields_treino treino){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TREINOS, KEY_NOME_TREINO + " = ?", new String[]{ treino.name});
        db.close();

    }






    /********************************************TRATAMENTO DA TABLE SERIES******************************/



    //ADDING NEW SERIE
    public void addSerie(fields_serie_id_treino serie, List<fields_trecho> ida, List<fields_trecho> volta){

        /********** CASO IDA == VOLTA, PASSAR LISTA VOLTA == NULL************/


        Log.d(TAG, "AddSerie");

        int id_serie = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();


            if(Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida, volta)!=-1 && !Search_treino_serie(serie.id_treino, Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida, volta))){
                //CONDICAO CASO HAJA SERIE COM OS MESMOS PARAMETROS, APENAS POPULAR MAIS UMA LINHA NA TABELA TREINO_SERIE

                id_serie = Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida, volta);
                values.clear();
                values.put(KEY_ID_TREINO, serie.id_treino);
                values.put(KEY_ID_SERIE, id_serie);
                db.insert(TABLE_TREINO_SERIE, null, values);

            }
            else if (Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida , volta)==-1){
                //CONDICAO CASO NÃO HAJA SERIE COM OS MESMOS PARAMETROS
                values.clear();
                values.put(KEY_REPETICOES, serie.repeticoes);
                values.put(KEY_DISTANCIA, serie.distancia);
                values.put(KEY_D_ENTRE_REPETICOES, serie.d_entre_repeticoes);
                values.put(KEY_D_ENTRE_SERIES, serie.d_entre_series);
                db.insert(TABLE_SERIES, null, values);

                id_serie = getHighestSeries();
                values1.clear();
                values1.put(KEY_ID_TREINO, serie.id_treino);
                values1.put(KEY_ID_SERIE, id_serie);
                db.insert(TABLE_TREINO_SERIE, null , values1);

                }


            if(ida!= null && volta == null) {

                int id_trecho = -1;

                if(id_serie == -1)
                    id_serie = Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida, volta);


                for(int i = 0; i < ida.size(); ++i) {

                    if(Search_Trecho(ida.get(i).tamanho, ida.get(i).tempo)!=-1) {

                        id_trecho = Search_Trecho(ida.get(i).tamanho, ida.get(i).tempo);

                        if(!Search_Serie_Ida(id_serie, id_trecho) && id_trecho != -1) {



                            values.clear();
                            values.put(KEY_ID_SERIE, id_serie);
                            values.put(KEY_ID_TRECHO, id_trecho);
                            db.insert(TABLE_SERIE_IDA, null, values);
                        }
                    }
                    else {
                        //CONDICAO CASO NÃO HAJA TRECHO COM OS MESMOS PARAMETROS
                        values.clear();
                        values.put(KEY_TAMANHO_TRECHO, ida.get(i).tamanho);
                        values.put(KEY_TEMPO_TRECHO, ida.get(i).tempo);
                        db.insert(TABLE_TRECHOS, null, values);
                        id_trecho = getHighestTrecho();

                        values1.clear();
                        values1.put(KEY_ID_SERIE, id_serie);
                        values1.put(KEY_ID_TRECHO, id_trecho);
                        db.insert(TABLE_SERIE_IDA, null , values1);

                    }
                }
            }

            if(ida !=null && volta!=null){

                int id_trecho = -1;


                if(id_serie == -1)
                    id_serie = Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida, volta);

                for(int i = 0; i < ida.size(); ++i) {

                    if(Search_Trecho(ida.get(i).tamanho, ida.get(i).tempo)!=-1 && !Search_Serie_Ida(id_serie, id_trecho)) {
                        //CASO HAJA TRECHOS COM MESMOS PARAMETROS NA TABELA TRECHOS, MAS NAO NA TABELA SERIE_TRECHO


                        id_trecho = Search_Trecho(ida.get(i).tamanho, ida.get(i).tempo);
                        values.clear();
                        if(id_serie != -1)
                            values.put(KEY_ID_SERIE, id_serie);

                        values.put(KEY_ID_TRECHO, id_trecho);
                        db.insert(TABLE_SERIE_IDA, null, values);

                    }
                    else if(Search_Trecho(ida.get(i).tamanho, ida.get(i).tempo)==-1){
                        //CONDICAO CASO NÃO HAJA TRECHO COM OS MESMOS PARAMETROS


                        values.clear();
                        values.put(KEY_TAMANHO_TRECHO, ida.get(i).tamanho);
                        values.put(KEY_TEMPO_TRECHO, ida.get(i).tempo);
                        db.insert(TABLE_TRECHOS, null, values);
                        id_trecho = getHighestTrecho();

                        values1.clear();
                        if(id_serie != -1)
                            values1.put(KEY_ID_SERIE, id_serie);

                        values1.put(KEY_ID_TRECHO, id_trecho);
                        db.insert(TABLE_SERIE_IDA, null , values1);


                    }
                }
                for(int i = 0; i < volta.size(); ++i){
                    if(Search_Trecho(volta.get(i).tamanho, volta.get(i).tempo)!=-1 && !Search_Serie_Volta(id_serie, id_trecho)) {

                        id_trecho = Search_Trecho(volta.get(i).tamanho, volta.get(i).tempo);
                        values.clear();

                        if(id_serie != -1)
                            values.put(KEY_ID_SERIE, id_serie);

                        values.put(KEY_ID_TRECHO, id_trecho);
                        db.insert(TABLE_SERIE_VOLTA, null, values);

                    }
                    else if(Search_Trecho(volta.get(i).tamanho, volta.get(i).tempo)==-1){
                        //CONDICAO CASO NÃO HAJA TRECHO COM OS MESMOS PARAMETROS
                        values.clear();
                        values.put(KEY_TAMANHO_TRECHO, volta.get(i).tamanho);
                        values.put(KEY_TEMPO_TRECHO, volta.get(i).tempo);
                        db.insert(TABLE_TRECHOS, null, values);
                        id_trecho = getHighestTrecho();

                        values1.clear();

                        if(id_serie != -1)
                            values1.put(KEY_ID_SERIE, id_serie);


                        values1.put(KEY_ID_TRECHO, id_trecho);
                        db.insert(TABLE_SERIE_VOLTA, null , values1);

                    }
                }
            }
        }


    public int getSeriesCount(int id_treino){
        int x;
        String countQuery = "SELECT * FROM " + TABLE_SERIES + " WHERE " + KEY_ID_TREINO + "= "+ id_treino + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        x = cursor.getCount();
        cursor.close();

        return x;

    }

    public int getHighestSeries(){
        /****METODO UTILIZADO PARA SABER MAIOR ID SERIE****/

        String selectquery = "SELECT * FROM " + TABLE_SERIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        int x = -1;

        if(cursor.moveToFirst()){
            do{
                if(Integer.parseInt(cursor.getString(0)) > x)
                    x = Integer.parseInt(cursor.getString(0));

            }while(cursor.moveToNext());

        }
        cursor.close();
        return x;
    }

    public boolean Search_treino_serie(int id_treino, int id_serie){

        boolean bool = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TREINO_SERIE + " WHERE " + KEY_ID_SERIE + " =" + Integer.toString(id_treino) + " AND " + KEY_ID_TREINO + " =" + Integer.toString(id_serie), null);


        if (cursor != null) {
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                bool = true;
            }
            cursor.close();
            return bool;
        }
        else{
            return bool;

        }
    }

    public int Search_serie(float repeticoes, float distancia, float d_entre_repeticoes, float d_entre_series, List<fields_trecho> ida, List<fields_trecho> volta){


        int id_serie;
        List<fields_trecho> ida_comparacao;
        List<fields_trecho> volta_comparacao;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + KEY_REPETICOES + "=? AND "
                + KEY_DISTANCIA + "=? AND "
                + KEY_D_ENTRE_REPETICOES + "=? AND "
                + KEY_D_ENTRE_SERIES + "=?",
                new String[] {Float.toString(repeticoes), Float.toString(distancia), Float.toString(d_entre_repeticoes), Float.toString(d_entre_series)});



        if(cursor.moveToFirst()){

           id_serie = Integer.parseInt(cursor.getString(0));

        }
        else {
            return -1;
        }

        ida_comparacao = this.getTrechos(id_serie).get(0);
        volta_comparacao = this.getTrechos(id_serie).get(1);

        if(volta == null && volta_comparacao != null)
            return -1;

        if(volta != null && volta_comparacao == null)
            return -1;


        if(volta == null && volta_comparacao == null){

            if(ida.size() != ida_comparacao.size())
                return -1;

            for(int i = 0; i < ida.size(); ++i){

                if((ida.get(i).tamanho != ida_comparacao.get(i).tamanho) || (ida.get(i).tempo != ida_comparacao.get(i).tempo)){
                    return -1;
                }
            }
        }
        if(volta != null && volta_comparacao != null) {

            if (volta.size() != volta_comparacao.size())
                return -1;

            if (ida.size() != ida_comparacao.size())
                return -1;

            for(int i = 0; i < ida.size(); ++i){

                if((ida.get(i).tamanho != ida_comparacao.get(i).tamanho) || (ida.get(i).tempo != ida_comparacao.get(i).tempo)){
                    return -1;
                }
            }

            for(int i = 0; i < volta.size(); ++i){

                if((volta.get(i).tamanho != volta_comparacao.get(i).tamanho) || (volta.get(i).tempo != volta_comparacao.get(i).tempo)){
                    return -1;
                }
            }
        }



        return id_serie;

    }

    //RETORNA LISTA DE SERIES DE UM TREINO CUJO ID É PASSADO COMO PARAMETRO
    public List<fields_serie> getSeries(int id_treino) {

        List<fields_serie> lista_series = new ArrayList<fields_serie>();
        List<Integer> lista_idseries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TREINO_SERIE + " WHERE " + KEY_ID_TREINO + " ="  + Integer.toString(id_treino), null);

        if(cursor.moveToFirst()){
            do{

                lista_idseries.add(Integer.parseInt(cursor.getString(1)));

            }while(cursor.moveToNext());
        }

        for(int i = 0; i < lista_idseries.size(); ++i) {


            cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + KEY_ID_SERIE + " =" + Integer.toString(lista_idseries.get(i)), null);

            if (cursor.moveToFirst()) {
                do {

                    fields_serie series = new fields_serie(Integer.parseInt(cursor.getString(0)),
                            Float.parseFloat(cursor.getString(1)),
                            Float.parseFloat(cursor.getString(2)),
                            Float.parseFloat(cursor.getString(3)),
                            Float.parseFloat(cursor.getString(4)));


                    lista_series.add(series);
                } while (cursor.moveToNext());

            }

        }

        cursor.close();
        return lista_series;
    }

    /*********METODO TESTARA SE SERIE É REFERENCIADA APENAS UMA VEZ NAS TABELAS AUX, CASO NEGATIVO, SERÁ ADICIONADO UMA NOVA SERIE E SEU ID SERA RETORNADO*********/
    public int updateSerie(fields_serie_id_treino serie, List<fields_trecho> ida, List<fields_trecho> volta){
        Log.d(TAG, "UpdateSerie");

        int count_treino_serie, count_serie_ida, count_serie_volta, id_ida, id_volta;

        id_ida = 0;
        id_volta = 0;


        List<List<fields_trecho>> trechos_atuais = this.getTrechos(serie.id_serie);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TREINO_SERIE + " WHERE " + KEY_ID_SERIE + " =" + Integer.toString(serie.id_serie),  null);
        count_treino_serie = cursor.getCount();

        count_serie_ida = trechos_atuais.get(0).size();
        count_serie_volta = trechos_atuais.get(1).size();


        if(count_treino_serie <= 1 && count_serie_ida <= 1 && count_serie_volta <= 1){

            db.delete(TABLE_TREINO_SERIE, KEY_ID_SERIE + " =" + Integer.toString(serie.id_serie) + " AND " + KEY_ID_TREINO + " =" +  Integer.toString(serie.id_treino), null);
            deleteTrechos(serie.id_serie, trechos_atuais.get(0));
            deleteTrechos(serie.id_serie, trechos_atuais.get(1));
            db.delete(TABLE_SERIES, KEY_ID_SERIE + " = ?", new String[]{ Integer.toString(serie.id_serie)});
            addSerie(serie, ida, volta);
            cursor.close();
            return Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida, volta);

        }
        else{

            db.delete(TABLE_TREINO_SERIE, KEY_ID_SERIE + " =" + Integer.toString(serie.id_serie)+ " AND " + KEY_ID_TREINO + " =" + Integer.toString(serie.id_treino), null);
            deleteTrechos(serie.id_serie, trechos_atuais.get(0));
            deleteTrechos(serie.id_serie, trechos_atuais.get(1));
            addSerie(serie, ida, volta);
            cursor.close();
            return Search_serie(serie.repeticoes, serie.distancia, serie.d_entre_repeticoes, serie.d_entre_series, ida , volta);

        }
    }


    public void deleteSerie(int id_treino, int id_serie){

        int count_treino_serie, count_serie_ida, count_serie_volta, id_ida, id_volta;

        id_ida = 0;
        id_volta = 0;

        List<List<fields_trecho>> trechos_atuais = this.getTrechos(id_serie);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TREINO_SERIE + " WHERE " + KEY_ID_SERIE + " =" + Integer.toString(id_serie),  null);
        count_treino_serie = cursor.getCount();

        count_serie_ida = trechos_atuais.get(0).size();
        count_serie_volta = trechos_atuais.get(1).size();

        if(count_treino_serie <= 1 && count_serie_ida <= 1 && count_serie_volta <= 1){

            db.delete(TABLE_TREINO_SERIE, KEY_ID_SERIE + " = ? AND " + KEY_ID_TREINO + " = ?", new String[]{ Integer.toString(id_serie), Integer.toString(id_treino)});
            deleteTrechos(id_serie, trechos_atuais.get(0));
            deleteTrechos(id_serie, trechos_atuais.get(1));
            db.delete(TABLE_SERIES, KEY_ID_SERIE + " = ?", new String[]{ Integer.toString(id_serie)});

        }
        else{

            db.delete(TABLE_TREINO_SERIE, KEY_ID_SERIE + " = ? AND " + KEY_ID_TREINO + " = ?", new String[]{ Integer.toString(id_serie), Integer.toString(id_treino)});
            deleteTrechos(id_serie, trechos_atuais.get(0));
            deleteTrechos(id_serie, trechos_atuais.get(1));

        }


        if(count_treino_serie == 1){

            db.delete(TABLE_SERIES, KEY_ID_SERIE + " = ?", new String[]{Integer.toString(id_serie) });
        }


    }


    /***************************************TRATAMENTO DA TABLE TRECHOS******************************/


    public void deleteTrechos(int id_serie, List<fields_trecho> trechos){

        int count_trecho_ida, count_trecho_volta;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        for(int i = 0; i < trechos.size(); ++i) {

            cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIE_IDA + " WHERE " + KEY_ID_TRECHO + " =?", new String[]{Integer.toString(trechos.get(i).id_trecho)}, null);
            count_trecho_ida = cursor.getCount();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIE_VOLTA + " WHERE " + KEY_ID_TRECHO + " =?", new String[]{Integer.toString(trechos.get(i).id_trecho)}, null);
            count_trecho_volta = cursor.getCount();

            if (count_trecho_ida <= 1 && count_trecho_volta <= 1) {

                db.delete(TABLE_SERIE_IDA, KEY_ID_SERIE + " = ? AND " + KEY_ID_TRECHO + " = ?", new String[]{Integer.toString(id_serie), Integer.toString(trechos.get(i).id_trecho)});
                db.delete(TABLE_SERIE_VOLTA, KEY_ID_SERIE + " = ? AND " + KEY_ID_TRECHO + " = ?", new String[]{Integer.toString(id_serie), Integer.toString(trechos.get(i).id_trecho)});
                db.delete(TABLE_TRECHOS, KEY_ID_TRECHO + " = ?", new String[]{Integer.toString(trechos.get(i).id_trecho)});

            } else {

                db.delete(TABLE_SERIE_IDA, KEY_ID_SERIE + " = ? AND " + KEY_ID_TRECHO + " = ?", new String[]{Integer.toString(id_serie), Integer.toString(trechos.get(i).id_trecho)});
                db.delete(TABLE_SERIE_VOLTA, KEY_ID_SERIE + " = ? AND " + KEY_ID_TRECHO + " = ?", new String[]{Integer.toString(id_serie), Integer.toString(trechos.get(i).id_trecho)});
            }
        }
    }

    public void addTrecho(int id_serie, fields_trecho trecho, int modo){

        /*********MODO DESCREVE SE O TRECHO EH DE IDA OU VOLTA**********/
        /*********MODO == 1 --> IDA; MODO == 2 --> VOLTA;***************/

        Log.d(TAG, "AddTrecho");


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(Search_Trecho(trecho.tamanho, trecho.tempo) != -1){
            if(modo == 1){

                values.put(KEY_ID_SERIE, id_serie);
                values.put(KEY_ID_TRECHO, Search_Trecho(trecho.tamanho, trecho.tempo));
                db.insert(TABLE_SERIE_IDA, null, values);

            }
            else if(modo == 2){

                values.put(KEY_ID_SERIE, id_serie);
                values.put(KEY_ID_TRECHO, Search_Trecho(trecho.tamanho, trecho.tempo));
                db.insert(TABLE_SERIE_VOLTA, null, values);

            }
        }
        else{

            values.clear();
            if(modo == 1){
                values.put(KEY_TAMANHO_TRECHO, trecho.tamanho);
                values.put(KEY_TEMPO_TRECHO, trecho.tempo);
                db.insert(TABLE_TRECHOS, null, values);

                values.put(KEY_ID_SERIE, id_serie);
                values.put(KEY_ID_TRECHO, Search_Trecho(trecho.tamanho, trecho.tempo));
                db.insert(TABLE_SERIE_IDA, null, values);

            }
            else if(modo == 2){
                values.put(KEY_TAMANHO_TRECHO, trecho.tamanho);
                values.put(KEY_TEMPO_TRECHO, trecho.tempo);
                db.insert(TABLE_TRECHOS, null, values);

                values.put(KEY_ID_SERIE, id_serie);
                values.put(KEY_ID_TRECHO, Search_Trecho(trecho.tamanho, trecho.tempo));
                db.insert(TABLE_SERIE_VOLTA, null, values);
            }

        }
    }










    public int Search_Trecho(float tamanho, float tempo_nado) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRECHOS + " WHERE " + KEY_TAMANHO_TRECHO + "=? AND "
                        + KEY_TEMPO_TRECHO + "=?", new String[] {Float.toString(tamanho), Float.toString(tempo_nado)});

        if(cursor.moveToFirst()){

            return Integer.parseInt(cursor.getString(0));

        }
        else {
            return -1;
        }




    }


    public boolean Search_Serie_Ida(int id_serie, int id_trecho) {

        boolean bool = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIE_IDA + " WHERE " + KEY_ID_SERIE + "=? AND " + KEY_ID_TRECHO + "=?", new String[] {Integer.toString(id_serie),Integer.toString(id_trecho)});


        if (cursor != null) {
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                bool = true;
            }
            cursor.close();
            return bool;
        }
        else{
            return bool;

        }


    }

    public boolean Search_Serie_Volta(int id_serie, int id_trecho) {

        boolean bool = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIE_VOLTA + " WHERE " + KEY_ID_SERIE + "=? AND " + KEY_ID_TRECHO + "=?", new String[] {Integer.toString(id_serie),Integer.toString(id_trecho)});


        if (cursor != null) {
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                bool = true;
            }
            cursor.close();
            return bool;
        }
        else{
            return bool;

        }


    }

    public int getHighestTrecho(){
        /****METODO UTILIZADO PARA SABER MAIOR ID SERIE****/

        String selectquery = "SELECT * FROM " + TABLE_TRECHOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        int x = -1;

        if(cursor.moveToFirst()){
            do{
                if(Integer.parseInt(cursor.getString(0)) > x)
                    x = Integer.parseInt(cursor.getString(0));

            }while(cursor.moveToNext());

        }
        cursor.close();
        return x;
    }

    //RETORNA LISTA DE LISTA DE TRECHOS, EM QUE A POS DA ESQUERDA EH A IDA E A DA DIREITA A VOLTA
    // POSICAO 0 == LISTA IDA / POSICAO 1 == LISTA VOLTA
    public List<List<fields_trecho>> getTrechos(int id_serie){

        List<fields_trecho> trechos_ida = new ArrayList<>();
        List<fields_trecho> trechos_volta = new ArrayList<>();
        List<List<fields_trecho>> trechos = new ArrayList<>();
        List<Integer> id_trechos_ida = new ArrayList<>();
        List<Integer> id_trechos_volta = new ArrayList<>();





        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIE_IDA +  " WHERE " + KEY_ID_SERIE +  " = ?", new String[] {Integer.toString(id_serie)});

        if(cursor.moveToFirst()){
            do{

                int id_trecho = Integer.parseInt(cursor.getString(1));

                id_trechos_ida.add(id_trecho);
            }while(cursor.moveToNext());
        }
        else{

            id_trechos_ida = null;

        }

        cursor = db.rawQuery("SELECT * FROM " + TABLE_SERIE_VOLTA +  " WHERE " + KEY_ID_SERIE +  " = ?", new String[] {Integer.toString(id_serie)});
        if(cursor.moveToFirst()){
            do{

                int id_trecho = Integer.parseInt(cursor.getString(1));

                id_trechos_volta.add(id_trecho);
            }while(cursor.moveToNext());
        }
        else{

            id_trechos_volta = null;

        }
        if(id_trechos_ida!= null) {
            for(int i = 0; i < id_trechos_ida.size(); ++i) {

                cursor = db.rawQuery("SELECT * FROM " + TABLE_TRECHOS + " WHERE " + KEY_ID_TRECHO + " = ?", new String[]{Float.toString(id_trechos_ida.get(i))});
                if (cursor.moveToFirst()) {
                    do {

                        fields_trecho trecho = new fields_trecho(id_trechos_ida.get(i), Float.parseFloat(cursor.getString(2)), Float.parseFloat(cursor.getString(1)));

                        trechos_ida.add(trecho);
                    } while (cursor.moveToNext());
                } else {

                    trechos_ida = null;

                }
            }
        }
        if(id_trechos_volta!= null) {
            for (int i = 0; i < id_trechos_volta.size(); ++i) {

                cursor = db.rawQuery("SELECT * FROM " + TABLE_TRECHOS + " WHERE " + KEY_ID_TRECHO + " = ?", new String[]{Float.toString(id_trechos_volta.get(i))});
                if (cursor.moveToFirst()) {
                    do {

                        fields_trecho trecho = new fields_trecho(id_trechos_volta.get(i), Float.parseFloat(cursor.getString(2)), Float.parseFloat(cursor.getString(1)));

                        trechos_volta.add(trecho);
                    } while (cursor.moveToNext());
                } else {

                    trechos_volta = null;

                }
            }
        }

        trechos.add(0, trechos_ida);
        trechos.add(1, trechos_volta);


        cursor.close();
        return trechos;

    }





    /*************************************TRATAMENTO DA TABLE TAMANHO********************************/




    //ADDING TAMANHO DE PISCINA
    public void addTamanho(int tamanho, String nome){
        Log.d(TAG, "AddTamanho");

        SQLiteDatabase db = this.getReadableDatabase();

        //COMANDO RESPONSAVEL POR APAGAR VALOR DE TAMANHO CASO HAJA ALGUM
        db.execSQL("DELETE FROM " + TABLE_PISCINAS + ";");

        ContentValues values = new ContentValues();

        values.put(KEY_TAMANHO_PISCINA, tamanho);
        values.put(KEY_NOME_PISCINA, nome);

        db.insert(TABLE_PISCINAS, null, values);


    }


    //RETORNA TAMANHO DA PISCINA SALVO NO DB
    public Map<String, Float> getTamanho(){
        Log.d(TAG, "GetTamanho");

        Map<String, Float> mapa  = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PISCINAS, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            float tamanho = Float.parseFloat(cursor.getString(1));
            mapa.put(cursor.getString(2), tamanho);
            cursor.close();
            return mapa;
        }

        else{
            return null;
        }


    }

    //NOT USED YET
    public int getTamanhoCount(){
        Log.d(TAG, "GetTamanhoCount");
        int x;


        String countQuery = "SELECT * FROM " + TABLE_PISCINAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        x = cursor.getCount();
        cursor.close();

        return x;


    }



}
