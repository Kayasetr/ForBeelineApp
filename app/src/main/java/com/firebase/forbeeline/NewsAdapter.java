package com.firebase.forbeeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<NewsItem> newsArrayList;
    private Context context;
    private OnItemClickListener mListener;
    public interface  OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView listItemImageView;
        TextView listItemTitleView;
        TextView listItemDescriptionView;

        public NewsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            listItemImageView = itemView.findViewById(R.id.newsImage);
            listItemDescriptionView = itemView.findViewById(R.id.discription);
            listItemTitleView = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
    public NewsAdapter(ArrayList<NewsItem> newsList, Context context) {
        this.context = context;
        newsArrayList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from((context));
        View view = layoutInflater.inflate(R.layout.news_list_item, viewGroup, false);
        NewsViewHolder viewHolder = new NewsViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        NewsItem currentItem = newsArrayList.get(i);
        Picasso.with(context).load(currentItem.getNewsImageResource()).into(newsViewHolder.listItemImageView);
       // newsViewHolder.listItemImageView.setImageResource(currentItem.getNewsImageResource());
        newsViewHolder.listItemTitleView.setText(currentItem.getTitle());
        newsViewHolder.listItemDescriptionView.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }
}
