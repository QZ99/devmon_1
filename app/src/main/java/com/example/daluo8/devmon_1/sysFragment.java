package com.example.daluo8.devmon_1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class sysFragment extends Fragment {
    TextView tv_sys;
    TextView tv_sto;
    TextView tv_sto_1;
    PieChart mPieChart;
    View view;
    SystemInfo sysInfo;
    StorageInfo stoInfo;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    draw();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sys, container, false);

        init();
        updateData();




        return view;

    }

    private void init(){
        tv_sys = view.findViewById(R.id.sys);
        sysInfo = new SystemInfo();
        tv_sto=view.findViewById(R.id.stor);
        stoInfo=new StorageInfo(getActivity().getBaseContext());
        tv_sto_1=view.findViewById(R.id.sto_remian);

    }

    private void updateData(){
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "storage");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    public  void draw(){
        mPieChart=view.findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 5, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        long total = stoInfo.getSDTotalSize();
        long remain =stoInfo.getSDAvailableSize();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(total-remain, "used"));
        entries.add(new PieEntry(remain, "unused"));
        setData(entries);

        //mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
        tv_sys.setText(sysInfo.getDeviceInfo());
        tv_sto.setText("Total Storage  "+(stoInfo.getSDTotalSize()>>20)+"MB");
        tv_sto_1.setText("Remain Storage"+(stoInfo.getSDAvailableSize()>>20)+"MB");
    }
}
