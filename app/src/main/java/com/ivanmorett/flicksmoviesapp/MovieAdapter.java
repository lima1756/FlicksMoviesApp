package com.ivanmorett.flicksmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmorett.flicksmoviesapp.controllers.TheMovieDbController;
import com.ivanmorett.flicksmoviesapp.models.Movie;
import com.ivanmorett.flicksmoviesapp.modules.GlideApp;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by ivanmorett on 6/27/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private ArrayList<Movie> movies;
    private Context context;
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }
    private final String TAG = "MovieAdapter";

    // creates and inflater a new video
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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

        // determine the device orientation

        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        String imgUrl = isPortrait ?
                TheMovieDbController.getImageURL(TheMovieDbController.getPosterSize(), movie.getPosterPath())
                : TheMovieDbController.getImageURL(TheMovieDbController.getBackdropSize(), movie.getBackdropPath());

        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView image = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        GlideApp.with(context)
                .load(imgUrl)
                .placeholder(placeholderId)
                .error(placeholderId)
                .transform(new RoundedCornersTransformation(15, 0))
                .into(image);
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //track objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }

    }
}
