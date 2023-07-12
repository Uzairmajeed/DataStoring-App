package com.facebook.database_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button buttonView,buttonSave,buttonDelete;
    private Toolbar toolbar;
    private ImageButton closeButton;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        closeButton = findViewById(R.id.closeButton);
        setSupportActionBar(toolbar);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });

        buttonView=findViewById(R.id.button1);
        buttonSave=findViewById(R.id.button2);
        buttonDelete=findViewById(R.id.button3);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Start the new activity here
                    Intent intent = new Intent(MainActivity.this, save_activity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace(); // Print the stack trace to the console

                    // Display the stack trace in a toast message
                    Toast.makeText(MainActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewActivity
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(intent);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all data from SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Notify the user that the data has been deleted
                Toast.makeText(MainActivity.this, "All data deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}