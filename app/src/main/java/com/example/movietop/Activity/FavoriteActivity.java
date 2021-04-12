package com.example.movietop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.movietop.R;
import com.example.movietop.adapter.MovieAdapter;
import com.example.movietop.data.FavoriteMovie;
import com.example.movietop.data.MainViewModel;
import com.example.movietop.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavouriteMovies;
    private MovieAdapter adapter;
    private MainViewModel viewModel;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.item_main:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case  R.id.item_favourite:
                Intent intentFavourite=new Intent(this,FavoriteActivity.class);
                startActivity(intentFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter();
        recyclerViewFavouriteMovies.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        LiveData<List<FavoriteMovie>> favouriteMovies=viewModel.getFavouritemovies();
        favouriteMovies.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                List<Movie> movies=new ArrayList<>();
                if (favoriteMovies!=null){
                movies.addAll(favoriteMovies);
                adapter.setMovies(movies);
            }
            }
        });

        adapter.setOnPosterClickListener(new MovieAdapter.onPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie=adapter.getMovies().get(position);
                Intent intent=new Intent(FavoriteActivity.this,DetailActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);

            }
        });

    }
}