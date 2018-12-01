package com.example.rkjc.news_app_2;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyJobService extends JobService {



    private List<NewsItem> newsItems = new ArrayList<>();
    NewsItemRepository newsItemRepository = new NewsItemRepository(this.getApplication());

    //Instantiate a new AsyncTask To do in
    private AsyncTask BackGroundTask;


    MainActivity mainActivity;
    NewsRecyclerViewAdapter newsRecyclerViewAdapter;

    NewsItemViewModel newsItemViewModel;

    @Override
    public boolean onStartJob(final JobParameters job) {

        BackGroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                Context context = MyJobService.this;

                return null;
            }

            @Override
            protected void onPostExecute(Object object){
                //Execute the sync of the database
                newsItemRepository.syncDataBase();
                Log.d("Notice", "Database is synced via FireBaseJobDispatcher");


                jobFinished(job, false);
            }
        };

        BackGroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        return true;
    }

}
