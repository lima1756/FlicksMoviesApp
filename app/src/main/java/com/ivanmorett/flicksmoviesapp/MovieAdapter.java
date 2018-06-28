package com.ivanmorett.flicksmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmorett.flicksmoviesapp.models.Movie;

import java.util.ArrayList;

/**
 * Created by ivanmorett on 6/27/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private ArrayList<Movie> movies;

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    // creates and inflater a new video
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater  inflater = LayoutInflater.from(context);
        // create the view  using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // binds view to list
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        // populating the view
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // TODO set image
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //track objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
        }
    }
}
