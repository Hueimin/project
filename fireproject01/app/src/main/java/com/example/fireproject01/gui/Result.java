package com.example.fireproject01.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fireproject01.chart.line.LineFingerAngle;
import com.example.fireproject01.chart.line.LineFingerSpeed;
import com.example.fireproject01.R;
import com.example.fireproject01.chart.line.LineWristAngle;
import com.example.fireproject01.chart.line.LineWristSpeed;
import com.example.fireproject01.chart.line.SlidePagerAdapter;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    public final static String[] FINGER_NAME = {"Thumb", "Index Finger", "Middle Finger", "Ring Finger", "Little Finger"};
    public final static String[] WRIST_NAME = {"Pitch X", "Roll Y", "Yaw Z"};


    private ViewPager pager;
    private PagerAdapter fingerAngleAdapter, fingerSpeedAdapter, wristAngleAdapter, wristSpeedAdapter;
    private ArrayList<Fragment> fingerAngleList = new ArrayList<>(),
                                fingerSpeedList = new ArrayList<>(),
                                wristAngleList = new ArrayList<>(),
                                wristSpeedList = new ArrayList<>();

    private Button fingerAngleButton, fingerSpeedButton, wristAngleButton, wristSpeedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //fingerAngle
        fingerAngleAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fingerAngleList);

        for (int i = 0; i < 5; i++) {
            Bundle take = getIntent().getExtras(), bundle = new Bundle();
            bundle.putString("NAME", take.getString("NAME"));
            bundle.putInt("FINGER_NAME", i);
            Fragment fragment = new LineFingerAngle();
            fragment.setArguments(bundle);
            fingerAngleList.add(fragment);
        }

        //fingerSpeed
        fingerSpeedAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fingerSpeedList);

        for (int i = 0; i < 5; i++) {
            Bundle take = getIntent().getExtras(), bundle = new Bundle();
            bundle.putString("NAME", take.getString("NAME"));
            bundle.putInt("FINGER_NAME", i);
            Fragment fragment = new LineFingerSpeed();
            fragment.setArguments(bundle);
            fingerSpeedList.add(fragment);
        }

        //wristAngle
        wristAngleAdapter = new SlidePagerAdapter(getSupportFragmentManager(), wristAngleList);

        for (int i = 0; i < 3; i++) {
            Bundle take = getIntent().getExtras(), bundle = new Bundle();
            bundle.putString("NAME", take.getString("NAME"));
            bundle.putInt("WRIST_NAME", i);
            Fragment fragment = new LineWristAngle();
            fragment.setArguments(bundle);
            wristAngleList.add(fragment);
        }

        //wristSpeed
        wristSpeedAdapter = new SlidePagerAdapter(getSupportFragmentManager(), wristSpeedList);

        for (int i = 0; i < 3; i++) {
            Bundle take = getIntent().getExtras(), bundle = new Bundle();
            bundle.putString("NAME", take.getString("NAME"));
            bundle.putInt("WRIST_NAME", i);
            Fragment fragment = new LineWristSpeed();
            fragment.setArguments(bundle);
            wristSpeedList.add(fragment);
        }

        pager = findViewById(R.id.pager);
        pager.setAdapter(fingerAngleAdapter); //初始設定

        ////////////////////////////////////////////////////////////////
        /////////////////////////////Finger/////////////////////////////
        ////////////////////////////////////////////////////////////////
        //angle
        fingerAngleButton = findViewById(R.id.F_ANGLE);
        fingerAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setAdapter(fingerAngleAdapter);
            }
        });

        //speed
        fingerSpeedButton = findViewById(R.id.F_SPEED);
        fingerSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setAdapter(fingerSpeedAdapter);
            }
        });

        ////////////////////////////////////////////////////////////////
        //////////////////////////////Wrist/////////////////////////////
        ////////////////////////////////////////////////////////////////
        //angle
        wristAngleButton = findViewById(R.id.W_ANGLE);
        wristAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setAdapter(wristAngleAdapter);
            }
        });

        //speed
        wristSpeedButton = findViewById(R.id.W_SPEED);
        wristSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setAdapter(wristSpeedAdapter);
            }
        });
    }
}
