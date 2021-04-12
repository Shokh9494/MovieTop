package com.example.movietop.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MovieDataBase dataBase;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavoriteMovie>> favouritemovies;


    public MainViewModel(@NonNull Application application) {
        super(application);
        dataBase = MovieDataBase.getInstance(getApplication());
        movies = dataBase.movieDao().getAllMovie();
        favouritemovies=dataBase.movieDao().getAllFavouriteMovies();
    }

    public LiveData<List<FavoriteMovie>> getFavouritemovies() {
        return favouritemovies;
    }

    public Movie getMovieById(int id) {
        try {
            return new GetMoviTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteAllMovies() {
        new DeleteMoviesTask().execute();
    }
    public void insertMovie(Movie movie) {
        new InsertTask().execute(movie);
    }
    public void deleteMovie(Movie movie) {
        new DeleteTask().execute(movie);
    }
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }


    public void insertFavouriteMovie( FavoriteMovie movies) {
        new InsertFavouriteTask().execute(movies);
    }
    public void deleteFavouriteMovie(FavoriteMovie movie) {
        new DeleteFavouriteTask().execute(movie);
    }
    public FavoriteMovie getFavouriteMovieById(int id) {
        try {
            return new GetFavouriteMoviTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




    private static class GetMoviTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return dataBase.movieDao().getMovieById(integers[0]);

            }
            return null;

        }
    }
    private static class DeleteMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... integers) {
                dataBase.movieDao().deleteAllMovies();
            return null;

        }
    }
    private static class InsertTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                dataBase.movieDao().insertMovie(movies[0]);

            }
            return null;

        }
    }
    private static class DeleteTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
                if (movies != null && movies.length > 0) {
                dataBase.movieDao().deleteMovie(movies[0]);

            }
            return null;

        }
    }




    private static class InsertFavouriteTask extends AsyncTask<FavoriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if (movies != null && movies.length > 0) {
                dataBase.movieDao().insertFavouriteMovies(movies[0]);

            }
            return null;

        }
    }
    private static class DeleteFavouriteTask extends AsyncTask<FavoriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if (movies != null && movies.length > 0) {
                dataBase.movieDao().deleteFavouriteMovie(movies[0]);

            }
            return null;

        }
    }
    private static class GetFavouriteMoviTask extends AsyncTask<Integer, Void, FavoriteMovie> {

        @Override
        protected FavoriteMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return dataBase.movieDao().getFavouriteMovieById(integers[0]);

            }
            return null;

        }
    }

}
