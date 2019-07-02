package com.example.daluo8.devmon_1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.EXTRA_HEALTH;
import static android.os.BatteryManager.EXTRA_TEMPERATURE;
import static android.os.BatteryManager.EXTRA_VOLTAGE;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView cpuRateView;


    CPUFragment cpu_Fragment;
    GPUFragment gpu_Fragment;
    BatteryFragment batt_Fragment;
    netFragment net_Fragment;

    sysFragment sys_Fragment;
    private List<Fragment> fragments;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();

        if (id == R.id.cpu) {
            switchTo(R.layout.activity_cpu);

        }  else if (id == R.id.sys) {
            switchTo(R.layout.activity_sys);

        } else if (id == R.id.gpu) {
            switchTo(R.layout.activity_gpu);

        } else if (id == R.id.battery) {
            switchTo(R.layout.activity_battery);

        }
       else if (id == R.id.net) {
            switchTo(R.layout.activity_net);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
   // TextView tv ;
    void init(){


        cpu_Fragment = new CPUFragment();
        gpu_Fragment = new GPUFragment();

        net_Fragment = new netFragment();
        batt_Fragment = new BatteryFragment();
        sys_Fragment = new sysFragment();
        fragments = new ArrayList<Fragment>();
        fragments.add(cpu_Fragment);
        fragments.add(gpu_Fragment);

        fragments.add(net_Fragment);

        fragments.add(sys_Fragment);
        fragments.add(batt_Fragment);

    switchTo(R.layout.activity_cpu);

    }

    void switchTo(int id){

        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();

        //先隐藏所有的Fragment
        hideFragments(transaction);

        //切换Fragment
        switch (id) {
            case R.layout.activity_cpu:
                transaction.replace(R.id.id_content, cpu_Fragment);
                transaction.show(cpu_Fragment);
                break;
            case R.layout.activity_gpu:
                transaction.replace(R.id.id_content, gpu_Fragment);
                transaction.show(gpu_Fragment);
                break;
            case R.layout.activity_net:
                transaction.replace(R.id.id_content, net_Fragment);
                transaction.show(net_Fragment);
                break;
            case R.layout.activity_sys:
                transaction.replace(R.id.id_content, sys_Fragment);
                transaction.show(sys_Fragment);
                break;
            case R.layout.activity_battery:
                transaction.replace(R.id.id_content, batt_Fragment);
                transaction.show(batt_Fragment);
                break;

        }
        transaction.commit();
    }
    private void hideFragments(FragmentTransaction transaction) {
        for(int i  = 0;i<fragments.size();i++){
            if(fragments.get(i) != null){
                transaction.hide(fragments.get(i));
            }
        }

    }
    @Override
    protected void onResume() {
         super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(new mReceiver(), filter);



    }

String str1 = "";

    public  String getStr1(){
        return str1;
    }

    public class mReceiver extends BroadcastReceiver {
        private TextView pow;



        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("myInfo","re");
            String action = intent.getAction();
            //if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            // 得到电池状态：
            // BatteryManager.BATTERY_STATUS_CHARGING：充电状态。
            // BatteryManager.BATTERY_STATUS_DISCHARGING：放电状态。
            // BatteryManager.BATTERY_STATUS_NOT_CHARGING：未充满。
            // BatteryManager.BATTERY_STATUS_FULL：充满电。
            // BatteryManager.BATTERY_STATUS_UNKNOWN：未知状态。
            int status = intent.getIntExtra("status", 0);
            // 得到健康状态：
            // BatteryManager.BATTERY_HEALTH_GOOD：状态良好。
            // BatteryManager.BATTERY_HEALTH_DEAD：电池没有电。
            // BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE：电池电压过高。
            // BatteryManager.BATTERY_HEALTH_OVERHEAT：电池过热。
            // BatteryManager.BATTERY_HEALTH_UNKNOWN：未知状态。
            int health = intent.getIntExtra("health", 0);

            int mhealth = intent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN);
            // boolean类型
            boolean present = intent.getBooleanExtra("present", false);
            // 得到电池剩余容量
            int level = intent.getIntExtra("level", 0);
            // 得到电池最大值。通常为100。
            int scale = intent.getIntExtra("scale", 0);
            // 得到图标ID
            int icon_small = intent.getIntExtra("icon-small", 0);
            // 充电方式：　BatteryManager.BATTERY_PLUGGED_AC：AC充电。　BatteryManager.BATTERY_PLUGGED_USB：USB充电。
            int plugged = intent.getIntExtra("plugged", 0);
            // 得到电池的电压
            int voltage = intent.getIntExtra(EXTRA_VOLTAGE, -1);
            // 得到电池的温度,0.1度单位。例如 表示197的时候，意思为19.7度
            //int temperature = intent.getIntExtra("temperature", 0);
            int temperature = intent.getIntExtra(EXTRA_TEMPERATURE, -1);
            // 得到电池的类型
            String technology = intent.getStringExtra("technology");
            // 得到电池状态
            String statusString = "";
            // 根据状态id，得到状态字符串
            switch (status) {
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusString = "unknown";
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusString = "charging";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusString = "discharging";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    statusString = "not charging";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    statusString = "full";
                    break;
            }
            //得到电池的寿命状态
            String healthString = "";
            //根据状态id，得到电池寿命
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    healthString = "unknown";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthString = "good";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthString = "overheat";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthString = "dead";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthString = "voltage";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthString = "unspecified failure";
                    break;
            }
            //得到充电模式
            String acString = "";
            //根据充电状态id，得到充电模式
            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    acString = "plugged ac";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    acString = "plugged usb";
                    break;
            }
            //显示电池信息
                /*tv_mac.setText("电池的状态：" + statusString
                        + "\n健康值: "+ healthString
                        + "\n电池剩余容量： " + level
                        + "\n电池的最大值：" + scale
                        + "\n小图标：" + icon_small
                        + "\n充电方式：" + plugged
                        + "\n充电方式: " + acString
                        + "\n电池的电压：" + voltage
                        + "\n电池的温度：" + (float) temperature * 0.1
                        + "\n电池的类型：" + technology
                        + "\nmhealth:"+ mhealth);*/

            str1 =  "电池的状态：" + statusString
                    + "\n健康值: "+ healthString
                    + "\n电池剩余容量： " + level
                    + "\n电池的最大值：" + scale

                    + "\n充电方式：" + plugged
                    + "\n充电方式: " + acString
                    + "\n电池的电压：" + voltage
                    + "\n电池的温度：" + (float) temperature * 0.1
                    + "\n电池的类型：" + technology
                    + "\nmhealth:"+ mhealth;
        }
    }

}


