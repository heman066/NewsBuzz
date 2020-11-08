package com.example.newsbuzz.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newsbuzz.NewsItem;
import com.example.newsbuzz.R;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NewsHolder> {

    Context context;
    List<NewsItem> newsList ;
    OnClickListener mListener;

    public RecyclerAdapter(Context context, OnClickListener mListener) {
        this.context = context;
        this.newsList = new ArrayList<>();
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);
        return new NewsHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        holder.title.setText(newsList.get(position).getTitle());
        holder.description.setText(newsList.get(position).getDescription());
        holder.author.setText(newsList.get(position).getAuthor());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(!newsList.get(position).getPublishedAt().equals(null)) {
                Instant instant = Instant.parse(newsList.get(position).getPublishedAt());
                LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                holder.publish_date.setText(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
        if(!newsList.get(position).getUrlToImage().equals("")){
            Picasso.get().load(newsList.get(position).getUrlToImage())
                    .fit().centerInside().into(holder.newsImage);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,description,publish_date,author;
        ImageView newsImage;
        OnClickListener mListener;

        public NewsHolder(@NonNull View itemView, OnClickListener mListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title_holder);
            description = itemView.findViewById(R.id.description_holder);
            publish_date =itemView.findViewById(R.id.published_holder);
            author = itemView.findViewById(R.id.author_holder);
            newsImage = itemView.findViewById(R.id.image_holder);
            this.mListener = mListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onNewsClick(getAdapterPosition());
        }
    }

    public interface OnClickListener{
        void onNewsClick(int position);
    }
}
