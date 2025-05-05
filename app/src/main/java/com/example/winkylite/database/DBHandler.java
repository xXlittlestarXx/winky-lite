package com.example.winkylite.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.winkylite.models.Meals;
import com.example.winkylite.models.Pets;
import com.example.winkylite.models.mealItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "WinkyDB_Version5.db";
    private static final int DB_VERSION = 4;
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
            copyDatabase();
        }
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
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor queryData (String query){
        return myDataBase.rawQuery(query, null);
    }

    public boolean insertPet (Pets Pets){

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

                if (Pets.getHasGoalWeight()) {
                    values.put("wPetGoalWeight", Pets.getPetGoalWeight());
                } else {
                    values.putNull("wPetGoalWeight");
                }

                values.put("wPetKcalGoal", Pets.getRecKcal());
                values.put("wPetProteinGoal", Pets.getRecProtein());
                values.put("wPetFatsGoal", Pets.getRecFats());
                values.put("wPetMoistureGoal", 75);

        long result = db.insert("Pets", null, values);
        return result != -1;
                //db.close();

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

        int wPetId = -1;
        String query = "SELECT wPetID FROM Pets WHERE wPetName = ?";
        Cursor cursor = myDataBase.rawQuery(query, new String[]{petName});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("wPetID");
                if (columnIndex != -1) {
                    wPetId = cursor.getInt(columnIndex);
                }
            }
            cursor.close();
        }
        return wPetId;
    }


    public long insertMeal(Meals Meals){

        String wPetFixed = "bug";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("petID", Meals.getPetID());
        values.put("wDate", Meals.getDate());
        values.put("wTime", Meals.getTime());
        values.put("wDescription", Meals.getDescription());
        values.put("totalKcal", Meals.getTotalKcal());
        values.put("totalMoisture", Meals.getTotalMoisture());
        values.put("totalProtein", Meals.getTotalProtein());
        values.put("totalFats", Meals.getTotalFats());
        values.put("wPetFixed", wPetFixed);

        long mealID = db.insert("Meals", null, values);
        return mealID;
        //db.close();

    }

    public boolean insertMealItem(int mealID, mealItem item) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();

        values.put("mealID", mealID);
        values.put("itemType", item.getType());
        values.put("kcalCount", item.getKcal());
        values.put("moistureAmt", item.getMoisture());
        values.put("proteinAmt", item.getProtein());
        values.put("fatsAmt", item.getFats());

        long result = db.insert("MealItems", null, values);
        return result != -1;
    }
}
