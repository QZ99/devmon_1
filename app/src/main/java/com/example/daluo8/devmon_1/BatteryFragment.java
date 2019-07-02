package com.example.daluo8.devmon_1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.EXTRA_HEALTH;
import static android.os.BatteryManager.EXTRA_TEMPERATURE;
import static android.os.BatteryManager.EXTRA_VOLTAGE;

public class BatteryFragment extends Fragment {
    private  Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    draw();
                    break;
                case 0x002:
                    tvBattery.setText((String)msg.obj);
                    break;

                default:
                    break;
            }
        }

        ;
    };
    View view;
   TextView tvBattery;
    LocalBroadcastManager broadcastManager;
    IntentFilter filter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_battery, container, false);

        init();
        updateData();

        return view;

    }

    private void init() {
        tvBattery = view.findViewById(R.id.tv_battery);
    }

    private void updateData() {
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }

    public void draw() {
        tvBattery.setText(((MainActivity)getActivity()).getStr1());
       /* new Thread() {
            public void run() {
                while(true){
                    tvBattery.setText(((MainActivity)getActivity()).getStr1());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();*/
    }

 /* static TextView postMsg(){
        return tvBattery;
  }*/

}
