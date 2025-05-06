package com.example.winkylite.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.winkylite.R;

public class FourthActivity_PetDetails extends AppCompatActivity {

    private String petName;
    private int currentPetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        petName = getIntent().getStringExtra("SELECTED_PET_NAME");
        currentPetID = getIntent().getIntExtra("SELECTED_PET_ID", -1);

        TextView titlePetName = findViewById(R.id.title);
        titlePetName.setText(petName);

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
            intent.putExtra("SELECTED_PET_ID", currentPetID);
            startActivity(intent);
        });

        addMealButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, FifthActivity_AddMeal.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", currentPetID);
            startActivity(intent);
        });

        viewMealsButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SixthActivity_ViewMeals.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", currentPetID);
            startActivity(intent);
        });

        viewChartsButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SeventhActivity_ViewCharts.class);
            intent.putExtra("SELECTED_PET_NAME", petName);
            intent.putExtra("SELECTED_PET_ID", currentPetID);
            startActivity(intent);
        });

        backButton.setOnClickListener(v->{
            Intent intent = new Intent(FourthActivity_PetDetails.this, SecondActivity_HomePage.class);
            startActivity(intent);
        });
    }
}
