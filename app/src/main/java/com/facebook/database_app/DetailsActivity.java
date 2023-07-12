package com.facebook.database_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {
    private Toolbar toolbar1;
    private ImageButton backButton;

    private ImageView imageView;
    private TextView textViewName, textViewBio, textViewPhone, textViewAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = findViewById(R.id.imageView);
        textViewName = findViewById(R.id.textViewName);
        textViewBio = findViewById(R.id.textViewBio);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewAddress = findViewById(R.id.textViewAddress);
        backButton=findViewById(R.id.backButton);
        toolbar1= findViewById(R.id.toolbardetails);
        setSupportActionBar(toolbar1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(DetailsActivity.this, ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Retrieve the data passed from ViewActivity
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1); // Retrieve the position of the item
        String name = intent.getStringExtra("name");
        Bitmap imageBitmap = intent.getParcelableExtra("imageBitmap");
        String bio = intent.getStringExtra("bio");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");

// Set the image and details
        imageView.setImageBitmap(imageBitmap);
        textViewName.setText("Name: " + name);
        textViewBio.setText("Bio: " + bio);
        textViewPhone.setText("Phone No: " + phone);
        textViewAddress.setText("Address: " + address);

        // Retrieve the image path from the intent extras
        String imagePath = intent.getStringExtra("imagePath");
        if (imagePath != null && !imagePath.isEmpty()) {
            // Load the image from the specified path
            Bitmap imageBitmap1 = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(imageBitmap1);
        } else {
            imageView.setImageResource(R.drawable.image); // Set a default image if no image is found
        }
    }
}