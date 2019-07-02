package com.example.daluo8.devmon_1;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

public class GPUFragment extends Fragment {
    TextView tv_renderer;
    TextView tv_vendor;
    TextView tv_version;
    TextView tv_extensions;
    GLSurfaceView glView;
    View view;
    FrameLayout temp;
    GPUInfo gpuInfo;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    draw();
                    break;
                case 0x002:
                    List<String> m = (List<String>) msg.obj;
                  tv_renderer.setText(m.get(0));
                  tv_vendor.setText(m.get(1));
                  tv_version.setText(m.get(2));
                  tv_version.setText(m.get(3));




                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_gpu, container, false);

        init();
        updateData();




        return view;

    }

    private void init(){
        tv_renderer = view.findViewById(R.id.renderer);
        tv_vendor = view.findViewById(R.id.vendor);
        tv_extensions = view.findViewById(R.id.extensions);
        tv_version = view.findViewById(R.id.version);
        //glView = view.findViewById(R.id.surfaceView);
        temp = view.findViewById(R.id.temp);
        gpuInfo = new GPUInfo(handler);

    }

    private void updateData(){
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }
    public  void draw(){

        GLSurfaceView glView = new DemoGLSurfaceView(getContext());
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glView.setZOrderOnTop(false);

        temp.addView(glView);


    }

    void updateInfo(){
        tv_renderer.setText(gpuInfo.gl_renderer);
        tv_vendor.setText(gpuInfo.gl_vendor);
        tv_version.setText(gpuInfo.gl_version);
        tv_extensions.setText(gpuInfo.gl_extensions);


    }

    class DemoGLSurfaceView extends GLSurfaceView {

        GPUInfo mRenderer;
        public DemoGLSurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(8,8,8,8,0,0);
            mRenderer = new GPUInfo(handler);
            setRenderer(mRenderer);
        }
    }
}
