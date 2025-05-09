package com.example.winkylite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;
import com.example.winkylite.models.Pets;
//import com.google.android.material.slider.Slider;

public class ThirdActivity_AddPetForm extends AppCompatActivity {
    /* VARIABLES FROM activity_third*/

    private RadioButton ageMonths;
private RadioButton typeDog;
private RadioButton genderMale;
 private RadioButton fixedYes;


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

        Button backButton = findViewById(R.id.backButton);
        Button submitButton = findViewById(R.id.submitButton);

        ageMonths = findViewById(R.id.months);
        RadioButton ageYears = findViewById(R.id.years);
        RadioButton typeCat = findViewById(R.id.cat);
        typeDog = findViewById(R.id.dog);
        genderMale = findViewById(R.id.male);
        RadioButton genderFemale = findViewById(R.id.female);
        fixedYes = findViewById(R.id.fixed);
        RadioButton fixedNo = findViewById(R.id.notFixed);

        petName = findViewById(R.id.petName);
        petAge = findViewById(R.id.petAge);
        petCWeight = findViewById(R.id.currentWeight);
        petGWeight = findViewById(R.id.goalWeight);
        setGoalWeight = findViewById(R.id.goalWeightCheckbox);
        petActivityLevel = findViewById(R.id.activitySlider);

        petGWeight.setVisibility(View.GONE);

        setGoalWeight.setOnCheckedChangeListener((buttonView, isChecked) ->{
            if(isChecked){
                petGWeight.setVisibility(View.VISIBLE);
            } else {
                petGWeight.setVisibility(View.GONE);
            }
        });

        backButton.setOnClickListener(v-> {
            Intent intent = new Intent(ThirdActivity_AddPetForm.this, SecondActivity_HomePage.class);

            startActivity(intent);

            //overridePendingTransition(R.a);
        });

        submitButton.setOnClickListener(v->{
                String name = petName.getText().toString();
                int age = Integer.parseInt(petAge.getText().toString());
                String ageUnit = ageMonths.isChecked() ? "months" : "years";
                String type = typeDog.isChecked() ? "Dog" : "Cat";
                String gender = genderMale.isChecked() ? "Male" : "Female";
                boolean isFixed = fixedYes.isChecked();

                double currentWeight = Double.parseDouble(petCWeight.getText().toString());

                boolean hasGoalWeight = setGoalWeight.isChecked();
                double goalWeight = hasGoalWeight ?
                        Double.parseDouble(petGWeight.getText().toString()) : 0.0;

                int activityLevel = petActivityLevel.getProgress();

                Pets newPet = new Pets(

                        name, ageUnit, gender, type,
                        isFixed, hasGoalWeight,
                        age, activityLevel,
                        currentWeight, goalWeight
                );
            newPet.processToCalculator();
            boolean insertSuccess = newPet.saveToDB(ThirdActivity_AddPetForm.this);

            if (insertSuccess) {
                Toast.makeText(ThirdActivity_AddPetForm.this, "Pet added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ThirdActivity_AddPetForm.this, SecondActivity_HomePage.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ThirdActivity_AddPetForm.this, "Failed to save pet to database.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
