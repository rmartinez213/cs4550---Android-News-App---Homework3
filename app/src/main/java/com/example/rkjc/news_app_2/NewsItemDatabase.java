package com.example.rkjc.news_app_2;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Database(entities = {NewsItem.class}, version = 1)
public abstract class NewsItemDatabase extends RoomDatabase {


    private static volatile NewsItemDatabase INSTANCE;
    public abstract NewsItemDao newsItemDao();

    public static synchronized NewsItemDatabase getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (NewsItemDatabase.class){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NewsItemDatabase.class, "newsitem_database").build();
            }
        }
        return INSTANCE;
    }
}