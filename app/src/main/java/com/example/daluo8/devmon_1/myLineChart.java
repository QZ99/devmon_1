package com.example.daluo8.devmon_1;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class myLineChart{
        Context mcontext;
        LineChart mLineChart;
        LineDataSet lineDataSet;
        myLineChart(LineChart lChart){

                mLineChart = lChart;
        }
        List<Entry> entries = new ArrayList<>();
        void init(){
                mLineChart.setDrawBorders(true);

        /*for (int i = 0; i < 60; i = i+6) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }*/
                entries.add(new Entry(60,0));
                lineDataSet = new LineDataSet(entries, "利用率");
                lineDataSet.setDrawValues(false);
                lineDataSet.setDrawCircles(false);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillColor(Color.BLUE);
                lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
                LineData data = new LineData(lineDataSet);

                mLineChart.setData(data);


                XAxis xAxis = mLineChart.getXAxis();
                xAxis.setLabelCount(2,true);

                xAxis.setAxisMaximum(60);
                xAxis.setAxisMinimum(0);
                xAxis.setAvoidFirstLastClipping(true);
                xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                                if(value == 0){
                                        return String.valueOf(60);

                                }else if(value == 60){
                                    return String.valueOf(0);
                                }


                                return super.getFormattedValue(value);
                        }
                });
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setCenterAxisLabels(false);
                xAxis.setDrawGridLines(false);

                YAxis leftYAxis = mLineChart.getAxisLeft();
                YAxis rightYAxis = mLineChart.getAxisRight();
                rightYAxis.setAxisMinimum(0f);
                rightYAxis.setAxisMaximum(100f);
                //rightYAxis.setLabelCount(1);
                rightYAxis.setCenterAxisLabels(false);
                //rightYAxis.setEnabled(true);
                /*rightYAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                                if(value!=100){
                                        return "";
                                }else{
                                        return "100%";
                                }

                                //return super.getFormattedValue(value);
                        }
                });*/

                leftYAxis.setEnabled(false);
                //lChart.setData(lineData);//装载数据
        Description description = new Description();
        description.setEnabled(false);
        mLineChart.setDescription(description);




        }

        void updateRight(Entry entry){                                     //动态更新数据

                int size = entries.size();
                for(int i  = 0;i<size;i++){
                        entries.get(i).setX(entries.get(i).getX()-1);

                }
                if(size>0 && entries.get(0).getX()<0){
                        entries.remove(0);
                }
                entries.add(entry);
                //lineDataSet.setFillColor(Color.BLUE);

                mLineChart.notifyDataSetChanged();
                mLineChart.invalidate();


        }


}