package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.database.Cursor;

import com.example.winkylite.R;
import com.example.winkylite.database.DBHandler;
import com.example.winkylite.models.Meals;
import com.example.winkylite.models.mealItem;

import java.util.List;

public class SixthActivity_ViewMeals extends AppCompatActivity {

    private DBHandler dbHandler;
    private LinearLayout mealsLayout;
    private int currentPetID;
    private String currentPetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);

        try {
            dbHandler = new DBHandler(this);
            dbHandler.initialize();

            mealsLayout = findViewById(R.id.mealsLayout);
            currentPetID = getIntent().getIntExtra("SELECTED_PET_ID", -1);
            currentPetName = getIntent().getStringExtra("SELECTED_PET_NAME");

            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                Intent intent = new Intent(SixthActivity_ViewMeals.this, FourthActivity_PetDetails.class);
                intent.putExtra("SELECTED_PET_ID", currentPetID);
                intent.putExtra("SELECTED_PET_NAME", currentPetName);
                startActivity(intent);
            });

            displayMeals();
        } catch (DBHandler.DatabaseException e) {
            e.printStackTrace();
            TextView errorMsg = new TextView(this);
            errorMsg.setText("Error loading meals: " + e.getMessage());
            mealsLayout.addView(errorMsg);
        }
    }

    private void displayMeals() throws DBHandler.DatabaseException {
        mealsLayout.removeAllViews(); // Clear any existing views

        List<Meals> mealsList = dbHandler.getMealsForPet(currentPetID);

        if (mealsList == null || mealsList.isEmpty()) {
            TextView noMealsMsg = new TextView(this);
            noMealsMsg.setText("No meals found for this pet.");
            mealsLayout.addView(noMealsMsg);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Meals meal : mealsList) {
            View mealView = inflater.inflate(R.layout.meal_view_template, mealsLayout, false);

            ((TextView) mealView.findViewById(R.id.mealDate)).setText("Date: " + meal.getDate());
            ((TextView) mealView.findViewById(R.id.mealTime)).setText("Time: " + meal.getTime());
            ((TextView) mealView.findViewById(R.id.mealDescription)).setText("Description: " +
                    (meal.getDescription() != null ? meal.getDescription() : ""));
            ((TextView) mealView.findViewById(R.id.mealTotalKcal)).setText(
                    String.format("Kcal Total: %.2f", meal.getTotalKcal()));
            ((TextView) mealView.findViewById(R.id.mealTotalMoisture)).setText(
                    String.format("Moisture Total: %.2f", meal.getTotalMoisture()));
            ((TextView) mealView.findViewById(R.id.mealTotalProtein)).setText(
                    String.format("Protein Total: %.2f", meal.getTotalProtein()));
            ((TextView) mealView.findViewById(R.id.mealTotalFats)).setText(
                    String.format("Fats Total: %.2f", meal.getTotalFats()));

            LinearLayout itemsLayout = mealView.findViewById(R.id.itemsLayout);
            try (Cursor cursor = dbHandler.getMealItemsForMeal(meal.getMealID())) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        View itemView = inflater.inflate(R.layout.item_view_template, itemsLayout, false);

                        String type = cursor.getString(cursor.getColumnIndexOrThrow("itemType"));
                        double kcal = cursor.getDouble(cursor.getColumnIndexOrThrow("kcalCount"));
                        double moisture = cursor.getDouble(cursor.getColumnIndexOrThrow("moistureAmt"));
                        double protein = cursor.getDouble(cursor.getColumnIndexOrThrow("proteinAmt"));
                        double fats = cursor.getDouble(cursor.getColumnIndexOrThrow("fatsAmt"));

                        ((TextView) itemView.findViewById(R.id.mealItemName)).setText(type);
                        ((TextView) itemView.findViewById(R.id.mealItemKcal)).setText(
                                String.format("Kcal: %.2f", kcal));
                        ((TextView) itemView.findViewById(R.id.mealItemMoisture)).setText(
                                String.format("Moisture: %.2f", moisture));
                        ((TextView) itemView.findViewById(R.id.mealItemProtein)).setText(
                                String.format("Protein: %.2f", protein));
                        ((TextView) itemView.findViewById(R.id.mealItemFat)).setText(
                                String.format("Fats: %.2f", fats));

                        itemsLayout.addView(itemView);
                    } while (cursor.moveToNext());
                }
            }

            mealsLayout.addView(mealView);
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