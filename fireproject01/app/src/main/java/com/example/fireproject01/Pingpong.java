package com.example.fireproject01;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Pingpong extends AppCompatActivity {

    public static final String GAME_NAME = "Table Tennis";

    private String gameAddress; //"data/" + take.getString("NAME") + "/" + GAME_NAME

    private ArrayList<String> time = new ArrayList<>();

    private Bundle take;

    private Spinner spinner;

    //將Angle與Speed.java宣告至此
    private WirstAngle angle;
    private WirstSpeed speed;

    DatabaseReference databaseAngle;

    Button angleButton;
    Button speedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingpong);

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

        angle = new WirstAngle();
        speed = new WirstSpeed();

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
                    choice[i] = DateFormat.format("yyyy/MM/dd 'at' HH:mm:ss", new Date(Long.parseLong(time.get(i)))).toString();
                }

                //放入timeList中，並由spinner顯示
                ArrayAdapter timeList = new ArrayAdapter<>(Pingpong.this, android.R.layout.simple_spinner_dropdown_item, choice);
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
                angle.setWirstValue("Table Tennis", time.get(position));
                speed.setWirstValue("Table Tennis", time.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        lineChart = (LineChart)findViewById(R.id.angleChart);
//        lineChart1 = (LineChart)findViewById(R.id.speedChart);
//
//        xtext();
//        ytext();
//
//        //X軸標題
//        Description description = new Description();
//        description.setText("時間(s)");
//        description.setTextSize(10);
//        description.setTextColor(Color.BLACK);
//        lineChart.setDescription(description);
//
//        lineChart1.setDescription(description);
//
//        //修改圖例的方式
//        Legend legend = lineChart.getLegend();
//        legend.setTextSize(10);
//
//        Legend legend1 = lineChart1.getLegend();
//        legend1.setTextSize(10);
//
//        //沒有數據出現時的顯示方式
//        lineChart.setNoDataText("無數據可顯示QQ");
//        lineChart.setNoDataTextColor(Color.BLUE);
//
//        lineChart1.setNoDataText("無數據可顯示QQ");
//        lineChart1.setNoDataTextColor(Color.YELLOW);
//
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
//        //數據顯示之內容
//        ArrayList<Entry> values1 = new ArrayList<>();
//        values1.add(new Entry(1,10));
//        values1.add(new Entry(4,50));
//        values1.add(new Entry(10,20));
//        values1.add(new Entry(6,40));
//        text_all(values1);
//
//        ArrayList<Entry> values2 = new ArrayList<>();
//        values2.add(new Entry(2,10));
//        values2.add(new Entry(4,20));
//        values2.add(new Entry(5,30));
//        values2.add(new Entry(6,40));
//        text_all1(values2);
//    }
//
//    //設定X軸
//    private void xtext() {
//        XAxis xAxis = lineChart.getXAxis();
//        //setPosition為設置X軸顯示位置，預設在上方，XAxis.XAxisPosition.BOTTOM則是在下方
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(0f); //設定從0開始畫
//
//        XAxis xAxis1 = lineChart1.getXAxis();
//        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis1.setAxisMinimum(0f); //設定從0開始畫
//    }
//
//    //設定Y軸
//    private void ytext() {
//        //Y軸有左右兩邊，右邊不顯示的方法如下
//        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setEnabled(false);
//
//        YAxis rightAxis1 = lineChart1.getAxisRight();
//        rightAxis1.setEnabled(false);
    }


//    private void text_all(ArrayList<Entry> values1) {
//        LineDataSet set1; //LineDataSet設定線數資料顯示方式
//        set1 = new LineDataSet(values1,"幅度"); //圖例
//        //可以透過setMode設定顯示的線條
//        //立方曲線CUBIC_BEZIER
//        //水平曲線HORIZONTAL_BEZIER
//        //折線LINEAR
//        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set1.setColor(Color.BLACK);
//        set1.setLineWidth(2);
//        set1.setCircleHoleRadius(4); //設置焦點圓心大小
//        set1.enableDashedHighlightLine(5,5,0);
//        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
//        set1.setHighlightLineWidth(2);
//        set1.setHighlightEnabled(true); //設定是否禁用點擊高亮線
//        set1.setHighLightColor(Color.RED);
//        set1.setDrawFilled(false); //設定禁用範圍背景填充
//
//        LineData data = new LineData(set1); //創建LineChart折線圖的數據集合
//        lineChart.setData(data);
//        lineChart.invalidate(); //為繪製圖表
//    }
//
//    private void text_all1(ArrayList<Entry> values2) {
//        LineDataSet set2; //LineDataSet設定線數資料顯示方式
//        set2 = new LineDataSet(values2,"速度"); //圖例
//        //可以透過setMode設定顯示的線條
//        //立方曲線CUBIC_BEZIER
//        //水平曲線HORIZONTAL_BEZIER
//        //折線LINEAR
//        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set2.setColor(Color.BLACK);
//        set2.setLineWidth(2);
//        set2.setCircleHoleRadius(4); //設置焦點圓心大小
//        set2.enableDashedHighlightLine(5,5,0);
//        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
//        set2.setHighlightLineWidth(2);
//        set2.setHighlightEnabled(true); //設定是否禁用點擊高亮線
//        set2.setHighLightColor(Color.RED);
//        set2.setDrawFilled(false); //設定禁用範圍背景填充
//
//        LineData data = new LineData(set2); //創建LineChart折線圖的數據集合
//        lineChart1.setData(data);
//        lineChart1.invalidate(); //為繪製圖表
//    }
}