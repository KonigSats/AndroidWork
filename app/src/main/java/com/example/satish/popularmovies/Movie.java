package com.example.satish.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private int movId;
    private String movTitle;
    private String imgUrl;
    private String synopsis;
    private double rating;
    private String date;

    public Movie(int movId, String movTitle, String imgUrl,String synopsis,double rating,String date)
    {
        this.movId = movId;
        this.movTitle = movTitle;
        this.imgUrl = imgUrl;
        this.synopsis = synopsis;
        this.rating = rating;
        this.date = date;
    }
    public int getMovId()
    {
        return this.movId;
    }
    public String getMovTitle()
    {
        return this.movTitle;
    }
    public String getImgUrl()
    {
        return this.imgUrl;
    }
    public String getSynopsis()
    {
        return this.synopsis;
    }
    public double getRating()
    {
        return this.rating;
    }
    public String getDate()
    {
        return this.date;
    }

    public void setMovId(int movId)
    {
        this.movId = movId;
    }
    public void setMovTitle(String movTitle)
    {
        this.movTitle = movTitle;
    }
    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }
    public void setSynopsis(String synopsis)
    {
        this.synopsis = synopsis;
    }
    public void setRating(double rating)
    {
        this.rating = rating;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(movId);
        dest.writeString(movTitle);
        dest.writeString(imgUrl);
        dest.writeString(synopsis);
        dest.writeDouble(rating);
        dest.writeString(date);
    }

    private Movie(Parcel in)
    {
        movId = in.readInt();
        movTitle = in.readString();
        imgUrl = in.readString();
        synopsis = in.readString();
        rating = in.readDouble();
        date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel in){
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };
}
