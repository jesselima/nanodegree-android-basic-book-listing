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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.udacity.booklisting.loaders.BookDetailsLoader;
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
    Button buttonBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        buttonBuy = findViewById(R.id.button_buy);

        Bundle bookData = getIntent().getExtras();
        bookDI = bookData.getString("id");

        mEmptyStateTextView = findViewById(R.id.empty_view);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<Book> onCreateLoader(int i, Bundle bundle) {

        String UrlBook = REQUEST_URL + bookDI;

        Uri baseUri = Uri.parse(UrlBook);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("projection", "full");

        // TODO: Test
        Log.v("Requested URL", uriBuilder.toString());

        return new BookDetailsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<Book> loader, Book book) {
        // Hide loading indicator because the data has been loaded
        ProgressBar loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
//        mEmptyStateTextView.setText(R.string.no_books);

        if (book != null) {
           updateUi(book);
        }
    }

    @Override
    public void onLoaderReset(Loader<Book> loader) {
        // Loader reset, so we can clear out our existing data.
        Book book = new Book();
    }

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
            buttonBuy.setText(R.string.unavailable);
            buttonBuy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTextAlert));
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

    private void updateBookImage(Bitmap result) {
        ProgressBar progressBar = findViewById(R.id.loading_image_book_indicator);
        ImageView imageBook = findViewById(R.id.image_view_book);
        imageBook.setImageBitmap(result);
        progressBar.setVisibility(View.GONE);
        imageBook.setVisibility(View.VISIBLE);
    }

    private String formatPrice(double price) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(price);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

}
