package com.example.movietop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movietop.R;
import com.example.movietop.adapter.ReviewAdapter;
import com.example.movietop.adapter.TrailerAdapter;
import com.example.movietop.data.FavoriteMovie;
import com.example.movietop.data.MainViewModel;
import com.example.movietop.data.Movie;
import com.example.movietop.data.Review;
import com.example.movietop.data.Trailer;
import com.example.movietop.utils.JSONUtils;
import com.example.movietop.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.*;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewRealeaseDate;
    private TextView textViewOverviwe;
    private ImageView imageViewAddToFavourite;


    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    private int id;
    private Movie movie;
    private FavoriteMovie favoriteMovie;
    private MainViewModel viewModel;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewRealeaseDate = findViewById(R.id.textViewRealiseDate);
        textViewOverviwe = findViewById(R.id.textViewOverview);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavorite);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id);
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRealeaseDate.setText(movie.getReleaseDate());
        textViewOverviwe.setText(movie.getOverview());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        setFavourite();

        recyclerViewTrailers = findViewById(R.id.recyclerViewTrellers);
        recyclerViewReviews = findViewById(R.id.recyclerViewRevies);
        reviewAdapter = new ReviewAdapter();
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOntrailerClickListner(new TrailerAdapter.OntrailerClickListner() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            }
        });
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        JSONObject jsonObject = NetworkUtils.getJSONForVideos(movie.getId());
        JSONObject jsonObject1 = NetworkUtils.getJSONForReviews(movie.getId());
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObject);
        ArrayList<Review> reviews = JSONUtils.getReviewFromJSON(jsonObject1);
        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);


    }

    public void onClickChangeFavorite(View view) {
        if (favoriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavoriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favourite, LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favoriteMovie);
            Toast.makeText(this, R.string.remove_from_favourite, LENGTH_SHORT).show();

        }
        setFavourite();
    }


    private void setFavourite() {
        favoriteMovie = viewModel.getFavouriteMovieById(id);
        if (favoriteMovie == null) {
            imageViewAddToFavourite.setImageResource(R.drawable.ic__starborder);
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.ic_star);
        }
    }

}