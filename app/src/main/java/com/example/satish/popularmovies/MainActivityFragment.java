package com.example.satish.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridActivity movieGrid;
    private ArrayList<Movie> movieData;
    private GridView gridView;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        final Context appContext = getContext();
        movieData = new ArrayList<Movie>();
        movieGrid = new GridActivity(appContext,R.layout.movie_layout, movieData);
        gridView = (GridView)rootView.findViewById(R.id.movieGrid);
        gridView.setAdapter(movieGrid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Intent intent = new Intent(appContext, DetailActivity.class);

                intent.putExtra("imgUrl", movie.getImgUrl());
                intent.putExtra("title", movie.getMovTitle());
                intent.putExtra("synopsis", movie.getSynopsis());
                intent.putExtra("rating", movie.getRating());
                intent.putExtra("date", movie.getDate());

                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart(){
        super.onStart();
        FetchMoviesTask fetch = new FetchMoviesTask();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderChoice = pref.getString(getString(R.string.pref_key_order), getString(R.string.pref_value_order));
        fetch.execute(orderChoice);
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<Movie>> {


        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MOV_ID = "id";
            final String MOV_TITLE = "original_title";
            final String MOV_SYNOPSIS = "overview";
            final String MOV_IMGURL = "poster_path";
            final String MOV_RATING = "vote_average";
            final String MOV_DATE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");


            ArrayList<Movie> movieList = new ArrayList<Movie>();
            for(int i = 0; i < movieArray.length(); i++) {
                int id;
                String title;
                String synopsis;
                String imgurl;
                double rating;
                String date;
                String baseImgURL = "http://image.tmdb.org/t/p/w500/";
                JSONObject movieDetails = movieArray.getJSONObject(i);

                id = Integer.parseInt(movieDetails.getString(MOV_ID));
                title = movieDetails.getString(MOV_TITLE);
                synopsis = movieDetails.getString(MOV_SYNOPSIS);
                imgurl = baseImgURL + movieDetails.getString(MOV_IMGURL);
                rating = Double.parseDouble(movieDetails.getString(MOV_RATING));
                date = movieDetails.getString(MOV_DATE);
                Movie one = new Movie(id,title,imgurl,synopsis,rating,date);
                movieList.add(one);
                movieData.add(one);
            }


            return movieList;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... Params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                String baseQuery = "";

                String preference = Params[0];
                if(preference.equals(getString(R.string.pref_value_topRated)))
                {
                    baseQuery = "https://api.themoviedb.org/3/movie/top_rated?";
                }
                else
                {
                    baseQuery = "https://api.themoviedb.org/3/movie/popular?";
                }

                String appId = "api_key";
                String appIdValue = getString(R.string.api_key);

                Uri newUri = Uri.parse(baseQuery).buildUpon()
                        .appendQueryParameter(appId, appIdValue)
                        .build();

                URL url = new URL(newUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FetchMoviesTask", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        return getMovieDataFromJson(forecastJsonStr);
                    } catch (JSONException e) {
                        Log.e("FetchMoviesTask", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> result)
        {
            if(result!= null)
            {
                movieGrid.clear();
                movieGrid.movieList = result;
                movieGrid.setGridData();
            }
        }
    }
}
