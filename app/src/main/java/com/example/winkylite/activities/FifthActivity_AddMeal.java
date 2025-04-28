package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.CheckBox;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

import java.io.IOException;


public class FifthActivity_AddMeal extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText etDate, etTime, etKcal, etMoisture, etFats, etProtein;
    private Spinner dropdown;
    private CheckBox addItemCheckBox;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        etDate = findViewById(R.id.editTextDate);
        etTime = findViewById(R.id.editTextTime);
        etKcal = findViewById(R.id.etKcal);
        etMoisture = findViewById(R.id.etMoisture);
        etFats = findViewById(R.id.etFats);
        etProtein = findViewById(R.id.etProtein);
        dropdown = findViewById(R.id.itemDropDownBox);
        addItemCheckBox = findViewById(R.id.addItemCheckBox);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBack = findViewById(R.id.backButton);

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnBack.setOnClickListener(v->{
            Intent intent = new Intent(FifthActivity_AddMeal.this, FourthActivity_PetDetails.class);

            startActivity(intent);
        });

        //Spinner dropdown = findViewById(R.id.itemDropDownBox);

        String[] items = new String[]{"kibble", "wet", "frozen", "topper", "treat",
                "supplement", "other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    private void showTimePicker() {
    }

    private void showDatePicker() {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
