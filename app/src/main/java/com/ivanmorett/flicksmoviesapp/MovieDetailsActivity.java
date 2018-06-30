package com.ivanmorett.flicksmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ivanmorett.flicksmoviesapp.controllers.TheMovieDbController;
import com.ivanmorett.flicksmoviesapp.models.Movie;
import com.ivanmorett.flicksmoviesapp.modules.GlideApp;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String TAG = "MovieDetailsActivity";
    Movie movie;

    @BindView(R.id.tvTitleDetails) TextView tvTitleDetails;
    @BindView(R.id.tvOverviewDetails) TextView tvOverviewDetails;
    @BindView(R.id.ivBackdropImageDetails) ImageView ivBackdropImageDetails;
    @BindView(R.id.ivPosterImageDetails) ImageView ivPosterImageDetails;
    @BindView(R.id.rbVoteAverageDetails) RatingBar rbVoteAverageDetails;
    @BindView(R.id.ivPlayButton) ImageView ivPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        tvTitleDetails.setText(movie.getTitle());
        tvOverviewDetails.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = (float) movie.getVoteAverage();
        rbVoteAverageDetails.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        int width = ivBackdropImageDetails.getWidth();
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ivBackdropImageDetails.getWidth()/2, ivBackdropImageDetails.getHeight()/2);
        ivPlayButton.setLayoutParams(layoutParams);

        String imgPosterUrl = TheMovieDbController.getImageURL(TheMovieDbController.getPosterSize(), movie.getPosterPath()),
               imgBackdropUrl = TheMovieDbController.getImageURL(TheMovieDbController.getBackdropSize(), movie.getBackdropPath());


        GlideApp.with(getApplicationContext())
                .load(imgPosterUrl)
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .transform(new RoundedCornersTransformation(15, 0))
                .into(ivPosterImageDetails);

        GlideApp.with(getApplicationContext())
                .load(imgBackdropUrl)
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(ivBackdropImageDetails);

        ivBackdropImageDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getApplicationContext(), MovieTrailerActivity.class);
                i.putExtra("id", movie.getId());
                startActivity(i);
            }
        });

    }
}
