package com.timmy.thumbup;

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
    private int likeCount = 100;

    private int mStartX;
    private int mStartY;
    private float mTextStartX;

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
        result = (int) (dip2px(THUMB_WIDTH) + mDrawablePadding + mTextPaint.measureText(String.valueOf(likeCount)));
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
        canvas.drawText(String.valueOf(likeCount), mStartX + mTextStartX, mStartY + y, mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStartX = (int) ((w - (dip2px(THUMB_WIDTH) + mDrawablePadding + mTextPaint.measureText(String.valueOf(likeCount)))) / 2);
        mStartY = (h - Math.max(sp2px(TEXT_DEFAULT_SIZE), dip2px(THUMB_HEIGHT + SHINING_HEIGHT) - dp_8)) / 2;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public interface ThumbUpClickListener {
        /**
         * 点赞回调
         */
        void thumbUpFinish();

        /**
         * 取消回调
         */
    }
}
