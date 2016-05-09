package com.example.satish.popularmovies;

import java.util.ArrayList;
import android.content.Context;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridActivity extends ArrayAdapter<Movie> {
    public Context mContext;
    public int layoutResID;
    public ArrayList<Movie> movieList;

    // Constructor
    public GridActivity(Context c,int layoutResID, ArrayList<Movie> movieList) {
        super(c,layoutResID,movieList);
        this.mContext = c;
        this.layoutResID = layoutResID;
        this.movieList = movieList;
    }

    public void setGridData()
    {
        //this.movieList = movieList;
        notifyDataSetChanged();
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent)
   {
       View row = convertView;
       ViewHolder holder;
       if (row == null)
       {
           LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
           row = inflater.inflate(layoutResID,parent,false);
           holder = new ViewHolder();
           holder.imageView = (ImageView)row.findViewById(R.id.movie_image);
           row.setTag(holder);
       }
       else {
            holder = (ViewHolder)row.getTag();
       }
       Movie movie = movieList.get(position);
       Picasso.with(mContext)
               .load(movie.getImgUrl())
               .into(holder.imageView);

       return row;
   }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }
static class ViewHolder
{
    ImageView imageView;
}
}
