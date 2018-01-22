package com.example.averma1212.teleprompter;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import com.example.averma1212.teleprompter.data.Script;
import com.example.averma1212.teleprompter.data.ScriptContract;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by HP on 10-01-2018.
 */

public class WidgetService extends IntentService {
    ArrayList<Script> Scripts;
    Script mScript;

    public static String ACTION_UPDATE_WIDGET ;
    public WidgetService() {
        super("WidgetService");
        Timber.plant(new Timber.DebugTree());
    }

    public static void setActionUpdateWidget(Context context){
        ACTION_UPDATE_WIDGET = context.getString(R.string.action_update_widget);
        Intent intent = new Intent(context,WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            String action = intent.getAction();
            if(action.equals(ACTION_UPDATE_WIDGET)){
                handleActionUpdateWidget();
            }
        } else {
            handleActionUpdateWidget();
        }
    }



    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TeleWidget.class));
        new fetch().execute();
            if(Scripts!=null && Scripts.size()>0){
                mScript = Scripts.get(0);
            } else {
                TeleWidget.updateWidget(this, appWidgetManager, appWidgetIds,null);
                return;
            }


        String[] script = new String[3];
        script[0] = mScript.title;
        script[1] = mScript.desc;
        script[2] = String.valueOf(mScript.id);
        TeleWidget.updateWidget(this, appWidgetManager, appWidgetIds,script);
    }

    private void extractData(Cursor cursor) {
        Scripts = new ArrayList<>();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                String title = cursor.getString(cursor.getColumnIndex(ScriptContract.ScriptEntry.TITLE));
                Long id = cursor.getLong(cursor.getColumnIndex(ScriptContract.ScriptEntry._ID));
                String desc = cursor.getString(cursor.getColumnIndex(ScriptContract.ScriptEntry.DESCRIPTION));
                Scripts.add(new Script(title,desc,id));
                Timber.d(title);
            }while(cursor.moveToNext());
        }
    }

    public class fetch extends AsyncTask<Void,Void,Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(ScriptContract.ScriptEntry.CONTENT_URI,
                    null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            extractData(cursor);
        }
    }

}
