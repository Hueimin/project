package com.example.fireproject01.chart.radar;

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

import com.example.fireproject01.R;
import com.example.fireproject01.data.Storage;
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

public class WristAngle extends Fragment {

    //we define min and max for the values on our Radar Chart
    public static final float MAX = 180, MIN = -180;
    public static final int NB_QUALITIES = 3; //nb qualities on our Radar Chart

    private String name;

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private RadarChart angleChart;

    private View view;

    DatabaseReference databaseAngle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //?????????angle.xml
        return inflater.inflate(R.layout.wrist_angle, container, false);
    }

    @Override
    public void onAttach(Context context) {
        //???NAME???????????????
        super.onAttach(context);
        Bundle take = getArguments();
        name = take.getString("NAME");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view; //?????????findViewById????????????????????????view.
        databaseAngle = FirebaseDatabase.getInstance().getReference();

        ////////////////////////////////////////////////////////////////////
        /////////////////////////////angleChart/////////////////////////////
        ////////////////////////////////////////////////////////////////////
        angleChart = view.findViewById(R.id.angleChart);

        //we configure the radar chart
        angleChart.setBackgroundColor(Color.WHITE);
        angleChart.getDescription().setEnabled(false);
        angleChart.setWebLineWidth(1f); //??????????????????????????????????????? //????????????

        //useful to export your graph
        angleChart.setWebColor(Color.GRAY); //????????????????????????(5??????)
        angleChart.setWebLineWidth(1.5f); //???????????????????????????????????????(5??????)
        angleChart.setWebColorInner(Color.GRAY); //?????????????????????
        angleChart.setWebLineWidthInner(1.5f);
        angleChart.setWebAlpha(255); //????????????????????????

        //animate the chart //????????????????????????
        angleChart.animateXY(1400, 1400, Easing.EasingOption.EaseInQuad, Easing.EasingOption.EaseInQuad);

        //x axis
        XAxis xaAxis = angleChart.getXAxis();
        xaAxis.setTextSize(12f);
        xaAxis.setTypeface(Typeface.DEFAULT_BOLD); //???????????????
        xaAxis.setYOffset(0); //?
        xaAxis.setXOffset(0); //?
        xaAxis.setValueFormatter(new IAxisValueFormatter() {
            //we will compare two employees in a radar chart
            //so we define qualities to compare
            private String[] qualities = new String[] {"Pitch X", "Roll Y", "Yaw Z"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return qualities[(int)value % qualities.length];
            }
        });

        xaAxis.setTextColor(Color.BLACK);

        //Y axis
        YAxis yaAsix = angleChart.getYAxis();
        yaAsix.setLabelCount(NB_QUALITIES, true);
        yaAsix.setTextSize(9f);
        yaAsix.setAxisMinimum(MIN);
        yaAsix.setAxisMaximum(MAX); //we define min and max for axis
        yaAsix.setDrawLabels(false);

        //we configure legend for our radar chart //??????
        Legend la = angleChart.getLegend();
        la.setTextSize(15f);
        la.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        la.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        la.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        la.setDrawInside(false);
        la.setXEntrySpace(7f);
        la.setYEntrySpace(5f);
        la.setTextColor(Color.BLACK);
    }

    public void setWristValue(String gameName, final String day) {
        gameAddress = "data/" + name + "/" + gameName;
        databaseAngle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ////////////////////////////////////////////////////////////////////
                /////////////////////////////RadarChart/////////////////////////////
                ////////////////////////////////////////////////////////////////////
                ArrayList<RadarEntry> employee1 = new ArrayList<>(); //?????????????????? maxWirstAngle
                ArrayList<RadarEntry> employee2 = new ArrayList<>(); //?????????????????? minWirstAngle

                Storage wristAngle = dataSnapshot.child(gameAddress).child(day).getValue(Storage.class);

                for(int i = 0; i < 3; i++) {
                    employee1.add(new RadarEntry(wristAngle.getMaxMpuAngle().get(i)));
                    employee2.add(new RadarEntry(wristAngle.getMinMpuAngle().get(i)));
                }

                //we create two radar data sets objects with these data
                //maxFingerAngle
                RadarDataSet set1 = new RadarDataSet(employee1, "Max Wrist's Angle");
                set1.setColor(Color.rgb(23, 185, 161));
                set1.setFillColor(Color.rgb(23, 185, 161));
                set1.setDrawFilled(true);
                set1.setFillAlpha(180); //?????????
                set1.setLineWidth(2f); //?????????
                set1.setDrawHighlightIndicators(false);
                set1.setDrawHighlightCircleEnabled(true);

                //minFingerAngle
                RadarDataSet set2 = new RadarDataSet(employee2, "Min Wrist's Angle");
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

                angleChart.setData(data);

                //????????????
                for (IDataSet<?> set : angleChart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                angleChart.invalidate(); //????????????


                ////////////////////////////////////////////////////////////////////
                //////////////////////////////textView//////////////////////////////
                ////////////////////////////////////////////////////////////////////
                String[] AngleNumber = new String[5];
                //Max
                for(int i = 0; i < 3; i++) {
                    AngleNumber[i] = String.valueOf(wristAngle.getMaxMpuAngle().get(i));
                }

                TextView MaxXAngle = view.findViewById(R.id.MaxXAngle);
                TextView MaxYAngle = view.findViewById(R.id.MaxYAngle);
                TextView MaxZAngle = view.findViewById(R.id.MaxZAngle);

                MaxXAngle.setText(AngleNumber[0]);
                MaxYAngle.setText(AngleNumber[1]);
                MaxZAngle.setText(AngleNumber[2]);

                //Min
                for(int i = 0; i < 3; i++) {
                    AngleNumber[i] = String.valueOf(wristAngle.getMinMpuAngle().get(i));
                }

                TextView MinXAngle = view.findViewById(R.id.MinXAngle);
                TextView MinYAngle = view.findViewById(R.id.MinYAngle);
                TextView MinZAngle = view.findViewById(R.id.MinZAngle);

                MinXAngle.setText(AngleNumber[0]);
                MinYAngle.setText(AngleNumber[1]);
                MinZAngle.setText(AngleNumber[2]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //?????????

            }
        });
    }
}
