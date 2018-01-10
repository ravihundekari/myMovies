package com.ravi.mymovies.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ravi.mymovies.ApiClient;
import com.ravi.mymovies.R;
import com.ravi.mymovies.fragments.MovieListFragment;
import com.ravi.mymovies.interfaces.ApiInterface;

public class MainActivity extends AppCompatActivity {
    public static ApiInterface sApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addFirstFragment();
    }

    private void addFirstFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_movies, MovieListFragment.newInstance());
        fragmentTransaction.commit();
    }

    private void init() {
        sApiService = ApiClient.getClient().create(ApiInterface.class);
    }

}
