package com.example.winkylite.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class TestInsert extends AppCompatActivity {

    private static final String TAG = "TestInsert";
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHandler(this);

        dbHelper.createDatabaseDirectory();
        dbHelper.openDatabase();

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("wUserID", 1);
            values.put("wUsername", "lilstink");
            values.put("wPassword", "testingpw");
            values.put("wTheme", 1);

            long result = db.insert("wUsers", null, values);

            if (result == -1) {
                Log.e(TAG, "Insert failed");
                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "Insert successful with ID: " + result);
                Toast.makeText(this, "User inserted successfully", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
