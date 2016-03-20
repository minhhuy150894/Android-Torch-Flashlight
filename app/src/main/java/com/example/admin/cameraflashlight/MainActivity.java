package com.example.admin.cameraflashlight;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView tvStatus;
    private Button btnSwitcher;

    private static android.hardware.Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    android.hardware.Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = (TextView)this.findViewById(R.id.tvStatus);
        btnSwitcher = (Button)this.findViewById(R.id.btnTurnOnOff);

        if(isFlashSupported()){
            tvStatus.setTextColor(Color.GREEN);
            tvStatus.setText("Flash supported on your device!");
        }

        btnSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlashOn){
                    turnOFF();
                    btnSwitcher.setText("Turn ON");
                } else {
                    turnON();
                    btnSwitcher.setText("Turn OFF");
                }
            }
        });

    }

    private void turnON() {
        if(! isFlashOn){
            if(camera == null || params == null){
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOFF() {
        if(isFlashOn){
            if(camera == null || params == null){
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }

    private void cameraInitialize(){
        if(camera == null){
            try{
                camera = android.hardware.Camera.open();
                params = camera.getParameters();
            } catch(Exception ex){
                Toast.makeText(getApplicationContext(), "Something went wrong! :D", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isFlashSupported(){
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraInitialize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraInitialize();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasFlash){
            turnON();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}