package com.example.daluo8.devmon_1;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessInfo {
    public int procNum;


    /**
     * 获取系统运行的进程信息
     *
     * @param context
     * @return
     */
    public  List<taskInfo> getTaskInfos1(Context context) {
        // 应用程序管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);

        // 应用程序包管理器
        PackageManager pm = context.getPackageManager();


        List<AndroidAppProcess> RunningAppInfos = AndroidProcesses.getRunningAppProcesses();
        // 遍历运行的程序,并且获取其中的信息

        List<taskInfo> infoList  = new ArrayList<taskInfo>();


        for (AndroidAppProcess processInfo : RunningAppInfos) {
            taskInfo p = new taskInfo();

           p.setName(processInfo.name);

            android.os.Debug.MemoryInfo[] memoryInfos = am
                    .getProcessMemoryInfo(new int[] { processInfo.pid });
            long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024L;
            p.setMemsize(memsize);




            Stat stat = null;
            try {
                stat = processInfo.stat();
            } catch (IOException e) {
                Log.e("stat exception",e.toString());
            }
            int pid = stat.getPid();


            int parentProcessId = stat.ppid();
            long startTime = stat.stime();
            int policy = stat.policy();
            char state = stat.state();

            Statm statm = null;
            try {
                statm = processInfo.statm();
            } catch (IOException e) {
                Log.e("stat exception",e.toString());
            }
            long totalSizeOfProcess = statm.getSize();
            long residentSetSize = statm.getResidentSetSize();



            PackageInfo packageInfo = null;
            try {
                packageInfo = processInfo.getPackageInfo(context, 0);
                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                p.setPackname(appName);

                ApplicationInfo applicationInfo = pm.getApplicationInfo(
                        packageInfo.packageName, 0);
                Drawable icon = pm.getApplicationIcon(applicationInfo);
                p.setIcon(icon);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("stat exception",e.toString());
            }




            try {
                infoList.add(p);
            }catch (NullPointerException e){
                Log.e("p is null",e.toString());
            }


        }
        procNum = infoList.size();
        return infoList;
    }


}

class taskInfo {
    // 应用程序的图标
    private Drawable icon;
    // 应用程序的名字
    private String name;
    // 应用程序的包名
    private String packname;
    // 占用内存的大小
    private long memsize;
    // true 用户进程 false 系统进程
    private boolean userTask;

    //是否已经勾选
    private boolean cbchecked;

    public boolean isCbchecked() {
        return cbchecked;
    }

    public void setCbchecked(boolean cbchecked) {
        this.cbchecked = cbchecked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public boolean isUserTask() {
        return userTask;
    }

    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    @Override
    public String toString() {
        return  "name=" + name + ", packname="
                + packname + ", memsize=" + memsize + ", userTask=" + userTask
                + "]";
    }
}

    /*public static List<TaskInfo> getTaskInfos1(Context context) {
        // 应用程序管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);

        // 应用程序包管理器
        PackageManager pm = context.getPackageManager();

        // 获取正在运行的程序信息, 就是以下粗体的这句代码,获取系统运行的进程     要使用这个方法，需要加载
              //  import com.jaredrummler.android.processes.ProcessManager;
              //  import com.jaredrummler.android.processes.models.AndroidAppProcess;  这两个包, 这两个包附件可以下载

        List<AndroidAppProcess> processInfos = ProcessManager.getRunningAppProcesses();

        List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
        // 遍历运行的程序,并且获取其中的信息
        for (AndroidAppProcess processInfo : processInfos) {
            TaskInfo taskinfo = new TaskInfo();
            // 应用程序的包名
            String packname = processInfo.name;
            taskinfo.setPackname(packname);
            // 湖区应用程序的内存 信息
            android.os.Debug.MemoryInfo[] memoryInfos = am
                    .getProcessMemoryInfo(new int[] { processInfo.pid });
            long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024L;
            taskinfo.setMemsize(memsize);
            try {
                // 获取应用程序信息
                ApplicationInfo applicationInfo = pm.getApplicationInfo(
                        packname, 0);
                Drawable icon = applicationInfo.loadIcon(pm);
                taskinfo.setIcon(icon);
                String name = applicationInfo.loadLabel(pm).toString();
                taskinfo.setName(name);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // 用户进程
                    taskinfo.setUserTask(true);
                } else {
                    // 系统进程
                    taskinfo.setUserTask(false);
                }
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // 系统内核进程 没有名称
                taskinfo.setName(packname);
                Drawable icon = context.getResources().getDrawable(
                        R.drawable.default_icon);
                taskinfo.setIcon(icon);
            }
            if (taskinfo != null) {
                taskinfos.add(taskinfo);
            }
        }*/