package com.ivanmorett.flicksmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ivanmorett.flicksmoviesapp.controllers.TheMovieDbController;

public class MovieListActivity extends AppCompatActivity {

    private final String TAG = "MovieListActivity";
    private TheMovieDbController dbController;
    RecyclerView rvMovies;
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        dbController = new TheMovieDbController(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);




        adapter = new MovieAdapter(dbController.getMovies());
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        dbController.setAdapter(adapter);


        initializeController();
        adapter.setController(dbController);

    }

    private void initializeController(){
        try {
            dbController.getConfiguration();
        } catch(Exception ex){
            Log.e(TAG, "Error on the latch");
        }
    }
}
