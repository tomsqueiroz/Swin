package com.example.tom.fitas_led;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by Tom on 09/03/2017.
 */

public class wifi {


    private Context context;

    //************Strings de Configuração da Wifi***************/
    private String SSID;
    private String TAG = "WIFI_LOG";
    private String PASSWORD = "";


    //************INSTANCIANDO WIFI MANAGER/ CONNECTIO MANAGER*******************/
    private WifiConfiguration conf = new WifiConfiguration();
    private WifiManager wifiManager;
    private ConnectivityManager connManager;
    private boolean isConnected;


    private static final int SEM_SENHA = 0;
    private static final int WPA_WPA2 = 1;
    private static final int WEP = 2;




    /*************WIFI CONSTRUCTOR***************************/
    public wifi(String SSID, String PASSWORD, Context context, int Mode){
        Log.d(TAG, "Constructor");
        this.SSID = SSID;
        this.PASSWORD = PASSWORD;
        this.context = context;
        conf.SSID = "\"" + SSID + "\"";

        /***********VARIAVEL MODE DETERMINA SEGURANÇA DA REDE*********/

        switch(Mode){

            case SEM_SENHA:     /*************MODE == 0 SEM SEGURANÇA************/

                /**********SETANDO QUE WIFI NAO TEM SENHA************/
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            break;

            case WPA_WPA2:     /************MODE == 1 WPA/WPA2**************/

                conf.preSharedKey = "\"" + PASSWORD + "\"";

            break;

            case WEP:           /************MODE == 2 WEP**************/

                conf.wepKeys[0] = "\"" + PASSWORD + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            break;







        }







        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "Before adding Network");


        /**********ADICIONANDO REDE A LISTA DE REDES CONHECIDAS*********/
        wifiManager.addNetwork(conf);
        Log.d(TAG, "Network added");



    }


    public void disconnect(String ssid){

        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if(wifiInfo.getSSID().contains(ssid)) {

            int networkId = wifiManager.getConnectionInfo().getNetworkId();
            wifiManager.disconnect();
            wifiManager.removeNetwork(networkId);
            wifiManager.saveConfiguration();
        }

    }




    /*********FUNCAO RESPONSAVEL POR SE CONECTAR A WIFI JA CONHECIDA*//////
    public void connect(){
        Log.d(TAG, "Connect");

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        if(list != null) {
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + SSID + "\"")) {

                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.addNetwork(conf);
                    wifiManager.reconnect();


                    break;
                }
            }
        }
        else{

            wifiManager.setWifiEnabled(true);


        }
    }


    /**********FUNCAO RESPONSAVEL POR RETORNAR STATUS DE CONECTIVIDADE**/
    public boolean is_connected_ssid(String ssid){
        this.isConnected = false;
        Log.d(TAG, "Is Connected");
        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();




        /***TESTANDO SE ESTÁ CONECTADO A WIFI OU NAO****/
        if (mWifi.isConnected() && wifiInfo.getSSID().contains(ssid)) {
            Log.d(TAG, "Está conectado a wifi " + ssid);


            return true;

        }
        else{

            return false;

        }


    }

    public boolean isConnected(){

        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();

    }





}
