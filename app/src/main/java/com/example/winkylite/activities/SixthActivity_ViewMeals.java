package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;


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
        currentPetID = getIntent().getIntExtra("petID", -1);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SixthActivity_ViewMeals.this, FourthActivity_PetDetails.class);

            startActivity(intent);
        });

        displayMeals();
    }

    private void displayMeals() {
        Cursor cursor = dbHelper.getMealsForPet(currentPetID);

        if (cursor != null && cursor.moveToFirst()) {
            LayoutInflater inflater = LayoutInflater.from(this);

            do {
                int mealID = cursor.getInt(cursor.getColumnIndexOrThrow("mealID"));
                String mealDate = cursor.getString(cursor.getColumnIndexOrThrow("wDate"));
                String mealTime = cursor.getString(cursor.getColumnIndexOrThrow("wTime"));
                String mealDescription = cursor.getString(cursor.getColumnIndexOrThrow("wDescription"));
                double totalKcal = cursor.getDouble(cursor.getColumnIndexOrThrow("totalKcal"));
                double totalMoisture = cursor.getDouble(cursor.getColumnIndexOrThrow("totalMoisture"));
                double totalProtein = cursor.getDouble(cursor.getColumnIndexOrThrow("totalProtein"));
                double totalFats = cursor.getDouble(cursor.getColumnIndexOrThrow("totalFats"));

                LinearLayout mealItemLayout = (LinearLayout) inflater.inflate(R.layout.meal_view_template, mealsLayout, false);

                TextView mealDateTV = mealItemLayout.findViewById(R.id.mealDate);
                mealDateTV.setText("Date: " + mealDate);

                TextView mealTimeTV = mealItemLayout.findViewById(R.id.mealTime);
                mealTimeTV.setText("Time: " + mealTime);

                TextView mealDescTV = mealItemLayout.findViewById(R.id.mealDescription);
                mealDescTV.setText("Description: " + (mealDescription != null ? mealDescription : ""));

                TextView totalKcalTV = mealItemLayout.findViewById(R.id.mealTotalKcal);
                totalKcalTV.setText(String.format("Kcal Total: %.2f", totalKcal));

                TextView totalMoistureTV = mealItemLayout.findViewById(R.id.mealTotalMoisture);
                totalMoistureTV.setText(String.format("Moisture Total: %.2f", totalMoisture));

                TextView totalProteinTV = mealItemLayout.findViewById(R.id.mealTotalProtein);
                totalProteinTV.setText(String.format("Protein Total: %.2f", totalProtein));

                TextView totalFatsTV = mealItemLayout.findViewById(R.id.mealTotalFats);
                totalFatsTV.setText(String.format("Fats Total: %.2f", totalFats));

                LinearLayout itemsLayout = mealItemLayout.findViewById(R.id.itemsLayout);
                Cursor itemsCursor = dbHelper.getMealItemsForMeal(mealID);

                if (itemsCursor != null && itemsCursor.moveToFirst()) {
                    do {
                        View itemView = inflater.inflate(R.layout.item_view_template, itemsLayout, false);

                        String itemType = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("itemType"));
                        double kcal = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("kcalCount"));
                        double moisture = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("moistureAmt"));
                        double protein = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("proteinAmt"));
                        double fats = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("fatsAmt"));

                        TextView itemNameTV = itemView.findViewById(R.id.mealItemName);
                        itemNameTV.setText(itemType);

                        TextView itemKcalTV = itemView.findViewById(R.id.mealItemKcal);
                        itemKcalTV.setText(String.format("Kcal: %.2f", kcal));

                        TextView itemMoistureTV = itemView.findViewById(R.id.mealItemMoisture);
                        itemMoistureTV.setText(String.format("Moisture: %.2f", moisture));

                        TextView itemProteinTV = itemView.findViewById(R.id.mealItemProtein);
                        itemProteinTV.setText(String.format("Protein: %.2f", protein));

                        TextView itemFatsTV = itemView.findViewById(R.id.mealItemFat);
                        itemFatsTV.setText(String.format("Fats: %.2f", fats));

                        itemsLayout.addView(itemView);
                    } while (itemsCursor.moveToNext());

                }

                mealsLayout.addView(mealItemLayout);

            } while (cursor.moveToNext());

            cursor.close();
        }
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
