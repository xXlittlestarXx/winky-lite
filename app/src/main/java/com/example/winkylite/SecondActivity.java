package com.example.winkylite;


import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class SecondActivity extends AppCompatActivity {

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
