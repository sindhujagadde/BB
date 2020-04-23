package com.sindhuja.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sindhuja.Model.City;
import com.sindhuja.R;
import com.sindhuja.utils.TouchSupportMapFragment;

import static com.sindhuja.R.id.mapSelected;

import androidx.fragment.app.FragmentActivity;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, TouchSupportMapFragment.setOnMapTouchListener {
    private Double latFromAct = 0.0, longFromAct = 0.0;
    private GoogleMap mMap;
    private ImageView ivBack;
    private City city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_selected_on_map);
        initMap();
        getSerializableData();
        setUpUi();
    }

    private void setUpUi() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getSerializableData() {
        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("latitude") && b.containsKey("longitude")) {
            latFromAct = b.getDouble("latitude");
            longFromAct = b.getDouble("longitude");
            city= (City) b.getSerializable("model");
        }
    }
        //Initializing Map
    private void initMap() {
        TouchSupportMapFragment mapFragment;
        mapFragment = (TouchSupportMapFragment) getSupportFragmentManager().findFragmentById(mapSelected);
        mapFragment.getMapAsync(this);
        mapFragment.setOnMapTouchListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(false);
//        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        setMapLocation();
    }

    private void setMapLocation() {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(latFromAct, longFromAct))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(city.getName()+","+city.getCountry())).showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latFromAct, longFromAct), 17));
        }
    }

    @Override
    public void onTouch() {

    }

    @Override
    public void onRelease() {

    }
}
