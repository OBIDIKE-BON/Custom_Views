package com.stackfloat.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompoundColorSelector extends LinearLayout {

    private static final String TAG = CompoundColorSelector.class.getSimpleName();
    private ColorSelectListener mColorSelectListener = null;
    private ImageView mImgPrevious;
    private ImageView mImgNext;
    private CheckBox mEnableColor;
    private View mSelectedColor;
    private List<Integer> mColors;
    private int mSelectedColorIndex;

    public CompoundColorSelector(Context context) {
        super(context);
        init(null, 0);
    }

    public CompoundColorSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CompoundColorSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public CompoundColorSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.CompoundColorSelector, defStyleAttr, 0);

        setOrientation(HORIZONTAL);

        mColors = new ArrayList<>();

  Arrays.asList(attributes.getTextArray(R.styleable.CompoundColorSelector_colors))
              .forEach( charSequence-> mColors.add(Color.parseColor(charSequence.toString())));

        attributes.recycle();

        mSelectedColorIndex = 0;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color_selector, this);
        mImgPrevious = findViewById(R.id.img_previous);
        mImgNext = findViewById(R.id.img_next);
        mEnableColor = findViewById(R.id.enable_color);
        mSelectedColor = findViewById(R.id.selected_color);
        setColor();

        mImgPrevious.setOnClickListener(v -> selectPreviousColor());

        mImgNext.setOnClickListener(v -> selectNextColor());

        mEnableColor.setOnCheckedChangeListener((buttonView, isChecked) -> setColor());
    }

    private void selectNextColor() {
        if (mSelectedColorIndex == mColors.size() - 1) {
            mSelectedColorIndex = 0;
        } else {
            mSelectedColorIndex++;
        }
        setColor();
    }

    private void selectPreviousColor() {
        if (mSelectedColorIndex == 0) {
            mSelectedColorIndex = mColors.size() - 1;
        } else {
            mSelectedColorIndex--;
        }
        setColor();
    }

    private void setColor() {
        int color = mColors.get(mSelectedColorIndex);
        mSelectedColor.setBackgroundColor(color);
        if (mEnableColor.isChecked() && mColorSelectListener != null) {
            mColorSelectListener.onColorSelected(color);
        } else if (mColorSelectListener != null) {
            mColorSelectListener.onColorSelected(Color.TRANSPARENT);
        }
    }

    public void setColorSelectListener(ColorSelectListener colorSelectListener) {
        mColorSelectListener = colorSelectListener;
    }

    //interface for events

    interface ColorSelectListener {
        void onColorSelected(int color);
    }
//    public void onColorSeleted()
}
