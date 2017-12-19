package com.jian.android.runtimeview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by M_fly on 2017/12/19.
 */

public class RunTimeView extends View{

    private static final int DEFAULT_TAB_TEXTSIZE=12;
    private static final int DEFAULT_TAB_TEXTCOLOR= Color.BLACK;
    private static final int DEFAULT_UP_SPACE=10;
    private static final int DEFAULT_DES_TEXTSIZE=20;
    private static final int DEFAULT_DES_TEXTCOLOR=Color.BLACK;
    private static final int DEFAULT_DOWN_SPACE=20;

    private int mTabTextColor;
    private int mTabTextSize;
    private int mUpSpace;
    private int mDesTextColor;
    private int mDesTextSize;
    private int mDownSpace;
    private Paint mTabPaint;
    private Paint.FontMetrics fmTab;
    private float mTabHeight;
    private Paint mDesPaint;
    private Paint.FontMetrics fmDes;
    private float mDesHeight;
    private int mRectWidth=380;
    private int mRectHeight=200;
    private int mLineHeight=10;
    private Paint mRectPaint;
    private String mDesText="运行时间(小时)";
    private int mPerWidth;
    private Paint mColorPaint;
    private int mCurrentValue;

    public RunTimeView(Context context) {
        this(context,null);
    }

    public RunTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RunTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        initPaint();
    }

    private void initAttrs(Context context,AttributeSet attrs){
        final TypedArray attributes=context.obtainStyledAttributes(attrs,R.styleable.RunTimeView);
        mTabTextColor=attributes.getColor(R.styleable.RunTimeView_tab_textColor,DEFAULT_TAB_TEXTCOLOR);
        mTabTextSize=attributes.getDimensionPixelSize(R.styleable.RunTimeView_tab_textSize,(int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, DEFAULT_TAB_TEXTSIZE, getResources().getDisplayMetrics()));
        mUpSpace= (int) attributes.getDimension(R.styleable.RunTimeView_top_space,DEFAULT_UP_SPACE);
        mDesTextColor=attributes.getColor(R.styleable.RunTimeView_des_textColor,DEFAULT_DES_TEXTCOLOR);
        mDesTextSize=attributes.getDimensionPixelSize(R.styleable.RunTimeView_des_textSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,DEFAULT_DES_TEXTSIZE,getResources().getDisplayMetrics()));
        mDownSpace= (int) attributes.getDimension(R.styleable.RunTimeView_down_space,DEFAULT_DOWN_SPACE);
        attributes.recycle();
    }

    private void initPaint(){
        mTabPaint = new Paint();
        mTabPaint.setAntiAlias(true);
        mTabPaint.setTextSize(mTabTextSize);
        mTabPaint.setColor(mTabTextColor);
        mTabPaint.setTextAlign(Paint.Align.CENTER);
        fmTab = mTabPaint.getFontMetrics();
        mTabHeight = fmTab.bottom- fmTab.top;

        mDesPaint = new Paint();
        mDesPaint.setTextSize(mDesTextSize);
        mDesPaint.setColor(mDesTextColor);
        mDesPaint.setAntiAlias(true);
        fmDes = mDesPaint.getFontMetrics();
        mDesHeight = fmDes.bottom- fmDes.top;

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(Color.BLACK);
        mRectPaint.setStrokeWidth(5);
        mRectPaint.setStyle(Paint.Style.STROKE);

        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int mWidth,mHeight;
        if (widthMode==MeasureSpec.EXACTLY){
            mWidth=widthSize;
        }else {
            mWidth=getPaddingLeft()+mRectWidth+getPaddingRight();
            if (widthMode==MeasureSpec.AT_MOST){
                mWidth=Math.min(mWidth,widthSize);
            }
        }

        if (heightMode==MeasureSpec.EXACTLY){
            mHeight=heightSize;
        }else {
            mHeight= (int) (getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+mRectHeight+mDownSpace+mDesHeight+getPaddingBottom());
            if (heightMode==MeasureSpec.AT_MOST){
                mHeight=Math.min(mHeight,heightSize);
            }
        }
        setMeasuredDimension(mWidth,mHeight);
        mRectWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
        mRectHeight= (int) (getMeasuredHeight()-getPaddingTop()-mTabHeight-mUpSpace-mLineHeight-mDownSpace-mDesHeight-getPaddingBottom());
        mPerWidth = mRectWidth/10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Rect mRect=new Rect(getPaddingLeft(),(int)(getPaddingTop()+mTabHeight+mUpSpace+mLineHeight),
                getMeasuredWidth()-getPaddingRight(),(int)(getMeasuredHeight()-mDownSpace-mDesHeight-getPaddingBottom()));
        canvas.drawRect(mRect,mRectPaint);

        for (int i=1;i<10;i++){
            canvas.drawLine(getPaddingLeft()+mPerWidth*i,getPaddingTop()+mTabHeight+mUpSpace,
                    getPaddingLeft()+mPerWidth*i,getPaddingTop()+mTabHeight+mUpSpace+mLineHeight,mRectPaint);
            canvas.drawText(String.valueOf(i*10),getPaddingLeft()+mPerWidth*i,getPaddingTop()-fmTab.top,mTabPaint);
        }

        float mDesWidth=mDesPaint.measureText(mDesText);
        canvas.drawText(mDesText,getMeasuredWidth()/2-mDesWidth/2,getPaddingTop()+mTabHeight+mUpSpace
                +mLineHeight+mRectHeight+mDownSpace-fmDes.top,mDesPaint);

        Rect mGreenRect=new Rect(getPaddingLeft()+2,(int)(getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+2),
                getPaddingLeft()+mPerWidth*6,(int)(getMeasuredHeight()-mDownSpace-mDesHeight-getPaddingBottom())-2);
        mColorPaint.setColor(Color.GREEN);
        canvas.drawRect(mGreenRect,mColorPaint);

        Rect mYellowRect=new Rect(getPaddingLeft()+mPerWidth*6,(int)(getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+2),
                getPaddingLeft()+mPerWidth*8,(int)(getMeasuredHeight()-mDownSpace-mDesHeight-getPaddingBottom())-2);
        mColorPaint.setColor(Color.YELLOW);
        canvas.drawRect(mYellowRect,mColorPaint);

        Rect mThirdRect=new Rect(getPaddingLeft()+mPerWidth*8,(int)(getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+2),
                getPaddingLeft()+mPerWidth*9,(int)(getMeasuredHeight()-mDownSpace-mDesHeight-getPaddingBottom())-2);
        mColorPaint.setColor(Color.parseColor("#FF9900"));
        canvas.drawRect(mThirdRect,mColorPaint);

        Rect mEndRect=new Rect(getPaddingLeft()+mPerWidth*9,(int)(getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+2),
                getMeasuredWidth()-getPaddingRight()-2,(int)(getMeasuredHeight()-mDownSpace-mDesHeight-getPaddingBottom())-2);
        mColorPaint.setColor(Color.parseColor("#FF3300"));
        canvas.drawRect(mEndRect,mColorPaint);

        canvas.drawLine(getPaddingLeft()+(float)mPerWidth/10*mCurrentValue,getPaddingTop()+mTabHeight+mUpSpace+mLineHeight,
                getPaddingLeft()+(float)mPerWidth/10*mCurrentValue,getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+40,mRectPaint);
        canvas.drawText("当前值:"+mCurrentValue,getPaddingLeft()+(float)mPerWidth/10*mCurrentValue,getPaddingTop()+mTabHeight+mUpSpace+mLineHeight+40-fmTab.top,mTabPaint);
    }

    public void setData(int data){
        ValueAnimator animator=ValueAnimator.ofInt(0,data);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentValue = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }
}
