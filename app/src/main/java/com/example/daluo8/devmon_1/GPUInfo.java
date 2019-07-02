package com.example.daluo8.devmon_1;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GPUInfo implements GLSurfaceView.Renderer {


    Handler handler;
    public String gl_renderer;

    //GPU 供应商
    public String gl_vendor;

    //GPU 版本
    public String gl_version;

    //GPU  扩展名
    public String gl_extensions;
    GPUInfo(Handler handler){
        this.handler = handler;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl_renderer = gl.glGetString(GL10.GL_RENDERER);
        gl_vendor = gl.glGetString(GL10.GL_VENDOR);
        gl_version = gl.glGetString(GL10.GL_VERSION);
        gl_extensions = gl.glGetString(GL10.GL_EXTENSIONS);
        Log.d("suhuazhi", "onSurfaceCreated = " + gl_renderer);
        List<String> p  = new ArrayList<>();
        p.add(gl_renderer);
        p.add(gl_vendor);
        p.add(gl_version);
        p.add(gl_extensions);
        final List<String> strList = p;
        new Thread() {
            public void run() {

                Message message =new Message();
                message.what=0x002;
                message.obj=strList;
                handler.sendMessage(message);


            }
        }.start();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }




    class ScreenInfo {
        // 英寸
        public double size;

        public String sizeStr;

        // 高
        public int heightPixels;

        // 宽
        public int widthPixels;

        public String screenRealMetrics;

        // 显示器的逻辑密度
        public float density;

        // 屏幕密度为点每英寸
        public int densityDpi;

        public String densityDpiStr;

        // 显示在显示器的字体的定标因子
        public float scaledDensity;

        // 每在 X 维屏幕英寸的确切物理像素
        public float xdpi;

        // 每在 Y 维屏幕英寸的确切物理像素
        public float ydpi;

        //在屏幕中显示的参考密度
        public int density_default;

        @Override
        public String toString() {
            return "ScreenInfo{" +
                    "size=" + size +
                    ", sizeStr='" + sizeStr + '\'' +
                    ", heightPixels=" + heightPixels +
                    ", screenRealMetrics='" + screenRealMetrics + '\'' +
                    ", widthPixels=" + widthPixels +
                    ", density=" + density +
                    ", densityDpi=" + densityDpi +
                    ", densityDpiStr='" + densityDpiStr + '\'' +
                    ", scaledDensity=" + scaledDensity +
                    ", xdpi=" + xdpi +
                    ", ydpi=" + ydpi +
                    ", density_default=" + density_default +
                    '}';
        }

    }
    public  ScreenInfo getScreenInfo(Context mContext) {
        ScreenInfo result = new ScreenInfo();
        int widthPixels;
        int heightPixels;

        WindowManager w = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        result.widthPixels = widthPixels;
        result.heightPixels = heightPixels;
        result.screenRealMetrics = widthPixels + " X " + heightPixels;
        result.density = metrics.density;
        result.density_default = metrics.DENSITY_DEFAULT;
        result.densityDpi = metrics.densityDpi;
        result.densityDpiStr = metrics.densityDpi + " dpi";
        result.scaledDensity = metrics.scaledDensity;
        result.xdpi = metrics.xdpi;
        result.ydpi = metrics.ydpi;
        result.size = (Math.sqrt(Math.pow(widthPixels, 2) + Math.pow(heightPixels, 2)) / metrics.densityDpi);
        result.sizeStr = String.format("%.2f", result.size); //+ mContext.getResources().getString(R.string.sys_inches_unit);

        return result;
    }





}
