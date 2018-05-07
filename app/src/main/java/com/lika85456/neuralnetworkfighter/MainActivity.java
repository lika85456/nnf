package com.lika85456.neuralnetworkfighter;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private static final String TAG = "SurfaceView";
    private MySurfaceView surfaceView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new MySurfaceView(this);
        surfaceView.setWillNotDraw(false);
        setContentView(surfaceView);

    }


}