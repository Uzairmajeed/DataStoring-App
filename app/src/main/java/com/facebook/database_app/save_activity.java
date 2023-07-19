package com.facebook.database_app;


import static com.facebook.database_app.ImageUtils.bitmapToBase64;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class save_activity extends AppCompatActivity {
    private Toolbar toolbar1;
    private ImageButton backButton;
    private EditText editname, editphone, editbio, editaddress,editTextEmail;
    private Button buttonsave, buttonupload;
    private Spinner spinnerGender;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";

    private static final String KEY_IMAGE_PATH = "image_path";
    private static final int REQUEST_IMAGE_GALLERY = 1000;

    private RecyclerView recyclerViewMen;
    private RecyclerView recyclerViewWomen;
    private RecyclerView recyclerViewOthers;
    private ImageAdapter imageAdapterMen;
    private ImageAdapter imageAdapterWomen;
    private ImageAdapter imageAdapterOthers;
    private List<ImageData> imageDataListMen;
    private List<ImageData> imageDataListWomen;
    private List<ImageData> imageDataListOthers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerGender = findViewById(R.id.spinnerGender);
        editname = findViewById(R.id.editTextName);
        editphone = findViewById(R.id.editTextPhone);
        editbio = findViewById(R.id.editTextBio);
        editaddress = findViewById(R.id.editTextAddress);
        buttonsave = findViewById(R.id.buttonSaves);
        buttonupload = findViewById(R.id.buttonUploadImage);
        imageView = findViewById(R.id.imageView);
        // Set the adapter for the gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);


        editphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber = s.toString();
                if (phoneNumber.length() > 10) {
                    editphone.setError("Phone number cannot exceed 10 digits");
                } else if (!isValidPhoneNumber(phoneNumber)) {
                    editphone.setError("Invalid phone number");
                    buttonsave.setEnabled(false);
                } else {
                    editphone.setError(null);
                }
            }
        });
        buttonsave.setEnabled(false);

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.matches(".*\\d.*") && input.matches(".*[a-zA-Z].*") && input.matches(".*[@].*")) {
                    editTextEmail.setError(null);
                    buttonsave.setEnabled(true);
                } else {
                    editTextEmail.setError("Please enter a combination of alphanumeric characters with at least one '@' symbol");
                }
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        backButton=findViewById(R.id.backButton);
        toolbar1= findViewById(R.id.toolbarsave);
        setSupportActionBar(toolbar1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(save_activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Load saved values from SharedPreferences
        editname.setText(sharedPreferences.getString(KEY_NAME, ""));
        editbio.setText(sharedPreferences.getString(KEY_BIO, ""));
        editphone.setText(sharedPreferences.getString(KEY_PHONE, ""));
        editaddress.setText(sharedPreferences.getString(KEY_ADDRESS, ""));
        // Load saved image from SharedPreferences
        String imageBase64 = sharedPreferences.getString(KEY_IMAGE_PATH, "");
        if (!imageBase64.isEmpty()) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageBase64);
            imageView.setImageBitmap(imageBitmap);
        }

        // Inside the buttonsave OnClickListener in save_activity class
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText fields
                String name = editname.getText().toString();
                String bio = editbio.getText().toString();
                String phone = editphone.getText().toString();
                String address = editaddress.getText().toString();
                String email = editTextEmail.getText().toString();
                String gender = spinnerGender.getSelectedItem().toString();

                // Generate a unique identifier for the image file
                String imageFileName = UUID.randomUUID().toString() + ".png";

                // Check if an image is selected
                Bitmap imageBitmap = null;
                if (imageView.getDrawable() != null) {
                    imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                }

                // Save the image to a file if imageBitmap is not null
                if (imageBitmap != null) {
                    saveImageToFile(imageBitmap, imageFileName);
                }

                // Create an instance of ImageData with the details
                String imagePath = imageBitmap != null ? getFilesDir() + "/" + imageFileName : "";
                ImageData imageData = new ImageData(name, imagePath, bio, phone, address, email, gender);

                // Generate a unique identifier for the data entry
                String uniqueId = UUID.randomUUID().toString();

                // Save the ImageData object to SharedPreferences using the unique key
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME + "_" + uniqueId, imageData.getName());
                editor.putString(KEY_IMAGE_PATH + "_" + uniqueId, imageData.getImagePath());
                editor.putString(KEY_BIO + "_" + uniqueId, imageData.getBio());
                editor.putString(KEY_PHONE + "_" + uniqueId, imageData.getPhone());
                editor.putString(KEY_ADDRESS + "_" + uniqueId, imageData.getAddress());
                editor.putString(KEY_EMAIL + "_" + uniqueId, imageData.getEmail());
                editor.putString(KEY_GENDER + "_" + uniqueId, imageData.getGender());
                editor.apply();

                // Navigate back to MainActivity
                Intent intent = new Intent(save_activity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity to prevent going back to it with the back button
            }
        });




        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch image picker options
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, REQUEST_IMAGE_GALLERY);
            }
        });
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]+") && phoneNumber.length() == 10;
    }



    // Helper method to save the image bitmap to a file
    private void saveImageToFile(Bitmap bitmap, String fileName) {
        File file = new File(getFilesDir(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}