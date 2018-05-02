package com.udacity.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.udacity.booklisting.adapters.BookAdapter;
import com.udacity.booklisting.loaders.BookLoader;
import com.udacity.booklisting.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    //private String searchTerms = "node,android";
    private String searchTerms = "ios, android, javascript";
    private static final String LOG_TAG = BookListActivity.class.getName();
    //private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter booksAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_activity);

        SearchView searchView = findViewById(R.id.search_view_book_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Updates the search term from the search input.
                searchTerms = query;
                // Restart the loader using the new term
                restartLoaderBooks();

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return false;
            }
        });

        ListView bookListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        booksAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(booksAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Book book = booksAdapter.getItem(position);
                String id = book.getId();

            Intent intent = new Intent(getApplicationContext(), BooksDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }


    public void restartLoaderBooks(){
        // Clear the ListView as a new query will be kicked off
        booksAdapter.clear();
        // Hide the empty state text view as the loading indicator will be displayed
        mEmptyStateTextView.setVisibility(View.GONE);
        // Show the loading indicator while new data is being fetched
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        // Restart the loader to requery the BOOKS as the query settings have been updated
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchTerms);
        uriBuilder.appendQueryParameter("filter", "ebooks");
        uriBuilder.appendQueryParameter("libraryRestrict", "no-restrict");
        uriBuilder.appendQueryParameter("maxResults", "40");
        uriBuilder.appendQueryParameter("orderBy", "relevance");

        Log.v("Requested URL", uriBuilder.toString());

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        //mAdapter.clear();
        
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            booksAdapter.addAll(books);
//            updateUi(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        booksAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
