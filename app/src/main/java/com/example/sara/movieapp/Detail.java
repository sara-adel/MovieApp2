package com.example.sara.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailFragment.DETAIL_URI , getIntent().getSerializableExtra(DetailFragment.DETAIL_URI));

        DetailFragment detail = new DetailFragment();
        detail.setArguments(bundle);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailActivity , detail)
                    .commit();
        }
    }
}
