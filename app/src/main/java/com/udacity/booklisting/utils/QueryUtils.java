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
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Book> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Ops! Problem making the HTTP request.", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
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
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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

    private static List<Book> extractFeatureFromJson(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {

            // Create a JSONObject from the JSON response string
            JSONObject rootJsonResponseObject = new JSONObject(bookJSON);
            JSONArray bookArray = rootJsonResponseObject.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book object in the bookArray (in within the list of books)
                JSONObject currentBook = bookArray.getJSONObject(i);

                String id = currentBook.getString("id");

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                int pages = 0;
                if (volumeInfo.has("pageCount")){
                    pages = volumeInfo.getInt("pageCount");
                }

                // Verifies if authors array exists inside volumeInfo JSONObject object
                JSONArray authorsArray = null;
                if (volumeInfo.has("authors")){
                      authorsArray = volumeInfo.getJSONArray("authors");
                 }

                String authors = "";
                if (volumeInfo.has("authors")) {
                    for (int index = 0; index < authorsArray.length(); index++){
                        authors += authorsArray.getString(index) + ", ";
                    }
                }

                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");

                double price = 0.0;
                String currencyCode = "";
                if (saleInfo.has("listPrice")){
                    JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                    price = listPrice.getDouble("amount");
                    currencyCode = listPrice.getString("currencyCode");
                }

                float rating = 0.0f;
                if (volumeInfo.has("averageRating")) {
                    rating  = volumeInfo.getInt("averageRating");
                }

                Book book = new Book(id, title, authors, pages, rating, price, currencyCode);
                books.add(book);
            }

            } catch (JSONException e) {
                Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        // Return the list of books
        return books;
    }

}
