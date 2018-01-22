package com.example.averma1212.teleprompter;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.averma1212.teleprompter.data.ScriptContract;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 09-01-2018.
 */

public class AddActivity extends AppCompatActivity {
    @BindView(R.id.addScript)
    EditText addScript;
    @BindView(R.id.addTitle)
    EditText addTitle;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName(AddActivity.class.getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.tracker_action))
                .setAction(getString(R.string.tracker_share))
                .build());
    }

    @OnClick(R.id.addButton)
    public void add(){
        String title = addTitle.getText().toString();
        String desc = addScript.getText().toString();
        if(title.length()>0 && desc.length()>0){
            title = "!" + title;
            ContentValues contentValues=new ContentValues();
            contentValues.put(ScriptContract.ScriptEntry.TITLE,title);
            contentValues.put(ScriptContract.ScriptEntry.DESCRIPTION,desc);
            this.getContentResolver().insert(ScriptContract.ScriptEntry.CONTENT_URI, contentValues);
            onBackPressed();
        }
        else if(title.length()==0){
            Toast.makeText(this, getString(R.string.add_title_toast), Toast.LENGTH_SHORT).show();
            addTitle.requestFocus();
        }
        else{
            Toast.makeText(this, getString(R.string.add_desc_toast), Toast.LENGTH_SHORT).show();
            addScript.requestFocus();
        }
    }

    @OnClick(R.id.discardButton)
    public void discard(){
        onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
