package com.example.ayush.androidchallenge.AndroidLifecycle;

import android.arch.persistence.room.Insert;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayush.androidchallenge.AppUtils.ImageAdapter;
import com.example.ayush.androidchallenge.DataManagementUtils.AppDatabase;
import com.example.ayush.androidchallenge.DataManagementUtils.Suggestion;
import com.example.ayush.androidchallenge.NetworkUtils.GetRequests;
import com.example.ayush.androidchallenge.POJO.Image;
import com.example.ayush.androidchallenge.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class SearchFragment extends Fragment implements ImageAdapter.OnItemClickListener {

    private final int THRESHOLD = 10;

    @Inject
    Retrofit retrofitClient;
    @Inject
    Context context;
    @Inject
    AppDatabase db;

    private RecyclerView imageGrid;
    private MenuItem menuItem;
    private ImageAdapter adapter;
    private Observable dataStream;
    private ArrayList<String> imageLinks;
    private int page = 1;
    private String currQuery;
    private ArrayList<String> suggestionStrings;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container,false);
        App.getComponent().inject(this);

        imageGrid = view.findViewById(R.id.image_grid);
        imageLinks = new ArrayList<String>();
        adapter = new ImageAdapter(this);
        suggestionStrings = new ArrayList<String>();

        adapter.setLinks(imageLinks);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        imageGrid.setLayoutManager(layoutManager);
        imageGrid.setAdapter(adapter);
        imageGrid.addOnScrollListener(gridScrollChangeListener);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(menuItem!=null){
            menuItem.setVisible(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(menuItem!=null){
            menuItem.setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_item, menu);
        menuItem = menu.findItem(R.id.search);
        menuItem.setVisible(true);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        dataStream = emitQueryObservable(searchView)
                .flatMap(query -> {
                    imageLinks.clear();
                    adapter.notifyDataSetChanged();
                    currQuery = query;
                    page = 1;
                    return emitImageLinksObservable(query, page);
                });
        dataStream.subscribe(imageObserver());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(String link) {
        Fragment mediaFragment = new MediaFragment();
        Bundle bundle = new Bundle();
        bundle.putString(context.getResources().getString(R.string.link), link);
        mediaFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .add(R.id.frame, mediaFragment)
                .hide(SearchFragment.this)
                .addToBackStack(SearchFragment.class.getName())
                .commit();
    }

    /**
     * Observable wrapper around SearchView's OnQueryTextListener. It is the first part of our reactive chain.
     * NOTE: It never calls onComplete. It should remain active throughout the lifecycle of this fragment.
     * @param searchView View
     * @return Observable that emit query texts
     */
    private Observable<String> emitQueryObservable(final android.support.v7.widget.SearchView searchView){
        return Observable.create((ObservableEmitter<String> emitter) -> searchView
                .setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) {
                    emitter.onNext(query);
                    Suggestion suggestion = new Suggestion();
                    suggestion.setSuggestion(query);
                    // insert suggestion to DB

                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    emitter.onNext(newText);
                }
                return true;
            }
        })).throttleLast(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /***
     * Make a get request and extracts the List of images from serialized POJO - Imgur
     * @param str String
     * @return Observable with list of images.
     */
    private Observable<List<Image>> emitImageLinksObservable(String str, int page){
        return retrofitClient.create(GetRequests.class).getData(page, str)
                .filter(imgur -> imgur!=null && imgur.getData()!=null)
                .flatMap(imgur -> Observable.just(imgur.getData()))
                .flatMap(Observable::fromIterable)
                .filter(datum -> datum!=null && datum.getImages()!=null)
                .flatMap(datum -> Observable.just(datum.getImages()))
                .filter(imageList -> imageList!=null && imageList.size() > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry();
    }

    /***
     * Observer that adds images to adapter.  It's the last part of our chain.
     */
    private final Observer<List<Image>> imageObserver() {
        return new Observer<List<Image>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Image> imageList) {
                for(int i = 0; i < imageList.size(); i++) {
                    imageLinks.add(imageList.get(i).getLink());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private final RecyclerView.OnScrollListener gridScrollChangeListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int[] spans = new int[layoutManager.getSpanCount()];

            int totalItems = layoutManager.getItemCount();
            int visibleItems = layoutManager.getChildCount();
            int lastPos = 0;
            layoutManager.findLastVisibleItemPositions(spans);
            for(int i = 0; i < spans.length; i++){
                lastPos = Math.max(lastPos, spans[i]);
            }
            if(lastPos + visibleItems + THRESHOLD >= totalItems){
                emitImageLinksObservable(currQuery, ++page).subscribe(imageObserver());
            }
        }
    };

}
