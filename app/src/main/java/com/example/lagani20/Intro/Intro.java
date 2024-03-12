package com.example.lagani20.Intro;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.lagani20.R;
import com.example.lagani20.RegisterLogin.MainActivity;
import com.example.lagani20.RegisterLogin.Register;

import java.util.ArrayList;
import java.util.List;

public class Intro extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if(restorePrefdata()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                savePrefData();
                finish();
            }
        });

        ArrayList<SlideModel> imageList = new ArrayList<>();

        SlideModel slideModel = new SlideModel(R.drawable.intro1, ScaleTypes.CENTER_CROP);
        imageList.add(slideModel);
        SlideModel slideMode1l = new SlideModel(R.drawable.intro2, ScaleTypes.CENTER_CROP);
        imageList.add(slideMode1l);
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);
    }

    private boolean restorePrefdata() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isactivityperformed = pref.getBoolean("isIntroOpend",false);
        return isactivityperformed;
    }

    private void savePrefData()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpend",true);
        editor.commit();
    }
}
