package com.example.rkjc.news_app_2;

import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //New Code Homework2
    private NewsItemViewModel newsItemViewModel;

    //Hungarian Notation m = Main (It's in the main class)
    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;
    private ProgressBar mProgressBar;
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private List<NewsItem> newsItems = new ArrayList<>();

    private static final String Job_Tag = "my_job_tag";
    private FirebaseJobDispatcher jobDispatcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultsTextView = (TextView) findViewById(R.id.date);
        mRecyclerView = (RecyclerView)findViewById(R.id.news_recyclerview);
        mAdapter = new NewsRecyclerViewAdapter(this, newsItems);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //New Code Homework2
        newsItemViewModel = ViewModelProviders.of(this).get(NewsItemViewModel.class);
        newsItemViewModel.getAllNewsItems().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable List<NewsItem> newsItems) {
                mAdapter.setNewsItems(newsItems);
            }
        });


        //New Code Homework3
        executeJobDispatcher();
    }


    public class QueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL...urls){
            String articleText = "";

            try{
                articleText = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e){
                e.printStackTrace();
            }

            return articleText;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            newsItems = JsonUtils.parseNews(s);
            mAdapter.mNewsItems.addAll(newsItems); //Add all values into adapter
            mAdapter.notifyDataSetChanged(); //Display *live* changes
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.action_search:
                newsItems = this.newsItemViewModel.sync();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Instantiate a Job Dispatcher
    public void executeJobDispatcher(){

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));


        Job job = jobDispatcher.newJobBuilder()
                // Service will call from 'MyJobService'
                .setService(MyJobService.class)
                // Will be 'my_job_tag'
                .setTag(Job_Tag)
                // Job will be executed through the lifetime of the application
                .setRecurring(true)
                // Set time of execution
                .setLifetime(Lifetime.FOREVER)
                // Sets the job to be repeated
                .setTrigger(Trigger.executionWindow(10,10))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // sets constraints based on device status
                .setConstraints(
                        // only executes when device is on a network (does http request from news API)
                        Constraint.ON_ANY_NETWORK
                )
                .build();

        jobDispatcher.mustSchedule(job);
        Toast.makeText(this, "Job Scheduled", Toast.LENGTH_LONG).show();
    }


    public void notifyNotification(){
        NotificationCompat.Builder buildNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_update)
                .setContentTitle("News Application")
                .setContentText("The database is synced!")
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        buildNotification.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, buildNotification.build());

    }
}
