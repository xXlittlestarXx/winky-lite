package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;

import com.example.winkylite.R;
import com.example.winkylite.models.Meals;
//import com.example.winkylite.database.DBHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FifthActivity_AddMeal extends AppCompatActivity{
    private EditText etDate, etTime, etDescription, etKcal, etMoisture,
                        etFats, etProtein;
    private Button btnBack, btnSave;
    private Spinner itemDropDownBox;
    //private DBHandler dbhandler;
    private int currentPetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        currentPetID = getIntent().getIntExtra("SELECTED_PET_ID", -1);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etDescription = findViewById(R.id.etDescription);

        itemDropDownBox = findViewById(R.id.itemDropDownBox);

        etKcal = findViewById(R.id.etKcal);
        etMoisture = findViewById(R.id.etMoisture);
        etFats = findViewById(R.id.etFats);
        etProtein = findViewById(R.id.etProtein);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemDropDownBox.setAdapter(adapter);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FifthActivity_AddMeal.this, FourthActivity_PetDetails.class);

            startActivity(intent);
        });

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnSave.setOnClickListener(v -> {
            String itemType = itemDropDownBox.getSelectedItem().toString();

            double kcal = parseDouble(etKcal);
            double moisture = parseDouble(etMoisture);
            double fats = parseDouble(etFats);
            double protein = parseDouble(etProtein);

            double totalKcal = kcal;
            double totalMoisture = moisture;
            double totalProtein = protein;
            double totalFats = fats;

            Meals newMeal = new Meals(
                    currentPetID,
                    etDate.getText().toString(),
                    etTime.getText().toString(),
                    etDescription.getText().toString(),
                    itemType,
                    kcal,
                    moisture,
                    protein,
                    fats,
                    totalKcal,
                    totalMoisture,
                    totalProtein,
                    totalFats
            );

            newMeal.saveToDB(FifthActivity_AddMeal.this);

        });

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
}
