package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;
import com.example.winkylite.models.Pets;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class EigthActivity_PetProfile extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView petNameTextView, petAgeTextView, petTypeTextView, petGenderTextView,
            petFixedTextView, petActivityTextView, petWeightTextView, petGoalWeightTextView,
            petKcalTextView, petProteinTextView, petFatsTextView, petMoistureTextView;

    private String currentPetName;
    private int currentPetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighth);

        try {
            dbHandler = new DBHandler(this);
            dbHandler.initialize();

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(this, "No pet information provided!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            currentPetID = extras.getInt("SELECTED_PET_ID");
            if (currentPetID == -1 || currentPetID == 0) {
                Toast.makeText(this, "Pet ID not provided!", Toast.LENGTH_LONG).show();
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
        } catch (DBHandler.DatabaseException e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
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

        Pets pet = dbHandler.getPetDetails(currentPetID);

        if (pet == null) {
            Toast.makeText(this, "Pet details not found in database!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        //rounded
        String roundKcal =df.format(pet.getRecKcal());
        String roundProtein = df.format(pet.getRecProtein());
        String roundFats = df.format(pet.getRecFats());

        petNameTextView.setText("Name: " + currentPetName);
        petAgeTextView.setText("Age: " + pet.getPetAge() + " " + pet.getAgeUnit());
        petTypeTextView.setText("Type: " + pet.getPetType());
        petGenderTextView.setText("Gender: " + pet.getPetGender());
        petFixedTextView.setText("Fixed: "  + (pet.getIsFixed() ? "Yes" : "No"));
        petActivityTextView.setText("Activity Level: "+ pet.getPetActivity());
        petWeightTextView.setText("Current Weight: " + pet.getPetCurrentWeight() + " kg");

        if (pet.getHasGoalWeight()) {
            petGoalWeightTextView.setText("Goal Weight: " + pet.getPetGoalWeight() + " kg");
        } else {
            petGoalWeightTextView.setText("Goal Weight Not Set.");
        }

        petKcalTextView.setText("Recommended Kilocalories: " + roundKcal + " kcal per day");
        petProteinTextView.setText("Recommended Protein: " + roundProtein + " % protein per day");
        petFatsTextView.setText("Recommended Fats: " + pet.getRecFats() + " % fats per day");
        petMoistureTextView.setText("Recommended Moisture: " + pet.getRecMoisture() + " % moisture per day");
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