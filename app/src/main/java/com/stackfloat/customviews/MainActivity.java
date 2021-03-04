package com.stackfloat.customviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.view);
        CompoundColorSelector colorSelector = findViewById(R.id.color_selector);
        colorSelector.setColorSelectListener(view::setBackgroundColor);
        ExtendViewColorSelector extendViewColorSelector  = findViewById(R.id.seekBar);
        extendViewColorSelector.setColorSelectListener(view::setBackgroundColor);
        FullCustomView fullCustomView= findViewById(R.id.fullCustomView);
        fullCustomView.setColorSelectListener(view::setBackgroundColor);
    }
}