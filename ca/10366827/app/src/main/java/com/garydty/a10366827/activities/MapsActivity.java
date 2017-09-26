package com.garydty.a10366827.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.garydty.a10366827.R;
import com.garydty.a10366827.utility.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Polyline> lines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        final DBHelper db = new DBHelper(getApplicationContext());
        ArrayList<MarkerOptions> mOptions = db.getMarkerOptions();
        for(MarkerOptions mO : mOptions){
            Marker m = mMap.addMarker(mO);
            markers.add(m);
        }

        if(markers.size() > 1){
            PolylineOptions polyLines = new PolylineOptions()
                    .width(5).color(Color.RED);
            for(Marker marker : markers){
                polyLines.add(marker.getPosition());
            }
            mMap.addPolyline(polyLines);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialog, int which) {
                                db.removeAllMarkers();
//                                for(Marker m : markers)
//                                    m.remove();
                                markers = null;
                                markers = new ArrayList<>();
                                mMap.clear();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .setMessage("Do you want to remove all map marker data history?")
                        .show();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                db.addMarker(latLng.longitude, latLng.latitude);
                //Do what you want on obtained latLng
                Marker m = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                markers.add(m);

                if(markers.size() > 1){
                    PolylineOptions polyLines = new PolylineOptions()
                            .width(5).color(Color.RED);
                    for(Marker marker : markers){
                        polyLines.add(marker.getPosition());
                    }
                    mMap.addPolyline(polyLines);
                }

            }
        });
    }
}
