package com.example.sara.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.sara.movieapp.Models.Movie;
import com.example.sara.movieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Sara on 10/17/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie>{

    public MovieAdapter( Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.poster_imageview , parent ,false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.postermovie);
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w185/" + getItem(position).getImage())
                .into(image);

        return convertView;
    }
}
