package com.example.dmapper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dmapper.BottomBarFragment.googlemaptab;
import com.example.dmapper.BottomBarFragment.kakaomaptab;
import com.example.dmapper.BottomBarFragment.lookupdatatab;
import com.example.dmapper.BottomBarFragment.settingtab;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;


public class MainActivity extends AppCompatActivity {

    private com.example.dmapper.BottomBarFragment.kakaomaptab kakaomaptab;
    private com.example.dmapper.BottomBarFragment.googlemaptab googlemaptab;
    private com.example.dmapper.BottomBarFragment.lookupdatatab lookupdatatab;
    private com.example.dmapper.BottomBarFragment.settingtab settingtab;
    BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        kakaomaptab = new kakaomaptab();
        googlemaptab= new googlemaptab();
        lookupdatatab= new lookupdatatab();
        settingtab = new settingtab();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //실행시 보여줄 탭
        initFragment();


        //클릭으로 탭 변경
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tabId){
                    case R.id.tab_kakaomap : transaction.replace(R.id.contentContainer, kakaomaptab).commit(); break;
                    case R.id.tab_googlemap : transaction.replace(R.id.contentContainer, googlemaptab).commit(); break;
                    case R.id.tab_LookupData : transaction.replace(R.id.contentContainer, lookupdatatab).commit(); break;
                    case R.id.tab_Setting : transaction.replace(R.id.contentContainer, settingtab).commit(); break;
                }
            }
        });

    }

    public void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, kakaomaptab);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
