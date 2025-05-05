package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

import java.io.IOException;


public class EigthActivity_PetProfile extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView petNameTextView, petAgeTextView, petTypeTextView, petGenderTextView,
            petFixedTextView, petActivityTextView, petWeightTextView, petGoalWeightTextView,
            petKcalTextView, petProteinTextView, petFatsTextView, petMoistureTextView;

    private String currentPetName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighth);

        dbHandler = new DBHandler(this);
        try {
            dbHandler.createDatabase();
            dbHandler.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPetName = getIntent().getStringExtra("SELECTED_PET_NAME");

        Log.d("EigthActivity", "Received pet name: " + currentPetName);

        if (currentPetName == null) {
            Toast.makeText(this, "Pet name not provided!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v->{
            Intent intent = new Intent(EigthActivity_PetProfile.this, FourthActivity_PetDetails.class);
            intent.putExtra("SELECTED_PET_NAME", currentPetName);
            startActivity(intent);
        });

        currentPetName = getIntent().getStringExtra("SELECTED_PET_NAME");
        int petId = dbHandler.getPetIdByName(currentPetName);

        petNameTextView = findViewById(R.id.nameTextView);
        petAgeTextView = findViewById(R.id.ageTextView);
        petTypeTextView = findViewById(R.id.typeTextView);
        petGenderTextView = findViewById(R.id.genderTextView);
        petFixedTextView = findViewById(R.id.fixedTextView);
        petActivityTextView = findViewById(R.id.activityTextView);
        petWeightTextView = findViewById(R.id.weightTextView);
        petGoalWeightTextView = findViewById(R.id.goalWeightTextView);
        petKcalTextView = findViewById(R.id.kcalGoalTextView);
        petProteinTextView = findViewById(R.id.proteinGoalTextView);
        petFatsTextView = findViewById(R.id.fatsGoalTextView);
        petMoistureTextView = findViewById(R.id.moistureGoalTextView);

        Cursor cursor = dbHandler.queryData("SELECT * FROM Pets WHERE wPetID = " + petId);
        if (cursor != null && cursor.moveToFirst()) {
            petNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetName")));
            petAgeTextView.setText(cursor.getInt(cursor.getColumnIndexOrThrow("wPetAge")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("wPetAgeMY")));
            petTypeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetType")));
            petGenderTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetGender")));
            petFixedTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetFixed")));
            petActivityTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetActivityLvl")));
            petWeightTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetCurrentWeight")) + " kg");

            double goalWeight = cursor.getDouble(cursor.getColumnIndexOrThrow("wPetGoalWeight"));
            if (cursor.isNull(cursor.getColumnIndexOrThrow("wPetGoalWeight"))) {
                petGoalWeightTextView.setText("Not Set");
            } else {
                petGoalWeightTextView.setText(goalWeight + " kg");
            }

            petKcalTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetKcalGoal")) + " kcal");
            petProteinTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetProteinGoal")) + " g protein");
            petFatsTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetFatsGoal")) + " g fats");
            petMoistureTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetMoistureGoal")) + " % moisture");

            cursor.close();
        }

    }
}
