package com.example.rkjc.news_app_2;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyJobService extends JobService {


    BackgroundTask backgroundTask;


    private List<NewsItem> newsItems = new ArrayList<>();
    private NewsItemRepository newsItemRepository;

    @Override
    public boolean onStartJob(final JobParameters job) {

        backgroundTask = new BackgroundTask(){

            @Override
            protected void onPostExecute(String s) {
                Log.d("TAAAAAG", "MyJobService Executed!!");

                

                Toast.makeText(getApplicationContext(), "Message from Background Task: " + s, Toast.LENGTH_LONG).show();
                jobFinished(job, false);
            }
        };

        backgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    public static class BackgroundTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void...voids){

            Log.d("EXECUTING IN BACKGROUND", "Message");

            return "Background Executed";
        }
    }
}
