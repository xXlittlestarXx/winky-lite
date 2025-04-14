package com.example.winkylite;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;


public class SecondActivity_HomePage extends AppCompatActivity {

    private Button addPetButton;
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        addPetButton = findViewById(R.id.AddPetButton);
        addPetButton.setOnClickListener(v-> {

        });
    }

}
