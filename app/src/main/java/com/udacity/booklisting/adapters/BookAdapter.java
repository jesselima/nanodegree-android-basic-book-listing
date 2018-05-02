package com.udacity.booklisting.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.udacity.booklisting.R;
import com.udacity.booklisting.models.Book;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

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
        double listPrice = currentBook.getListPrice();

        if(listPrice == 0){
//            LinearLayout linearLayout = listItemView.findViewById(R.id.book_item_list_layout);
//            linearLayout.setVisibility(View.GONE);
            mPrice.setText(R.string.not_for_sale);
            mPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            mCurrency.setVisibility(View.GONE);
        }else {
            mPrice.setText(String.valueOf(formatPrice(listPrice)));
            mCurrency.setText(String.valueOf(currency));
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    private String formatPrice(double price) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(price);
    }


}
