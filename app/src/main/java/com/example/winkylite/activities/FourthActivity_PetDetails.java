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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        String petName = getIntent().getStringExtra("SELECTED_PET_NAME");

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

        });

        addMealButton.setOnClickListener(v->{

        });

        viewMealsButton.setOnClickListener(v->{

        });

        viewChartsButton.setOnClickListener(v->{

        });

        backButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SecondActivity_HomePage.class);

            startActivity(intent);
        });
    }
}
