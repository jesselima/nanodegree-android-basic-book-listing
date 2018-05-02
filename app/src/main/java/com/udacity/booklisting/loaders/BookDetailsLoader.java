package com.udacity.booklisting.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.udacity.booklisting.utils.QueryBookUtils;
import com.udacity.booklisting.models.Book;


public class BookDetailsLoader extends AsyncTaskLoader<Book> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookDetailsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookDetailsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookDetailsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public Book loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        Book book = QueryBookUtils.fetchBookData(mUrl);
        return book;
    }
}

