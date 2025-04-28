package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.winkylite.R;
import com.example.winkylite.database.DBHandler;
import com.example.winkylite.calculators.mealCalculator;
import com.example.winkylite.models.mealItem;

import java.util.ArrayList;
import java.util.List;

public class FifthActivity_AddMeal extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private LinearLayout nutritionInputsContainer;
    private EditText etDate, etTime, etDescription;
    private Button btnBack, btnAddItem, btnSave;
    private DBHandler dbhandler;
    private List<mealItem> mealItemList = new ArrayList<>();
    private int currentPetID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        initializeViews();
        setupUI();
    }
    private void initializeViews(){
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etDescription = findViewById(R.id.etDescription);
        nutritionInputsContainer = findViewById((R.id.nutritionInputsContainer));

        btnBack = findViewById(R.id.btnBack);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnSave = findViewById(R.id.btnSave);

        dbhandler = new DBHandler(this);
    }

    private void setupUI() {

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FifthActivity_AddMeal.this, FourthActivity_PetDetails.class);

            startActivity(intent);
        });

        btnAddItem.setOnClickListener(v -> addItemGroup());
        btnSave.setOnClickListener(v -> saveMeal());
    }
    private void addItemGroup(){
        View itemView = getLayoutInflater().inflate(
                R.layout.meal_item_template, null);

        Spinner spinner = itemView.findViewById(R.id.itemDropDownBox);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.item_types,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        nutritionInputsContainer.addView(itemView);
    }
    private void saveMeal() {
        mealItemList.clear();

        for (int i = 0; i < nutritionInputsContainer.getChildCount(); i++){
            View itemView = nutritionInputsContainer.getChildAt(i);

            EditText etKcal = itemView.findViewById(R.id.etKcal);
            EditText etMoisture = itemView.findViewById(R.id.etMoisture);
            EditText etFats = itemView.findViewById(R.id.etFats);
            EditText etProtein = itemView.findViewById(R.id.etProtein);

            double kcal = parseDouble(etKcal);
            double moisture = parseDouble(etMoisture);
            double fats = parseDouble(etFats);
            double protein = parseDouble(etProtein);

            mealItem item = new mealItem(kcal, moisture, fats, protein);
            mealItemList.add(item);
        }

        double[] totals = mealCalculator.calculateMeal(mealItemList);

        dbhandler.addMeal(
                currentPetID,
                etDate.getText().toString(),
                etTime.getText().toString(),
                etDescription.getText().toString(),
                totals[0], // kcal
                totals[1], // moisture
                totals[2], // fats
                totals[3]  // protein
        );
    }

    private double parseDouble(EditText et) {
        try {
            return Double.parseDouble(et.getText().toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
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
