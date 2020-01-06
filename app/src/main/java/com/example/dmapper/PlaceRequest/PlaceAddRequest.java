package com.example.dmapper.PlaceRequest;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dmapper.BottomBarFragment.googlemaptab;
import com.example.dmapper.BottomBarFragment.kakaomaptab;
import com.example.dmapper.ImageViewPager.ImageViewPager;
import com.example.dmapper.ImageViewPager.ImageViewPagerAdapter;
import com.example.dmapper.MainActivity;
import com.example.dmapper.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlaceAddRequest extends AppCompatActivity{

    public static int imageitemposition = 0;
    public List<String> imagePathList;
    public ImageViewPagerAdapter fragmentAdapter;

    final int PICK_IMAGE_MULTIPLE = 100;
    String imagePath;
    ArrayList<Uri> mArrayUri;


    Button submit_btn;

    TextInputLayout textInputLayout,textInputLayout2,textInputLayout3,textInputLayout4;
    TextInputEditText place_name_et,address_name_et,phonenumber_et,etcinfo_et;

    CheckBox kakao_map_check, google_map_check, entrance_check, seat_check, parking_check ,restroom_check, elevator_check;

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    ViewPager viewPager;
    ImageView image_upload_btn;

    InputMethodManager imm;

    kakaomaptab kakaomaptab;
    googlemaptab googlemaptab;

    String place_name_st,address_name_st,category_name_st,phonenumber_st,etcinfo_st;
    Boolean kakao_bool,google_bool,entrance_bool,seat_bool,parking_bool,restroom_bool,elevator_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_place_add_request);

        init_variable();
        init_spinner_list();
        init_BindValue();

        image_upload_btn = findViewById(R.id.image_upload_button);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getValue();

        etcinfo_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 50){
                    textInputLayout4.setErrorEnabled(true);
                    textInputLayout4.setError("최대 허용 길이를 초과하였습니다");
                }else{
                    textInputLayout4.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        image_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTakeMultiAlbumAction();
            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("@@","pl"+place_name_st+"ad"+address_name_st+"ca"+category_name_st+"et"+etcinfo_st+
                        "ka"+kakao_bool+"go"+google_bool+"en"+entrance_bool+"se"+seat_bool+"pa"+parking_bool+"re"+restroom_bool+"el"+elevator_bool);

            }
        });

    }
    public void init_focus(){
        address_name_et.clearFocus();
        place_name_et.clearFocus();
        imm.hideSoftInputFromWindow(place_name_et.getWindowToken(),0);
        imm.hideSoftInputFromWindow(address_name_et.getWindowToken(),0);
    }

    public void init_spinner_list(){
        arrayList = new ArrayList<>();
        arrayList.add("장애인용 화장실");
        arrayList.add("주차장");
        arrayList.add("숙박업소");
        arrayList.add("숙박업소");
        arrayList.add("숙박업소");
        arrayList.add("숙박업소");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner = (Spinner)findViewById(R.id.category_spinner);
        spinner.setAdapter(arrayAdapter);
    }

    public void init_variable(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(Html.fromHtml("<font color='#746E66'>"+"누락된 장소 추가"+"</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        submit_btn = (Button)findViewById(R.id.place_add_submit_btn);

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

        if(MainActivity.Map_foreground_selector_kakao == true || MainActivity.Map_foreground_selector_google == false){
            address_name_et.setText(kakaomaptab.ReverseGeoCodeValue);
        }else if(MainActivity.Map_foreground_selector_google == true || MainActivity.Map_foreground_selector_kakao == false){
            address_name_et.setText(googlemaptab.ReverseGeoCodeValue);
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                category_name_st = arrayList.get(i);
                init_focus();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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

    public void doTakeMultiAlbumAction(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK  && data != null){


            imagePathList = new ArrayList<>();
            if(data.getClipData() != null){
                ClipData mClipData = data.getClipData();
                mArrayUri = new ArrayList<Uri>();
                for (int i=0; i<mClipData.getItemCount(); i++){
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    //getImageUri(imageUri);
                    getImageFilePath(imageUri);//2. 절대경로 구함 쓸데가 있겠지?
                }
            }
            else if(data.getData() != null){
                Uri imgUri = data.getData();
                getImageFilePath(imgUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
            cursor.close();

            viewPager = (ViewPager) findViewById(R.id.image_viewpager);
            fragmentAdapter = new ImageViewPagerAdapter(getSupportFragmentManager());
            // ViewPager와  FragmentAdapter 연결
            viewPager.setAdapter(fragmentAdapter);

            viewPager.setClipToPadding(false);
            int dpValue = 16;
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);
            viewPager.setPadding(margin, 0, margin, 0);
            viewPager.setPageMargin(margin / 2);

            Log.i("image 가져옴",""+imagePathList);
            // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
            for (int i = 0; i < imagePathList.size(); i++) {
                ImageViewPager imageFragment = new ImageViewPager();
                Bundle bundle = new Bundle();
                bundle.putString("imgRes", imagePathList.get(i));
                imageFragment.setArguments(bundle);
                fragmentAdapter.addItem(imageFragment);
            }
            fragmentAdapter.notifyDataSetChanged();
        }
        getItemposition(viewPager);

    }

    public void getItemposition(final ViewPager viewPager){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                imageitemposition = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
