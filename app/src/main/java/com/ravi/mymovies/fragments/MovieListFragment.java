package com.ravi.mymovies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravi.mymovies.R;
import com.ravi.mymovies.activities.MainActivity;
import com.ravi.mymovies.adapter.MoviesAdapter;
import com.ravi.mymovies.model.MovieResult;
import com.ravi.mymovies.model.Result;
import com.ravi.mymovies.utils.Constants;
import com.ravi.mymovies.utils.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment{
    private static final String TAG = "MovieListFragment";
    @BindView(R.id.recycler_movie_list)
    RecyclerView recyclerMovieList;
    @BindView(R.id.text_connectivity_message)
    TextView textConnectivityMessage;
    private Call<MovieResult> mCallMovieList;

    public MovieListFragment() {
    }

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        init();
        getActivity().setTitle(getString(R.string.upcoming_movies));
        return view;
    }

    private void init() {
        recyclerMovieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMovieList.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider)));
        mCallMovieList = MainActivity.sApiService.getUpcomingMovies(Constants.API_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCallMovieList.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                if (textConnectivityMessage.isShown())
                    textConnectivityMessage.setVisibility(View.GONE);
                List<Result> movieList = response.body().getResults();
                recyclerMovieList.setAdapter(new MoviesAdapter(movieList, getActivity()));
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                textConnectivityMessage.setVisibility(View.VISIBLE);
                Log.d(TAG, "onFailure: " + t.toString());

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_movies, InformationFragment.newInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
