package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.winkylite.R;
import com.example.winkylite.calculators.mealCalculator;
import com.example.winkylite.database.DBHandler;
import com.example.winkylite.models.Meals;
import com.example.winkylite.models.mealItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FifthActivity_AddMeal extends AppCompatActivity{

    private LinearLayout itemContainer;
    private EditText etDate, etTime, etDescription;
    private Button btnBack, btnSave, btnAddItem;
    private LayoutInflater inflater;
    private int itemCount = 0;
    private final int MAX_ITEMS = 5;
    private int currentPetID, currentMealId;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        try {
            dbHandler = new DBHandler(this);
            dbHandler.initialize();
            Log.d("DB", "Database initialized in onCreate");
        } catch (DBHandler.DatabaseException e) {
            Log.e("DB", "Database initialization failed", e);
            Toast.makeText(this, "Database initialization failed. Please restart the app.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        currentPetID = getIntent().getIntExtra("SELECTED_PET_ID", -1);
        currentMealId = getIntent().getIntExtra("SELECTED_MEAL_ID", -1);

        itemContainer = findViewById(R.id.itemContainer);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etDescription = findViewById(R.id.etDescription);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnAddItem = findViewById(R.id.btnAddItem);

        inflater = LayoutInflater.from(this);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FifthActivity_AddMeal.this, FourthActivity_PetDetails.class);
            startActivity(intent);
        });

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnAddItem.setOnClickListener(v -> {
            if (itemCount >= MAX_ITEMS) {
                Toast.makeText(this, "Maximum 5 items allowed", Toast.LENGTH_SHORT).show();
                return;
            }
            View itemView = inflater.inflate(R.layout.meal_item_template, itemContainer, false);
            setupSpinner(itemView);
            itemContainer.addView(itemView);
            itemCount++;
        });

        btnSave.setOnClickListener(v -> {

            if (itemContainer.getChildCount() == 0) {
                Toast.makeText(this, "Add at least one item", Toast.LENGTH_SHORT).show();
                return;
            }

            List<mealItem> mealItems = new ArrayList<>();
            List<String> foodTypes = new ArrayList<>();

            for (int i = 0; i < itemContainer.getChildCount(); i++) {
                View itemView = itemContainer.getChildAt(i);
                Spinner spinner = itemView.findViewById(R.id.itemDropDownBox);
                EditText etKcal = itemView.findViewById(R.id.etKcal);
                EditText etMoisture = itemView.findViewById(R.id.etMoisture);
                EditText etFats = itemView.findViewById(R.id.etFats);
                EditText etProtein = itemView.findViewById(R.id.etProtein);

                String type = spinner.getSelectedItem().toString();
                double kcal = parseDouble(etKcal);
                double moisture = parseDouble(etMoisture);
                double fats = parseDouble(etFats);
                double protein = parseDouble(etProtein);

                mealItems.add(new mealItem(type, kcal, moisture, fats, protein));
            }

            double[] averages = mealCalculator.calculateMeal(mealItems);

            Meals meal = new Meals(
                    currentMealId,
                    currentPetID,
                    etDate.getText().toString(),
                    etTime.getText().toString(),
                    etDescription.getText().toString(),
                    mealItems
            );

            boolean success;
            try {
                success = meal.saveToDB(this);
            } catch (DBHandler.DatabaseException e) {
                throw new RuntimeException(e);
            }

            if (success) {
                Toast.makeText(this, "Added to database successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error adding to database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner(View itemView) {
        Spinner spinner = itemView.findViewById(R.id.itemDropDownBox);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                    etTime.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    etDate.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private double parseDouble(EditText et) {
        try {
            return Double.parseDouble(et.getText().toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            try {
                dbHandler.close();
            } catch (Exception e) {
                Log.e("DB", "Error closing database", e);
            }
        }
    }
}
