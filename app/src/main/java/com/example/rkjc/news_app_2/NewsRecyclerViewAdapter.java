package com.example.rkjc.news_app_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NewsRecyclerViewAdapter  extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    Context mContext;
    List<NewsItem> mNewsItems;

    public NewsRecyclerViewAdapter(Context context, List<NewsItem> newsItems){
        this.mContext = context;
        this.mNewsItems = newsItems;
    }

    //Creates it's context and instantiates View (for displaying as a UI)
    @Override
    public NewsRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.card_layout, parent, shouldAttachToParentImmediately);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsRecyclerViewAdapter.NewsViewHolder holder, int position){
        holder.bind(position);
    }

    @Override
    public int getItemCount(){
        return mNewsItems.size();
    }

    public void setNewsItems(List<NewsItem> items){
        this.mNewsItems = items;
        notifyDataSetChanged();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView description;
        TextView date;
        TextView url;

        //Instantiated views now have set view
        public NewsViewHolder(View itemView){
            super(itemView);

            //imageView with Picasso
            imageView = (ImageView) itemView.findViewById(R.id.imageView);



            title = (TextView) itemView.findViewById(R.id.textViewTitle);
            description = (TextView) itemView.findViewById(R.id.textViewDescription);
            date = (TextView) itemView.findViewById(R.id.textViewDate);
            url = (TextView) itemView.findViewById(R.id.textViewURL);




            //Include onClickListener to view (opens window to that url)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url.getText().toString()));
                    Log.d("LOOOG Test", url.getText().toString()); //Display URL
                    mContext.startActivity(i);
                }
            });
        }

        //bind the texts with their views
        void bind(final int listIndex){
            title.setText(mNewsItems.get(listIndex).getmTitle());
            description.setText(mNewsItems.get(listIndex).getmDescription());
            date.setText("Date: " + mNewsItems.get(listIndex).getmPublishedAt());
            url.setText(mNewsItems.get(listIndex).getmUrl());

            Picasso.get().load(mNewsItems.get(listIndex).getmUrlToImage()).fit().into(imageView);

        }
    }
}