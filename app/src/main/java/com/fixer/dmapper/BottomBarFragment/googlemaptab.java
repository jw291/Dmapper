package com.fixer.dmapper.BottomBarFragment;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fixer.dmapper.GetGpsCoordinates.GetGpsCoordinates;
import com.fixer.dmapper.MainActivity;
import com.fixer.dmapper.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class googlemaptab extends Fragment implements OnMapReadyCallback {
    GetGpsCoordinates getGpsCoordinates;
    PlaceAutocompleteFragment autocompleteFragment;
    MapView mapview;
    ImageView mylocationbutton;
    Button kakaobutton;
    public GoogleMap gMap;
    public LatLng SEOUL;
    private LatLng CenterLocation = new LatLng(0,0);
    private Marker markerCenter;
    public static View view;

    public static String ReverseGeoCodeValue;//핀 위치에서 얻어낸 addressname

    public static String place_name_query_result="";//결과를 통해 얻어낸 Place_name
    public static String address_name_query_result="";//결과를 통해 얻어낸 addressname

    public static googlemaptab newInstance() {
        return new googlemaptab();
    }

    public googlemaptab() {
        // Required empty public constructor
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * registerFragment 내에 kakaoFragment googleFragment 즉, 이중Fragment이다 보니까
         * 생명주기 관리, inflate 관리가 매우 까다로움
         * 특히 카카오tab을 Init으로 하고 구글tab은 버튼클릭으로 이동해야 할때는
         * PlaceAutocompleteFragment 검색 이후 destroy돼서 kakaomap으로 이동해버림 그래서 그냥 googlemap을 init으로함
         * 이때도 문제가 굉장히 많이 발생했으나 밑에로 해결 하지만 완전 해결은 아니다
         * kakaotab을 다녀왔을때 검색이 한번에 되지 않는 문제가 있다(다른 fragment다녀올땐 한번에 됌)
         * 두번 해야 검색이 되지만 critical하지는 않다. 그러므로 일단 다른 기능부터 구현하자
         */
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.fragment_googlemaptab, container, false);
        } catch (InflateException e) {

        }
        
        init_bindView();
        setupAutoCompleteFragment(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        kakaobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭하면 카카오maptab으로 이동하겠다.
                MainActivity.Map_foreground_selector_google = false;
                ((MainActivity) getActivity()).replaceFragment(kakaomaptab.newInstance());
            }
        });
        mylocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });
    }

    public void init_bindView(){
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.hide();

        ((MainActivity) getActivity()).StartAnimationvisible();

        getGpsCoordinates = new GetGpsCoordinates(getContext());

        MainActivity.Map_foreground_selector_google = true;

        SEOUL = new LatLng(getGpsCoordinates.getLatitude(), getGpsCoordinates.getLongitude());
        mylocationbutton = (ImageView) view.findViewById(R.id.mylocationbutton);
        kakaobutton = (Button) view.findViewById(R.id.kakaobutton);
        mapview = (MapView) view.findViewById(R.id.google_map_view);
        mapview.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapview.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mapview != null) {
            mapview.onCreate(savedInstanceState);
        }

    }


    //검색 시에 호출되는 method
    private void setupAutoCompleteFragment(final OnMapReadyCallback instance) {
        if(autocompleteFragment == null) {
            //원래 destroy에서 remove해줬는데 이렇게 처리하는게 더 확실
            autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        }else{
            MainActivity.Map_foreground_selector_kakao= false;
        }
        autocompleteFragment.setHint("장소를 입력하세요");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                SEOUL = place.getLatLng();
                place_name_query_result = place.getName().toString();
                address_name_query_result = place.getAddress().toString();
                mapview.getMapAsync(instance);
            }

            @Override
            public void onError(Status status) {
                Log.e("Error", status.getStatusMessage());
            }
        });
    }

    //getMapAsync호출시 호출되는 method
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        Log.i("omMapReady호출","onMapReady호출"+SEOUL);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL,20.0f));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(17));
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
    //mylocation으로 이동하는 method
    private void getMyLocation() {
        SEOUL = new LatLng(getGpsCoordinates.getLatitude(), getGpsCoordinates.getLongitude());
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL,20));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    private void getCenterLocation(){
        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                ((MainActivity)getActivity()).StartAnimationUp();
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
                ((MainActivity)getActivity()).StartAnimationInvisible();
                markerCenter.setPosition(gMap.getCameraPosition().target);
                Log.i("position","latitude"+markerCenter.getPosition().latitude+" longitude"+markerCenter.getPosition().longitude);
                //핀을 가운데에 놓고 핀의 위치를 이용해서 위도 경도 뽑아옴 카카오맵이랑 다름.
            }
        });
    }

    public void getAddressname(double latitude, double longitude){
        Geocoder gc = new Geocoder(getContext());

        if(gc.isPresent()){
            try {
                List<Address> list = gc.getFromLocation(latitude,longitude,10);

                ReverseGeoCodeValue = list.get(0).getAddressLine(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //PlaceAutocompleteFragment가 누적되는 오류 binary xml inflate exception 해결을 위해서 화면 나가면 제거함.
    /*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getContext(), "googledestroy", Toast.LENGTH_SHORT).show();
        if(autocompleteFragment!= null){
            getActivity().getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
        }
    }
*/
}
