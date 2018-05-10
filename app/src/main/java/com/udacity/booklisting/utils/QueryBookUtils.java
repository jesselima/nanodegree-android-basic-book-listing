package com.udacity.booklisting.utils;

import android.text.TextUtils;
import android.util.Log;

import com.udacity.booklisting.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public final class QueryBookUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryBookUtils() {
    }

    public static Book fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Ops! Problem making the HTTP request.", e);
        }

        Book book = extractFeatureFromJson(jsonResponse);

        return book;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static Book extractFeatureFromJson(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        Book book = new Book();

        try {

            // Create a JSONObject from the JSON response string
            JSONObject currentBook = new JSONObject(bookJSON);

                String id = currentBook.getString("id");

            // GET BOOK TITLE
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

            // GET BOOK NUMBER OF PAGES
                int pages = 0;
                if (volumeInfo.has("pageCount")){
                    pages = volumeInfo.getInt("pageCount");
                }

                // GET BOOK AUTHORS INFO
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                String authors = "";
                for (int index = 0; index < authorsArray.length(); index++){
                    authors += authorsArray.getString(index) + ", ";
                }

                // GET BOOK PRICE
                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");
                double price = 0.0;
                String currencyCode = "";
                if (saleInfo.has("listPrice")){
                    JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                    price = listPrice.getDouble("amount");
                    currencyCode = listPrice.getString("currencyCode");
                }

                // GET BOOK BOOK AVERAGE RATING
                float rating = 0.0f;
                if (volumeInfo.has("averageRating")) {
                    rating  = volumeInfo.getInt("averageRating");
                }

                String subTitle = "";
                if (volumeInfo.has("subtitle")){
                    subTitle = volumeInfo.getString("subtitle");
                }

                String publisher = "";
                if (volumeInfo.has("publisher")){
                    publisher = volumeInfo.getString("publisher");
                }

                String publishedDate = "";
                if (volumeInfo.has("publishedDate")){
                    publishedDate = volumeInfo.getString("publishedDate");
                }

                String description = "";
                if (volumeInfo.has("description")){
                    description = volumeInfo.getString("description");
                    // Removes HTML tags from the description string.
                    description = description.replaceAll("<.*?>", "");
                }

                String buyLink = "";
                if (saleInfo.has("buyLink")){
                    buyLink = saleInfo.getString("buyLink");
                }

                int ratingsCount = 0;
                if (volumeInfo.has("ratingsCount")){
                    ratingsCount = volumeInfo.getInt("ratingsCount");
                }

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String mImageLink = "";
                if (volumeInfo.has("imageLinks")){
                    mImageLink = imageLinks.getString("medium");
                }

                // ADD ALL THE INFO TO A BOOK OBJECT
                book.setId(id);
                book.setTitle(title);
                book.setAuthors(authors);
                book.setPageCount(pages);
                book.setAverageRating(rating);
                book.setListPrice(price);
                book.setCurrencyCode(currencyCode);
                book.setmSubTitle(subTitle);
                book.setmPublisher(publisher);
                book.setmPublishedDate(publishedDate);
                book.setmDescription(description);
                book.setMbuyLink(buyLink);
                book.setmRatingsCount(ratingsCount);
                book.setmImageLink(mImageLink);

          } catch (JSONException e) {
            Log.e("QueryBookUtils", "Problem parsing the book JSON result", e);
        }
        // Return the list of books
        return book;
    }

}

