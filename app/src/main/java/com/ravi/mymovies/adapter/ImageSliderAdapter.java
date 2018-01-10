package com.ravi.mymovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ravi.mymovies.R;
import com.ravi.mymovies.model.Backdrop;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageSliderAdapter extends PagerAdapter {
    private final List<Backdrop> mBackdropList;
    private final LayoutInflater mInflater;
    private final Context mContext;
    @BindView(R.id.image_movie)
    ImageView imageMovie;

    public ImageSliderAdapter(List<Backdrop> mBackdropList, Context mContext) {
        this.mBackdropList = mBackdropList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mBackdropList.size() > 5)
            return 5;
        else
            return mBackdropList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myImageLayout = mInflater.inflate(R.layout.image_slider, view, false);
        ButterKnife.bind(this, myImageLayout);
        setImage(position);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    private void setImage(int position) {
        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w500" + mBackdropList.get(position).getFilePath())
                .fit()
                .placeholder(R.drawable.place_holder)
                .into(imageMovie);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
