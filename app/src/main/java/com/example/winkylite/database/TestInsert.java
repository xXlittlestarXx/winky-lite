package com.example.winkylite.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class TestInsert extends AppCompatActivity {
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // Must be called first
        dbHelper = new DBHandler(this);

        try {
            dbHelper.createDatabase();
            dbHelper.openDatabase();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("wUserID", 1);
            values.put("wUsername", "lilstink");
            values.put("wPassword", "testingpw");
            values.put("wTheme", 1);

            long result = db.insert("wUsers", null, values);

            if(result == -1) {
                throw new RuntimeException("Insert failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Database creation failed");
        }
    }
}