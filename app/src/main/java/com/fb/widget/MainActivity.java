package com.fb.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps.PinItemRequest;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    PinItemRequest mRequest;
    AppWidgetHost mHost;
    AppWidgetManager mManager;

    AppWidgetProviderInfo mInfo;

    private FrameLayout mFrame;

    private Context mContext;

    private static final int REQUEST_BIND_APPWIDGET = 1;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("","==============onConfigurationChanged");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("","==============onDestroy");
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Configuration configuration = newBase.getResources().getConfiguration();
//        Locale locale = configuration.locale;
//        // en or zh
//        Log.e("", "=========locale : " + locale.getLanguage());
//        if ("en".equals(locale.getLanguage())) {
//            configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
//        } else {
//            configuration.setLocale(Locale.ENGLISH);
//        }
//        DisplayMetrics dm = newBase.getResources().getDisplayMetrics();
//        newBase.getResources().updateConfiguration(configuration,dm);
//        Context configurationContext = newBase.createConfigurationContext(configuration);
//
//        super.attachBaseContext(configurationContext);
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("","==============onCreate");
        setContentView(R.layout.activity_main);
        mFrame = findViewById(R.id.frame);

        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.switch_lang).setOnClickListener(this);
        mHost = new AppWidgetHost(this,1024);
        mHost.startListening();
        mManager = AppWidgetManager.getInstance(this);

        List<AppWidgetProviderInfo> providers = mManager.getInstalledProviders();
        for (AppWidgetProviderInfo info : providers) {
            ComponentName provider = info.provider;
            Log.e("", "=========info : " + provider.getClassName());

//            if ("com.android.music.MediaAppWidgetProvider".equals(provider.getClassName())) {
//                                if ("com.android.alarmclock.AnalogAppWidgetProvider".equals(provider.getClassName())) {
//            if ("com.android.calendar.widget.CalendarAppWidgetProvider".equals(provider.getClassName())) {
                if ("com.fb.widget.MyProvider".equals(provider.getClassName())) {
                mInfo = info;
            }
        }
        mContext = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1111);
                    add();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    private void add() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("","============acceptWidget ");
                mPendingBindWidgetId = mHost.allocateAppWidgetId();
                mAppWidgetHostView = mHost.createView(mContext, mPendingBindWidgetId, mInfo);
                Log.e("","============view "+mAppWidgetHostView);
                mFrame.addView(mAppWidgetHostView);

//                AppWidgetManager.getInstance(MainActivity.this).updateAppWidgetOptions(mPendingBindWidgetId, appWidgetOptions);
            }
        });
    }



    private void setConfig() {
        Configuration configuration = getResources().getConfiguration();
        Locale locale = configuration.locale;
        // en or zh
        Log.e("", "=========locale : " + locale.getLanguage());
        if ("en".equals(locale.getLanguage())) {
            configuration.locale=Locale.SIMPLIFIED_CHINESE;
        } else {
            configuration.locale=Locale.ENGLISH;
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        getResources().updateConfiguration(configuration,dm);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            addWidget();
        } else if (view.getId()==R.id.settings) {
            //start settings
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
        } else {
//            setConfig();
            this.recreate();
        }

    }
    int mPendingBindWidgetId;
    private void addWidget() {
        mPendingBindWidgetId = mHost.allocateAppWidgetId();
        Log.e("", "=========mPendingBindWidgetId : " + mPendingBindWidgetId);


        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mPendingBindWidgetId)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, mInfo.provider)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER_PROFILE, mInfo.getProfile());
        startActivityForResult(intent, REQUEST_BIND_APPWIDGET);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_BIND_APPWIDGET) {
            Log.e("", "==============REQUEST_BIND_APPWIDGET : " + requestCode + " , " + resultCode);
            if (resultCode == RESULT_OK) {
                int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mPendingBindWidgetId);
                Log.e("", "=========widgetId : " + widgetId);
                acceptWidget(widgetId);
            }
        }
    }
    AppWidgetHostView mAppWidgetHostView;

    private void acceptWidget(int widgetId) {
        Log.e("","============acceptWidget ");
        mAppWidgetHostView = mHost.createView(mContext, widgetId, mInfo);
        Log.e("","============view "+mAppWidgetHostView);

        mFrame.addView(mAppWidgetHostView);
    }
}