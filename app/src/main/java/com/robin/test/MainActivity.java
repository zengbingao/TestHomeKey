package com.robin.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean aBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mHomeKeyEventReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        long name=preferences.getLong("time",0L);
        if (aBoolean&&(System.currentTimeMillis()-name>10*1000)) {
            Toast.makeText(getApplicationContext(), "超过10秒又回来了", Toast.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    aBoolean = true;
                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onReceive: 按一下home");
                    SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putLong("time", System.currentTimeMillis());
                    editor.apply();
                    Log.i(TAG, "onReceive: 当前时间是："+System.currentTimeMillis());
                }
            }
        }
    };
}
