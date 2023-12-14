package com.fb.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

public class MyWidgetHost extends AppWidgetHost {

    private final SparseArray<MyWidgetHostView> mPendingViews = new SparseArray<>();
    private final SparseArray<MyWidgetHostView> mViews = new SparseArray<>();

    public MyWidgetHost(Context context, int hostId) {
        super(context, hostId);
    }

    @Override
    protected AppWidgetHostView onCreateView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget) {
        final MyWidgetHostView view;
        if (mPendingViews.get(appWidgetId) != null) {
            view = mPendingViews.get(appWidgetId);
            mPendingViews.remove(appWidgetId);
            Log.e("","===================onCreateView000");
        } else {
            Log.e("","===================onCreateView1111");
            view = new MyWidgetHostView(context);
            Log.e("","===================onCreateView22222");
        }
        mViews.put(appWidgetId, view);
        return view;
    }


    @Override
    public void deleteAppWidgetId(int appWidgetId) {
        super.deleteAppWidgetId(appWidgetId);
        mViews.remove(appWidgetId);
    }

    @Override
    public void clearViews() {
        super.clearViews();
        mViews.clear();
    }
}
