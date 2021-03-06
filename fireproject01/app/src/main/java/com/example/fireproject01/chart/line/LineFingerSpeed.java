package com.example.fireproject01.chart.line;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fireproject01.R;
import com.example.fireproject01.gui.Result;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class LineFingerSpeed extends Fragment {

    private final String[] fingerGame = new String[] {"Baseball", "Flappy Bird", "Card Matching"};

    private LineChart lineChart;

    private String[] gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private TreeMap<Long, Float> maxValues = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o1 > o2 ? 1 : -1;
        }
    });

    private int fingerNumber;

    DatabaseReference databaseSpeed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.line_finger_speed, container, false);
    }

    @Override
    public void onAttach(Context context) {
        //???NAME???????????????
        super.onAttach(context);
        Bundle take = getArguments();
        gameAddress = new String[fingerGame.length];
        for(int i = 0; i < fingerGame.length; i++) {
            gameAddress[i] = "data/" + take.getString("NAME") + "/" + fingerGame[i];
        }

        fingerNumber = take.getInt("FINGER_NAME");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseSpeed = FirebaseDatabase.getInstance().getReference();

        //??????????????????????????????
        TextView labelText = view.findViewById(R.id.result_finger_speed_text);
        labelText.setText(Result.FINGER_NAME[fingerNumber] + "'s Speed");

        //linechart
        lineChart = (LineChart) view.findViewById(R.id.speedChart);

        databaseSpeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //????????????iter(??????)
                for(int i = 0; i < gameAddress.length; i++) {
                    Iterator<DataSnapshot> timeIter = dataSnapshot.child(gameAddress[i]).getChildren().iterator();
                    while (timeIter.hasNext()) {
                        DataSnapshot time = timeIter.next();

                        //???????????????Infinity??????
                        if (time.child("maxFingerSpeed/" + fingerNumber).getValue().equals("Infinity"))
                            continue;

                        maxValues.put(Long.parseLong(time.getKey()), time.child("maxFingerSpeed/" + fingerNumber).getValue(Float.class));
                    }
                }

                xtext();
                ytext();

                //X?????????
                Description description = new Description();
                description.setText("??????(Date)");
                description.setTextSize(10);
                description.setTextColor(Color.BLACK);
                lineChart.setDescription(description);

                //?????????????????????
                Legend legend = lineChart.getLegend();
                legend.setTextSize(10);

                //????????????????????????????????????
                lineChart.setNoDataText("??????????????????");
                lineChart.setNoDataTextColor(Color.BLUE);

                /////////////////////////////////////////////////////////////////////
                ////////////////////////////?????????????????????////////////////////////////
                /////////////////////////////////////////////////////////////////////
                ArrayList<Entry> max = new ArrayList<>();

                Iterator<Map.Entry<Long, Float>> Values = maxValues.entrySet().iterator();
                //Max
                while (Values.hasNext()) {
                    Map.Entry<Long, Float> data = Values.next();
                    max.add(new Entry(data.getKey(), data.getValue()));
                }

//                maxValues.forEach((k, v) -> max.add(new Entry(k, v));

                Set(max);
                lineChart.invalidate(); //???????????????
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //??????X???
    private void xtext() {
        XAxis xAxis = lineChart.getXAxis();
        //setPosition?????????X????????????????????????????????????XAxis.XAxisPosition.BOTTOM???????????????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //??????X????????????????????????
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            public String getFormattedValue(float value, AxisBase axis) {
                return DateFormat.format("MM/dd", new Date((long) value)).toString();
            }
        });

        xAxis.setAxisMinimum(maxValues.firstKey());
        xAxis.setAxisMaximum(maxValues.lastKey());
    }

    //??????Y???
    private void ytext() {
        //Y???????????????????????????????????????????????????
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void Set(ArrayList<Entry> max) {
        //Max
        LineDataSet maxValue = new LineDataSet(max, "?????????"); //LineDataSet?????????????????????????????? //??????
        //????????????setMode?????????????????????
        //????????????CUBIC_BEZIER
        //????????????HORIZONTAL_BEZIER
        //??????LINEAR
        maxValue.setMode(LineDataSet.Mode.LINEAR);
        maxValue.setColor(Color.rgb(23, 185, 161));
        maxValue.setLineWidth(2);
        maxValue.setCircleHoleRadius(4); //????????????????????????
        maxValue.enableDashedHighlightLine(5, 5, 0);
        //enableDashedHighlightLine(?????????, ????????????, ??????)????????????????????????????????????
        maxValue.setHighlightLineWidth(2);
        maxValue.setHighlightEnabled(true); //?????????????????????????????????
        maxValue.setHighLightColor(Color.BLACK);
        maxValue.setDrawFilled(false); //??????????????????????????????
        maxValue.setValueTextSize(10);

        LineData data = new LineData(maxValue); //??????LineChart????????????????????????
        lineChart.setData(data);
    }
}
