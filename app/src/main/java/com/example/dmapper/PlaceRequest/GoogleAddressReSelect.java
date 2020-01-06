package com.example.dmapper.PlaceRequest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dmapper.GetGpsCoordinates.GetGpsCoordinates;
import com.example.dmapper.MainActivity;
import com.example.dmapper.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;

public class GoogleAddressReSelect extends AppCompatActivity implements OnMapReadyCallback {

    GetGpsCoordinates getGpsCoordinates;
    PlaceAutocompleteFragment autocompleteFragment;
    SupportMapFragment mapFragment;
    ImageView mylocationbutton;
    Button reload_button;
    private GoogleMap gMap;
    private LatLng SEOUL;
    private LatLng CenterLocation = new LatLng(0,0);
    private Marker markerCenter;
    public String Reload_ReverseGeoCodeValue;//핀 위치에서 얻어낸 addressname

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_address_re_select);

        init_variable();
    }

    @Override
    protected void onResume() {
        super.onResume();

        reload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoogleAddressReSelect.this,PlaceUpdateRequest.class);
                intent.putExtra("Reload_Address_Value",Reload_ReverseGeoCodeValue);
                startActivity(intent);

                finish();
            }
        });
        mylocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });
        setupAutoCompleteFragment(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL,20.0f));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        setMarkerCenter();
        getCenterLocation();
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(getActivity(), "가운데"+CenterLocation.latitude, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public void init_variable(){

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(Html.fromHtml("<font color='#746E66'>"+"주소 재설정"+"</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        getGpsCoordinates = new GetGpsCoordinates(getApplicationContext());
        mylocationbutton = (ImageView) findViewById(R.id.mylocationbutton);
        reload_button = (Button) findViewById(R.id.address_reload);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_view);
        SEOUL = new LatLng(getGpsCoordinates.getLatitude(), getGpsCoordinates.getLongitude());
        mapFragment.getMapAsync(this);
    }
    //mylocation으로 이동하는 method
    private void getMyLocation() {
        SEOUL = new LatLng(getGpsCoordinates.getLatitude(), getGpsCoordinates.getLongitude());
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL,20));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
    private void getCenterLocation(){
        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                CenterLocation = cameraPosition.target;
                getAddressname(markerCenter.getPosition().latitude,markerCenter.getPosition().longitude);
            }
        });
    }
    //마커를 무조건 가운데에 놓는 method
    public void setMarkerCenter(){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(gMap.getCameraPosition().target);
        markerCenter = gMap.addMarker(new MarkerOptions()
                .position(gMap.getCameraPosition().target)
                //.title("Spot")
                //.snippet("gg")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                markerCenter.setPosition(gMap.getCameraPosition().target);
                Log.i("position","latitude"+markerCenter.getPosition().latitude+" longitude"+markerCenter.getPosition().longitude);
                //핀을 가운데에 놓고 핀의 위치를 이용해서 위도 경도 뽑아옴 카카오맵이랑 다름.
            }
        });
    }
    public void getAddressname(double latitude, double longitude){
        Geocoder gc = new Geocoder(getApplicationContext());

        if(gc.isPresent()){
            try {
                List<Address> list = gc.getFromLocation(latitude,longitude,10);

                Reload_ReverseGeoCodeValue = list.get(0).getAddressLine(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //검색 시에 호출되는 method
    private void setupAutoCompleteFragment(final OnMapReadyCallback instance) {
        if(autocompleteFragment == null) {
            //원래 destroy에서 remove해줬는데 이렇게 처리하는게 더 확실
            autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        }else{
            MainActivity.Map_foreground_selector_kakao= false;
        }
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                SEOUL = place.getLatLng();
                mapFragment.getMapAsync(instance);
            }
            @Override
            public void onError(Status status) {
                Log.e("Error", status.getStatusMessage());
            }
        });
    }

}
