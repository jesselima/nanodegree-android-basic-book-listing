package com.udacity.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Auto complete for the most used terms
//        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
//        String[] termsArray = getResources().getStringArray(R.array.list_terms);
//        List<String> dogList = Arrays.asList(termsArray);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layoutItemId, dogList);
//        AutoCompleteTextView autocompleteView = findViewById(R.id.autocompleteView);
//        autocompleteView.setAdapter(adapter);

    }
}
