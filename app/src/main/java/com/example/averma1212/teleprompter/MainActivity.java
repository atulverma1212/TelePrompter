package com.example.averma1212.teleprompter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.averma1212.teleprompter.adapters.MainAdapter;
import com.example.averma1212.teleprompter.data.Script;
import com.example.averma1212.teleprompter.data.ScriptContract;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LOADER_CONSTANT = 123;
    private static String SCRIPTS;
    @BindView(R.id.main_recycler_view)
    RecyclerView rv;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    private ArrayList <Script> scripts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        SCRIPTS = getString(R.string.scripts_tag_main_to_detail);
        toolbarLayout.setTitle(getString(R.string.app_name));
        MobileAds.initialize(this,getString(R.string.app_Id));
        getIdlingResource();
        getSupportLoaderManager().initLoader(LOADER_CONSTANT,null,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.add_fab)
    public void addScript(){
        Intent intent = new Intent(this,AddActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickListener(int position) {
        Intent intent = new Intent(this,DetailActivity.class);
        String[] mScript = new String[3];
        mScript[0] = scripts.get(position).title;
        mScript[1] = scripts.get(position).desc;
        mScript[2] = String.valueOf(scripts.get(position).id);

        //Update Widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TeleWidget.class));
        TeleWidget.updateWidget(this, appWidgetManager, appWidgetIds, mScript);
        intent.putExtra(SCRIPTS,mScript);
        startActivity(intent);
    }
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;


    // Only called from test, creates and returns a new {@link SimpleIdlingResource}.
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    private void extractData(Cursor cursor) {
        scripts = new ArrayList<>();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                String title = cursor.getString(cursor.getColumnIndex(ScriptContract.ScriptEntry.TITLE));
                Long id = cursor.getLong(cursor.getColumnIndex(ScriptContract.ScriptEntry._ID));
                String desc = cursor.getString(cursor.getColumnIndex(ScriptContract.ScriptEntry.DESCRIPTION));
                scripts.add(new Script(title,desc,id));
                Timber.d(title);
            }while(cursor.moveToNext());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {



        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                return getContentResolver().query(ScriptContract.ScriptEntry.CONTENT_URI,
                        null,null,null,null);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,final Cursor data) {
        Handler handler = new Handler();
        extractData(data);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        }, 3000);
        final MainAdapter adapter = new MainAdapter(scripts,this);
        rv.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_cols),StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
