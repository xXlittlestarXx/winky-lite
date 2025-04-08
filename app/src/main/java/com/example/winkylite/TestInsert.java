package com.example.winkylite;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TestInsert extends AppCompatActivity {
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper.openDatabase();

        super.onCreate(savedInstanceState);
        dbHelper = new DBHandler(this);

        String testQuery = "INSERT INTO Users (wUsername, wPassword, wTheme) VALUES ('testUser', 'testPW', '1')";
        dbHelper.getWritableDatabase().execSQL(testQuery);
    }
}
