package com.timmy.hencoderlearn;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.timmy.thumbup.ThumbupActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] sampleStr = {
            "即刻的点赞效果",
            "薄荷健康的滑动卷尺效果",
            "小米运动首页顶部的运动记录界面",
            "Flipboard 红板报的翻页效果"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        ArrayList<TextView> textViews = new ArrayList<>();

        textViews.add(getTextViewById(R.id.text1));
        textViews.add(getTextViewById(R.id.text2));
        textViews.add(getTextViewById(R.id.text3));
        textViews.add(getTextViewById(R.id.text4));

        int size = textViews.size();
        for (int i = 0; i < size; i++) {
            TextView textView = textViews.get(i);
            textView.setOnClickListener(this);
            textView.setText(sampleStr[i]);
        }

        ThumbupActivity.start(this);
    }

    private TextView getTextViewById(@IdRes int id) {
        return findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                ThumbupActivity.start(this);
                break;
            case R.id.text2:
                break;
            case R.id.text3:
                break;
            case R.id.text4:
                break;
            default:
                break;
        }
    }
}
