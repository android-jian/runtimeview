package com.jian.android.runtimeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RunTimeView mRunTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRunTimeView = (RunTimeView) findViewById(R.id.mRunTimeView);
        mRunTimeView.setData(66);
    }
}
