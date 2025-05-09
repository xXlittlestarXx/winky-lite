package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.winkylite.R;
import com.example.winkylite.calculators.chartCalculator;
import com.example.winkylite.calculators.chartCalculator.DailyAverage;
import com.example.winkylite.database.DBHandler;
import com.example.winkylite.models.Meals;
import com.example.winkylite.models.Pets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeventhActivity_ViewCharts extends AppCompatActivity {
    private ImageView kcalChart, proteinChart, fatChart, moistureChart;
    private TextView kcalStatus, proteinStatus, fatStatus, moistureStatus;
    private int currentPetId;
    private String petName;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh);

        try {
            dbHandler = new DBHandler(this);
            dbHandler.initialize();
            Log.d("DB", "Database initialized in onCreate");
        } catch (DBHandler.DatabaseException e) {
            Log.e("DB", "Database initialization failed", e);
            Toast.makeText(this, "Database initialization failed. Please restart the app.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("SeventhActivity", "No extras found in intent");
            finish();
            return;
        }

        petName = getIntent().getStringExtra("SELECTED_PET_NAME");
        currentPetId = getIntent().getIntExtra("SELECTED_PET_ID", -1);

        if (currentPetId == -1) {
            Log.e("SeventhActivity", "Invalid Pet ID passed. Returning to main.");
            finish();
            return;
        }

        kcalChart = findViewById(R.id.kcalChart);
        kcalStatus = findViewById(R.id.kcalStatus);
        proteinChart = findViewById(R.id.proteinChart);
        proteinStatus = findViewById(R.id.proteinStatus);
        fatChart = findViewById(R.id.fatChart);
        fatStatus = findViewById(R.id.fatStatus);
        moistureChart = findViewById(R.id.moistureChart);
        moistureStatus = findViewById(R.id.moistureStatus);

        //DBHandler db = new DBHandler(this);
        try {
            loadCharts();
        } catch (DBHandler.DatabaseException e) {
            Log.e("SeventhActivity", "Error loading charts: " + e.getMessage());
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SeventhActivity_ViewCharts.this, FourthActivity_PetDetails.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", currentPetId);
            startActivity(intent);
        });
    }

    private void loadCharts() throws DBHandler.DatabaseException {
        List<Meals> meals = dbHandler.getMealsForPet(currentPetId);
        Pets pet = dbHandler.getPetDetails(currentPetId);

        if (pet == null) {
            Log.e("SeventhActivity", "Pet with ID " + currentPetId + " not found.");
            return;
        }

        Map<String, DailyAverage> averages = chartCalculator.calculateDailyAverages(meals);

        List<Double> kcalValues = getOrderedValues(averages, "kcal");
        String kcalChartURL = chartCalculator.generateChartURL(kcalValues, pet.getRecKcal(), "Daily Calories");
        loadChartImage(kcalChartURL, kcalChart);
        double latestKcal = kcalValues.get(kcalValues.size() - 1);
        kcalStatus.setText(chartCalculator.compareWithRecommendation(latestKcal, pet.getRecKcal()));

        List<Double> proteinValues = getOrderedValues(averages, "protein");
        String proteinChartURL = chartCalculator.generateChartURL(proteinValues, pet.getRecProtein(), "Daily Protein");
        loadChartImage(proteinChartURL, proteinChart);
        double latestProtein = proteinValues.get(proteinValues.size() - 1);
        proteinStatus.setText(chartCalculator.compareWithRecommendation(latestProtein, pet.getRecProtein()));

        List<Double> fatValues = getOrderedValues(averages, "fats");
        String fatChartURL = chartCalculator.generateChartURL(fatValues, pet.getRecFats(), "Daily Fats");
        loadChartImage(fatChartURL, fatChart);
        double latestFat = fatValues.get(fatValues.size() - 1);
        fatStatus.setText(chartCalculator.compareWithRecommendation(latestFat, pet.getRecFats()));

        List<Double> moistureValues = getOrderedValues(averages, "moisture");
        String moistureChartURL = chartCalculator.generateChartURL(moistureValues, pet.getRecMoisture(), "Daily Moisture");
        loadChartImage(moistureChartURL, moistureChart);
        double latestMoisture = moistureValues.get(moistureValues.size() - 1);
        moistureStatus.setText(chartCalculator.compareWithRecommendation(latestMoisture, pet.getRecMoisture()));
    }

    private List<Double> getOrderedValues(Map<String, DailyAverage> averages, String nutrient) {
        List<Double> values = new ArrayList<>();
        for (DailyAverage day : averages.values()) {
            switch (nutrient) {
                case "kcal":
                    values.add(day.averageKcal);
                    break;
                case "protein":
                    values.add(day.averageProtein);
                    break;
                case "fats":
                    values.add(day.averageFats);
                    break;
                case "moisture":
                    values.add(day.averageMoisture);
                    break;
            }
        }
        return values;
    }

    private void loadChartImage(String url, ImageView imageView) {
        Glide.with(this).load(url).into(imageView);
    }
}
