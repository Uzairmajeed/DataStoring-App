package com.facebook.database_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private List<ImageData> imageDataList;
    private ImageAdapter imageAdapter;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_BIO = "bio";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private int columns;
    private int imageWidth;
    private int imageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(ViewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        columns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 6;
        setupRecyclerView(columns);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        imageDataList = new ArrayList<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        LinkedHashMap<String, String> sortedEntries = sortEntriesByKeys(allEntries);

        for (Map.Entry<String, String> entry : sortedEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(KEY_NAME)) {
                String name = entry.getValue();
                String imagePathKey = KEY_IMAGE_PATH + key.substring(KEY_NAME.length());
                String imagePath = sharedPreferences.getString(imagePathKey, "");

                String bioKey = KEY_BIO + key.substring(KEY_NAME.length());
                String bio = sharedPreferences.getString(bioKey, "");

                String phoneKey = KEY_PHONE + key.substring(KEY_NAME.length());
                String phone = sharedPreferences.getString(phoneKey, "");

                String addressKey = KEY_ADDRESS + key.substring(KEY_NAME.length());
                String address = sharedPreferences.getString(addressKey, "");

                imageDataList.add(new ImageData(name, imagePath, bio, phone, address));
            }
        }

        if (imageDataList.isEmpty()) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            imageAdapter = new ImageAdapter(this, imageDataList);
            recyclerView.setAdapter(imageAdapter);
            imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    ImageData imageData = imageAdapter.getImageData(position);
                    if (imageData != null) {
                        Intent intent = new Intent(ViewActivity.this, DetailsActivity.class);
                        intent.putExtra("name", imageData.getName());
                        intent.putExtra("imagePath", imageData.getImagePath());
                        intent.putExtra("bio", imageData.getBio());
                        intent.putExtra("phone", imageData.getPhone());
                        intent.putExtra("address", imageData.getAddress());
                        startActivity(intent);
                    } else {
                        Toast.makeText(ViewActivity.this, "Invalid item position", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setupRecyclerView(int columns) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int totalWidth = recyclerView.getWidth();
                int spacing = recyclerView.getPaddingStart() * (columns - 1);
                int availableWidth = totalWidth - spacing;
                imageWidth = availableWidth / columns;
                imageHeight = imageWidth; // Square images

                // Update the image dimensions in the ImageAdapter
                imageAdapter.setImageDimensions(imageWidth, imageHeight);
            }
        });
    }

    private LinkedHashMap<String, String> sortEntriesByKeys(Map<String, ?> allEntries) {
        LinkedHashMap<String, String> sortedEntries = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>(allEntries.keySet());
        keys.sort(String::compareTo);
        for (String key : keys) {
            String value = allEntries.get(key).toString();
            sortedEntries.put(key, value);
        }
        return sortedEntries;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        columns = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 6;
        setupRecyclerView(columns);
    }
}




