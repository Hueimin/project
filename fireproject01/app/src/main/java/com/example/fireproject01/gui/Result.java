package com.example.fireproject01.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fireproject01.chart.line.LineFingerAngle;
import com.example.fireproject01.chart.line.LineFingerSpeed;
import com.example.fireproject01.R;
import com.example.fireproject01.chart.line.SlidePagerAdapter;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    public final static String[] FINGER_NAME = {"Thumb", "Index Finger", "Middle Finger", "Ring Finger", "Little Finger"};

    private ViewPager pager;
    private PagerAdapter fingerAngleAdapter, fingerSpeedAdapter;
    private ArrayList<Fragment> fingerAngleList = new ArrayList<>(),
                                fingerSpeedList = new ArrayList<>();

    private Button fingerAngleButton, fingerSpeedButton, wristAngleButton, wristSpeedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        fingerAngleAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fingerAngleList);

        for(int i = 0; i < 5; i++) {
            Bundle take = getIntent().getExtras(), bundle = new Bundle();
            bundle.putString("NAME", take.getString("NAME"));
            bundle.putInt("FINGER_NAME", i);
            Fragment fragment = new LineFingerAngle();
            fragment.setArguments(bundle);
            fingerAngleList.add(fragment);
        }

        fingerSpeedAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fingerSpeedList);

        for(int i = 0; i < 5; i++) {
            Bundle take = getIntent().getExtras(), bundle = new Bundle();
            bundle.putString("NAME", take.getString("NAME"));
            bundle.putInt("FINGER_NAME", i);
            Fragment fragment = new LineFingerSpeed();
            fragment.setArguments(bundle);
            fingerSpeedList.add(fragment);
        }

        pager = findViewById(R.id.pager);
        pager.setAdapter(fingerAngleAdapter);

        fingerAngleButton = findViewById(R.id.F_ANGLE);
        fingerAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setAdapter(fingerAngleAdapter);
            }
        });

        fingerSpeedButton = findViewById(R.id.F_SPEED);
        fingerSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setAdapter(fingerSpeedAdapter);
            }
        });

        wristAngleButton = findViewById(R.id.W_ANGLE);

        wristSpeedButton = findViewById(R.id.W_SPEED);

//        lineChart = (LineChart)findViewById(R.id.angleChart);

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
//

    }
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
//    }
//
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
//

}
