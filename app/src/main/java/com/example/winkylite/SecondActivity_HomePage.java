package com.example.winkylite;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
            Intent intent = new Intent(SecondActivity_HomePage.this, ThirdActivity_AddPetForm.class);

            startActivity(intent);

            //overridePendingTransition(R.a);
        });
    }

}
