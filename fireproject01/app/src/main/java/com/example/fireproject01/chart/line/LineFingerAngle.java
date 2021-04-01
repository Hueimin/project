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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class LineFingerAngle extends Fragment {

    private LineChart lineChart;

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private TreeMap<Long, Float> maxValues = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o1 > o2 ? 1 : -1;
        }
    }), minValues = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o1 > o2 ? 1 : -1;
        }
    });

    private int fingerNumber;

    private DatabaseReference databaseDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.line_finger_angle, container, false);
    }

    @Override
    public void onAttach(Context context) {
        //傳NAME的路徑至此
        super.onAttach(context);
        Bundle take = getArguments();
        gameAddress = "data/" + take.getString("NAME");
        fingerNumber = take.getInt("FINGER_NAME");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseDate = FirebaseDatabase.getInstance().getReference();

        TextView text = view.findViewById(R.id.result_finger_angle_text);
        text.setText(Result.FINGER_NAME[fingerNumber] + "'s Angle");

        //linechart
        lineChart = (LineChart)view.findViewById(R.id.angleChart);

        databaseDate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //存遊戲名稱到iter(迭代)
                Iterator<DataSnapshot> gameIter = dataSnapshot.child(gameAddress).getChildren().iterator();

                while(gameIter.hasNext()) {
                    DataSnapshot game = gameIter.next(); //快照
                    Iterator<DataSnapshot> timeIter = game.getChildren().iterator();
                    while(timeIter.hasNext()) {
                        DataSnapshot data = timeIter.next();
                        if(data.child("maxFingerAngle/" + fingerNumber).getValue().equals("Infinity")) continue;
                        maxValues.put(Long.parseLong(data.getKey()), data.child("maxFingerAngle/" + fingerNumber).getValue(Float.class));
                        minValues.put(Long.parseLong(data.getKey()), data.child("minFingerAngle/" + fingerNumber).getValue(Float.class));
                    }
                }
                System.out.println(Arrays.toString(maxValues.values().toArray()));
                System.out.println(Arrays.toString(minValues.values().toArray()));

                xtext();
                ytext();

                //X軸標題
                Description description = new Description();
                description.setText("日期(date)");
                description.setTextSize(10);
                description.setTextColor(Color.BLACK);
                lineChart.setDescription(description);

                //修改圖例的方式
                Legend legend = lineChart.getLegend();
                legend.setTextSize(10);

                //沒有數據出現時的顯示方式
                lineChart.setNoDataText("無數據可顯示QQ");
                lineChart.setNoDataTextColor(Color.BLUE);

///////////////////////////////////////////////////////////////////////////////////////////////////
                //數據顯示之內容
                ArrayList<Entry> max = new ArrayList<>(), min = new ArrayList<>();
                Iterator<Map.Entry<Long, Float>> iter = maxValues.entrySet().iterator();
                while(iter.hasNext()) {
                    Map.Entry<Long, Float> data = iter.next();
                    max.add(new Entry(data.getKey(), data.getValue()));
                }
                iter = minValues.entrySet().iterator();
                while(iter.hasNext()) {
                    Map.Entry<Long, Float> data = iter.next();
                    min.add(new Entry(data.getKey(), data.getValue()));
                }
//      maxValues.forEach((k, v) -> max.add(new Entry(k, v));
                text_all(max, min);
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

    private void text_all(ArrayList<Entry> max, ArrayList<Entry> min) {
        LineDataSet set1 = new LineDataSet(max,"幅度"); //LineDataSet設定線數資料顯示方式 //圖例
        //可以透過setMode設定顯示的線條
        //立方曲線CUBIC_BEZIER
        //水平曲線HORIZONTAL_BEZIER
        //折線LINEAR
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setColor(Color.GREEN);
        set1.setLineWidth(2);
        set1.setCircleHoleRadius(4); //設置焦點圓心大小
        set1.enableDashedHighlightLine(5,5,0);
        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
        set1.setHighlightLineWidth(2);
        set1.setHighlightEnabled(true); //設定是否禁用點擊高亮線
        set1.setHighLightColor(Color.RED);
        set1.setDrawFilled(false); //設定禁用範圍背景填充

        LineDataSet set2 = new LineDataSet(min,"幅度"); //LineDataSet設定線數資料顯示方式 //圖例
        //可以透過setMode設定顯示的線條
        //立方曲線CUBIC_BEZIER
        //水平曲線HORIZONTAL_BEZIER
        //折線LINEAR
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setColor(Color.GRAY);
        set2.setLineWidth(2);
        set2.setCircleHoleRadius(4); //設置焦點圓心大小
        set2.enableDashedHighlightLine(5,5,0);
        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
        set2.setHighlightLineWidth(2);
        set2.setHighlightEnabled(true); //設定是否禁用點擊高亮線
        set2.setHighLightColor(Color.RED);
        set2.setDrawFilled(false); //設定禁用範圍背景填充

        LineData data = new LineData(set1); //創建LineChart折線圖的數據集合
        data.addDataSet(set2);
        lineChart.setData(data);
    }
}
