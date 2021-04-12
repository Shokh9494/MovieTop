package com.example.movietop.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavoriteMovie extends Movie {
    public FavoriteMovie(int uniqueId, int id, int voteCount, String title, String originalTitle, String overview, String posterPath, String backdropPath, double voteAverage, String releaseDate, String bigPosterPath) {
        super(uniqueId,id, voteCount, title, originalTitle, overview, posterPath, backdropPath, voteAverage, releaseDate, bigPosterPath);
    }

    @Ignore
    public FavoriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getVoteCount(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverview(), movie.getPosterPath(), movie.getBackdropPath(), movie.getVoteAverage(), movie.getReleaseDate(), movie.getBigPosterPath());
    }
}
