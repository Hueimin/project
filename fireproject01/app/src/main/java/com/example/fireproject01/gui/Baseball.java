package com.example.fireproject01.gui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fireproject01.Delete;
import com.example.fireproject01.chart.radar.FingerAngle;
import com.example.fireproject01.chart.radar.FingerSpeed;
import com.example.fireproject01.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class Baseball extends AppCompatActivity {

    public static final String GAME_NAME = "Baseball";

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private ArrayList<String> time = new ArrayList<>();

    private Bundle take;

    private Spinner spinner;

    //將Angle與Speed.java宣告至此
    private FingerAngle angle;
    private FingerSpeed speed;

    DatabaseReference databaseAngle;

    Button angleButton;
    Button speedButton;

    Button delete;
    String chooseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseball);

        spinner = findViewById(R.id.spinner);

        take = this.getIntent().getExtras();

        //firebase
        databaseAngle = FirebaseDatabase.getInstance().getReference();

        //////new///////
        ////////////////////////////////////////////////////////////////////
        ///////////////////////使用Angle, Speed.java////////////////////////
        ////////////////////////////////////////////////////////////////////
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        angle = new FingerAngle();
        speed = new FingerSpeed();

        //傳資料給Angle,Speed.java
        Bundle bundle = new Bundle();
        bundle.putString("NAME", take.getString("NAME"));
        angle.setArguments(bundle);
        speed.setArguments(bundle);

        fragmentTransaction.add(R.id.Frame, angle, "Angle");
        fragmentTransaction.add(R.id.Frame, speed, "Speed");
        fragmentTransaction.hide(speed);
        fragmentTransaction.commit();

        angleButton = (Button)findViewById(R.id.ANGLE);
        speedButton = (Button)findViewById(R.id.SPEED);

        angleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(angle);
                fragmentTransaction.hide(speed);
                fragmentTransaction.commit();
            }
        });

        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.show(speed);
                fragmentTransaction.hide(angle);
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
                    choice[i] = DateFormat.format("yyyy/MM/dd 'at' HH:mm:ss", Long.parseLong(time.get(i))).toString();
                }

                //放入timeList中，並由spinner顯示
                ArrayAdapter timeList = new ArrayAdapter<>(Baseball.this, android.R.layout.simple_spinner_dropdown_item, choice);
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
                angle.setFingerValue("Baseball", time.get(position));
                speed.setFingerValue("Baseball", time.get(position));
                chooseTime = time.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        delete = (Button)findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(Baseball.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("OK",null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Delete.delete(take.getString("NAME"), GAME_NAME, chooseTime);
                        time.remove(chooseTime);

                        recreate();

                        String deleteTime = DateFormat.format("yyyy/MM/dd HH:mm:ss ' is already removed'", Long.parseLong(chooseTime)).toString();
                        Toast.makeText(Baseball.this, deleteTime, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
