package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        backButton.setOnClickListener(v->{
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
                String mealDate = cursor.getString(cursor.getColumnIndexOrThrow("wDate"));
                String mealTime = cursor.getString(cursor.getColumnIndexOrThrow("wTime"));
                String mealDescription = cursor.getString(cursor.getColumnIndexOrThrow("wDescription"));
                int mealID = cursor.getInt(cursor.getColumnIndexOrThrow("mealID"));

                int itemCount = getItemCountForMeal(mealID);

                LinearLayout mealItemLayout = (LinearLayout) inflater.inflate(R.layout.meal_item_layout, mealsLayout, false);
                TextView mealDetailsText = mealItemLayout.findViewById(R.id.mealDetailsText);

                mealDetailsText.setText(String.format(
                        "Date: %s\nTime: %s\nDescription: %s\nNumber of Items: %d",
                        mealDate, mealTime, mealDescription, itemCount));

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
