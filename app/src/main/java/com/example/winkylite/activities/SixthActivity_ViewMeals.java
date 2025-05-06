package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;
import com.example.winkylite.models.Meals;
import com.example.winkylite.models.mealItem;

import java.io.IOException;
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

        displayMeals();
    }

    private void displayMeals() throws DBHandler.DatabaseException {
        List<Meals> mealsList = dbHelper.getMealsForPet(currentPetID);

        if (mealsList != null && !mealsList.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);

            for (Meals meal : mealsList) {
                /*int mealID = cursor.getInt(cursor.getColumnIndexOrThrow("mealID"));
                String mealDate = cursor.getString(cursor.getColumnIndexOrThrow("wDate"));
                String mealTime = cursor.getString(cursor.getColumnIndexOrThrow("wTime"));
                String mealDescription = cursor.getString(cursor.getColumnIndexOrThrow("wDescription"));
                double totalKcal = cursor.getDouble(cursor.getColumnIndexOrThrow("totalKcal"));
                double totalMoisture = cursor.getDouble(cursor.getColumnIndexOrThrow("totalMoisture"));
                double totalProtein = cursor.getDouble(cursor.getColumnIndexOrThrow("totalProtein"));
                double totalFats = cursor.getDouble(cursor.getColumnIndexOrThrow("totalFats"));
                */

                View mealView = inflater.inflate(R.layout.meal_view_template, mealsLayout, false);

                TextView mealDateTV = mealView.findViewById(R.id.mealDate);
                mealDateTV.setText("Date: " + meal.getDate());

                TextView mealTimeTV = mealView.findViewById(R.id.mealTime);
                mealTimeTV.setText("Time: " + meal.getTime());

                TextView mealDescTV = mealView.findViewById(R.id.mealDescription);
                mealDescTV.setText("Description: " + (meal.getDescription() != null ? meal.getDescription() : ""));

                TextView totalKcalTV = mealView.findViewById(R.id.mealTotalKcal);
                totalKcalTV.setText(String.format("Kcal Total: %.2f", meal.getTotalKcal()));

                TextView totalMoistureTV = mealView.findViewById(R.id.mealTotalMoisture);
                totalMoistureTV.setText(String.format("Moisture Total: %.2f", meal.getTotalMoisture()));

                TextView totalProteinTV = mealView.findViewById(R.id.mealTotalProtein);
                totalProteinTV.setText(String.format("Protein Total: %.2f", meal.getTotalProtein()));

                TextView totalFatsTV = mealView.findViewById(R.id.mealTotalFats);
                totalFatsTV.setText(String.format("Fats Total: %.2f", meal.getTotalFats()));

                LinearLayout itemsLayout = mealView.findViewById(R.id.itemsLayout);
                List<mealItem> items = meal.getMealItems();

                if (items != null && !items.isEmpty()) {
                    for (mealItem item : items){
                        View itemView = inflater.inflate(R.layout.item_view_template, itemsLayout, false);

                        /*String itemType = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("itemType"));
                        double kcal = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("kcalCount"));
                        double moisture = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("moistureAmt"));
                        double protein = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("proteinAmt"));
                        double fats = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("fatsAmt"));
                        */


                        TextView itemNameTV = itemView.findViewById(R.id.mealItemName);
                        itemNameTV.setText(item.getType());

                        TextView itemKcalTV = itemView.findViewById(R.id.mealItemKcal);
                        itemKcalTV.setText(String.format("Kcal: %.2f", item.getKcal()));

                        TextView itemMoistureTV = itemView.findViewById(R.id.mealItemMoisture);
                        itemMoistureTV.setText(String.format("Moisture: %.2f", item.getMoisture()));

                        TextView itemProteinTV = itemView.findViewById(R.id.mealItemProtein);
                        itemProteinTV.setText(String.format("Protein: %.2f", item.getProtein()));

                        TextView itemFatsTV = itemView.findViewById(R.id.mealItemFat);
                        itemFatsTV.setText(String.format("Fats: %.2f", item.getFats()));

                        itemsLayout.addView(itemView);
                    }

                }

                mealsLayout.addView(mealView);

            }
        }
        dbHelper.close();
    }
    private int getItemCountForMeal(int mealID) {
        Cursor cursor = dbHelper.getMealItemsForMeal(mealID);
        int itemCount = 0;
        if (cursor != null) {
            itemCount = cursor.getCount();
            cursor.close();
        }

        return itemCount;
    }
}
