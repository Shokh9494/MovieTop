package com.example.movietop.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movietop.R;
import com.example.movietop.adapter.MovieAdapter;
import com.example.movietop.data.MainViewModel;
import com.example.movietop.data.Movie;
import com.example.movietop.utils.JSONUtils;
import com.example.movietop.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.movietop.R.color.design_default_color_primary_variant;
import static com.example.movietop.R.color.purple_200;
import static com.example.movietop.R.color.white;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private Switch switchSort;
    private RecyclerView recyclerViewSmallPosters;
    private MovieAdapter movieAdapter;
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    private ProgressBar progressBarLoading;

    private MainViewModel viewModel;


    private static int LOADER_id = 133;
    private LoaderManager loaderManager;


    private static int page = 1;
    private static int methodOfSort;
    private static boolean isLoading = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_main:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.item_favourite:
                Intent intentFavourite = new Intent(this, FavoriteActivity.class);
                startActivity(intentFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getColumnCount(){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width= (int) (displayMetrics.widthPixels/displayMetrics.density);
        return width/185>2?width/185:2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = LoaderManager.getInstance(this);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        progressBarLoading = findViewById(R.id.progresBarLoading);
        switchSort = findViewById(R.id.swichSort);
        recyclerViewSmallPosters = findViewById(R.id.recyclerViewPosters);
        recyclerViewSmallPosters.setLayoutManager(new GridLayoutManager(this, 3));
        movieAdapter = new MovieAdapter();


        recyclerViewSmallPosters.setAdapter(movieAdapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
                setMethodOfSort(isChecked);
                movieAdapter.setOnPosterClickListener(new MovieAdapter.onPosterClickListener() {
                    @Override
                    public void onPosterClick(int position) {
                        Movie movie = movieAdapter.getMovies().get(position);
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("id", movie.getId());
                        startActivity(intent);

                    }
                });
                movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
                    @Override
                    public void onReachEnd() {
                        if (!isLoading) {
                            downloadData(methodOfSort, page);
                        }
                    }
                });
                LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
                moviesFromLiveData.observe(MainActivity.this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        if (page==1){
                            movieAdapter.setMovies(movies);
                        }

                    }
                });

            }


        });

        switchSort.setChecked(false);
    }

    public void setPopularity(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }


    public void setTopRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }


    private void setMethodOfSort(boolean isTopRated) {
        if (isTopRated) {
            methodOfSort = NetworkUtils.TOP_RATED;

        } else {
            methodOfSort = NetworkUtils.POPULARITY;
        }

        downloadData(methodOfSort, page);
    }

    private void downloadData(int methodOfSort, int page) {
        URL url = NetworkUtils.builURL(methodOfSort, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_id, bundle, this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoadingListener() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {

        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                viewModel.deleteAllMovies();
                movieAdapter.clear();
            }
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            movieAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_id);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}