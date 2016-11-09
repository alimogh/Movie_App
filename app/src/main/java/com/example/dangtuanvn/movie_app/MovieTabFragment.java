package com.example.dangtuanvn.movie_app;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dangtuanvn.movie_app.datastore.FeedDataStore;
import com.example.dangtuanvn.movie_app.datastore.MovieFeedDataStore;
import com.example.dangtuanvn.movie_app.datastore.NewsFeedDataStore;
import com.example.dangtuanvn.movie_app.model.Movie;
import com.example.dangtuanvn.movie_app.model.News;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sinhhx on 11/7/16.
 */
public class MovieTabFragment extends Fragment {

    private int mPage;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String ARG_PAGE = "ARG_PAGE";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private MovieFeedDataStore movieFeedDataStore;
    private NewsFeedDataStore newsFeedDataStore;

    public static MovieTabFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MovieTabFragment fragment = new MovieTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.movietabrecycler, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (mPage == 1) {
                movieFeedDataStore = new MovieFeedDataStore(getContext(), MovieFeedDataStore.DataType.SHOWING);
                swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeLayout.setRefreshing(true);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                movieFeedDataStore.getList(new FeedDataStore.OnDataRetrievedListener() {
                                    @Override
                                    public void onDataRetrievedListener(List list, Exception ex) {
                                        List<Movie> movieShowingList = (List<Movie>) list;
                                        mAdapter = new MovieDetailAdapter(getContext(), movieShowingList, mPage);
                                        mRecyclerView.setAdapter((mAdapter));
                                        swipeLayout.setRefreshing(false);
                                    }
                                });
                            }
                        }, 2000);
                    }
                });
                movieFeedDataStore.getList(new FeedDataStore.OnDataRetrievedListener() {
                    @Override
                    public void onDataRetrievedListener(List list, Exception ex) {
                        List<Movie> movieShowingList = (List<Movie>) list;
                        mAdapter = new MovieDetailAdapter(getContext(), movieShowingList, mPage);
                        mRecyclerView.setAdapter((mAdapter));
                    }
                });
            }

            else if(mPage == 2 || mPage == 3) {
                movieFeedDataStore = new MovieFeedDataStore(getContext(), MovieFeedDataStore.DataType.UPCOMING);

                swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        swipeLayout.setRefreshing(true);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                movieFeedDataStore.getList(new FeedDataStore.OnDataRetrievedListener() {
                                    @Override
                                    public void onDataRetrievedListener(List list, Exception ex) {
                                        List<Movie> movieShowingList = (List<Movie>) list;
                                        mAdapter = new MovieDetailAdapter(getContext(), movieShowingList, mPage);
                                        mRecyclerView.setAdapter((mAdapter));
                                        swipeLayout.setRefreshing(false);
                                    }
                                });
                            }
                        }, 2000);

                    }

                });
                movieFeedDataStore.getList(new FeedDataStore.OnDataRetrievedListener() {
                    @Override
                    public void onDataRetrievedListener(List list, Exception ex) {
                        List<Movie> movieShowingList = (List<Movie>) list;
                        mAdapter = new MovieDetailAdapter(getContext(), movieShowingList, mPage);
                        mRecyclerView.setAdapter((mAdapter));
                    }
                });
            }

            else if(mPage == 4) {
                newsFeedDataStore = new NewsFeedDataStore(getContext());
                swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        swipeLayout.setRefreshing(true);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                newsFeedDataStore.getList(new FeedDataStore.OnDataRetrievedListener() {
                                    @Override
                                    public void onDataRetrievedListener(List list, Exception ex) {
                                        List<News> newsShowingList = (List<News>) list;
                                        mAdapter = new NewsDetailAdapter(getContext(), newsShowingList, mPage);
                                        mRecyclerView.setAdapter((mAdapter));
                                        swipeLayout.setRefreshing(false);
                                    }
                                });
                            }
                        }, 2000);
                    }

                });
                newsFeedDataStore.getList(new FeedDataStore.OnDataRetrievedListener() {
                    @Override
                    public void onDataRetrievedListener(List list, Exception ex) {
                        List<News> newsShowingList = (List<News>) list;
                        mAdapter = new NewsDetailAdapter(getContext(), newsShowingList, mPage);
                        mRecyclerView.setAdapter((mAdapter));
                    }
                });
            }
        }
        else {
            // NO NETWORK CONNECTION
        }
        return view;
    }

    private enum TAB{
        SHOWING,
        UPCOMING,
        AROUND,
        NEWS
    }
}

