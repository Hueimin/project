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
        //傳NAME的路徑至此
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

        //標示為哪隻手指的數據
        TextView labelText = view.findViewById(R.id.result_finger_speed_text);
        labelText.setText(Result.FINGER_NAME[fingerNumber] + "'s Speed");

        //linechart
        lineChart = (LineChart) view.findViewById(R.id.speedChart);

        databaseSpeed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //存時間到iter(迭代)
                for(int i = 0; i < gameAddress.length; i++) {
                    Iterator<DataSnapshot> timeIter = dataSnapshot.child(gameAddress[i]).getChildren().iterator();
                    while (timeIter.hasNext()) {
                        DataSnapshot time = timeIter.next();

                        //排除時間為Infinity的值
                        if (time.child("maxFingerSpeed/" + fingerNumber).getValue().equals("Infinity"))
                            continue;

                        maxValues.put(Long.parseLong(time.getKey()), time.child("maxFingerSpeed/" + fingerNumber).getValue(Float.class));
                    }
                }

                xtext();
                ytext();

                //X軸標題
                Description description = new Description();
                description.setText("日期(Date)");
                description.setTextSize(10);
                description.setTextColor(Color.BLACK);
                lineChart.setDescription(description);

                //修改圖例的方式
                Legend legend = lineChart.getLegend();
                legend.setTextSize(10);

                //沒有數據出現時的顯示方式
                lineChart.setNoDataText("無數據可顯示");
                lineChart.setNoDataTextColor(Color.BLUE);

                /////////////////////////////////////////////////////////////////////
                ////////////////////////////數據顯示之內容////////////////////////////
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
                lineChart.invalidate(); //為繪製圖表
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //設定X軸
    private void xtext() {
        XAxis xAxis = lineChart.getXAxis();
        //setPosition為設置X軸顯示位置，預設在上方，XAxis.XAxisPosition.BOTTOM則是在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //自訂X軸數值顯示為日期
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            public String getFormattedValue(float value, AxisBase axis) {
                return DateFormat.format("MM/dd", new Date((long) value)).toString();
            }
        });

        xAxis.setAxisMinimum(maxValues.firstKey());
        xAxis.setAxisMaximum(maxValues.lastKey());
    }

    //設定Y軸
    private void ytext() {
        //Y軸有左右兩邊，右邊不顯示的方法如下
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void Set(ArrayList<Entry> max) {
        //Max
        LineDataSet maxValue = new LineDataSet(max, "最大值"); //LineDataSet設定線數資料顯示方式 //圖例
        //可以透過setMode設定顯示的線條
        //立方曲線CUBIC_BEZIER
        //水平曲線HORIZONTAL_BEZIER
        //折線LINEAR
        maxValue.setMode(LineDataSet.Mode.LINEAR);
        maxValue.setColor(Color.rgb(23, 185, 161));
        maxValue.setLineWidth(2);
        maxValue.setCircleHoleRadius(4); //設置焦點圓心大小
        maxValue.enableDashedHighlightLine(5, 5, 0);
        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
        maxValue.setHighlightLineWidth(2);
        maxValue.setHighlightEnabled(true); //設定是否禁用點擊高亮線
        maxValue.setHighLightColor(Color.BLACK);
        maxValue.setDrawFilled(false); //設定禁用範圍背景填充
        maxValue.setValueTextSize(10);

        LineData data = new LineData(maxValue); //創建LineChart折線圖的數據集合
        lineChart.setData(data);
    }
}
