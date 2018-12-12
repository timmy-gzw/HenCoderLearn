package com.timmy.thumbup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author timmy
 * 点击效果的activity
 */
@SuppressWarnings("SpellCheckingInspection")
public class ThumbupActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, ThumbupActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbup);
    }
}
