package com.example.dmapper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dmapper.BottomBarFragment.googlemaptab;
import com.example.dmapper.BottomBarFragment.kakaomaptab;
import com.example.dmapper.BottomBarFragment.lookupdatatab;
import com.example.dmapper.BottomBarFragment.settingtab;
import com.example.dmapper.PlaceRequest.PlaceAddRequest;
import com.example.dmapper.PlaceRequest.PlaceUpdateRequest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    public static Boolean Map_foreground_selector_kakao = false;
    public static Boolean Map_foreground_selector_google = true;

    private com.example.dmapper.BottomBarFragment.googlemaptab googlemaptab;
    private com.example.dmapper.BottomBarFragment.kakaomaptab kakaomaptab;
    private com.example.dmapper.BottomBarFragment.lookupdatatab lookupdatatab;
    private com.example.dmapper.BottomBarFragment.settingtab settingtab;
    Animation translate_up;
    Animation translate_down;
    LinearLayout linearLayout;
    BottomBar bottomBar;
    private Button placeaddbtn;
    private Button placeupdatebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        googlemaptab = new googlemaptab();
        kakaomaptab = new kakaomaptab();
        lookupdatatab= new lookupdatatab();
        settingtab = new settingtab();

        placeaddbtn = (Button)findViewById(R.id.placeadd);
        placeupdatebtn = (Button)findViewById(R.id.placeupdate);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        translate_up = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_up);
        translate_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_down);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //initFragment();

        //정보추가 버튼 클릭
        placeaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlaceAddRequest.class);
                startActivity(intent);
            }
        });
        //정보수정 버튼 클릭
        placeupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlaceUpdateRequest.class);
                startActivity(intent);
            }
        });
        //클릭으로 탭 변경
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId){
                    case R.id.tab_registermap :
                        if (MainActivity.Map_foreground_selector_kakao == true || MainActivity.Map_foreground_selector_google == false) {
                            replaceFragment(kakaomaptab); break;
                        }else if (MainActivity.Map_foreground_selector_google == true || MainActivity.Map_foreground_selector_kakao == false) {
                            replaceFragment(googlemaptab); break;
                        }
                    case R.id.tab_LookupData : replaceFragment(lookupdatatab); break;
                    case R.id.tab_Setting : replaceFragment(settingtab); break;
                }
            }
        });
    }
    public void replaceFragment(Fragment newfragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer,newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void StartAnimationDown(){

        linearLayout.setVisibility(View.INVISIBLE);
        linearLayout.startAnimation(translate_down);
    }
    public void StartAnimationInvisible(){
        linearLayout.setVisibility(View.INVISIBLE);
    }

    public void StartAnimationvisible(){
        linearLayout.setVisibility(View.VISIBLE);
    }

    public void StartAnimationgone(){
        linearLayout.setVisibility(View.GONE);
    }
    public void StartAnimationUp(){
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(translate_up);
    }

/*
    //fragment중 첫 화면은 kakaomaptab으로 보여주겠다.
    public void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //MainActivity가 실행될때마다 kakaomaptab fragment를 add하면 충돌이 생기므로
        //add되지 않았을경우(첫 실행)만 add하고 아니면(두 번 이상) return
        if(kakaomaptab.isAdded()){
            return;
        }else {
            transaction.add(R.id.contentContainer, googlemaptab);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
*/
    //kakaomaptab안에서 googlemaptab으로 또 그 반대로 이동하기 위해서 사용하는 메서드


}
