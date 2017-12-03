package com.example.sara.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sara.movieapp.Models.Movie;

public class Main extends AppCompatActivity implements MovieFragment.Callback{

    private boolean twopane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main, new MovieFragment(), "Main Fragment List")
                    .commit();
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (twopane) {
            Bundle args = new Bundle();
            args.putSerializable(DetailFragment.DETAIL_URI,  movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailActivity, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, Detail.class).putExtra(DetailFragment.DETAIL_URI, movie);
            startActivity(intent);
        }
    }
}
