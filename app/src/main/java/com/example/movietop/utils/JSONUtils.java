package com.example.movietop.utils;

import com.example.movietop.data.Movie;
import com.example.movietop.data.Review;
import com.example.movietop.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static final String KEY_RESULT = "results";


    //For reviews
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";


    //For Video
    private static final String KEY_OF_VIDEO = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    //All information;
    private static final String KEY_VOTECOUNT = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINALTITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTERPATH = "poster_path";
    private static final String KEY_BACKDROPPATH = "backdrop_path";
    private static final String KEY_VOTEAVERAGE = "vote_average";
    private static final String KEY_REALESEDATE = "release_date";


    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    public static ArrayList<Review> getReviewFromJSON(JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(KEY_RESULT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjecReview = jsonArray.getJSONObject(i);
                String author = jsonObjecReview.getString(KEY_AUTHOR);
                String content = jsonObjecReview.getString(KEY_CONTENT);
                Review review = new Review(author, content);
                result.add(review);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static ArrayList<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(KEY_RESULT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjecTrailers = jsonArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + jsonObjecTrailers.getString(KEY_OF_VIDEO);
                String name = jsonObjecTrailers.getString(KEY_NAME);
                Trailer trailer=new Trailer(key,name);
                result.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                int voteCount = objectMovie.getInt(KEY_VOTECOUNT);
                String title = objectMovie.getString(KEY_TITLE);
                String originaTitle = objectMovie.getString(KEY_ORIGINALTITLE);
                String overview = objectMovie.getString(KEY_OVERVIEW);
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTERPATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTERPATH);
                String backDropPAth = objectMovie.getString(KEY_BACKDROPPATH);
                double voteAverage = objectMovie.getDouble(KEY_VOTEAVERAGE);
                String realeseDate = objectMovie.getString(KEY_REALESEDATE);
                Movie movie = new Movie(id, voteCount, title, originaTitle, overview, posterPath, backDropPAth, voteAverage, realeseDate, bigPosterPath);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
