package com.stackfloat.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtendViewColorSelector extends AppCompatSeekBar {

    private ColorSelectListener mColorSelectListener= null;

    private float mDensity;
    private List<CharSequence> mColors;
    private int mSpacing;
    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;
    private Paint mPaint;
    private float mShapeSize;
    private float mHalfShapeSize;
    private int mCount;
    private Drawable mDrawable;

    public ExtendViewColorSelector(Context context) {
        super(context);
        init(null, 0);
    }

    public ExtendViewColorSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ExtendViewColorSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.ExtendViewColorSelector, defStyleAttr, 0);

        mColors = new ArrayList<>(
                Arrays.asList(attributes.getTextArray(R.styleable.ExtendViewColorSelector_colors)));

        attributes.recycle();

        mDensity = getContext().getResources().getDisplayMetrics().density;

        mShapeSize = 16 * mDensity;
        mHalfShapeSize = mShapeSize / 2;
        mCount = mColors.size() + 1;
        mDrawable = getDrawable(R.drawable.ic_no_color);

        setMax(mCount - 1);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                (int) (getPaddingBottom() + mHalfShapeSize));
        setThumb(getDrawable(R.drawable.ic_baseline_arrow_drop_down_24));
        setProgressTintList(
                ContextCompat.getColorStateList(getContext(), android.R.color.transparent));
        setSplitTrack(false);
        setProgressBackgroundTintList(
                ContextCompat.getColorStateList(getContext(), android.R.color.transparent));

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);


        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mColorSelectListener!=null){
                    if (progress==0){
                        mColorSelectListener.onColorSelected(android.R.color.transparent);
                    }else {
                        mColorSelectListener.onColorSelected(getColor(mColors.get(progress-1)));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Drawable getDrawable(int drawableRes) {
        return ResourcesCompat.getDrawable(getContext().getResources(), drawableRes, null);
    }

    private int getColor(CharSequence charSequence) {
        return Color.parseColor(charSequence.toString());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mSpacing = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / (mCount - 1);
        mLeft = -mHalfShapeSize;
        mTop = mShapeSize - mHalfShapeSize;
        mRight = mHalfShapeSize;
        mBottom = mShapeSize + mHalfShapeSize;
        int canvasState = canvas.save();
        canvas.translate(getPaddingLeft(),
                ((getHeight() - getPaddingBottom() - getPaddingTop()) / 2f));

        drawNoColor(canvas);
        mColors.forEach(color -> drawShape(getColor(color), canvas));

        canvas.restoreToCount(canvasState);
    }

    private void drawNoColor(Canvas canvas) {

        mDrawable.setBounds((int) mLeft, (int) mTop, (int) mRight, (int) mBottom);
        mDrawable.draw(canvas);
        canvas.translate(mSpacing, 0);
    }

    private void drawShape(int color, Canvas canvas) {
        mPaint.setColor(color);
        canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);
        canvas.translate(mSpacing, 0);
    }


    interface ColorSelectListener{
        void onColorSelected(int color);
    }

    public void setColorSelectListener(ColorSelectListener colorSelectListener) {
        mColorSelectListener = colorSelectListener;
    }

}
