package com.example.kylem.myapplication;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.String;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.LocationRequest;





public class ProgressTracker extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ArrayList<LatLng> points;

    double latitude = 0.0;
    double longitude = 0.0;
    double prev_lat = 0.0;
    double prev_long = 0.0;
    boolean trackActive = false;

    private static final String TAG = "MainActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter




    private final LocationListener listener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude); //you already have this


            if(trackActive){
                points.add(latLng); //added
                redrawLine(); //added
            }


        }

        @Override
        public void onProviderDisabled(String provider){

        }

        @Override
        public void onProviderEnabled(String provider){

        }

        public void onStatusChanged(String provider, int status, Bundle extra){

        }

    };

    Polyline line;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String start_stop_button = getString(R.string.start_stop_button,"Start");
        super.onCreate(savedInstanceState);


        points = new ArrayList<LatLng>();
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        }catch(SecurityException e){
            System.out.println("Permission Denied.");
        }
        setContentView(R.layout.activity_progress_tracker);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btn = (Button) findViewById(R.id.button);
        btn.setText("Start");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });


        }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            mMap.setMyLocationEnabled(true);
            UiSettings mUiSettings = mMap.getUiSettings();
            mUiSettings.setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            System.out.println("Permission Denied.");
        }
        /*
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


            Toast.makeText(getApplicationContext(),"Latitude = " + latitude + "\nLongitude = " + longitude,Toast.LENGTH_SHORT).show();

            LatLng newLatLong = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(newLatLong).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLong));


            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(latitude, longitude), new LatLng(prev_lat,prev_long ))
                    .width(5)
                    .color(Color.RED));

            prev_lat = latitude;
            prev_long = longitude;*/
            //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Add a marker in Sydney and move the camera
            //LatLng toronto = new LatLng(43, -79);
            //mMap.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));


    }


    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line = mMap.addPolyline(options); //add Polyline
    }

    private void clearLine(){
        mMap.clear();  //clears all Markers and Polylines
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onButtonClick() {
        Button btn = (Button) findViewById(R.id.button);
        Toast.makeText(getApplicationContext(),"Got your click",Toast.LENGTH_SHORT).show();
        if(btn.getText().equals("Start")){
            btn.setText("Stop");
            trackActive = true;
            clearLine();
        }
        else if(btn.getText().equals("Stop")){
            btn.setText("Start");
            trackActive = false;
            points.clear();
        }
        else{
            //Do nothing
        }



    }


}












