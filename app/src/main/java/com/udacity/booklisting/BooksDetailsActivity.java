package com.udacity.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.udacity.booklisting.loaders.BookDetailsLoader;
import com.udacity.booklisting.loaders.BookLoader;
import com.udacity.booklisting.models.Book;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BooksDetailsActivity extends AppCompatActivity implements LoaderCallbacks<Book> {

    private static final String LOG_TAG = BooksDetailsActivity.class.getName();
    private String bookDI;
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes/";
    private static final int BOOK_LOADER_ID = 2;
    private TextView mEmptyStateTextView;
    private LinearLayout rootContentLayout;
    Button buttonBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        buttonBuy = findViewById(R.id.button_buy);

        Bundle bookData = getIntent().getExtras();
        bookDI = bookData.getString("id");

        rootContentLayout = findViewById(R.id.root_content_layout);
        mEmptyStateTextView = findViewById(R.id.empty_view);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            rootContentLayout.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);

        }

    }

    /**
     * This method get the REQUEST_URL ("https://www.googleapis.com/books/v1/volumes") and
     * parse this REQUEST_URL + the Book ID to a Uri object. Then uses the Uri.Builder to create the query over the
     * REQUEST_URL. The final result is a Uri.Builder that is converted to String and added to
     * a new BookDetailsLoader object. This BookDetailsLoader object instantiate a new Book object.
     * Then the this Book will receive the result of QueryUtils.fetchBookData(mUrl) that in its turn
     * will make the request under the loadInBackground method in the BookDetailsLoader;
     * @param i is the ID whose loader is to be created.
     * @param bundle is any arguments supplied by the caller.
     * @return a new {@link BookDetailsLoader} object. This object receives the full
     * Url (including its query parameters)
     */
    @Override
    public Loader<Book> onCreateLoader(int i, Bundle bundle) {

        String UrlBook = REQUEST_URL + bookDI;

        Uri baseUri = Uri.parse(UrlBook);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("projection", "full");

        // Test: Output the Requested URL
        Log.v("Requested URL", uriBuilder.toString());

        return new BookDetailsLoader(this, uriBuilder.toString());
    }

    /**
     * Take action when the loader finishes its task. Hide the loading indicator and add
     * the Book data {@link Book} object.
     * I the Book object has data then call the update UI method.
     * @param loader is the {@link BookDetailsLoader} object.
     * @param book is the book object with data. It
     */
    @Override
    public void onLoadFinished(Loader<Book> loader, Book book) {
        // Hide loading indicator because the data has been loaded
        ProgressBar loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        if (book != null) {
           updateUi(book);
        }
    }

    /**
     * Clear the Book object data and creates another new Book object.
     * @param loader is the loader object that call the BookLoaderDetails
     */
    @Override
    public void onLoaderReset(Loader<Book> loader) {
        // Loader reset, so we can clear out our existing data.
        Book book = new Book();
    }

    /**
     * Update the UI with tha book data.
     * @param book is the book object with book details data.
     */
    public void updateUi(final Book book){

        TextView title = findViewById(R.id.text_view_title);
        title.setText(book.getTitle());

        TextView authors = findViewById(R.id.text_view_authors);
        authors.setText(book.getAuthors());

        TextView pages = findViewById(R.id.text_view_pages_count);
        pages.setText(String.valueOf(book.getPageCount()));

        RatingBar ratingBar = findViewById(R.id.rating_bar_view);
        ratingBar.setRating(book.getAverageRating());

        String price = formatPrice(book.getListPrice());
        double listPrice = book.getListPrice();
        if(listPrice == 0){
            buttonBuy.setText(R.string.free);
            buttonBuy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textWhite));
            if(book.getMbuyLink().isEmpty()){
                buttonBuy.setEnabled(false);
                buttonBuy.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
            }
        }else {
            String buttonBuyText = getString(R.string.buy) + book.getCurrencyCode() + getString(R.string.space) + price;
            buttonBuy.setText(buttonBuyText);
        }

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri bookUri = Uri.parse(book.getMbuyLink());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });

        TextView ratingNumber = findViewById(R.id.text_view_ratings_number);
        int rating = book.getmRatingsCount();
        if(rating == 0){
            ratingNumber.setText("");
        }else {
            ratingNumber.setText(String.valueOf(book.getmRatingsCount()));
        }

        TextView subTitle = findViewById(R.id.text_view_sub_title);
        if (book.getmSubTitle().length() > 0){
            subTitle.setText(book.getmSubTitle());
        } else {
            subTitle.setVisibility(View.GONE);
        }

        TextView description = findViewById(R.id.text_view_description);
        description.setText(book.getmDescription());

        TextView publisher = findViewById(R.id.text_view_publisher);
        publisher.setText(book.getmPublisher());

        TextView publishedDate = findViewById(R.id.text_view_published_date);
        String dateString = book.getmPublishedDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(dateString);
            publishedDate.setText(String.valueOf(formatDate(date)));
        } catch (ParseException e) {
            publishedDate.setText(R.string.unknown_date);
            e.printStackTrace();
        }

        new DownloadImageTask((ImageView)findViewById(R.id.image_view_book))
                .execute(book.getmImageLink());

    }

    /**
     * Download the book cover image data in a AsyncTask
     * When the Download is finished update the UI with the image.
     */
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageViewBook;

        private DownloadImageTask(ImageView imageViewBook) {
            this.imageViewBook = imageViewBook;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            updateBookImage(result);
        }
    }

    /**
     * When call update the Book cover image on the UI. It's called after the AsyncTask DownloadImageTask is completed.
     * @param result
     */
    private void updateBookImage(Bitmap result) {
        ProgressBar progressBar = findViewById(R.id.loading_image_book_indicator);
        ImageView imageBook = findViewById(R.id.image_view_book);
        imageBook.setImageBitmap(result);
        progressBar.setVisibility(View.GONE);
        imageBook.setVisibility(View.VISIBLE);
    }

    /**
     * This method receives the price (data type double) as input parameter and convert to string in the given pattern.
     * @param price is the price of the book
     * @return a string with the price formatted according to the DecimalFormat method pattern.
     */
    private String formatPrice(double price) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(price);
    }

    /**
     * This method receives the date (data type Date) as input parameter and
     * @param dateObject is the date to be formatted.
     * @return a string with the date formatted according to the SimpleDateFormat method pattern.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

}
