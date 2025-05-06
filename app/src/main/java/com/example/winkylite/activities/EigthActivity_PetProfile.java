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
        dbHandler.createDatabaseDirectory();
        dbHandler.openDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "No pet information provided!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        currentPetName = extras.getString("SELECTED_PET_NAME");
        if (currentPetName == null || currentPetName.isEmpty()) {
            Toast.makeText(this, "Pet name not provided!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initializeViews();

        loadPetData();

        setUpActivityButtons();
    }

    private void setUpActivityButtons() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v->{
            Intent intent = new Intent(EigthActivity_PetProfile.this, FourthActivity_PetDetails.class);
            intent.putExtra("SELECTED_PET_NAME", currentPetName);
            startActivity(intent);
        });
    }

    private void loadPetData() throws DBHandler.DatabaseException {
        int petId = dbHandler.getPetIdByName(currentPetName);

        if (petId == -1) {
            Toast.makeText(this, "Pet not found in database!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Cursor cursor = null;
        try {
            cursor = dbHandler.queryData("SELECT * FROM Pets WHERE wPetID = " + petId);
            if (cursor != null && cursor.moveToFirst()) {
                petNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetName")));
                petAgeTextView.setText(cursor.getInt(cursor.getColumnIndexOrThrow("wPetAge")) + " " + cursor.getString(cursor.getColumnIndexOrThrow("wPetAgeMY")));
                petTypeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetType")));
                petGenderTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetGender")));
                petFixedTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetFixed")));
                petActivityTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("wPetActivityLvl")));
                petWeightTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetCurrentWeight")) + " kg");

                int goalWeightCol = cursor.getColumnIndexOrThrow("wPetGoalWeight");
                if (cursor.isNull(goalWeightCol)) {
                    petGoalWeightTextView.setText("Not Set");
                } else {
                    double goalWeight = cursor.getDouble(goalWeightCol);
                    petGoalWeightTextView.setText(goalWeight + " kg");
                }

                petKcalTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetKcalGoal")) + " kcal");
                petProteinTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetProteinGoal")) + " g protein");
                petFatsTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetFatsGoal")) + " g fats");
                petMoistureTextView.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetMoistureGoal")) + " % moisture");

                //cursor.close();
            }
        }finally {
            if (cursor != null) cursor.close();
        }
    }

    private void initializeViews() {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}
