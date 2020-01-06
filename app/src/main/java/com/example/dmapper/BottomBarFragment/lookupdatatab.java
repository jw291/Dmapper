package com.example.dmapper.BottomBarFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmapper.Lookup_detail;
import com.example.dmapper.MainActivity;
import com.example.dmapper.R;

import java.util.ArrayList;

class LookupDataProducts {
    private String place_name;
    private String place_address;
    private int image;

    private double x;//경도
    private double y;//위도

    public LookupDataProducts(String place_name, String place_address, double x , double y) {
        this.place_name = place_name;
        this.place_address = place_address;
        this.x = x;
        this.y = y;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_address() {
        return place_address;
    }
    public double getx(){
        return x;
    }
    public double gety(){
        return y;
    }

}
class LookUpDataViewHolder extends RecyclerView.ViewHolder {

    public TextView place_nameTV;
    public TextView address_nameTV;
    public LookUpDataViewHolder(View itemView) {
        super(itemView);
        place_nameTV = (TextView) itemView.findViewById(R.id.place_name);
        address_nameTV = (TextView) itemView.findViewById(R.id.address_name);
    }
}
class LookupDataAdapter extends RecyclerView.Adapter<LookUpDataViewHolder>{

    private static final String TAG = "RecyclerAdapter";
    ArrayList<LookupDataProducts> place_list;
    ArrayList<LookupDataProducts> place_list_All;
    LookUpDataViewHolder holder;
    Context context;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    public void setArrayList(ArrayList<LookupDataProducts> place_list,Context context) {
        this.place_list = place_list;
        this.place_list_All = new ArrayList<>(place_list);
        this.context = context;
    }

    @NonNull
    @Override
    public LookUpDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lookup_list_item, parent, false);
        holder = new LookUpDataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LookUpDataViewHolder holder, int i) {
        LookupDataProducts data = place_list.get(holder.getAdapterPosition());
        holder.place_nameTV.setText(data.getPlace_name());
        holder.address_nameTV.setText(data.getPlace_address());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int itemposition = holder.getAdapterPosition();
            @Override
            public void onClick(View view) {
                if(itemposition != RecyclerView.NO_POSITION){
                    if(mListener != null){
                        mListener.onItemClick(view,itemposition);
                        Log.i("클릭!","클릭");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return place_list.size();
    }

    public void filter(String text){

        if(text.isEmpty()){
            place_list.clear();
            place_list.addAll(place_list_All);
        } else{
            ArrayList<LookupDataProducts> result = new ArrayList<>();
            text = text.toLowerCase();
            for(LookupDataProducts item: place_list_All){
                //match by name or phone
                if(item.getPlace_name().toLowerCase().contains(text)||item.getPlace_address().toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            place_list.clear();
            place_list.addAll(result);
        }
        notifyDataSetChanged();
    }

}
public class lookupdatatab extends Fragment {
    View view;
    RecyclerView recyclerView;
    LookupDataAdapter lookupDataAdapter;
    ArrayList<LookupDataProducts> arrayList;

    public lookupdatatab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lookupdatatab, container, false);

        init_variable(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onListItemButtonClick();
    }

    private  void init_variable(View view){
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"+"조회"+"</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3FA7FF")));
        setHasOptionsMenu(true);


        ((MainActivity)getActivity()).StartAnimationgone();

        arrayList = new ArrayList<>();
        arrayList.add(new LookupDataProducts("장소명1","주소명1",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명2","주소명2",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명3","주소명3",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명4","주소명4",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명5","주소명5",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명6","주소명6",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명7","주소명7",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명8","주소명8",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명9","주소명9",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명10","주소명10",0.1,0.1));
        arrayList.add(new LookupDataProducts("장소명111","주소명11",0.1,0.1));
        recyclerView = (RecyclerView) view.findViewById(R.id.place_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lookupDataAdapter = new LookupDataAdapter();
        lookupDataAdapter.setArrayList(arrayList,getContext());
        recyclerView.setAdapter(lookupDataAdapter);
        lookupDataAdapter.notifyDataSetChanged();

    }

    public void onListItemButtonClick(){
        lookupDataAdapter.setOnItemClickListener(new LookupDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(),Lookup_detail.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "장소명을 입력하세요" + "</font>"));
        ImageView searchClose = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(Color.argb(255, 255, 255, 255));
        searchClose.setAlpha(255);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                lookupDataAdapter.filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.category1:
                lookupDataAdapter.filter("");
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category2:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category3:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category4:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category5:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category6:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category7:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category8:
                lookupDataAdapter.filter(item.getTitle().toString());
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.category9:
                lookupDataAdapter.filter("병원");
                Toast.makeText(getActivity(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}