package com.example.fireproject01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.ArrayList;

public class WirstSpeed extends Fragment {

    //we define min and max for the values on our Radar Chart
    public static final float MAX = 180, MIN = 0;
    public static final int NB_QUALITIES = 5; //nb qualities on our Radar Chart

    private String name;

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private RadarChart speedChart;

    private View view;

    DatabaseReference databaseSpeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wirst_speed, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle take = getArguments();
        name = take.getString("NAME");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        databaseSpeed = FirebaseDatabase.getInstance().getReference();

        ////////////////////////////////////////////////////////////////////
        /////////////////////////////speedChart/////////////////////////////
        ////////////////////////////////////////////////////////////////////
        speedChart = view.findViewById(R.id.speedChart);

        //we configure the radar chart
        speedChart.setBackgroundColor(Color.WHITE);
        speedChart.getDescription().setEnabled(false);
        speedChart.setWebLineWidth(1f); //設置直徑方向上那條線的寬度 //好像沒用

        //useful to export your graph
        speedChart.setWebColor(Color.GRAY); //設置直徑線的顏色(5條線)
        speedChart.setWebLineWidth(1.5f); //設置直徑方向上那條線的寬度(5條線)
        speedChart.setWebColorInner(Color.GRAY); //設置圈線的顏色
        speedChart.setWebLineWidthInner(1.5f);
        speedChart.setWebAlpha(255); //設置顏色的透明度

        //animate the chart //圖表數據顯示動畫
        speedChart.animateXY(1400, 1400, Easing.EasingOption.EaseInQuad, Easing.EasingOption.EaseInQuad);

        //x axis
        XAxis xsAxis = speedChart.getXAxis();
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
        YAxis ysAsix = speedChart.getYAxis();
        ysAsix.setLabelCount(NB_QUALITIES, true);
        ysAsix.setTextSize(9f);
        ysAsix.setAxisMinimum(MIN);
        ysAsix.setAxisMaximum(MAX); //we define min and max for axis
        ysAsix.setDrawLabels(false);

        //we configure legend for our radar chart //圖例
        Legend ls = speedChart.getLegend();
        ls.setTextSize(15f);
        ls.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        ls.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ls.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        ls.setDrawInside(false);
        ls.setXEntrySpace(7f);
        ls.setYEntrySpace(5f);
        ls.setTextColor(Color.BLACK);
    }

    public void setWirstValue(String gameName, final String day) {
        gameAddress = "data/" + name + "/" + gameName;
        databaseSpeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ////////////////////////////////////////////////////////////////////
                /////////////////////////////RadarChart/////////////////////////////
                ////////////////////////////////////////////////////////////////////
                ArrayList<RadarEntry> employee1 = new ArrayList<>(); //最大手指速度 maxFingerSpeed

                Storage wirstSpeed = dataSnapshot.child(gameAddress).child(day).getValue(Storage.class);

                for(int i = 0; i < 3; i++) {
                    employee1.add(new RadarEntry(wirstSpeed.getMaxMpuSpeed().get(i)));
                }

                //we create two radar data sets objects with these data
                //maxFingerSpeed
                RadarDataSet set1 = new RadarDataSet(employee1, "Max Wirst's Speed");
                set1.setColor(Color.rgb(23, 185, 161));
                set1.setFillColor(Color.rgb(23, 185, 161));
                set1.setDrawFilled(true);
                set1.setFillAlpha(180); //透明度
                set1.setLineWidth(2f); //線粗度
                set1.setDrawHighlightIndicators(false);
                set1.setDrawHighlightCircleEnabled(true);

                ArrayList<IRadarDataSet> sets = new ArrayList<>();
                sets.add(set1);

                //we create Radar Data object which will be added to the Radar Chart for rendering
                RadarData data = new RadarData(sets);
                data.setValueTextSize(8f);
                data.setDrawValues(false);
                data.setValueTextColor(Color.BLACK);

                speedChart.setData(data);

                //顯示數值
                for (IDataSet<?> set : speedChart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                speedChart.invalidate(); //畫出圖形


                ////////////////////////////////////////////////////////////////////
                //////////////////////////////textView//////////////////////////////
                ////////////////////////////////////////////////////////////////////
                String[] AngleNumber = new String[5];
                //Max
                for(int i = 0; i < 3; i++) {
                    AngleNumber[i] = String.valueOf(wirstSpeed.getMaxMpuSpeed().get(i));
                }

                TextView MaxXSpeed = (TextView)view.findViewById(R.id.MaxXSpeed);
                TextView MaxYSpeed = (TextView)view.findViewById(R.id.MaxYSpeed);
                TextView MaxZSpeed = (TextView)view.findViewById(R.id.MaxZSpeed);

                MaxXSpeed.setText(AngleNumber[0]);
                MaxYSpeed.setText(AngleNumber[1]);
                MaxZSpeed.setText(AngleNumber[2]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //不可刪

            }
        });
    }

}
