package com.example.ayush.androidchallenge.AndroidLifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ayush.androidchallenge.R;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class MediaFragment extends android.support.v4.app.Fragment{
    @Inject
    Retrofit retrofitClient;
    ImageView enlargedImage;
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        enlargedImage = view.findViewById(R.id.enlarged_image);
        App.getComponent().inject(this);
        Bundle bundle = getArguments();
        String url = bundle.getString(getContext().getResources().getString(R.string.link));
        Picasso.with(context).load(url).placeholder(R.drawable.loading).into(enlargedImage);
        return view;
    }
}
