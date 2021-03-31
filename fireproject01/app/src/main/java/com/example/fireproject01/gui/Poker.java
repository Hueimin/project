package com.example.fireproject01.gui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fireproject01.chart.radar.FingerAngle;
import com.example.fireproject01.chart.radar.FingerSpeed;
import com.example.fireproject01.R;
import com.example.fireproject01.chart.radar.WristAngle;
import com.example.fireproject01.chart.radar.WristSpeed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class Poker extends AppCompatActivity {

    public static final String GAME_NAME = "Card Matching";

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private ArrayList<String> time = new ArrayList<>();

    private Bundle take;

    private Spinner spinner;

    //將Angle與Speed.java宣告至此
    private FingerAngle f_angle;
    private FingerSpeed f_speed;
    private WristAngle w_angle;
    private WristSpeed w_speed;

    DatabaseReference databaseAngle;

    Button f_angleButton;
    Button f_speedButton;
    Button w_angleButton;
    Button w_speedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poker);

        spinner = findViewById(R.id.spinner); //下拉式選單

        take = this.getIntent().getExtras(); //取得intent中的bundle物件

        //firebase
        databaseAngle = FirebaseDatabase.getInstance().getReference();

        //////new///////
        ////////////////////////////////////////////////////////////////////
        ///////////////////////使用Angle, Speed.java////////////////////////
        ////////////////////////////////////////////////////////////////////
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        f_angle = new FingerAngle();
        f_speed = new FingerSpeed();
        w_angle = new WristAngle();
        w_speed = new WristSpeed();

        //傳資料給Angle,Speed.java
        Bundle bundle = new Bundle();
        bundle.putString("NAME", take.getString("NAME"));
        f_angle.setArguments(bundle);
        f_speed.setArguments(bundle);
        w_angle.setArguments(bundle);
        w_speed.setArguments(bundle);

        fragmentTransaction.add(R.id.Frame, f_angle, "Angle");
        fragmentTransaction.add(R.id.Frame, f_speed, "Speed");
        fragmentTransaction.add(R.id.Frame, w_angle, "Angle");
        fragmentTransaction.add(R.id.Frame, w_speed, "Speed");

        fragmentTransaction.hide(f_speed);
        fragmentTransaction.hide(w_angle);
        fragmentTransaction.hide(w_speed);
        fragmentTransaction.commit();

        f_angleButton = (Button)findViewById(R.id.F_ANGLE);
        f_speedButton = (Button)findViewById(R.id.F_SPEED);
        w_angleButton = (Button)findViewById(R.id.W_ANGLE);
        w_speedButton = (Button)findViewById(R.id.W_SPEED);

        f_angleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(f_angle);
                fragmentTransaction.hide(f_speed);
                fragmentTransaction.hide(w_angle);
                fragmentTransaction.hide(w_speed);
                fragmentTransaction.commit();
            }
        });

        f_speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(f_speed);
                fragmentTransaction.hide(f_angle);
                fragmentTransaction.hide(w_angle);
                fragmentTransaction.hide(w_speed);
                fragmentTransaction.commit();
            }
        });

        w_angleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(w_angle);
                fragmentTransaction.hide(w_speed);
                fragmentTransaction.hide(f_angle);
                fragmentTransaction.hide(f_speed);
                fragmentTransaction.commit();
            }
        });

        w_speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(w_speed);
                fragmentTransaction.hide(w_angle);
                fragmentTransaction.hide(f_angle);
                fragmentTransaction.hide(f_speed);
                fragmentTransaction.commit();
            }
        });
        ///////finish///////

        ////////////////////////////////////////////////////////////////////
        /////////////////////////下拉式選單(spinner)/////////////////////////
        ////////////////////////////////////////////////////////////////////
        gameAddress = "data/" + take.getString("NAME") + "/" + GAME_NAME;

        //控制下拉選單的選項
        databaseAngle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //存時間到iter(迭代)
                Iterator<DataSnapshot> iter = dataSnapshot.child(gameAddress).getChildren().iterator();
                //再把時間從iter(迭代)存入time(矩陣)中
                while(iter.hasNext()) {
                    DataSnapshot data = iter.next(); //快照
                    time.add(data.getKey()); //時間
                }

                String[] choice = new String[time.size()]; //將choice的大小設的和time一樣
                for(int i = 0; i < time.size(); i++) {
                    choice[i] = DateFormat.format("yyyy/MM/dd 'at' HH:mm:ss", new Date(Long.parseLong(time.get(i)))).toString();
                }

                //放入timeList中，並由spinner顯示
                ArrayAdapter timeList = new ArrayAdapter<>(Poker.this, android.R.layout.simple_spinner_dropdown_item, choice);
                spinner.setAdapter(timeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //不可刪

            }
        });

        //下拉式選單選項觸發事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                f_angle.setFingerValue("Card Matching", time.get(position));
                f_speed.setFingerValue("Card Matching", time.get(position));
                w_angle.setWristValue("Card Matching", time.get(position));
                w_speed.setWristValue("Card Matching", time.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

