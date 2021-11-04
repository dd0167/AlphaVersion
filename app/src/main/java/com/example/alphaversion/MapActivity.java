package com.example.alphaversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap gmap;
    Bundle mapViewBundle;
    TextView tv_latitude;
    TextView tv_longitude;
    TextView tv_country;
    TextView tv_city;
    TextView tv_address;
    ProgressBar progressBar;
    AlertDialog.Builder adb;
    EditText et_GetAddress;

    FusedLocationProviderClient fusedLocationProviderClient;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        tv_latitude=(TextView) findViewById(R.id.tv_latitude);
        tv_longitude=(TextView) findViewById(R.id.tv_longitude);
        tv_country=(TextView) findViewById(R.id.tv_country);
        tv_city=(TextView) findViewById(R.id.tv_city);
        tv_address=(TextView) findViewById(R.id.tv_address);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMaxZoomPreference(21);
        gmap.setMinZoomPreference(0);

        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        LatLng isr = new LatLng(31.38269, 35.071805);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(isr));
        progressBar.setVisibility(View.INVISIBLE);

        float zoomLevel = 7.0f; //This goes up to 21
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(isr, zoomLevel));
    }

    public void get_location(View view) {
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            progressBar.setVisibility(View.VISIBLE);
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location!=null)
                {
                    try {
                        gmap.clear();
                        Geocoder geocoder=new Geocoder(MapActivity.this);

                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        LatLng cl = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(cl);
                        gmap.addMarker(markerOptions);

                        float zoomLevel = 17.0f; //This goes up to 21
                        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(cl, zoomLevel));

                        tv_latitude.setText("Latitude: "+addresses.get(0).getLatitude());
                        tv_longitude.setText("Longitude: "+addresses.get(0).getLongitude());
                        tv_country.setText("Country Name: "+addresses.get(0).getCountryName());
                        tv_city.setText("City Name: "+addresses.get(0).getLocality());
                        tv_address.setText("Address: "+addresses.get(0).getAddressLine(0));

                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        String st=item.getTitle().toString();
        if (st.equals("Auth"))
        {
            Intent in=new Intent(this,MainActivity.class);
            startActivity(in);
            finish();
        }
        if (st.equals("Map"))
        {
            Intent in=new Intent(this,MapActivity.class);
            startActivity(in);
            finish();
        }
        if (st.equals("Gallery"))
        {
            Intent in=new Intent(this,GalleryActivity.class);
            startActivity(in);
            finish();
        }
        return true;
    }

    public void insert_address(View view) {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Insert Address");
        et_GetAddress=new EditText(this);
        et_GetAddress.setHint("Type Text Here: Street, City, Country");
        et_GetAddress.setGravity(Gravity.CENTER);
        adb.setView(et_GetAddress);
        adb.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(DialogInterface dialog, int which) {
                String address=et_GetAddress.getText().toString();
                if (!address.equals(""))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location!=null)
                            {
                                Geocoder geocoder=new Geocoder(MapActivity.this);
                                try {
                                    List<Address> addressList=geocoder.getFromLocationName(address,6);
                                    Address user_address=addressList.get(0);

                                    LatLng latLng = new LatLng(user_address.getLatitude(), user_address.getLongitude());

                                    gmap.clear();
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    gmap.addMarker(markerOptions);

                                    float zoomLevel = 17.0f; //This goes up to 21
                                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                                    tv_latitude.setText("Latitude: "+user_address.getLatitude());
                                    tv_longitude.setText("Longitude: "+user_address.getLongitude());
                                    tv_country.setText("Country Name: "+user_address.getCountryName());
                                    tv_city.setText("City Name: "+user_address.getLocality());
                                    tv_address.setText("Address: "+user_address.getAddressLine(0));

                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(MapActivity.this, "Error Address!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MapActivity.this, "Insert Address!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog ad=adb.create();
        ad.show();
    }
}