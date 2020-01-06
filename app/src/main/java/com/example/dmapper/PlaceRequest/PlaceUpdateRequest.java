package com.example.dmapper.PlaceRequest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.dmapper.BottomBarFragment.googlemaptab;
import com.example.dmapper.BottomBarFragment.kakaomaptab;
import com.example.dmapper.ImageViewPager.ImageViewPagerAdapter;
import com.example.dmapper.MainActivity;
import com.example.dmapper.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceUpdateRequest extends AppCompatActivity {

    Button submit_btn;
    ImageView Address_Reload_btn;

    TextInputLayout textInputLayout,textInputLayout2,textInputLayout3,textInputLayout4;
    TextInputEditText place_name_et,address_name_et,phonenumber_et,etcinfo_et;

    CheckBox kakao_map_check, google_map_check, entrance_check, seat_check, parking_check ,restroom_check, elevator_check;

    InputMethodManager imm;

    com.example.dmapper.BottomBarFragment.kakaomaptab kakaomaptab;
    com.example.dmapper.BottomBarFragment.googlemaptab googlemaptab;

    String place_name_st,address_name_st,category_name_st,phonenumber_st,etcinfo_st;
    Boolean kakao_bool,google_bool,entrance_bool,seat_bool,parking_bool,restroom_bool,elevator_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_update_request);

        init_variable();
        init_BindValue();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getValue();
        onClickBox();
    }

    public void init_variable(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(Html.fromHtml("<font color='#746E66'>"+"정보 수정"+"</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        submit_btn = (Button)findViewById(R.id.place_add_submit_btn);
        Address_Reload_btn = (ImageView) findViewById(R.id.reload_address_btn);

        textInputLayout = (TextInputLayout)findViewById(R.id.textinputlayout);
        place_name_et = (TextInputEditText)findViewById(R.id.placename_et);

        textInputLayout2 = (TextInputLayout)findViewById(R.id.textinputlayout2);
        address_name_et = (TextInputEditText)findViewById(R.id.addressname_et);

        textInputLayout3 = (TextInputLayout)findViewById(R.id.textinputlayout3);
        phonenumber_et = (TextInputEditText)findViewById(R.id.phonenumber_et);

        textInputLayout4 = (TextInputLayout)findViewById(R.id.textinputlayout4);
        etcinfo_et = (TextInputEditText)findViewById(R.id.etcinfo_et);

        kakao_map_check = (CheckBox)findViewById(R.id.kakao_map_check);
        google_map_check = (CheckBox)findViewById(R.id.google_map_check);
        entrance_check = (CheckBox)findViewById(R.id.wheel_Entrance_check);
        seat_check = (CheckBox)findViewById(R.id.wheel_seat_check);
        parking_check = (CheckBox)findViewById(R.id.wheel_parking_check);
        restroom_check = (CheckBox)findViewById(R.id.wheel_restroom_check);
        elevator_check = (CheckBox)findViewById(R.id.wheel_elevator_check);

        textInputLayout4.setCounterEnabled(true);
        textInputLayout4.setCounterMaxLength(50);

    }

    private void init_BindValue(){

        //Toast.makeText(getApplicationContext(), ""+MainActivity.Map_foreground_selector_kakao, Toast.LENGTH_SHORT).show();

            if (MainActivity.Map_foreground_selector_kakao == true || MainActivity.Map_foreground_selector_google == false) {
                if(kakaomaptab.place_name_query_result.equals("") && kakaomaptab.address_name_query_result.equals("")){
                    place_name_et.setText("검색하여 수정할 장소를 선택해주세요");
                    address_name_et.setText("검색하여 수정할 장소를 선택해주세요");
                }else {
                    place_name_et.setText(kakaomaptab.place_name_query_result);
                    if(getReload_Address_Value() == null) {
                        address_name_et.setText(kakaomaptab.address_name_query_result);
                    }else{
                        address_name_et.setText(getReload_Address_Value());
                    }
                }
            } else if (MainActivity.Map_foreground_selector_google == true || MainActivity.Map_foreground_selector_kakao == false) {
                if(kakaomaptab.place_name_query_result.equals("") && kakaomaptab.address_name_query_result.equals("")){
                    place_name_et.setText("검색하여 수정할 장소를 선택해주세요");
                    address_name_et.setText("검색하여 수정할 장소를 선택해주세요");
                }else {
                    place_name_et.setText(googlemaptab.place_name_query_result);
                    if(getReload_Address_Value() == null){
                        address_name_et.setText(googlemaptab.address_name_query_result);
                    }else{
                        address_name_et.setText(getReload_Address_Value());
                    }
                }
            }

        kakao_map_check.setChecked(false);
        google_map_check.setChecked(false);
        entrance_check.setChecked(false);
        seat_check.setChecked(false);
        parking_check.setChecked(false);
        restroom_check.setChecked(false);
        elevator_check.setChecked(false);
    }
    private void getValue(){

        place_name_st = place_name_et.getText().toString();
        address_name_st = address_name_et.getText().toString();

        phonenumber_st = phonenumber_et.getText().toString();
        etcinfo_st = etcinfo_et.getText().toString();

        if (kakao_map_check.isChecked()) kakao_bool = true;
        else kakao_bool = false;


        if (google_map_check.isChecked()) google_bool = true;
        else google_bool = false;

        if (entrance_check.isChecked()) entrance_bool = true;
        else entrance_bool = false;

        if (seat_check.isChecked()) seat_bool = true;
        else seat_bool = false;

        if (parking_check.isChecked()) parking_bool = true;
        else parking_bool = false;

        if (restroom_check.isChecked()) restroom_bool = true;
        else restroom_bool = false;

        if (elevator_check.isChecked()) elevator_bool = true;
        else elevator_bool = false;
    }

    private void onClickBox(){
        Address_Reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceUpdateRequest.this,GoogleAddressReSelect.class);
                startActivity(intent);
            }
        });
    }
    private String getReload_Address_Value(){
        String getAddress = getIntent().getStringExtra("Reload_Address_Value");
        return getAddress;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
