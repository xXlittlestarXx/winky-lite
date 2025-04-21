package com.example.winkylite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

public class ThirdActivity_AddPetForm extends AppCompatActivity {
    /* VARIABLES FROM activity_third*/

    private Button backButton, submitButton;
    private RadioButton ageMonths, ageYears, typeCat, typeDog,
            genderMale, genderFemale, fixedYes, fixedNo;

    private EditText petName, petAge, petCWeight, petGWeight;
    private CheckBox setGoalWeight;

    private SeekBar petActivityLevel;

    /* DATABASE HANDLER*/
    /* only db handler class can interact with database to limit
    overloading and overriding of data*/
    /*interacts with Pets class which interacts with db handler instead*/
    //private DBHandler dbHelper;

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
