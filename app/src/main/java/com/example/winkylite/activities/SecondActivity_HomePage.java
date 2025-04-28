package com.example.winkylite.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

import java.io.IOException;


public class SecondActivity_HomePage extends AppCompatActivity {

    private Button addPetButton;
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        dbHelper = new DBHandler(this);
        dbHelper.openDatabase();


        addPetButton = findViewById(R.id.AddPetButton);
        addPetButton.setOnClickListener(v-> {
            Intent intent = new Intent(SecondActivity_HomePage.this, ThirdActivity_AddPetForm.class);

            startActivity(intent);


        });

        loadPetButtons();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPetButtons();
    }

    private void loadPetButtons() {
        LinearLayout petButtonContainer = findViewById(R.id.petButtonContainer);
        petButtonContainer.removeAllViews();

        Cursor cursor = dbHelper.getAllPets();

        if (cursor != null && cursor.moveToFirst()){
            int count = 0;
            do{
                @SuppressLint("Range") String petName = cursor.getString(cursor.getColumnIndex("wPetName"));
                Button petButton = new Button(this);
                petButton.setText(petName);
                petButton.setOnClickListener(v->{
                    int petID = dbHelper.getPetIdByName(petName);

                    Intent detailsIntent = new Intent(
                            SecondActivity_HomePage.this,
                            FourthActivity_PetDetails.class );
                    detailsIntent.putExtra("SELECTED_PET_NAME", petName);
                    detailsIntent.putExtra("SELECTED_PET_ID", petID);
                    startActivity(detailsIntent);
                });
                petButtonContainer.addView(petButton);
                count++;
            } while (cursor.moveToNext() && count < 5);
            cursor.close();
        }
    }

}
