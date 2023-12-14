package com.fb.widget;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.widget.RemoteViews;

public class MyWidgetHostView extends AppWidgetHostView {

    public MyWidgetHostView(Context context) {
        super(context);
    }

    public MyWidgetHostView(Context context, int animationIn, int animationOut) {
        super(context, animationIn, animationOut);
    }

    @Override
    public void updateAppWidget(RemoteViews remoteViews) {
        super.updateAppWidget(remoteViews);
    }
}
