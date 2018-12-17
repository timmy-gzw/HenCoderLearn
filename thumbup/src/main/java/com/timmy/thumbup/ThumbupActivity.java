package com.timmy.thumbup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * @author timmy
 * 点击效果的activity
 */
@SuppressWarnings("SpellCheckingInspection")
public class ThumbupActivity extends AppCompatActivity {

    private EditText mEditText;
    private ThumbupView mThumbupView;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ThumbupActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbup);

        mEditText = findViewById(R.id.et_likecount);
        mThumbupView = findViewById(R.id.thumbupview);
        mThumbupView.setThumbUpClickListener(new ThumbupView.ThumbUpClickListener() {
            @Override
            public void thumbUpFinish() {
                Log.i("logcat", "点赞成功");
            }

            @Override
            public void thumbDownFinish() {
                Log.i("logcat", "点赞失败");
            }
        });
    }

    public void setLikeCount(View v) {

        String trim = mEditText.getText().toString().trim();

        if (!trim.isEmpty()) {
            int likeCount = Integer.parseInt(trim);
            mThumbupView.setLikeCount(likeCount).setThumbup(false);
        }
    }

}
