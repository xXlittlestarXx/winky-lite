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


public class SeventhActivity_ViewCharts extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v->{
            Intent intent = new Intent(SeventhActivity_ViewCharts.this, FourthActivity_PetDetails.class);

            startActivity(intent);
        });
    }
}
