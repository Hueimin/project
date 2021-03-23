package com.example.fireproject01;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

public class Result extends AppCompatActivity {
    LineChart lineChart;
    LineChart lineChart1;

    //we define min and max for the values on our Radar Chart
    public static final float MAX = 12, MIN = 1f;
    public static final int NB_QUALITIES = 5; //nb qualities on our Radar Chart

    private RadarChart anglechart;
    private RadarChart speedchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        lineChart = (LineChart)findViewById(R.id.angleChart);
        lineChart1 = (LineChart)findViewById(R.id.speedChart);

        xtext();
        ytext();

        //X軸標題
        Description description = new Description();
        description.setText("時間(s)");
        description.setTextSize(10);
        description.setTextColor(Color.BLACK);
        lineChart.setDescription(description);

        lineChart1.setDescription(description);

        //修改圖例的方式
        Legend legend = lineChart.getLegend();
        legend.setTextSize(10);

        Legend legend1 = lineChart1.getLegend();
        legend1.setTextSize(10);

        //沒有數據出現時的顯示方式
        lineChart.setNoDataText("無數據可顯示QQ");
        lineChart.setNoDataTextColor(Color.BLUE);

        lineChart1.setNoDataText("無數據可顯示QQ");
        lineChart1.setNoDataTextColor(Color.YELLOW);

///////////////////////////////////////////////////////////////////////////////////////////////////

        //數據顯示之內容
        ArrayList<Entry> values1 = new ArrayList<>();
        values1.add(new Entry(1,10));
        values1.add(new Entry(4,50));
        values1.add(new Entry(10,20));
        values1.add(new Entry(6,40));
        text_all(values1);

        ArrayList<Entry> values2 = new ArrayList<>();
        values2.add(new Entry(2,10));
        values2.add(new Entry(4,20));
        values2.add(new Entry(5,30));
        values2.add(new Entry(6,40));
        text_all1(values2);


        //Radar Chart

        /////////////////////////////angleChart/////////////////////////////
        anglechart = findViewById(R.id.angleRadarChart);

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

        setangleData();

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



        /////////////////////////////speedChart/////////////////////////////
        speedchart = findViewById(R.id.speedRadarChart);

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

        setspeedData();

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
    }

    //設定X軸
    private void xtext() {
        XAxis xAxis = lineChart.getXAxis();
        //setPosition為設置X軸顯示位置，預設在上方，XAxis.XAxisPosition.BOTTOM則是在下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f); //設定從0開始畫

        XAxis xAxis1 = lineChart1.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setAxisMinimum(0f); //設定從0開始畫
    }

    //設定Y軸
    private void ytext() {
        //Y軸有左右兩邊，右邊不顯示的方法如下
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis rightAxis1 = lineChart1.getAxisRight();
        rightAxis1.setEnabled(false);
    }

    private void text_all(ArrayList<Entry> values1) {
        LineDataSet set1; //LineDataSet設定線數資料顯示方式
        set1 = new LineDataSet(values1,"幅度"); //圖例
        //可以透過setMode設定顯示的線條
        //立方曲線CUBIC_BEZIER
        //水平曲線HORIZONTAL_BEZIER
        //折線LINEAR
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setColor(Color.BLACK);
        set1.setLineWidth(2);
        set1.setCircleHoleRadius(4); //設置焦點圓心大小
        set1.enableDashedHighlightLine(5,5,0);
        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
        set1.setHighlightLineWidth(2);
        set1.setHighlightEnabled(true); //設定是否禁用點擊高亮線
        set1.setHighLightColor(Color.RED);
        set1.setDrawFilled(false); //設定禁用範圍背景填充

        LineData data = new LineData(set1); //創建LineChart折線圖的數據集合
        lineChart.setData(data);
        lineChart.invalidate(); //為繪製圖表
    }

    private void text_all1(ArrayList<Entry> values2) {
        LineDataSet set2; //LineDataSet設定線數資料顯示方式
        set2 = new LineDataSet(values2,"速度"); //圖例
        //可以透過setMode設定顯示的線條
        //立方曲線CUBIC_BEZIER
        //水平曲線HORIZONTAL_BEZIER
        //折線LINEAR
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setColor(Color.BLACK);
        set2.setLineWidth(2);
        set2.setCircleHoleRadius(4); //設置焦點圓心大小
        set2.enableDashedHighlightLine(5,5,0);
        //enableDashedHighlightLine(線寬度, 分隔寬度, 階段)，為點擊後的虛線顯示樣式
        set2.setHighlightLineWidth(2);
        set2.setHighlightEnabled(true); //設定是否禁用點擊高亮線
        set2.setHighLightColor(Color.RED);
        set2.setDrawFilled(false); //設定禁用範圍背景填充

        LineData data = new LineData(set2); //創建LineChart折線圖的數據集合
        lineChart1.setData(data);
        lineChart1.invalidate(); //為繪製圖表
    }

    private void setangleData() {
        ArrayList<RadarEntry> employee1 = new ArrayList<>();
        ArrayList<RadarEntry> employee2 = new ArrayList<>();

        for(int i = 0; i < NB_QUALITIES; i++) {
            float val1 = (int)(Math.random() * MAX) + MIN;
            employee1.add(new RadarEntry(val1));

            float val2 = (int)(Math.random() * MAX) + MIN;
            employee2.add(new RadarEntry(val2));
        }

        //we create two radar data sets objects with these data
        RadarDataSet set1 = new RadarDataSet(employee1, "Employee A");
        set1.setColor(Color.rgb(23, 185, 161));
        set1.setFillColor(Color.rgb(23, 185, 161));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightIndicators(false);
        set1.setDrawHighlightCircleEnabled(true);

        RadarDataSet set2 = new RadarDataSet(employee2, "Employee B");
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

        for (IDataSet<?> set : anglechart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }

        anglechart.invalidate();
    }

    private void setspeedData() {
        ArrayList<RadarEntry> employee1 = new ArrayList<>();
        ArrayList<RadarEntry> employee2 = new ArrayList<>();

        for(int i = 0; i < NB_QUALITIES; i++) {
            float val1 = (int)(Math.random() * MAX) + MIN;
            employee1.add(new RadarEntry(val1));

            float val2 = (int)(Math.random() * MAX) + MIN;
            employee2.add(new RadarEntry(val2));
        }

        //we create two radar data sets objects with these data
        RadarDataSet set1 = new RadarDataSet(employee1, "Employee A");
        set1.setColor(Color.rgb(23, 185, 161));
        set1.setFillColor(Color.rgb(23, 185, 161));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightIndicators(false);
        set1.setDrawHighlightCircleEnabled(true);

        RadarDataSet set2 = new RadarDataSet(employee2, "Employee B");
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

        speedchart.setData(data);

        for (IDataSet<?> set : speedchart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }

        speedchart.invalidate();
    }
}
