package com.example.dmapper.BottomBarFragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dmapper.MainActivity;
import com.example.dmapper.R;

import org.w3c.dom.Text;

public class settingtab extends Fragment {
    ConstraintLayout notice_seemorebtn;
    ConstraintLayout myrequest_seemorebtn;
    ConstraintLayout ranking_seemorebtn;

    RecyclerView recy_notice;
    RecyclerView recy_myrequest;
    RecyclerView recy_ranking;

    TextView notice_tv;
    TextView myrequest_tv;
    TextView ranking_tv;

    ImageView notice_iv;
    ImageView myrequest_iv;
    ImageView ranking_iv;

    Boolean notice_Click_state = false;
    Boolean myrequest_Click_state = false;
    Boolean ranking_Click_state = false;
    View view;
    public settingtab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settingtab, container, false);
        init_variable();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).StartAnimationInvisible();

        notice_seemorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notice_Click_state == false){
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1500);
                    recy_notice.setLayoutParams(lp);
                    notice_tv.setText("접기");
                    notice_iv.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    notice_Click_state = true;
                }else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,500);
                    recy_notice.setLayoutParams(lp);
                    notice_tv.setText("더 보기");
                    notice_iv.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    notice_Click_state = false;
                }
            }
        });

        myrequest_seemorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myrequest_Click_state == false){
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1500);
                    recy_myrequest.setLayoutParams(lp);
                    myrequest_tv.setText("접기");
                    myrequest_iv.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    myrequest_Click_state = true;
                }else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,500);
                    recy_myrequest.setLayoutParams(lp);
                    myrequest_tv.setText("더 보기");
                    myrequest_iv.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    myrequest_Click_state = false;
                }
            }
        });

        ranking_seemorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ranking_Click_state == false){
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1500);
                    recy_ranking.setLayoutParams(lp);
                    ranking_tv.setText("접기");
                    ranking_iv.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    ranking_Click_state = true;
                }else{
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,500);
                    recy_ranking.setLayoutParams(lp);
                    ranking_tv.setText("더 보기");
                    ranking_iv.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    ranking_Click_state = false;
                }
            }
        });

    }

    public void init_variable(){

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"+"설정"+"</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3FA7FF")));

        ((MainActivity)getActivity()).StartAnimationgone();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        notice_seemorebtn = (ConstraintLayout)view.findViewById(R.id.seemore_notice);
        myrequest_seemorebtn = (ConstraintLayout)view.findViewById(R.id.seemore_myrequest);
        ranking_seemorebtn = (ConstraintLayout)view.findViewById(R.id.seemore_ranking);

        notice_tv = (TextView)view.findViewById(R.id.notice_tv);
        myrequest_tv = (TextView)view.findViewById(R.id.myrequest_tv);
        ranking_tv = (TextView)view.findViewById(R.id.ranking_tv);

        notice_iv = (ImageView)view.findViewById(R.id.notice_imageView);
        myrequest_iv = (ImageView)view.findViewById(R.id.myrequest_imageView);
        ranking_iv = (ImageView)view.findViewById(R.id.ranking_imageView);

        recy_notice = (RecyclerView)view.findViewById(R.id.recy_notice);
        recy_myrequest = (RecyclerView)view.findViewById(R.id.recy_myrequest);
        recy_ranking = (RecyclerView)view.findViewById(R.id.recy_ranking);
    }
}
