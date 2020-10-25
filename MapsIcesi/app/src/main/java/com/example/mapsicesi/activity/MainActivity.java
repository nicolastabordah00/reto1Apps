package com.example.mapsicesi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mapsicesi.R;
import com.example.mapsicesi.model.User;
import com.example.mapsicesi.util.Constans;
import com.example.mapsicesi.comm.HTTPSWebUtilDomi;
import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userET;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userET = findViewById(R.id.userET);
        loginButton = findViewById(R.id.loginButton);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION},1);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                /*User user =new User(UUID.randomUUID().toString(), ,new Date().getTime(),0.0,0.0,"");
                Gson gson = new Gson();
                String json = gson.toJson(user);
                HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();*/

                new Thread(
                        ()->{
                            //String response = https.PUTrequest(Constans.BASE_URL +"users/"+user.getNombre()+".json",json);
                            runOnUiThread(
                                    ()->{
                                        Intent i = new Intent(this, MapsActivity.class);
                                        i.putExtra("user",userET.getText().toString());
                                        startActivity(i);
                                    }
                            );
                        }
                ).start();



                break;
        }
    }
}