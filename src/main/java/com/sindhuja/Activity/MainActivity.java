package com.sindhuja.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.sindhuja.Model.City;
import com.sindhuja.Model.Coordinates;
import com.sindhuja.R;
import com.sindhuja.adapter.AdapterCityListing;
import com.sindhuja.adapter.AdapterCityListingLandScape;
import com.sindhuja.listener.OnClickListener;
import com.sindhuja.utils.TouchSupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.sindhuja.R.id.mapMainLandScape;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TouchSupportMapFragment.setOnMapTouchListener {
    private FastScrollRecyclerView fastScrollRecyclerView;
    private RecyclerView recyclerView;
    private AppCompatEditText editTextSearch;
    private AdapterCityListing mAdapter;
    private AdapterCityListingLandScape mAdapterLandScape;
    private GoogleMap mMap;
    int config = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initial setup
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            config = 1;
            setContentView(R.layout.activity_main);
        //If the device is in potrait mode
            showPotraitConfig();
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            config = 0;
        //If the device is in landscape mode
            initMap();
            showLandScapeConfig();
        }
    }
        //Loading the list of cities from JSON file
    private void loadListFromJsonFile() {
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
            ArrayList<City> mArrListCity = new ArrayList<>();
            City mCity;
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                mCity = new City();
                mCity.setCountry(jo_inside.optString("country"));
                mCity.setName(jo_inside.optString("name"));
                mCity.setId(jo_inside.optLong("_id"));

                JSONObject jCoordinate = jo_inside.getJSONObject("coord");
                Coordinates coordinates = new Coordinates();
                coordinates.setLat(jCoordinate.optDouble("lat"));
                coordinates.setLon(jCoordinate.optDouble("lon"));
                mCity.setCoord(coordinates);
                mArrListCity.add(mCity);
                if (config == 1)
                    setUpAdapter(mArrListCity);
                else
                    setUpAdapterLandScape(mArrListCity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        //Setting up Adapter
    private void setUpAdapter(ArrayList<City> mArrListCity) {
        mAdapter = new AdapterCityListing(mArrListCity, MainActivity.this);
        mAdapter.setOnItemClickListener(new OnClickListener() {
            @Override
            public void onItemClicked(int pos, City city) {
                if (config == 1)
                    startActivity(new Intent(MainActivity.this, MapActivity.class).putExtra("latitude", city.getCoord().getLat()).putExtra("longitude", city.getCoord().getLon()).putExtra("model",city));
                else
                    setMapLocation(city.getCoord().getLat(), city.getCoord().getLon());
            }
        });
        fastScrollRecyclerView.setAdapter(mAdapter);
    }

    private void setUpAdapterLandScape(ArrayList<City> mArrListCity) {
        mAdapterLandScape = new AdapterCityListingLandScape(mArrListCity, MainActivity.this);
        mAdapterLandScape.setOnItemClickListener(new OnClickListener() {
            @Override
            public void onItemClicked(int pos, City city) {
                if (config == 1)
                    startActivity(new Intent(MainActivity.this, MapActivity.class).putExtra("latitude", city.getCoord().getLat()).putExtra("longitude", city.getCoord().getLon()).putExtra("model",city));
                else
                    setMapLocation(city.getCoord().getLat(), city.getCoord().getLon());
            }
        });
        recyclerView.setAdapter(mAdapterLandScape);
    }

        //JSON file loading
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
            config = 0;
            initMap();
            showLandScapeConfig();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
            config = 1;
            showPotraitConfig();
        }
    }
        //Landscape Configuration
    private void showLandScapeConfig() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        editTextSearch = (AppCompatEditText) findViewById(R.id.etSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadListFromJsonFile();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                mAdapterLandScape.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
        //Potrait configuration
    private void showPotraitConfig() {
        fastScrollRecyclerView = (FastScrollRecyclerView) findViewById(R.id.rvCityList);
        editTextSearch = (AppCompatEditText) findViewById(R.id.etSearch);
        fastScrollRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadListFromJsonFile();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {


                // filter your list from your input
//                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }
        //Initializing Map
    private void initMap() {
        TouchSupportMapFragment mapFragment;
        mapFragment = (TouchSupportMapFragment) getSupportFragmentManager().findFragmentById(mapMainLandScape);
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
//        setMapLocation();
    }
        //Fetching Map and adding the markers
    private void setMapLocation(Double latFromAct, Double longFromAct) {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(latFromAct, longFromAct))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
