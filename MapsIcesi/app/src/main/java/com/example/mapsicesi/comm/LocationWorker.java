package com.example.mapsicesi.comm;

import com.example.mapsicesi.activity.MapsActivity;
import com.example.mapsicesi.model.Position;
import com.google.gson.Gson;

public class LocationWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;

    public LocationWorker(MapsActivity ref){
        this.ref = ref;
        isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while(isAlive){
            delay(3000);
            //HACER EL PUT DE NUESTRA POSICION
            if(ref.getCurrentPosition() != null){
                utilDomi.PUTrequest("https://nuevo-proyecto-appmoviles-2020.firebaseio.com/users/"+ref.getUser()+"/location.json ",gson.toJson(ref.getCurrentPosition()));
            }


        }
    }

    public void delay(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        this.isAlive = false;
    }
}
