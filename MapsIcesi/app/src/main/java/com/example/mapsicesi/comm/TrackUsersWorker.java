package com.example.mapsicesi.comm;

import android.util.Log;

import com.example.mapsicesi.activity.MapsActivity;
import com.example.mapsicesi.model.Position;
import com.example.mapsicesi.model.PositionContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackUsersWorker extends Thread{

    public MapsActivity ref;
    private boolean isAlive;

    public TrackUsersWorker(MapsActivity ref){
        this.ref = ref;
        this.isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi httpsWebUtilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while(isAlive){
            delay(3000);
            String json = httpsWebUtilDomi.GETrequest("https://nuevo-proyecto-appmoviles-2020.firebaseio.com/users.json");
            Log.e(">>>",json);
            Type tipo = new TypeToken<HashMap<String, PositionContainer>>(){}.getType();
            HashMap<String, PositionContainer> users = gson.fromJson(json,tipo);
            ArrayList<Position> positions = new ArrayList<>();
            users.forEach(
                    (key,value)->{
                        PositionContainer positionContainer = value;
                        double lat = positionContainer.getLocation().getLat();
                        double lng = positionContainer.getLocation().getLng();
                        positions.add(new Position(lat,lng));
                    }
            );
            ref.updateMerkers(positions);
        }
    }

    public void delay(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finish(){
        this.isAlive = false;
    }
}
