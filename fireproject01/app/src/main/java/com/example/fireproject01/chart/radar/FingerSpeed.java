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

public class FingerSpeed extends Fragment {

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
        return inflater.inflate(R.layout.finger_speed, container, false);
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
        speedChart.setWebLineWidth(1f); //??????????????????????????????????????? //????????????

        //useful to export your graph
        speedChart.setWebColor(Color.GRAY); //????????????????????????(5??????)
        speedChart.setWebLineWidth(1.5f); //???????????????????????????????????????(5??????)
        speedChart.setWebColorInner(Color.GRAY); //?????????????????????
        speedChart.setWebLineWidthInner(1.5f);
        speedChart.setWebAlpha(255); //????????????????????????

        //animate the chart //????????????????????????
        speedChart.animateXY(1400, 1400, Easing.EasingOption.EaseInQuad, Easing.EasingOption.EaseInQuad);

        //x axis
        XAxis xsAxis = speedChart.getXAxis();
        xsAxis.setTextSize(12f);
        xsAxis.setTypeface(Typeface.DEFAULT_BOLD); //???????????????
        xsAxis.setYOffset(0); //?
        xsAxis.setXOffset(0); //?
        xsAxis.setValueFormatter(new IAxisValueFormatter() {
            //we will compare two employees in a radar chart
            //so we define qualities to compare
            private String[] qualities = new String[] {"?????????", "??????", "??????", "?????????", "?????????"};

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

        //we configure legend for our radar chart //??????
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

    public void setFingerValue(String gameName, final String day) {
        gameAddress = "data/" + name + "/" + gameName;
        databaseSpeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ////////////////////////////////////////////////////////////////////
                /////////////////////////////RadarChart/////////////////////////////
                ////////////////////////////////////////////////////////////////////
                ArrayList<RadarEntry> employee1 = new ArrayList<>(); //?????????????????? maxFingerSpeed

                Storage fingerSpeed = dataSnapshot.child(gameAddress).child(day).getValue(Storage.class);

                for(int i = 0; i < 5; i++) {
                    employee1.add(new RadarEntry(fingerSpeed.getMaxFingerSpeed().get(i)));
                }

                //we create two radar data sets objects with these data
                //maxFingerSpeed
                RadarDataSet set1 = new RadarDataSet(employee1, "Max Finger's Speed");
                set1.setColor(Color.rgb(23, 185, 161));
                set1.setFillColor(Color.rgb(23, 185, 161));
                set1.setDrawFilled(true);
                set1.setFillAlpha(180); //?????????
                set1.setLineWidth(2f); //?????????
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

                //????????????
                for (IDataSet<?> set : speedChart.getData().getDataSets()) {
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                speedChart.invalidate(); //????????????


                ////////////////////////////////////////////////////////////////////
                //////////////////////////////textView//////////////////////////////
                ////////////////////////////////////////////////////////////////////
                String[] AngleNumber = new String[5];
                //Max
                for(int i = 0; i < 5; i++) {
                    AngleNumber[i] = String.valueOf(fingerSpeed.getMaxFingerSpeed().get(i));
                }

                TextView MaxthumbSpeed = (TextView)view.findViewById(R.id.MaxthumbSpeed);
                TextView MaxindexSpeed = (TextView)view.findViewById(R.id.MaxindexSpeed);
                TextView MaxmiddleSpeed = (TextView)view.findViewById(R.id.MaxmiddleSpeed);
                TextView MaxringSpeed = (TextView)view.findViewById(R.id.MaxringSpeed);
                TextView MaxlittleSpeed = (TextView)view.findViewById(R.id.MaxlittleSpeed);
                MaxthumbSpeed.setText(AngleNumber[0]);
                MaxindexSpeed.setText(AngleNumber[1]);
                MaxmiddleSpeed.setText(AngleNumber[2]);
                MaxringSpeed.setText(AngleNumber[3]);
                MaxlittleSpeed.setText(AngleNumber[4]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //?????????

            }
        });
    }

}
