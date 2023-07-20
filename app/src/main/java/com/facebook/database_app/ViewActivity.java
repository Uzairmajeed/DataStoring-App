package com.facebook.database_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {
    private ImageButton backButton;
    private RecyclerView recyclerViewMen;
    private RecyclerView recyclerViewWomen;
    private RecyclerView recyclerViewOthers;
    private ImageAdapter imageAdapterMen;
    private ImageAdapter imageAdapterWomen;
    private ImageAdapter imageAdapterOthers;
    private List<ImageData> imageDataListMen;
    private List<ImageData> imageDataListWomen;
    private List<ImageData> imageDataListOthers;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_BIO = "bio";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ADDRESS = "address";
    private static final int GRID_SPAN_COUNT_PORTRAIT = 3;
    private static final int GRID_SPAN_COUNT_LANDSCAPE = 6;

    private Spinner spinnerLayoutStyle;
    private int gridSpanCount = 3;

    private boolean isGridBased = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        backButton = findViewById(R.id.backButton);
        spinnerLayoutStyle = findViewById(R.id.spinnerLayoutStyle);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(ViewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerViewMen = findViewById(R.id.recyclerViewMen);
        recyclerViewWomen = findViewById(R.id.recyclerViewWomen);
        recyclerViewOthers = findViewById(R.id.recyclerViewOthers);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        imageDataListMen = new ArrayList<>();
        imageDataListWomen = new ArrayList<>();
        imageDataListOthers = new ArrayList<>();

        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(KEY_NAME)) {
                String name = sharedPreferences.getString(key, "");
                String imagePathKey = KEY_IMAGE_PATH + key.substring(KEY_NAME.length());
                String imagePath = sharedPreferences.getString(imagePathKey, "");
                String bioKey = KEY_BIO + key.substring(KEY_NAME.length());
                String bio = sharedPreferences.getString(bioKey, "");
                String phoneKey = KEY_PHONE + key.substring(KEY_NAME.length());
                String phone = sharedPreferences.getString(phoneKey, "");
                String addressKey = KEY_ADDRESS + key.substring(KEY_NAME.length());
                String address = sharedPreferences.getString(addressKey, "");
                String emailKey = KEY_EMAIL + key.substring(KEY_NAME.length());
                String email = sharedPreferences.getString(emailKey, "");
                String genderKey = KEY_GENDER + key.substring(KEY_NAME.length());
                String gender = sharedPreferences.getString(genderKey, "");

                ImageData imageData = new ImageData(name, imagePath, bio, phone, address, email, gender);

                if (gender.equalsIgnoreCase("male")) {
                    imageDataListMen.add(imageData);
                } else if (gender.equalsIgnoreCase("female")) {
                    imageDataListWomen.add(imageData);
                } else {
                    imageDataListOthers.add(imageData);
                }
            }
        }

        setupLayoutStyleSpinner();
        setLayoutStyle(isGridBased);

        if (imageDataListMen.isEmpty() && imageDataListWomen.isEmpty() && imageDataListOthers.isEmpty()) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }
    }

    public void navigateToDetailsActivity(ImageData imageData) {
        Intent intent = new Intent(ViewActivity.this, DetailsActivity.class);
        intent.putExtra("name", imageData.getName());
        intent.putExtra("imagePath", imageData.getImagePath() != null ? imageData.getImagePath() : "");
        intent.putExtra("bio", imageData.getBio());
        intent.putExtra("phone", imageData.getPhone());
        intent.putExtra("address", imageData.getAddress());
        intent.putExtra("email", imageData.getEmail());
        intent.putExtra("gender", imageData.getGender());
        startActivity(intent);
    }

    private void setupLayoutStyleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.layout_style_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLayoutStyle.setAdapter(adapter);
        // Retrieve the saved spinner position from SharedPreferences
        int spinnerPosition = sharedPreferences.getInt("spinner_position", spinnerLayoutStyle.getSelectedItemPosition());
        spinnerLayoutStyle.setSelection(spinnerPosition);
        spinnerLayoutStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals(getString(R.string.list_based))) {
                    setLayoutStyleForListBased();
                } else {
                    // Update the layout style based on the spinner selection
                    isGridBased = position == 1;
                    setGridSpanCount(); // Set the grid span count based on the isGridBased flag
                    setLayoutStyle(isGridBased);
                }
                // Save the selected position in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("spinner_position", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setLayoutStyleForListBased() {
        // Use ListBasedImageAdapter for the "List-based" option
        ListBasedImageAdapter listBasedImageAdapterMen = new ListBasedImageAdapter(this, imageDataListMen);
        ListBasedImageAdapter listBasedImageAdapterWomen = new ListBasedImageAdapter(this, imageDataListWomen);
        ListBasedImageAdapter listBasedImageAdapterOthers = new ListBasedImageAdapter(this, imageDataListOthers);

        // Set item click listener for the ListBasedImageAdapters
        listBasedImageAdapterMen.setOnItemClickListener(new ListBasedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageData imageData) {
                navigateToDetailsActivity(imageData);
            }
        });

        listBasedImageAdapterWomen.setOnItemClickListener(new ListBasedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageData imageData) {
                navigateToDetailsActivity(imageData);
            }
        });

        listBasedImageAdapterOthers.setOnItemClickListener(new ListBasedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageData imageData) {
                navigateToDetailsActivity(imageData);
            }
        });

        recyclerViewMen.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewWomen.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewOthers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerViewMen.setAdapter(listBasedImageAdapterMen);
        recyclerViewWomen.setAdapter(listBasedImageAdapterWomen);
        recyclerViewOthers.setAdapter(listBasedImageAdapterOthers);
    }
    public void setGridSpanCount() {
        int spanCount = isGridBased ? (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? GRID_SPAN_COUNT_PORTRAIT : GRID_SPAN_COUNT_LANDSCAPE) : 1;

        this.gridSpanCount = spanCount;
        imageAdapterMen.setGridSpanCount(spanCount);
        imageAdapterWomen.setGridSpanCount(spanCount);
        imageAdapterOthers.setGridSpanCount(spanCount);

        recyclerViewMen.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerViewWomen.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerViewOthers.setLayoutManager(new GridLayoutManager(this, spanCount));
    }
    private void setLayoutStyle(boolean isGridBased) {
        this.isGridBased = isGridBased;

        if (isGridBased) {
            setGridSpanCount();

            // Set GridLayoutManager for the RecyclerViews
            GridLayoutManager gridLayoutManagerMen = new GridLayoutManager(this, gridSpanCount);
            GridLayoutManager gridLayoutManagerWomen = new GridLayoutManager(this, gridSpanCount);
            GridLayoutManager gridLayoutManagerOthers = new GridLayoutManager(this, gridSpanCount);

            recyclerViewMen.setLayoutManager(gridLayoutManagerMen);
            recyclerViewWomen.setLayoutManager(gridLayoutManagerWomen);
            recyclerViewOthers.setLayoutManager(gridLayoutManagerOthers);

            // Use the ImageAdapter for Grid-based layout
            if (imageAdapterMen == null) {
                imageAdapterMen = new ImageAdapter(this, imageDataListMen, isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            } else {
                imageAdapterMen.setGridBased(isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            }

            if (imageAdapterWomen == null) {
                imageAdapterWomen = new ImageAdapter(this, imageDataListWomen, isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            } else {
                imageAdapterWomen.setGridBased(isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            }

            if (imageAdapterOthers == null) {
                imageAdapterOthers = new ImageAdapter(this, imageDataListOthers, isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            } else {
                imageAdapterOthers.setGridBased(isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            }
        } else {
            // Set LinearLayoutManager for the RecyclerViews
            LinearLayoutManager layoutManagerMen = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManagerWomen = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManagerOthers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            recyclerViewMen.setLayoutManager(layoutManagerMen);
            recyclerViewWomen.setLayoutManager(layoutManagerWomen);
            recyclerViewOthers.setLayoutManager(layoutManagerOthers);

            // Use the ImageAdapter for List-based layout
            if (imageAdapterMen == null) {
                imageAdapterMen = new ImageAdapter(this, imageDataListMen, isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            } else {
                imageAdapterMen.setGridBased(isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            }

            if (imageAdapterWomen == null) {
                imageAdapterWomen = new ImageAdapter(this, imageDataListWomen, isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            } else {
                imageAdapterWomen.setGridBased(isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            }

            if (imageAdapterOthers == null) {
                imageAdapterOthers = new ImageAdapter(this, imageDataListOthers, isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            } else {
                imageAdapterOthers.setGridBased(isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            }
        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isGridBased) {
            int spanCount = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
                    ? GRID_SPAN_COUNT_PORTRAIT
                    : GRID_SPAN_COUNT_LANDSCAPE;
            setGridSpanCount();
        }
    }
}