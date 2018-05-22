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

/**
 * This class offers Helper methods related to requesting and receiving a list of book data from Google Play Books.
 */
public final class QueryUtils {

    /** Tag for the log messages output */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google Books API data and return a list of {@link Book}s objects.
     * @param requestUrl is the URL request to the API.
     * @return a list of Books.
     */
    public static List<Book> fetchBookData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Ops! Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     * @param stringUrl is the String URl for the request
     * @return a URL object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * @param url is the given URL object
     * @return a json in a String data type
     * @throws IOException if there is a problem during the request throw a error at the log.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, do not make the request.
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

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from the server.
     * @param inputStream
     * @return a String with the JSON data inside it.
     * @throws IOException
     */
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

    /**
     * Return a list of {@link Book}s objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding Books.
        List<Book> books = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject rootJsonResponseObject = new JSONObject(bookJSON);
            // Create a JSONArray and put the array of Books (items) inside it.
            JSONArray bookArray = rootJsonResponseObject.getJSONArray("items");

            // For each position in the bookArray (JSONArray object)
            // extract the JSON data from such position in the array
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

                // Create a Book object the receives the JSON data as inputs parameters.
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
