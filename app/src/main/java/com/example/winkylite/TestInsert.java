package com.example.winkylite;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class TestInsert extends AppCompatActivity {
    protected static DatabaseHelper dbHelper;

    public static void main(String[] args){
        dbHelper.openDatabase();

        String testUsername = "INSERT INTO Users (wUsername) VALUES ('testUser')";
        dbHelper.getWritableDatabase().execSQL(testUsername);

        String testPassword = "INSERT INTO Users (wPassword) VALUES ('testPW')";
        dbHelper.getWritableDatabase().execSQL(testPassword);

        String testTheme = "INSERT INTO Users (wTheme) VALUES ('1')";
        dbHelper.getWritableDatabase().execSQL(testTheme);
    }

}
