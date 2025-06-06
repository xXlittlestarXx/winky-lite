package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

public class SecondActivity_HomePage extends AppCompatActivity {
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        try {
            dbHandler = new DBHandler(this);
            dbHandler.initialize();

            Button addPetButton = findViewById(R.id.AddPetButton);
            addPetButton.setOnClickListener(v-> {
                Intent intent = new Intent(SecondActivity_HomePage.this, ThirdActivity_AddPetForm.class);
                startActivity(intent);
            });

            loadPetButtons();
        } catch (DBHandler.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            loadPetButtons();
        } catch (DBHandler.DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void loadPetButtons() throws DBHandler.DatabaseException {
        LinearLayout petButtonContainer = findViewById(R.id.petButtonContainer);
        petButtonContainer.removeAllViews();

        try (Cursor cursor = dbHandler.getAllPets()) {
            if (cursor != null && cursor.moveToFirst()) {
                int count = 0;
                do {
                    @SuppressLint("Range") String petName = cursor.getString(cursor.getColumnIndex("wPetName"));
                    Button petButton = new Button(this);
                    petButton.setText(petName);
                    petButton.setOnClickListener(v -> {
                        try {
                            int petID = dbHandler.getPetIdByName(petName);
                            Intent detailsIntent = new Intent(
                                    SecondActivity_HomePage.this,
                                    FourthActivity_PetDetails.class);
                            detailsIntent.putExtra("SELECTED_PET_NAME", petName);
                            detailsIntent.putExtra("SELECTED_PET_ID", petID);
                            startActivity(detailsIntent);
                        } catch (DBHandler.DatabaseException e) {
                            e.printStackTrace();
                        }
                    });
                    petButtonContainer.addView(petButton);
                    count++;
                } while (cursor.moveToNext() && count < 7);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}