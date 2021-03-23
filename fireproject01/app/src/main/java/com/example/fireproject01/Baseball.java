package com.example.fireproject01;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class Baseball extends AppCompatActivity {

    //we define min and max for the values on our Radar Chart
    public static final float MAX = 180, MIN = 0;
    public static final int NB_QUALITIES = 5; //nb qualities on our Radar Chart

    public static final String GAME_NAME = "Baseball";

    private Bundle take;

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private Spinner spinner;

    private RadarChart anglechart;
    private RadarChart speedchart;

    private ArrayList<String> time = new ArrayList<>();

    DatabaseReference databaseAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseball);

        spinner = findViewById(R.id.spinner);

        take = this.getIntent().getExtras();

        //firebase
        databaseAngle = FirebaseDatabase.getInstance().getReference();

        ////////////////////////////////////////////////////////////////////
        /////////////////////////////angleChart/////////////////////////////
        ////////////////////////////////////////////////////////////////////
        anglechart = findViewById(R.id.angleChart);

        //we configure the radar chart
        anglechart.setBackgroundColor(Color.WHITE);
        anglechart.getDescription().setEnabled(false);
        anglechart.setWebLineWidth(1f); //設置直徑方向上那條線的寬度 //好像沒用

        //useful to export your graph
        anglechart.setWebColor(Color.BLACK); //設置直徑線的顏色(5條線)
        anglechart.setWebLineWidth(1.5f); //設置直徑方向上那條線的寬度(5條線)
        anglechart.setWebColorInner(Color.BLACK); //設置圈線的顏色
        anglechart.setWebLineWidthInner(1.5f);
        anglechart.setWebAlpha(100); //設置顏色的透明度

        //animate the chart //圖表數據顯示動畫
        anglechart.animateXY(1400, 1400, Easing.EasingOption.EaseInQuad, Easing.EasingOption.EaseInQuad);

        //x axis
        XAxis xaAxis = anglechart.getXAxis();
        xaAxis.setTextSize(12f);
        xaAxis.setTypeface(Typeface.DEFAULT_BOLD); //五角的文字
        xaAxis.setYOffset(0); //?
        xaAxis.setXOffset(0); //?
        xaAxis.setValueFormatter(new IAxisValueFormatter() {
            //we will compare two employees in a radar chart
            //so we define qualities to compare
            private String[] qualities = new String[] {"大拇指", "食指", "中指", "無名指", "小拇指"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return qualities[(int)value % qualities.length];
            }
        });

        xaAxis.setTextColor(Color.BLACK);

        //Y axis
        YAxis yaAsix = anglechart.getYAxis();
        yaAsix.setLabelCount(NB_QUALITIES, true);
        yaAsix.setTextSize(9f);
        yaAsix.setAxisMinimum(MIN);
        yaAsix.setAxisMaximum(MAX); //we define min and max for axis
        yaAsix.setDrawLabels(false);

        //we configure legend for our radar chart //圖例
        Legend la = anglechart.getLegend();
        la.setTextSize(15f);
        la.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        la.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        la.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        la.setDrawInside(false);
        la.setXEntrySpace(7f);
        la.setYEntrySpace(5f);
        la.setTextColor(Color.BLACK);


        ////////////////////////////////////////////////////////////////////
        /////////////////////////////speedChart/////////////////////////////
        ////////////////////////////////////////////////////////////////////
        speedchart = findViewById(R.id.speedChart);

        //we configure the radar chart
        speedchart.setBackgroundColor(Color.WHITE);
        speedchart.getDescription().setEnabled(false);
        speedchart.setWebLineWidth(1f); //設置直徑方向上那條線的寬度 //好像沒用

        //useful to export your graph
        speedchart.setWebColor(Color.BLACK); //設置直徑線的顏色(5條線)
        speedchart.setWebLineWidth(1.5f); //設置直徑方向上那條線的寬度(5條線)
        speedchart.setWebColorInner(Color.BLACK); //設置圈線的顏色
        speedchart.setWebLineWidthInner(1.5f);
        speedchart.setWebAlpha(100); //設置顏色的透明度

        //animate the chart //圖表數據顯示動畫
        speedchart.animateXY(1400, 1400, Easing.EasingOption.EaseInQuad, Easing.EasingOption.EaseInQuad);

        //x axis
        XAxis xsAxis = speedchart.getXAxis();
        xsAxis.setTextSize(12f);
        xsAxis.setTypeface(Typeface.DEFAULT_BOLD); //五角的文字
        xsAxis.setYOffset(0); //?
        xsAxis.setXOffset(0); //?
        xsAxis.setValueFormatter(new IAxisValueFormatter() {
            //we will compare two employees in a radar chart
            //so we define qualities to compare
            private String[] qualities = new String[] {"大拇指", "食指", "中指", "無名指", "小拇指"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return qualities[(int)value % qualities.length];
            }
        });

        xsAxis.setTextColor(Color.BLACK);

        //Y axis
        YAxis ysAsix = speedchart.getYAxis();
        ysAsix.setLabelCount(NB_QUALITIES, true);
        ysAsix.setTextSize(9f);
        ysAsix.setAxisMinimum(MIN);
        ysAsix.setAxisMaximum(MAX); //we define min and max for axis
        ysAsix.setDrawLabels(false);

        //we configure legend for our radar chart //圖例
        Legend ls = speedchart.getLegend();
        ls.setTextSize(15f);
        ls.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ls.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ls.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        ls.setDrawInside(false);
        ls.setXEntrySpace(7f);
        ls.setYEntrySpace(5f);
        ls.setTextColor(Color.BLACK);

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
                setangleData(position);
                setspeedData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setangleData(final int index) {

        //RadarChart中的數值
        databaseAngle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RadarEntry> employee1 = new ArrayList<>(); //最大手指角度 maxFingerAngle
                ArrayList<RadarEntry> employee2 = new ArrayList<>(); //最小手指角度 minFingerAngle

                Storage fingerangle = dataSnapshot.child(gameAddress).child(time.get(index)).getValue(Storage.class);

                for(int i = 0; i < 5; i++) {
                    employee1.add(new RadarEntry(fingerangle.getMaxFingerAngle().get(i)));
                    employee2.add(new RadarEntry(fingerangle.getMinFingerAngle().get(i)));
                }

                //we create two radar data sets objects with these data
                //maxFingerAngle
                RadarDataSet set1 = new RadarDataSet(employee1, "Max Finger's Angle");
                set1.setColor(Color.rgb(23, 185, 161));
                set1.setFillColor(Color.rgb(23, 185, 161));
                set1.setDrawFilled(true);
                set1.setFillAlpha(180); //透明度
                set1.setLineWidth(2f); //線粗度
                set1.setDrawHighlightIndicators(false);
                set1.setDrawHighlightCircleEnabled(true);

                //minFingerAngle
                RadarDataSet set2 = new RadarDataSet(employee2, "Min Finger's Angle");
                set2.setColor(Color.rgb(255, 118, 137));
                set2.setFillColor(Color.rgb(255, 118, 137));
                set2.setDrawFilled(true);
                set2.setFillAlpha(180);
                set2.setLineWidth(2f);
                set2.setDrawHighlightIndicators(false);
                set2.setDrawHighlightCircleEnabled(true);

                ArrayList<IRadarDataSet> sets = new ArrayList<>();
                sets.add(set1);
                sets.add(set2);

                //we create Radar Data object which will be added to the Radar Chart for rendering
                RadarData data = new RadarData(sets);
                data.setValueTextSize(8f);
                data.setDrawValues(false);
                data.setValueTextColor(Color.BLACK);

                anglechart.setData(data);

                //顯示數值
                for (IDataSet<?> set : anglechart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                anglechart.invalidate(); //畫出圖形
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //不可刪

            }
        });
    }

    private void setspeedData(final int index) {
        databaseAngle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RadarEntry> employee1 = new ArrayList<>();

                Storage fingerspeed = dataSnapshot.child(gameAddress).child(time.get(index)).getValue(Storage.class);

                for(int i = 0; i < 5; i++) {
                    employee1.add(new RadarEntry(fingerspeed.getMaxFingerSpeed().get(i)));
                }

                //we create two radar data sets objects with these data
                RadarDataSet set1 = new RadarDataSet(employee1, "Max Finger's Speed");
                set1.setColor(Color.rgb(23, 185, 161));
                set1.setFillColor(Color.rgb(23, 185, 161));
                set1.setDrawFilled(true);
                set1.setFillAlpha(180);
                set1.setLineWidth(2f);
                set1.setDrawHighlightIndicators(false);
                set1.setDrawHighlightCircleEnabled(true);

                ArrayList<IRadarDataSet> sets = new ArrayList<>();
                sets.add(set1);

                //we create Radar Data object which will be added to the Radar Chart for rendering
                RadarData data = new RadarData(sets);
                data.setValueTextSize(8f);
                data.setDrawValues(false);
                data.setValueTextColor(Color.BLACK);

                speedchart.setData(data);

                for (IDataSet<?> set : speedchart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                speedchart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
