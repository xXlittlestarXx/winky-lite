package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

import java.io.IOException;


public class FourthActivity_PetDetails extends AppCompatActivity {

    private String petName;
    private int petID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        petName = getIntent().getStringExtra("SELECTED_PET_NAME");
        petID = getIntent().getIntExtra("SELECTED_PET_ID", -1);

        TextView titlePetName = findViewById(R.id.title);
        titlePetName.setText(petName);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        setupActionButtons();
    }

    private void setupActionButtons() {
        Button profileButton = findViewById(R.id.profileButton);
        Button addMealButton = findViewById(R.id.addMealButton);
        Button viewMealsButton = findViewById(R.id.viewMealsButton);
        Button viewChartsButton = findViewById(R.id.viewChartsButton);
        Button backButton = findViewById(R.id.backButton);

        profileButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, EigthActivity_PetProfile.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", petID);
            startActivity(intent);
        });

        addMealButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, FifthActivity_AddMeal.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", petID);
            startActivity(intent);
        });

        viewMealsButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SixthActivity_ViewMeals.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", petID);
            startActivity(intent);
        });

        viewChartsButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SeventhActivity_ViewCharts.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", petID);
            startActivity(intent);
        });

        backButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SecondActivity_HomePage.class);

            startActivity(intent);
        });
    }
}
