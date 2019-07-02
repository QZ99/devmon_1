package com.example.daluo8.devmon_1;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class netFragment extends Fragment {

    TextView tv_ip ;
    TextView tv_mac;

    View view;
    NetInfo netInfo;
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
        view = inflater.inflate(R.layout.activity_net, container, false);

        init();
        updateData();




        return view;

    }

    private void init(){
        tv_ip = view.findViewById(R.id.ip);
        tv_mac = view.findViewById(R.id.mac);


        netInfo = new NetInfo();

    }

    private void updateData(){
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }
    public  void draw(){
        tv_ip.setText("local ip address : "+netInfo.getLocalIpAddress());
        tv_mac.setText("MAC address : "+netInfo.getMacAddress());


    }


}
