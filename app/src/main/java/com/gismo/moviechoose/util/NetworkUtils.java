package com.gismo.moviechoose.util;

import android.net.Uri;
import android.util.Log;

import com.gismo.moviechoose.BuildConfig;
import com.gismo.moviechoose.enums.SortingOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by test on 2018. 02. 24..
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String MOVIE_DB_API = "https://api.themoviedb.org/3/movie";
    public static final String MOVIE_PICTURE_URL = "https://image.tmdb.org/t/p/";
    public static final String MOVIE_PICTURE_THUMBNAIL = "w500";
    public static final String MOVIE_PICTURE_BIG = "w1280";
    public static final String MOVIE_PICTURE_ORIGINAL = "original";


    final static String POPULAR = "popular";
    final static String TOP_RATED = "top_rated";
    final static String PAGE = "page";
    final static String API_KEY_PARAM = "api_key";
    final static String API_KEY = BuildConfig.API_KEY;


    public static URL buildUrl(SortingOptions sortingOption) {

        String sortingPath = null;
        switch (sortingOption) {
            case TOP_RATED:
                sortingPath = TOP_RATED;
                break;
            case MOST_POPULAR:
                sortingPath = POPULAR;
                break;
        }

        Uri builtUri = Uri.parse(MOVIE_DB_API).buildUpon()
                .appendPath(sortingPath)
                .appendQueryParameter(PAGE, "1")
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}