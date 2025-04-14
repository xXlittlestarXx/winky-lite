package com.example.winkylite;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private DBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHandler(this);

        try {
            new Thread(() -> {
                try {
                    dbHelper.createDatabase();
                    dbHelper.openDatabase();

                    Cursor cursor = dbHelper.queryData("SELECT * FROM wUsers");

                } catch (IOException e) {
                    runOnUiThread(() -> {
                            Toast.makeText(this, "Database Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                    });

                    Log e ("Database", "Initilization error", e);
                }
            }).start();
        } catch (Exception e) {
            Toast.makeText(this, "Thread Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}