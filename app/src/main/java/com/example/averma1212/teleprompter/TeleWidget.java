package com.example.averma1212.teleprompter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.averma1212.teleprompter.data.Script;

/**
 * Implementation of App Widget functionality.
 */
public class TeleWidget extends AppWidgetProvider {
    private static String SCRIPTS;
    private static String NO_SCRIPT;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,String[] script) {
        // Construct the RemoteViews object
        SCRIPTS = context.getString(R.string.scripts_tag_main_to_detail);
        NO_SCRIPT = context.getString(R.string.no_script_available);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tele_widget);
        if(script==null){
            views.setTextViewText(R.id.appwidget_text,NO_SCRIPT);
            return;
        }
        String widgetTitle = script[0].substring(1);
        String widgetText = script[1];

        views.setTextViewText(R.id.appwidget_title, widgetTitle);
        views.setTextViewText(R.id.appwidget_text,widgetText);

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.layout_widget,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.setActionUpdateWidget(context);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,String[] script) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,script);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

