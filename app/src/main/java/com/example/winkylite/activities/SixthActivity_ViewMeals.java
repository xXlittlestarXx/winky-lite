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


public class SixthActivity_ViewMeals extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v->{
            Intent intent = new Intent(SixthActivity_ViewMeals.this, SecondActivity_HomePage.class);

            startActivity(intent);
        });
    }
}
