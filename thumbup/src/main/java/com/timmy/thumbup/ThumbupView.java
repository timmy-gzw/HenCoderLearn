package com.timmy.thumbup;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : timmy
 * @date : 2018/12/12
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class ThumbupView extends View {

    Paint mBitmapPaint;
    Paint mTextPaint;

    /**
     * 图表的宽高
     */
    private static final float THUMB_WIDTH = 20f;
    private static final float THUMB_HEIGHT = 20f;
    private static final float SHINING_HEIGHT = 16f;
    private static final float SHINING_WIDTH = 16f;

    private final int dp_2 = dip2px(2);
    private final int dp_8 = dip2px(8);

    /**
     * 文本颜色
     */
    private static final int TEXT_DEFAULT_COLOR = Color.parseColor("#cccccc");
    private static final int TEXT_DEFAULT_END_COLOR = Color.parseColor("#00cccccc");

    /**
     * 字体默认大小
     */
    private static final float TEXT_DEFAULT_SIZE = 15;

    private Bitmap mLikeUnSelectedBitmap;
    private Bitmap mLikeSelectedBitmap;
    private Bitmap mLikeSelectedShiningBitmap;

    private float mDrawablePadding;
    /**
     * nums[0]是不变的部分
     * nums[1]原来的部分
     * nums[2]变化后的部分
     */
    private String[] nums;
    private int mLikeCount = 88;

    /**
     * 当前是否点赞
     */
    private boolean mIsThumbUp;

    private int mStartX;
    private int mStartY;
    private float mTextStartX;

    /**
     * 文本的上下移动变化值
     */
    private float OFFSET_MIN;
    private float OFFSET_MAX;
    private float mOldOffsetY;
    private float mNewOffsetY;

    /**
     * 缩放动画的时间
     */
    private static final int SCALE_DURING = 150;
    /**
     * 圆圈扩散动画的时间
     */
    private static final int RADIUS_DURING = 100;

    /**
     * 最后一次点击的时间
     */
    private long mLastClickTime;
    private boolean mToBigger;

    public ThumbupView(Context context) {
        super(context);
    }

    public ThumbupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThumbupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(dip2px(TEXT_DEFAULT_SIZE));
        mTextPaint.setColor(TEXT_DEFAULT_COLOR);

        mLikeUnSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        mLikeSelectedShiningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
        mLikeSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);

        mDrawablePadding = dp_2 * 2;

        // 这里text的坐标,会偏移thunb的宽度+一个固定的padding
        mTextStartX = dip2px(THUMB_WIDTH) + mDrawablePadding;
        nums = new String[]{String.valueOf(mLikeCount), "", ""};
        // 设置点击事件
        setOnClickListener(mOnClickListener);

        OFFSET_MIN = 0;

        // 字体上下移动的距离
        OFFSET_MAX = 1.5f * sp2px(TEXT_DEFAULT_SIZE);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mLastClickTime = System.currentTimeMillis();
            if (mIsThumbUp) {
                // 取消点赞
                calculateChangeNum(-1);
                mLikeCount--;
                showThumbDownAnim();

            } else {
                // 点赞
                calculateChangeNum(1);
                mLikeCount++;
                showThumbupAnimation();
            }


        }
    };

    /**
     * 点赞的动画
     */
    private void showThumbupAnimation() {
        mIsThumbUp = true;

        ObjectAnimator offsetYAnimation = ObjectAnimator.ofFloat(this, "textOffsetY", OFFSET_MIN, OFFSET_MAX);
        offsetYAnimation.setDuration(SCALE_DURING + RADIUS_DURING);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(offsetYAnimation);
        animatorSet.start();
    }

    /**
     * 取消点赞的动画
     */
    private void showThumbDownAnim() {
        mIsThumbUp = false;

        ObjectAnimator textOffsetY = ObjectAnimator.ofFloat(this, "textOffsetY", OFFSET_MIN, -OFFSET_MAX);
        textOffsetY.setDuration(SCALE_DURING + RADIUS_DURING);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(textOffsetY);

        animatorSet.start();
    }


    private void calculateChangeNum(int changeCount) {
        if (changeCount == 0) {
            nums[0] = String.valueOf(changeCount);
            nums[1] = "";
            nums[2] = "";
            return;
        }

        mToBigger = changeCount > 0;

        // 点击之前的数
        String oldNum = String.valueOf(mLikeCount);
        // 新的点赞数
        String newNum = String.valueOf(mLikeCount + changeCount);
        // 两次点赞数的长度
        int oldNumLen = oldNum.length();
        int newNumLen = newNum.length();

        if (oldNumLen != newNumLen) {
            nums[0] = "";
            nums[1] = oldNum;
            nums[2] = newNum;
        } else {
            // 说明点赞前和点赞后没有变化
            for (int i = 0; i < oldNumLen; i++) {
                char oldC1 = oldNum.charAt(i);
                char newC1 = newNum.charAt(i);
                if (oldC1 != newC1) {
                    if (i == 0) {
                        nums[0] = "";
                    } else {
                        nums[0] = newNum.substring(0, 1);
                    }
                    nums[1] = oldNum.substring(i);
                    nums[2] = newNum.substring(i);
                    break;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getWidth(widthMeasureSpec), getHeight(heightMeasureSpec));
    }

    private static final String TAG = "gzw";

    private int getWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = getContentWidth();
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                result = Math.max(getContentWidth(), result);
                break;
            default:
                break;
        }
        return result;
    }

    private int getHeight(int measureSpec) {

        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = getContentWidth();
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                result = Math.max(getContentHeight(), result);
                break;
            default:
                break;
        }

        return result;
    }

    private int getContentWidth() {
        int result;
        result = (int) (dip2px(THUMB_WIDTH) + mDrawablePadding + mTextPaint.measureText(String.valueOf(mLikeCount)));
        result += getPaddingLeft() + getPaddingRight();
        return result;
    }

    private int getContentHeight() {
        int result;
        result = Math.max(sp2px(TEXT_DEFAULT_SIZE), dip2px(THUMB_HEIGHT + SHINING_HEIGHT) - dp_8);
        result += getPaddingTop() + getPaddingBottom();
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIcon(canvas);
        drawText(canvas);
    }

    private void drawIcon(Canvas canvas) {
        canvas.drawBitmap(mLikeUnSelectedBitmap, mStartX, mStartY + dp_8, mBitmapPaint);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        float y = (dip2px(THUMB_HEIGHT + SHINING_HEIGHT) - fontMetricsInt.bottom - fontMetricsInt.top) / 2;

        mTextPaint.setColor(TEXT_DEFAULT_COLOR);
        canvas.drawText(String.valueOf(nums[0]), mStartX + mTextStartX, mStartY + y, mTextPaint);

        String text = String.valueOf(mLikeCount);
        float textWidth = mTextPaint.measureText(text) / text.length();
        float fraction = (OFFSET_MAX - Math.abs(mOldOffsetY)) / (OFFSET_MAX - OFFSET_MIN);

        mTextPaint.setColor((Integer) evaluate(fraction, TEXT_DEFAULT_END_COLOR, TEXT_DEFAULT_COLOR));
        canvas.drawText(String.valueOf(nums[1]),
                mStartX + mTextStartX + textWidth * nums[0].length(),
                mStartY + y - mOldOffsetY,
                mTextPaint);

        mTextPaint.setColor((Integer) evaluate(fraction, TEXT_DEFAULT_COLOR, TEXT_DEFAULT_END_COLOR));

        canvas.drawText(String.valueOf(nums[2]),
                mStartX + mTextStartX + textWidth * nums[0].length(),
                mStartY + y - mNewOffsetY,
                mTextPaint);
    }

    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >> 8) & 0xff) / 255.0f;
        float startB = (startInt & 0xff) / 255.0f;

        int endInt = (Integer) endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >> 8) & 0xff) / 255.0f;
        float endB = (endInt & 0xff) / 255.0f;

        // convert from sRGB to linear
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // compute the interpolated color in linear space
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStartX = (int) ((w - (dip2px(THUMB_WIDTH) + mDrawablePadding + mTextPaint.measureText(String.valueOf(mLikeCount)))) / 2);
        mStartY = (h - Math.max(sp2px(TEXT_DEFAULT_SIZE), dip2px(THUMB_HEIGHT + SHINING_HEIGHT) - dp_8)) / 2;
    }

    @SuppressWarnings("unused")
    public void setTextOffsetY(float offsetY) {
        // 变大是从[0,1]，变小是[0,-1]
        this.mOldOffsetY = offsetY;
        // 从下到上[-1,0]
        if (mToBigger) {
            this.mNewOffsetY = offsetY - OFFSET_MAX;
        } else {
            // 从上到下[1,0]
            this.mNewOffsetY = OFFSET_MAX + offsetY;
        }
        postInvalidate();
    }

    @SuppressWarnings("unused")
    public float getTextOffsetY() {
        return OFFSET_MIN;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 点击监听
     */
    private ThumbUpClickListener mThumbUpClickListener;

    public void setThumbUpClickListener(ThumbUpClickListener thumbUpClickListener) {
        mThumbUpClickListener = thumbUpClickListener;
    }

    public interface ThumbUpClickListener {
        /**
         * 点赞回调
         */
        void thumbUpFinish();

        /**
         * 取消回调
         */
        void thumbDownFinish();
    }
}
