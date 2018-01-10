package com.ravi.mymovies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ravi.mymovies.R;
import com.ravi.mymovies.activities.MainActivity;
import com.ravi.mymovies.adapter.ImageSliderAdapter;
import com.ravi.mymovies.model.MovieImage;
import com.ravi.mymovies.model.Result;
import com.ravi.mymovies.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends Fragment {
    private static final String TAG = "MovieDetailsFragment";
    @BindView(R.id.view_pager_movie_image)
    ViewPager viewPagerMovieImage;
    @BindView(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.text_movie_title)
    TextView textMovieTitle;
    @BindView(R.id.text_movie_description)
    TextView textMovieDescription;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    private String mTitle;
    private int mId;
    private Call<Result> mCallResultList;
    private Call<MovieImage> mCallMovieImage;

    public MovieDetailsFragment() {
    }

    public static MovieDetailsFragment newInstance(Integer mId, String mTitle) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_ID, mId);
        bundle.putString(Constants.BUNDLE_TITLE, String.valueOf(mTitle));
        movieDetailsFragment.setArguments(bundle);
        return movieDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getInt(Constants.BUNDLE_ID);
            mTitle = bundle.getString(Constants.BUNDLE_TITLE);
        }
        getActivity().setTitle(mTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mCallResultList = MainActivity.sApiService.getMovieDetails(mId, Constants.API_KEY);
        mCallMovieImage = MainActivity.sApiService.getMovieImages(mId, Constants.API_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCallResultList.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                Result result = response.body();
                if (result != null) {
                    textMovieTitle.setText(result.getTitle());
                    textMovieDescription.setText(result.getOverview());
                    ratingBar.setRating(getRating(Math.ceil(result.getPopularity())));
                    ratingBar.setIsIndicator(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

        mCallMovieImage.enqueue(new Callback<MovieImage>() {
            @Override
            public void onResponse(@NonNull Call<MovieImage> call, @NonNull Response<MovieImage> response) {
                viewPagerMovieImage.setAdapter(new ImageSliderAdapter(response.body().getBackdrops(), getContext()));
                circleIndicator.setViewPager(viewPagerMovieImage);
            }

            @Override
            public void onFailure(Call<MovieImage> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private float getRating(double ceil) {
        if (ceil <= 50)
            return (float) 0.5;
        else if (ceil > 50 && ceil <= 100)
            return (float) 1.0;
        else if (ceil > 100 && ceil <= 150)
            return (float) 1.5;
        else if (ceil > 150 && ceil <= 200)
            return (float) 2.0;
        else if (ceil > 200 && ceil <= 250)
            return (float) 2.5;
        else if (ceil > 250 && ceil <= 300)
            return (float) 3.0;
        else if (ceil > 300 && ceil <= 350)
            return (float) 3.5;
        else if (ceil > 350 && ceil <= 400)
            return (float) 4.0;
        else if (ceil > 400 && ceil <= 450)
            return (float) 4.5;
        else
            return (float) 5.0;
    }

}
