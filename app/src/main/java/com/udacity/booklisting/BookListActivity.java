package com.udacity.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.udacity.booklisting.adapters.BookAdapter;
import com.udacity.booklisting.loaders.BookLoader;
import com.udacity.booklisting.models.Book;

import java.util.ArrayList;
import java.util.List;

// import android.support.v7.widget.SearchView;

public class BookListActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    private String searchTerms = "ios, android, javascript";
    private static final String LOG_TAG = BookListActivity.class.getName();
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter booksAdapter;
    private TextView mEmptyStateTextView, textViewNoResultsFound;
    private static String lastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //** Get the search input and update search term
        handleIntent(getIntent());

        textViewNoResultsFound = findViewById(R.id.no_books_found_text);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoaderBooks();
                Snackbar.make(view, "List reloaded!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    /**
     * Restart the loader.
     */
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

    /**
     * This method get the REQUEST_URL ("https://www.googleapis.com/books/v1/volumes") and
     * parse this REQUEST_URL to a Uri object. Then uses the Uri.Builder to create the query over the
     * REQUEST_URL. The final result is a Uri.Builder that is converted to String and added to
     * a new BookLoader object. This BookLoader object instantiate a new List<Book> object.
     * Then the this List<Book> will receive the result of QueryUtils.fetchBookData(mUrl) that in its turn
     * will make the request under the loadInBackground method in the BookLoader;
     * @param i is the ID whose loader is to be created.
     * @param bundle is any arguments supplied by the caller.
     * @return a new {@link BookLoader} object. This object receives the full
     * Url (including its query parameters)
     */
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchTerms);
        uriBuilder.appendQueryParameter("filter", "ebooks");
        uriBuilder.appendQueryParameter("libraryRestrict", "no-restrict");
        uriBuilder.appendQueryParameter("maxResults", "40");
        uriBuilder.appendQueryParameter("orderBy", "relevance");

        Log.v("Requested URL: ", uriBuilder.toString());

        return new BookLoader(this, uriBuilder.toString());
    }

    /**
     * Take action when the loader finishes its task. Hide the loading indicator and add
     * the list of books do the {@link BookAdapter} object. But is the list of books is empty
     * show a text message in the UI.
     * @param loader is the {@link BookLoader} object
     * @param books is the list od books.
     */
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            textViewNoResultsFound.setVisibility(View.GONE);
            booksAdapter.addAll(books);
        }else{
            textViewNoResultsFound.setVisibility(View.VISIBLE);
        }
    }

    /**
     * When the loader is reset, its clear the adapter for a better performance.
     * @param loader is the loader of the list of books
     */
    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        booksAdapter.clear();
    }

    // Inflates the search box in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        //associate the searchable configuration with the SearchView by calling setSearchableInfo(SearchableInfo)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    /**
     * This method is call the handleIntent method every time the user type a Term in the search box on the action bar.
     * @param intent Intent object that gets the
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * When this method is call, it receives a Intent object as argument. This object handles the
     * user text input and updates the searchTerms variable fetching books according to the
     * @param intent is the Intent object that takes the user input term.
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchTerms = query;
            lastSearch = query;
            Log.v("Search term: ", query);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartLoaderBooks();
    }

}
