package com.example.ayush.androidchallenge.NetworkUtils;

import com.example.ayush.androidchallenge.ImgurAuth;
import com.example.ayush.androidchallenge.POJO.Datum;
import com.example.ayush.androidchallenge.POJO.Imgur;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetRequests {
    @Headers(ImgurAuth.CLIENT_ID)
    @GET("3/gallery/search/{page}")
    Observable<Imgur> getData(@Path("page") int pageNum, @Query("q") String query);
}