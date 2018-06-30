package com.ivanmorett.flicksmoviesapp.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ivanmorett.flicksmoviesapp.MovieAdapter;
import com.ivanmorett.flicksmoviesapp.R;
import com.ivanmorett.flicksmoviesapp.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ivanmorett on 6/27/18.
 */

public class TheMovieDbController {

    private Context context;
    private AsyncHttpClient client;
    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private final String API_KEY_PARAM = "api_key";
    private final String TAG  = "TheMovieDbController";
    // url for loading the images
    private static String imageBaseUrl;

    // Sizes retrieved from de db
    private static String posterSize;
    private static String backdropSize;

    //Movie list
    private ArrayList<Movie> movies;

    //Adapter
    private MovieAdapter adapter;

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public static String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public static String getPosterSize() {
        return posterSize;
    }

    public static String getBackdropSize() {
        return backdropSize;
    }

    public TheMovieDbController(Context context) throws IllegalArgumentException{
        if(context == null)
            throw new IllegalArgumentException("Context must be not null");
        this.context = context;
        this.client = new AsyncHttpClient();
        movies = new ArrayList<>();
    }

    public void setAdapter(MovieAdapter adapter){
        this.adapter = adapter;
    }

    public void getConfiguration(){
        // Url
        String url = API_BASE_URL + "/configuration";
        // Set the params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, context.getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    JSONObject images = response.getJSONObject("images");
                    //obtain imageBaseUrl
                    imageBaseUrl = images.getString("secure_base_url");
                    //obtain posterSize
                    posterSize = images.getJSONArray("poster_sizes").optString(3, "w342");
                    backdropSize = images.getJSONArray("backdrop_sizes").optString(1, "w780");
                    Log.i(TAG, String.format("Loaded configuration with image url: %s and poster size of: %s", imageBaseUrl, posterSize));
                    getNowPlaying();

                } catch(JSONException ex) {
                    logError("Failed to parse configuration", ex, true );
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed to get data from configuration", throwable, true);
            }
        });
    }

    public void getNowPlaying(){
        // Url
        String url = API_BASE_URL + "/movie/now_playing";
        // Set the params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, context.getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    JSONArray results = response.getJSONArray("results");
                    for(int i = 0; i<results.length(); i++) {
                        movies.add(new Movie(results.getJSONObject(i)));
                        // notify the adapter
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));

                } catch(JSONException ex) {
                    logError("Failed to parse now playing movies", ex, true );
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed to get data from playing endpoint ", throwable, true);
            }
        });
    }

    public static String getYoutubeLink(String id){
        return API_BASE_URL + "/movie/"+id+"/videos";
    }

    private void logError(String message,  Throwable error, boolean alertUser){
        Log.e(TAG, message);
        if(alertUser){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static String getImageURL(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

}
