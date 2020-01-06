package com.example.dmapper;

import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Lookup_detail extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * 서버에서 값 가져오면 DataContainer_init_variable() 함수 이용하면 돼
     * 저기에 매개변수 넣어놔서 서버에서 값 가져오면 저 함수통해서 변수들 초기화 되도록 하고
     * DataContainer_init_variable() 이걸로 싹다 set해버리면 돼
     */
    SupportMapFragment mapFragment;
    private GoogleMap gMap;
    CoordinatorLayout mRootLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    private LatLng Location_place;


    //placename은 텍스트뷰에 set해주는게 아니고 init_variable()함수에서 mCollapsingToolbarLayout.setTitle("My App Title"); 여기에 넣어주면 됌
    TextView category_tv, phone_num_tv, etc_tv,address_name_tv, address2_name_tv,register_user_name_tv, register_datetime_tv , register_kakao_tv, register_google_tv;
    ImageView place_iv, entrance_iv, elevator_iv, parking_iv, toilet_iv, seat_iv;

    String place_url;
    String place_name; //장소명
    String category;// 카테고리
    String phone_num;
    String address_name;
    String etc;// 부가정보
    boolean entrance , elevator, parking, toilet, seat; //서버로부터 입구 엘리베이터 등등의 유무가 true(자료형은 자유인데 보내줄때 체크박스로 해서)라면 그에 맞게 setImageDrawable해주면 됌.
                                                        //if문에서 조건문으로 쓰라고 만든 변수임.
    String register_user_name;
    String register_datetime;
    boolean kakao, google;
    public double latitude,longitude; // 아이템 클릭 이후 해당 아이템에 대한 위도 경도가 들어갈 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_detail);

        init_variable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataContainer_init_variable();
        DataContainer_Bind_View();
    }

    public void init_variable(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        latitude = 37.320511; // 일단 하드코딩으로 값 넣었는데 서버로부터 받아온 위도 경도를 넣어주어야함.
        longitude = 127.125740;

        category_tv = (TextView)findViewById(R.id.detail_category);
        phone_num_tv = (TextView)findViewById(R.id.detail_phone_num);
        etc_tv = (TextView)findViewById(R.id.detail_etc);
        address_name_tv = (TextView)findViewById(R.id.detail_location);//위치 넣는 칸이 두개임 최대한 많아보이게 하려고
        address2_name_tv = (TextView)findViewById(R.id.detail_location2);
        register_user_name_tv = (TextView)findViewById(R.id.detail_register_user);
        register_datetime_tv = (TextView)findViewById(R.id.detail_register_datetime);
        register_kakao_tv = (TextView)findViewById(R.id.detail_register_kakaotalk);
        register_google_tv = (TextView)findViewById(R.id.detail_register_google);
        place_iv = (ImageView)findViewById(R.id.parallax_header_imageview);
        entrance_iv = (ImageView)findViewById(R.id.entrance_iv);
        elevator_iv = (ImageView)findViewById(R.id.elevator_iv);
        parking_iv = (ImageView)findViewById(R.id.parking_iv);
        toilet_iv = (ImageView)findViewById(R.id.toilet_iv);
        seat_iv = (ImageView)findViewById(R.id.seat_iv);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorRootLayout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayoutAndroidExample);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_view);
        Location_place = new LatLng(latitude, longitude);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location_place,17.0f));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Location_place);
        gMap.addMarker(markerOptions);
    }

    public void DataContainer_init_variable(){

        //서버로부터 받은 값을 이 변수에 넣으면 알아서 다
        place_name = "";
        category = "";
        phone_num = "";
        address_name = "";
        etc="";
        register_user_name = "";
        register_datetime = "";

        place_url = "https://www.travelopy.com/static/img/cover.jpg";
        entrance = false;
        elevator = false;
        parking = false;
        toilet = false;
        seat = false;

        kakao = false;
        google = false;

    }

    public void DataContainer_Bind_View(){
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.bb_darkBackgroundColor));//타이틀컬러
        mCollapsingToolbarLayout.setTitle("장소명"); //"장소명 대신 place_name 변수" 넣으면 돼
        Glide.with(this).load(place_url).thumbnail(0.1f).into(place_iv);
/*
        //phone number랑 etc는 부가입력이라서 유저가 입력 안했을 수도 있기 때문에 if else 만들어주자 입력 안했으면 그냥 하이픈 하나 뜨게
        category_tv.setText(category);
        phone_num_tv.setText(phone_num);
        address_name_tv.setText(address_name);
        etc_tv.setText(etc);
        register_user_name_tv.setText(register_user_name);
        register_datetime_tv.setText(register_datetime);


        if(entrance == true){
            entrance_iv.setImageDrawable();
        }
        if(elevator == true){
            entrance_iv.setImageDrawable();
        }
        if(parking == true){
            entrance_iv.setImageDrawable();
        }
        if(toilet == true){
            entrance_iv.setImageDrawable();
        }
        if(seat == true){
            entrance_iv.setImageDrawable();
        }
        if(kakao == true){
            register_kakao_tv.setText("카카오톡"); //텍스트로 해도되고 이쁜 이미지로 바꿔도돼
        }
        if(kakao == true){
            register_kakao_tv.setText("구글");
        }
        */
    }

}
