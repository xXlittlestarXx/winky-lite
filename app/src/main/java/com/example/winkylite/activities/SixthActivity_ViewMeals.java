package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.winkylite.R;
import com.example.winkylite.database.DBHandler;
import com.example.winkylite.models.Meals;
import com.example.winkylite.models.mealItem;

import java.util.List;

public class SixthActivity_ViewMeals extends AppCompatActivity {

    private DBHandler dbHelper;
    private LinearLayout mealsLayout;
    private int currentPetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);

        dbHelper = new DBHandler(this);
        mealsLayout = findViewById(R.id.mealsLayout);
        currentPetID = getIntent().getIntExtra("SELECTED_PET_ID", -1);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SixthActivity_ViewMeals.this, FourthActivity_PetDetails.class);
            intent.putExtra("SELECTED_PET_ID", currentPetID);
            startActivity(intent);
        });

        try {
            displayMeals();
        } catch (DBHandler.DatabaseException e) {
            e.printStackTrace();
            TextView errorMsg = new TextView(this);
            errorMsg.setText("Error loading meals.");
            mealsLayout.addView(errorMsg);
        }
    }

    private void displayMeals() throws DBHandler.DatabaseException {
        List<Meals> mealsList = dbHelper.getMealsForPet(currentPetID);

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
            ((TextView) mealView.findViewById(R.id.mealDescription)).setText("Description: " + (meal.getDescription() != null ? meal.getDescription() : ""));
            ((TextView) mealView.findViewById(R.id.mealTotalKcal)).setText(String.format("Kcal Total: %.2f", meal.getTotalKcal()));
            ((TextView) mealView.findViewById(R.id.mealTotalMoisture)).setText(String.format("Moisture Total: %.2f", meal.getTotalMoisture()));
            ((TextView) mealView.findViewById(R.id.mealTotalProtein)).setText(String.format("Protein Total: %.2f", meal.getTotalProtein()));
            ((TextView) mealView.findViewById(R.id.mealTotalFats)).setText(String.format("Fats Total: %.2f", meal.getTotalFats()));

            LinearLayout itemsLayout = mealView.findViewById(R.id.itemsLayout);
            List<mealItem> items = meal.getMealItems();

            if (items != null && !items.isEmpty()) {
                for (mealItem item : items) {
                    View itemView = inflater.inflate(R.layout.item_view_template, itemsLayout, false);

                    ((TextView) itemView.findViewById(R.id.mealItemName)).setText(item.getType());
                    ((TextView) itemView.findViewById(R.id.mealItemKcal)).setText(String.format("Kcal: %.2f", item.getKcal()));
                    ((TextView) itemView.findViewById(R.id.mealItemMoisture)).setText(String.format("Moisture: %.2f", item.getMoisture()));
                    ((TextView) itemView.findViewById(R.id.mealItemProtein)).setText(String.format("Protein: %.2f", item.getProtein()));
                    ((TextView) itemView.findViewById(R.id.mealItemFat)).setText(String.format("Fats: %.2f", item.getFats()));

                    itemsLayout.addView(itemView);
                }
            }

            mealsLayout.addView(mealView);
        }

        dbHelper.close();
    }
}
