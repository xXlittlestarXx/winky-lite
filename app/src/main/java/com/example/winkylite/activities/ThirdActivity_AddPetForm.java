package com.example.winkylite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

public class ThirdActivity_AddPetForm extends AppCompatActivity {
    private Button backButton;
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v-> {
            Intent intent = new Intent(ThirdActivity_AddPetForm.this, SecondActivity_HomePage.class);

            startActivity(intent);

            //overridePendingTransition(R.a);
        });
    }
}
