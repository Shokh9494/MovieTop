package com.example.movietop.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovie();

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavoriteMovie>> getAllFavouriteMovies();



    @Query("SELECT * FROM movies WHERE id == :moviId ")
    Movie getMovieById(int moviId);

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId ")
    FavoriteMovie getFavouriteMovieById(int movieId);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Insert
    void insertMovie(Movie movie);


    @Delete
    void deleteMovie(Movie movie);



    @Insert
    void insertFavouriteMovies(FavoriteMovie movie);


    @Delete
    void deleteFavouriteMovie(FavoriteMovie movie);




}
