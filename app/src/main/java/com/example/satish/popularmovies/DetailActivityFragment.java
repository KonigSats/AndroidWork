package com.example.satish.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private TextView summary;
    private ImageView thumbnail;
    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        String imgUrl = intent.getStringExtra("imgUrl");
        String title = intent.getStringExtra("title");
        String synopsis = intent.getStringExtra("synopsis");
        double rating = intent.getDoubleExtra("rating", 0.00);
        String date = intent.getStringExtra("date");
        String details = " TITLE: " + title + "\n RELEASE DATE: "+ date + "\n RATING: "+ rating + "\n PLOT: \n " + synopsis;

        summary = (TextView)rootView.findViewById(R.id.movie_details);
        thumbnail = (ImageView)rootView.findViewById(R.id.grid_movie_image);

        Picasso.with(getContext())
                .load(imgUrl)
                .into(thumbnail);
        summary.setText(details);

        return rootView;
    }
}
