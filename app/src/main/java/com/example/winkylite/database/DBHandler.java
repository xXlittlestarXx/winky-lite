package com.example.winkylite.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.winkylite.models.Meals;
import com.example.winkylite.models.Pets;
import com.example.winkylite.models.mealItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
        myDataBase = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase myDataBase) {
        Log.d("DBHandler", "Pre-populated database — onCreate not used.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDataBase, int oldVersion, int newVersion) {
        Log.d("DBHandler", "Pre-populated database — onUpgrade not used.");
    }

    public Cursor queryData (String query){
        return myDataBase.rawQuery(query, null);
    }

    public boolean insertPet (Pets Pets){

                if (myDataBase == null || !myDataBase.isOpen()){
                    openDatabase();
                }

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

        long result = myDataBase.insert("Pets", null, values);
        return result != -1;

    }

    public Cursor getAllPets () {
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        String query = "SELECT wPetName FROM Pets WHERE userID = 1";
        return myDataBase.rawQuery(query, null);
    }
    public int getPetIdByName (String petName){
        if (petName == null || petName.isEmpty()) {
            Log.e("DBHandler", "Pet name is null or empty!");
            return -1;
        }
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        int wPetId = -1;
        String query = "SELECT wPetID FROM Pets WHERE wPetName = ?";
        Cursor cursor = null;
        try{
                cursor = myDataBase.rawQuery(query, new String[]{petName});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("wPetID");
                if (columnIndex != -1) {
                    wPetId = cursor.getInt(columnIndex);
                }
            }
        }
        }finally{
                if (cursor != null) cursor.close();
        }

        return wPetId;
    }


    public long insertMeal(Meals Meals){
        if (myDataBase == null || !myDataBase.isOpen()){
            openDatabase();
        }
        String wPetFixed = "bug";

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

        long wMealID = myDataBase.insert("Meals", null, values);
        return wMealID;
    }

    public boolean insertMealItem(int wMealID, mealItem item) {
        //SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("mealID", wMealID);
        values.put("itemType", item.getType());
        values.put("kcalCount", item.getKcal());
        values.put("moistureAmt", item.getMoisture());
        values.put("proteinAmt", item.getProtein());
        values.put("fatsAmt", item.getFats());

        long result = myDataBase.insert("MealItems", null, values);
        return result != -1;
    }

    public List<Meals> getMealsForPet(int petID) {
        List<Meals> mealsList = new ArrayList<>();

        SQLiteDatabase myDataBase = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery("SELECT * FROM Meals WHERE petID = ? ORDER BY wDate DESC, wTime DESC",
                    new String[]{String.valueOf(petID)});

            if (cursor.moveToFirst()) {
                do {
                    int mealId = cursor.getInt(cursor.getColumnIndexOrThrow("wMealID"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("wDate"));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow("wTime"));
                    double avgKcal = cursor.getDouble(cursor.getColumnIndexOrThrow("totalKcal"));
                    double avgFat = cursor.getDouble(cursor.getColumnIndexOrThrow("totalFats"));
                    double avgProtein = cursor.getDouble(cursor.getColumnIndexOrThrow("totalProtein"));
                    double avgMoisture = cursor.getDouble(cursor.getColumnIndexOrThrow("totalMoisture"));

                    Meals meal = new Meals(mealId, petID, date, time, avgKcal, avgFat, avgProtein, avgMoisture);
                    mealsList.add(meal);
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) cursor.close();
        }
        return mealsList;
    }

    public Cursor getMealItemsForMeal(int mealID) {
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        String query = "SELECT * FROM MealItems WHERE mealID = ?";
        return myDataBase.rawQuery(query, new String[]{String.valueOf(mealID)});
    }

    public double[] getPetRecommendations(int petID) {
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        double[] recs = new double[4];
        Cursor cursor = null;

        try {
            cursor = myDataBase.rawQuery(
                    "SELECT wPetKcalGoal, wPetProteinGoal, wPetFatsGoal, wPetMoistureGoal FROM Pets WHERE wPetID = ?",
                    new String[]{String.valueOf(petID)}
            );

            if (cursor.moveToFirst()) {
                recs[0] = cursor.getDouble(0);
                recs[1] = cursor.getDouble(1);
                recs[2] = cursor.getDouble(2);
                recs[3] = cursor.getDouble(3);
            }

        }finally{
            if (cursor != null) cursor.close();
        }
        return recs;
    }

    public Pets getPetDetails(int petID) {
        if (myDataBase == null || !myDataBase.isOpen()) {
            openDatabase();
        }

        Cursor cursor = null;

        try {
            cursor = myDataBase.rawQuery("SELECT wPetKcalGoal, wPetProteinGoal, wPetFatsGoal, wPetMoistureGoal FROM Pets WHERE wPetID = ?",
                    new String[]{String.valueOf(petID)});

            Pets pet = null;
            if (cursor.moveToFirst()) {
                pet = new Pets();
                pet.setRecKcal(cursor.getDouble(0));
                pet.setRecProtein(cursor.getDouble(1));
                pet.setRecFats(cursor.getDouble(2));
                pet.setRecMoisture(cursor.getDouble(3));
            }

            return pet;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    @Override
    public synchronized void close () {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }


}
