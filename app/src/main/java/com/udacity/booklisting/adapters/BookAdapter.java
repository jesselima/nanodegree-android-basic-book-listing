package com.udacity.booklisting.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.udacity.booklisting.R;
import com.udacity.booklisting.models.Book;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * An {@link BookAdapter} knows how to create a list item layout for each book
 * in the data source (a list of {@link Book} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Constructs a new {@link BookAdapter}.
     *
     * @param context of the app
     * @param books is the list of earthquakes, which is the data source of the adapter
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position is the position of each book object in the list.
     * @param convertView is the View object the receives the inflated layout.
     * @param parent is the ViewGroup object used by the Inflater.
     * @return a listItemView object represents the inflated layout filled with
     * data for each item in the list on th UI
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.book_list_item, parent, false);
            }

        Book currentBook = getItem(position);

        TextView mTitle = listItemView.findViewById(R.id.text_view_title);
            mTitle.setText(currentBook.getTitle());

        TextView mAuthors = listItemView.findViewById(R.id.text_view_authors);
            if(currentBook.getAuthors().equals("")){
                mAuthors.setText("-");
            }else {
                mAuthors.setText(currentBook.getAuthors());
            }

        TextView mPages = listItemView.findViewById(R.id.text_view_pages_count);
            if(currentBook.getPageCount() == 0){
                mPages.setText("-");
            }else {
                mPages.setText(String.valueOf(currentBook.getPageCount()));
            }

        RatingBar ratingBar = listItemView.findViewById(R.id.rating_bar_view);
            ratingBar.setRating(currentBook.getAverageRating());

        TextView mPrice = listItemView.findViewById(R.id.text_view_price);
            TextView mCurrency = listItemView.findViewById(R.id.text_view_currency_code);

        String currency = currentBook.getCurrencyCode();
        String price = formatPrice(currentBook.getListPrice());
            double listPrice = currentBook.getListPrice();
            if(listPrice == 0){
                mPrice.setText(R.string.free);
                mPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                mCurrency.setVisibility(View.GONE);
            }else {
                mPrice.setText(price);
                mCurrency.setText(String.valueOf(currency));
            }
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * This method get the price (data type double) and convert to string in the given pattern.
     * @param price is the price of the book
     * @return a string with the price formatted according to the DecimalFormat method pattern.
     */
    private String formatPrice(double price) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(price);
    }

}
