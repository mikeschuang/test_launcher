
package com.example.testlauncher;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class LauncherActivity extends Activity {
    private List<AppWidgetProviderInfo> mWidgetList;

    private AppWidgetManager mAppWidgetManager;

    public static final int APPWIDGET_HOST_ID = 3388;// arbitrary

    private RelativeLayout mWidgetContainer;

    private AppWidgetHost mAppWidgetHost;

    private static final long sLeaveAppInterval = 2000;

    private long mBackPressedLastTime = 0;

    private static final String sDemoWidgetPackage = "com.benq.widget";

    private static String TAG = "LauncherActivity";

    private static final int REQUEST_BIND_APPWIDGET = 11;

    private static final int REQUEST_CREATE_APPWIDGET = 2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_home);
        onInit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAppWidgetHost != null)
            mAppWidgetHost.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAppWidgetHost != null)
            mAppWidgetHost.stopListening();
    }

    private void onInit() {
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mWidgetList = mAppWidgetManager.getInstalledProviders();
        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
        mWidgetContainer = (RelativeLayout)findViewById(R.id.widget_container);
        attachWidget();
    }

    private void attachWidget() {
        final int widgetId = mAppWidgetHost.allocateAppWidgetId();
        AppWidgetProviderInfo appWidgetProviderInfo = null;
        for (final AppWidgetProviderInfo info : mWidgetList) {
            if (info.provider.getPackageName().equalsIgnoreCase(sDemoWidgetPackage)) {
                appWidgetProviderInfo = info;
                break;
            }
            System.out.println("info package name = " + info.provider.getPackageName());
        }
        final boolean success = mAppWidgetManager.bindAppWidgetIdIfAllowed(widgetId, appWidgetProviderInfo.provider);
        if (success) {
            final AppWidgetHostView hostView = mAppWidgetHost.createView(LauncherActivity.this, widgetId, appWidgetProviderInfo);
            //            final RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //            param.addRule(RelativeLayout.CENTER_IN_PARENT);
            //            hostView.setLayoutParams(param);
            mWidgetContainer.addView(hostView);
        } else {
            try {
                final Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_BIND);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, appWidgetProviderInfo.provider);
                startActivityForResult(intent, REQUEST_BIND_APPWIDGET);
            } catch (final ActivityNotFoundException e) {
                Log.e(TAG, "start action bind fail!");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.i(TAG, "requestCode=" + requestCode + " ,resultCode=" + resultCode);
        if (resultCode == RESULT_CANCELED) {
            Log.w(TAG, "user cancel request!");
            return;
        }

        switch (requestCode) {
            case REQUEST_BIND_APPWIDGET:

                final int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

                if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    final AppWidgetProviderInfo appWidgetProviderInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

                    if (appWidgetProviderInfo.configure != null) {

                        Log.i(TAG, "The AppWidgetProviderInfo configure info -----> " + appWidgetProviderInfo.configure);

                        final Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
                        intent.setComponent(appWidgetProviderInfo.configure);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                        startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
                    } else
                        onActivityResult(REQUEST_CREATE_APPWIDGET, RESULT_OK, data);
                }
                break;
            case REQUEST_CREATE_APPWIDGET:
                completeAddAppWidget(data);
                break;
        }

    }

    private void completeAddAppWidget(final Intent data) {
        final Bundle extra = data.getExtras();
        final int appWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        Log.i(TAG, "completeAddAppWidget : appWidgetId is ----> " + appWidgetId);
        if (appWidgetId == -1) {
            Toast.makeText(LauncherActivity.this, "appWidget invalid!", Toast.LENGTH_SHORT).show();
            return;
        }
        final AppWidgetProviderInfo appWidgetProviderInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        final AppWidgetHostView hostView = mAppWidgetHost.createView(LauncherActivity.this, appWidgetId, appWidgetProviderInfo);
        //        final RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        //        hostView.setLayoutParams(param);
        mWidgetContainer.addView(hostView);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressedLastTime + sLeaveAppInterval > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press back button again to leave", Toast.LENGTH_SHORT).show();
        }

        mBackPressedLastTime = System.currentTimeMillis();
    }

    private void showToast(final String toast) {
        try {
            Toast.makeText(LauncherActivity.this, toast, Toast.LENGTH_SHORT).show();
        } catch (final Exception e) {
            Log.e(TAG, "showToast fail\n");
            e.printStackTrace();
        }
    }
}
