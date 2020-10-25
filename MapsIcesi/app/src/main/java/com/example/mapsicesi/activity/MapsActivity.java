package com.example.mapsicesi.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapsicesi.R;
import com.example.mapsicesi.comm.LocationWorker;
import com.example.mapsicesi.comm.TrackUsersWorker;
import com.example.mapsicesi.model.Hueco;
import com.example.mapsicesi.model.Position;
import com.example.mapsicesi.util.Constans;
import com.example.mapsicesi.comm.HTTPSWebUtilDomi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private String user;
    private TextView output;
    private FloatingActionButton addBtn;
    private LocationManager manager;
    //private Marker me;
    private ArrayList<Hueco> huecos;
    private ArrayList<Marker> points;
    private LocationWorker locationWorker;
    private TrackUsersWorker trackUsersWorker;
    private Position currentPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        output = findViewById(R.id.output);
        addBtn = findViewById(R.id.addBtn);

        user = getIntent().getExtras().getString("user");

        addBtn.setOnClickListener(this);


        points = new ArrayList<>();
        huecos = new ArrayList<>();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,2,this);

        // Add a marker in Sydney and move the camera
        setInitialPos();
        getHuecos();

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        locationWorker = new LocationWorker(this);
        locationWorker.start();

        trackUsersWorker = new TrackUsersWorker(this);
        trackUsersWorker.start();
    }

    @Override
    protected void onDestroy() {
        locationWorker.finish();
        trackUsersWorker.finish();
        super.onDestroy();
    }

    private void setHuecos() {
        for(int i=0; i<huecos.size(); i++){
            LatLng posHueco = new LatLng(huecos.get(i).getLatitude(), huecos.get(i).getLongitude());
            if(huecos.get(i).getConfirmado() == true){
                Marker mark = mMap.addMarker(new MarkerOptions()
                        .position(posHueco)
                        .title("Hueco puesto por "+ huecos.get(i).getUser())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .anchor(0.0f,1.0f));
                //points.add(mark);
            }else{
                Marker mark = mMap.addMarker(new MarkerOptions()
                        .position(posHueco)
                        .title("Hueco puesto por "+ huecos.get(i).getUser())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .anchor(0.0f,1.0f));
                //points.add(mark);
            }
        }

        huecos.forEach(
                (hueco)->{

                }
        );
    }

    @SuppressLint("MissingPermission")
    public void setInitialPos(){
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            updateMyLocation(location);
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateMyLocation(location);
        //boolean inAtIcesi = PolyUtil.containsLocation(new LatLng(location.getLatitude(),location.getLongitude()),icesi.getPoints(),false);
    }

    public void updateMyLocation(Location location){
        LatLng myPos = new LatLng(location.getLatitude(),location.getLongitude());
        /*
        if(me == null){
            me = mMap.addMarker(new MarkerOptions()
                    .position(myPos)
                    .title("Yo")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .anchor(0.0f,1.0f));
        }else{
            me.setPosition(myPos);
        }*/

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos,15));
        currentPosition = new Position(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Marker p = mMap.addMarker(new MarkerOptions().position(latLng).title("marcador"));
        //points.add(p);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getPosition().latitude+","+marker.getPosition().longitude, Toast.LENGTH_LONG).show();
        marker.showInfoWindow();
        return true;
    }
    public void getHuecos(){
        HTTPSWebUtilDomi https =new HTTPSWebUtilDomi();

        new Thread(
                ()->{
                    String response =https.GETrequest(Constans.BASE_URL + "holes/.json");

                    Type tipo = new TypeToken<HashMap<String, Hueco>>(){}.getType();
                    Gson gson = new Gson();
                    HashMap<String, Hueco> huecoss =gson.fromJson(response,tipo);

                    if(huecoss != null){
                        huecoss.forEach(
                                (key, value)->{
                                    huecos.add(value);
                                }
                        );

                        /*if(huecos.isEmpty() == false){
                            runOnUiThread(()-> {Toast.makeText(this,"esta lleno el arreglo con "+huecos.size()+" huecos" ,Toast.LENGTH_LONG).show();});
                        }else{
                            runOnUiThread(()-> {Toast.makeText(this,"esta vacio el arreglo" ,Toast.LENGTH_LONG).show();});
                        }*/

                    }else{
                        runOnUiThread(
                                ()-> {
                                    Toast.makeText(this,"Aun no hay huecos" ,Toast.LENGTH_LONG).show();
                                }
                        );
                    }



                }
        ).start();

        setHuecos();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addBtn:
                Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                String address = "";

                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    address = addresses.get(0).getAddressLine(0);
                }catch (Exception e){

                }

                Hueco hueco = new Hueco(UUID.randomUUID().toString(),user,location.getLatitude(),location.getLongitude(),new Date().getTime(),false);
                Gson gson = new Gson();
                String json = gson.toJson(hueco);

                //AGREGO EL HUECO A LOS HUECOS
                huecos.add(hueco);
                //AGREGO EL MARCADOR DEL HUECO AL MAPA
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(hueco.getLatitude(),hueco.getLongitude()))
                        .title("Hueco puesto por "+hueco.getUser())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).anchor(0.0f,1.0f)
                );
                //AGREGO EL MARCADOR A LOS MARCADORES
                points.add(marker);
                HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();

                new Thread(
                        ()->{
                            String response = https.PUTrequest(Constans.BASE_URL +"holes/"+hueco.getId()+".json",json);
                        }
                ).start();

                mostrarDialogo(location.getLatitude(),location.getLongitude(),address);

                break;
        }

    }

    private void mostrarDialogo(double latitude, double longitude, String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Hueco");
        builder.setMessage("Coordenada: \n"+latitude+","+longitude+""+"Dirección: \n"+address)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Ha quedado registrado su hueco",Toast.LENGTH_LONG).show();
            }
        }).show();
    }

    public Position getCurrentPosition(){
        return currentPosition;
    }

    public String getUser(){
        return user;
    }

    public void updateMerkers(ArrayList<Position> positions){
        runOnUiThread(
                ()->{

                    for (int i=0; i<points.size();i++){
                        Marker m = points.get(i);
                        m.remove();
                    }
                    points.clear();

                    for(int i=0; i<positions.size();i++){
                        LatLng latLng = new LatLng(positions.get(i).getLat(),positions.get(i).getLng());
                        Marker m = mMap.addMarker(new MarkerOptions().position(latLng));
                        points.add(m);
                    }
                }
        );
    }
}