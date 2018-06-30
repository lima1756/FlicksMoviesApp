package com.ivanmorett.flicksmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ivanmorett.flicksmoviesapp.controllers.TheMovieDbController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        Intent i = getIntent();

        RequestParams params = new RequestParams();
        params.put("api_key", getApplicationContext().getString(R.string.api_key));

        new AsyncHttpClient().get(TheMovieDbController.getYoutubeLink(i.getStringExtra("id")), params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    JSONArray videos = response.getJSONArray("results");
                    boolean isTrailer = false;

                    // resolve the player view from the layout
                    YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);
                    for(int i = 0; i < videos.length(); i++){
                        JSONObject video = videos.getJSONObject(i);
                        if(video.getString("type").equals("Trailer")){
                            isTrailer = true;
                            final String videoId = video.getString("key");
                            playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {
                                    // do any work here to cue video, play video, etc.
                                    youTubePlayer.cueVideo(videoId);
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {
                                    // log the error
                                    Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                                }
                            });
                            break;
                        }

                    }
                    if(!isTrailer){
                        Log.e("Youtube", "There is no trailer" );

                    }

                } catch(JSONException ex) {
                    Log.e("Youtube", "Error on showing video" );
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Youtube", "Error on retreiving from TheMovieDB" );
            }
        });



    }
}
