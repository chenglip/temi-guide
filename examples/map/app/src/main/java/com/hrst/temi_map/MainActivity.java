package com.hrst.temi_map;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.map.MapDataModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements
        OnRobotReadyListener {

    private String TAG = "MainActivity";

    private Robot mRobot;
    private Bitmap mBitmap;
    private MapDataModel mMapDataModel;
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    Runnable task = () -> {
        Log.i(TAG, "New thread started");
        mMapDataModel = mRobot.getMapData();
        Log.i(TAG, mMapDataModel.getMapImage().toString());
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRobot = Robot.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRobot.addOnRobotReadyListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRobot.removeOnRobotReadyListener(this);
    }

    @Override
    public void onRobotReady(boolean b) {
        if (mRobot.isReady()) {
            mExecutorService.submit(task);
            mExecutorService.shutdown();
        }
    }
}