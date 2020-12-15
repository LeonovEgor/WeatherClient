package ru.leonov.weather.sensorsView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import ru.leonov.weather.R;

public class SensorView extends View {
    private final int PROGRESS_HEIGHT = 5;

    private String paramName;
    private String paramValue;
    private String paramDimension;
    private int progressPercent;

    private float mTextHeight;
    private float mTextWidth;

    private int textColor = Color.BLACK;
    private float textDimension = 0;
    private Drawable mDrawable;

    private TextPaint mTextPaint;
    private Paint mRectPaint;
    private Rect mTextBoundRect = new Rect();

    public SensorView(Context context) {
        super(context);
        init(null, 0);
    }

    public SensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SensorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SensorView, defStyle, 0);

        paramName = a.getString(R.styleable.SensorView_paramName);
        paramValue = a.getString(R.styleable.SensorView_paramValue);
        paramDimension = a.getString(R.styleable.SensorView_paramDimension);
        progressPercent = a.getInt(R.styleable.SensorView_progressPercent, 0);
        textColor = a.getColor(R.styleable.SensorView_textColor, textColor);
        textDimension = a.getDimension(R.styleable.SensorView_textDimension, textDimension);
        if (a.hasValue(R.styleable.SensorView_Drawable)) {
            mDrawable = a.getDrawable(R.styleable.SensorView_Drawable);
            if (mDrawable != null) {
                mDrawable.setCallback(this);
            }
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mRectPaint = new Paint();
        mRectPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(textDimension);
        mTextPaint.setColor(textColor);

        String text = getFullText();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBoundRect);
        mTextHeight = mTextBoundRect.height();
        mTextWidth = mTextBoundRect.width();

        mRectPaint.setColor(textColor);
    }

    private String getFullText() {
        return String.format("%s %s %s", paramName, paramValue, paramDimension);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = getResources().getDisplayMetrics().widthPixels;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            int iHeight = Math.round(mTextHeight);
            height = Math.min(iHeight + getPaddingBottom() + getPaddingTop() + PROGRESS_HEIGHT, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight();

        // Draw the text.
        canvas.drawText(getFullText(), paddingLeft, contentHeight - paddingTop - paddingBottom, mTextPaint);
        // Draw the progress (value)
        drawProgress(canvas, paddingLeft, contentHeight - paddingTop, Math.round(mTextWidth));

        // Draw the example drawable on top of the text.
        if (mDrawable != null) {
            mDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mDrawable.draw(canvas);
        }
    }

    private void drawProgress(Canvas canvas, float left, float top, float width) {
        mRectPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, left + width, top + PROGRESS_HEIGHT, mRectPaint);

        float percentWidth = (width/100)*progressPercent;
        mRectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, left + percentWidth, top+ PROGRESS_HEIGHT, mRectPaint);
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String name) {
        paramName = name;
        invalidateTextPaintAndMeasurements();
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String value) {
        paramValue = value;
        invalidateTextPaintAndMeasurements();
    }

    public String getParamDimension() {return paramDimension;}

    public void setParamDimension(String dimension) {
        paramDimension = dimension;
        invalidateTextPaintAndMeasurements();
    }

    public int getProgressPercent() {return progressPercent;}

    public void setProgressPercent(int percent) {
        progressPercent = percent;
        invalidate();
    }

    public void setTextColor(int color) {
        textColor = color;
        invalidateTextPaintAndMeasurements();
    }

    public void setTextSize(float size) {
        textDimension = size;
        invalidateTextPaintAndMeasurements();
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
