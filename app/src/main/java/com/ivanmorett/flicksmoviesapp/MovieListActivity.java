package com.ivanmorett.flicksmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ivanmorett.flicksmoviesapp.controllers.TheMovieDbController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity {

    private final String TAG = "MovieListActivity";
    private TheMovieDbController dbController;

    // Bind xml to java
    RecyclerView rvMovies;
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbController = new TheMovieDbController(getBaseContext());
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setContentView(R.layout.activity_movie_list);
        rvMovies = findViewById(R.id.rvMovies);
        adapter = new MovieAdapter(dbController.getMovies());

        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        dbController.setAdapter(adapter);


        initializeController();

    }

    private void initializeController(){
        try {
            dbController.getConfiguration();
        } catch(Exception ex){
            Log.e(TAG, "Error on the latch");
        }
    }
}
