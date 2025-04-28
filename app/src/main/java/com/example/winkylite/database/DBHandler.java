package com.example.winkylite.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.winkylite.models.Pets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "myDBupdated.db";
    private static final int DB_VERSION = 3;
    private final Context context;
    private String DB_PATH;
    private SQLiteDatabase myDataBase;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();

        if (!dbExist) {
            File dbFile = new File(DB_PATH);
            dbFile.getParentFile().mkdirs();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else {
            int currentVersion = getDatabaseVersion();
            if (currentVersion < DB_VERSION) {
                updateDatabase();
            }
        }
    }

    private void updateDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        dbUpdater.update(db, getDatabaseVersion(),DB_VERSION);
    }

    private boolean checkDatabase () {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            // Database doesn't exist
        }

        if (checkDB != null) {
            checkDB.close();
            }
        return checkDB != null;
    }

    private void copyDatabase () throws IOException {
        InputStream input = context.getAssets().open(DB_NAME);
        OutputStream output = new FileOutputStream(DB_PATH);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
        updateDatabaseVersion(DB_VERSION);
    }
    private int getDatabaseVersion(){
        return DB_VERSION;
    }
    private void updateDatabaseVersion(int dbVersion) {
        SQLiteDatabase db = getWritableDatabase();
    }

    public void openDatabase () throws SQLException {
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null,
                        SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close () {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate (SQLiteDatabase db){

    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        oldVersion = 2;
        if (oldVersion < DB_VERSION) {
            dbUpdater.update(db, oldVersion, DB_VERSION);
        }
    }

    public Cursor queryData (String query){
        return myDataBase.rawQuery(query, null);
    }

    public void insertPet (Pets Pets){

        SQLiteDatabase db = SQLiteDatabase.openDatabase
                (DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);

                ContentValues values = new ContentValues();

                values.put("userID", 1);
                values.put("wPetName", Pets.getPetName());
                values.put("wPetType", Pets.getPetType());
                values.put("wPetAge", Pets.getPetAge());
                values.put("wPetAgeMY", Pets.getAgeUnit());
                values.put("wPetGender", Pets.getPetGender());
                values.put("wPetFixed", Pets.getIsFixed() ? "Yes" : "No");
                values.put("wPetActivityLvl", Pets.getPetActivity());
                values.put("wPetActivity", (double) Pets.getPetActivityLevel());
                values.put("wPetCurrentWeight", Pets.getPetCurrentWeight());
                values.put("wPetGoalWeight", Pets.getHasGoalWeight() ? Pets.getPetGoalWeight() : null);
                values.put("wPetKcalGoal", Pets.getRecKcal());
                values.put("wPetProteinGoal", Pets.getRecProtein());
                values.put("wPetFatsGoal", Pets.getRecFats());
                values.put("wPetMoistureGoal", 75);

                db.insert("Pets", null, values);

                db.close();

    }

    public Cursor getAllPets () {
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        String query = "SELECT wPetName FROM Pets WHERE userID = 1";
        return myDataBase.rawQuery(query, null);
    }
    public int getPetIdByName (String petName){
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        int petId = -1;
        String query = "SELECT wPetID FROM Pets WHERE wPetName = ?";
        Cursor cursor = myDataBase.rawQuery(query, new String[]{petName});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("wPetID");
                if (columnIndex != -1) {
                    petId = cursor.getInt(columnIndex);
                }
            }
            cursor.close();
        }
        return petId;
    }


    public long addMeal ( int petID, String date, String time, String description,
                          double avgKcal, double avgMoisture, double avgFats, double avgProtein){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);

        ContentValues values = new ContentValues();
        values.put("wPetID", petID);
        values.put("wMealDate", date);
        values.put("wMealTime", time);
        values.put("wMealDescription", description);
        values.put("wMealAvgKcal", avgKcal);
        values.put("wMealAvgMoisture", avgMoisture);
        values.put("wMealAvgFats", avgFats);
        values.put("wMealAvgProtein", avgProtein);

        long result = db.insert("Meals", null, values);

        db.close();

        return result;
    }
}
