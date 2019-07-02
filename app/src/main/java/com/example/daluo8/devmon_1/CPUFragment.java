package com.example.daluo8.devmon_1;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;


public class CPUFragment extends Fragment {

    private View view;
    LineChart mLineChart;
    myLineChart myChart;
    TextView tv_cpu;
    TextView tv_usage;
    TextView tv_process;
    TextView tv_mem;
    TextView tv_totalmem;
    TextView tv_availmem;
    MemInfo memInfo;


    LineChart mMemLineChart;
    myLineChart myMemChart;

    CPU cpuInfo;
    List<taskInfo> procList;
    ProcessInfo procInfo;

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
        view = inflater.inflate(R.layout.activity_cpu, container, false);

        init();
        updateData();




        return view;

    }

    private void init(){

        mLineChart = view.findViewById(R.id.chart);
        tv_cpu = view.findViewById(R.id.CRView);
        tv_usage = view.findViewById(R.id.usage);
        tv_process = view.findViewById(R.id.process);
        tv_mem = view.findViewById(R.id.memRate);
        tv_totalmem = view.findViewById(R.id.total);
        tv_availmem = view.findViewById(R.id.avail);

        mMemLineChart = view.findViewById(R.id.memchart);


        cpuInfo = new CPU();
        procInfo = new ProcessInfo();

    }




    private void updateData(){
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }
    public  void draw(){

        myChart = new myLineChart(mLineChart);
        procList = procInfo.getTaskInfos1(getActivity().getBaseContext());
        myChart.init();
        cpuRateChart();

        myMemChart = new myLineChart(mMemLineChart);
        myMemChart.init();


        String str = cpuInfo.getCpuName();
        if(str.equals("0")){

        }else {
            tv_cpu.setText("CPU:" + cpuInfo.getCpuName());
        }
        tv_process.setText("number of user process : " + String.valueOf(procInfo.procNum));

        memInfo = new MemInfo(getActivity().getBaseContext());
        memInfo.getPenter();

        tv_totalmem.setText(String.valueOf(memInfo.totalMem));

        tv_availmem.setText(String.valueOf(memInfo.availMem));


        memRateChart();
    }
    private void cpuRateChart(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 使用post方式修改UI组件的属性
                final double curRate = CPU.getCPURateDesc();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myChart.updateRight(new Entry(60, 0));
                        tv_usage.setText(String.format("usage：%d",(int)(curRate*100))+"%");
                    }
                });

                while(true){
                    final double t_curRate = CPU.getCPURateDesc();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("error of cpuRate",Log.getStackTraceString(e));
                    }

                    handler.post(new Runnable() {     //传递消息
                        @Override
                        public void run() {

                            myChart.updateRight(new Entry(60,(float)(t_curRate*100)));  //每隔1s更新

                            tv_usage.setText(String.format("usage：%d",(int)(t_curRate*100))+"%");

                        }
                    });

                }

            }

        }).start();

    }

    private void memRateChart(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 使用post方式修改UI组件的属性
                final int memRate = memInfo.getPenter();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       myMemChart.updateRight(new Entry(60, 0));
                       tv_mem.setText(String.valueOf(memRate) + "%");
                    }
                });

                while(true){
                    final int t_memRate = memInfo.getPenter();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("error of cpuRate",Log.getStackTraceString(e));
                    }



                    handler.post(new Runnable() {     //传递消息
                        @Override
                        public void run() {

                           myMemChart.updateRight(new Entry(60,(float)(t_memRate)));  //每隔1s更新

                          tv_mem.setText(String.valueOf(t_memRate) + "%");

                        }
                    });

                }

            }

        }).start();

    }

}
