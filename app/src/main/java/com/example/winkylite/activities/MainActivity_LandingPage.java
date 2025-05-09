package com.example.winkylite.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import com.example.winkylite.database.DBHandler;
import com.example.winkylite.R;

public class MainActivity_LandingPage extends AppCompatActivity {
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button continueButton = findViewById(R.id.button);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_LandingPage.this, SecondActivity_HomePage.class);
            startActivity(intent);
        });

        dbHelper = new DBHandler(this);
        dbHelper.createDatabaseDirectory();
        dbHelper.openDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }


    private void insertTestUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("wUserID", 1);
        values.put("wUsername", "lilstink");
        values.put("wPassword", "testingpw");
        values.put("wTheme", 1);

       db.insertWithOnConflict("Users", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

}
