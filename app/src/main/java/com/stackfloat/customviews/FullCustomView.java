package com.stackfloat.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class FullCustomView extends View {

    private Paint mPaint;
    private float mPaddingTop;
    private float mPaddingLeft;
    private float mPaddingRight;
    private float mPaddingBottom;
    private List<Integer> mColors = new ArrayList<>();
    private Drawable mDialDrawable;
    private int mHalfDialDiameter;
    private float mDialDiameter;
    private float mTickMarkRadius;
    private float mExtraPadding;
    private float mTotalPaddingBottom;
    private float mTotalPaddingRight;
    private float mTotalPaddingTop;
    private float mTotalPaddingLeft;
    private float mCenterX;
    private float mCenterY;
    private float mHalfExtraPadding;
    private float mTickMarkCenterY;
    private float mTickMarkCenterX;
    private int selectedPosition;
    private int mSnapAngle=0;
    private float mRotationDegree;
    private Drawable mNoColor;
    private int mHeight;
    private int mWidth;

    public FullCustomView(Context context) {
        super(context);
        init(null, 0);
    }

    public FullCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FullCustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FullCustomView, defStyle, 0);

        mColors.add(Color.TRANSPARENT);
        if (a.hasValue(R.styleable.FullCustomView_colors)) {
            Arrays.asList(a.getTextArray(R.styleable.FullCustomView_colors))
                    .forEach(color -> mColors.add(getColor(color)));
        } else {
            mColors.add(Color.LTGRAY);
            mColors.add(Color.YELLOW);
            mColors.add(Color.BLUE);
            mColors.add(Color.GREEN);
            mColors.add(Color.RED);
            mColors.add(Color.BLACK);
        }

        mTickMarkRadius = a.getDimension(R.styleable.FullCustomView_tickMarkRadius, toDP(10));
        mDialDiameter = a.getDimension(R.styleable.FullCustomView_dialSize, toDP(100));

        a.recycle();

        // Set up a default  Paint object
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.CYAN);
        mExtraPadding = (mTickMarkRadius * 2) + 10;
        mHalfExtraPadding = mExtraPadding / 2;
        mRotationDegree = 360f / mColors.size();
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        mDialDrawable = getDrawable(R.drawable.ic_dial);
        mNoColor = getDrawable(R.drawable.ic_no_color);
        mDialDrawable.setTint(Color.DKGRAY);
        mHalfDialDiameter = (int) (mDialDiameter / 2f);

        mDialDrawable.setBounds(-mHalfDialDiameter, -mHalfDialDiameter,
                mHalfDialDiameter, mHalfDialDiameter);
        mNoColor.setBounds((int) -mTickMarkRadius, (int) -mTickMarkRadius,
                (int) mTickMarkRadius, (int) mTickMarkRadius);
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setUpInitialValues(w, h);
    }

    private void setUpInitialValues(int w, int h) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.

        int saveCanvas = canvas.save();

        mColors.forEach(color -> drawTickMark(color, canvas));

        canvas.restoreToCount(saveCanvas);
        saveCanvas = canvas.save();

        canvas.translate(mTickMarkCenterX, mTickMarkCenterY);
        mNoColor.draw(canvas);
        canvas.restoreToCount(saveCanvas);
        canvas.rotate(mSnapAngle, mCenterX, mCenterY);
        canvas.translate(mCenterX, mCenterY);
        mDialDrawable.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

        mTotalPaddingLeft = getPaddingLeft() + mExtraPadding;
        mTotalPaddingTop = getPaddingTop() + mExtraPadding;
        mTotalPaddingRight = getPaddingRight() + mExtraPadding;
        mTotalPaddingBottom = getPaddingBottom() + mExtraPadding;

        mCenterY = mTotalPaddingTop + mHalfDialDiameter;
        mCenterX = mTotalPaddingLeft + mHalfDialDiameter;

        mTickMarkCenterX = mCenterX;
        mTickMarkCenterY = mTotalPaddingTop - mHalfExtraPadding;
        int desiredHeight = (int) (mTotalPaddingTop + mTotalPaddingBottom + mDialDiameter);
        int desiredWidth = (int) (mTotalPaddingLeft + mTotalPaddingRight + mDialDiameter);
        mHeight = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);
        mWidth = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
        setMeasuredDimension(mWidth, mHeight);
    }

    private void drawTickMark(Integer color, Canvas canvas) {
        mPaint.setColor(color);
        canvas.drawCircle(mTickMarkCenterX, mTickMarkCenterY, mTickMarkRadius, mPaint);
        canvas.rotate(mRotationDegree, mCenterX, mCenterY);
    }

    private Drawable getDrawable(int drawableRes) {
        return ResourcesCompat.getDrawable(getContext().getResources(), drawableRes, null);
    }

    private int getColor(CharSequence charSequence) {
        return Color.parseColor(charSequence.toString());
    }

    private int toDP(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) value, getContext().getResources().getDisplayMetrics());
    }

    private float cartasianToPolar(float x, float y) {
        float angle = (float) Math.toDegrees(Math.atan2(y, x));

        return (angle >= -180 && angle <= 0) ? angle + 360f : angle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_MOVE) {
            if (getSnapAngle(x, y)) {
                broadcastColor();
                invalidate();
            }

        }
        return true;
    }

    private boolean getSnapAngle(float x, float y) {
        float draAngle = cartasianToPolar(x - (mWidth / 2f), (mHeight - y) - (mHeight / 2f));
        int nearestAngle= (int) (getNearestAngle(draAngle)/mRotationDegree);
        float newAngle = nearestAngle * mRotationDegree;
        boolean shouldUpdate=false;
        if (newAngle != mSnapAngle) {
            shouldUpdate=true;
            selectedPosition = nearestAngle;
        }
        mSnapAngle= (int) newAngle;
        return shouldUpdate;
    }

    private float getNearestAngle(float draAngle) {
        float adjustedAngle = (360f - draAngle) + 90;
        while (adjustedAngle>360f)
            adjustedAngle-=360f;
        return adjustedAngle;
    }


    // interface for selecting color
    interface ColorSelectListener{
        void onColorSelected(int color);
    }

    public void setColorSelectListener(ColorSelectListener colorSelectListener) {
        mColorSelectListener = colorSelectListener;
    }

    private ColorSelectListener mColorSelectListener= null;

    private void broadcastColor(){
        if (mColorSelectListener!= null){
            mColorSelectListener.onColorSelected(mColors.get(selectedPosition));
        }
    }
}
