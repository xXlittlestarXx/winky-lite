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
    private static final String TAG = "DBHandler";
    private static final String DB_NAME = "WinkyDB_Version5.db";
    private static final int DB_VERSION = 4;

    private final Context context;
    private final String dbPath;
    private SQLiteDatabase database;
    private volatile boolean initialized = false;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context.getApplicationContext();
        this.dbPath = context.getDatabasePath(DB_NAME).getPath();
    }

    public synchronized void initialize() throws DatabaseException {
        Log.d(TAG, "Attempting to initialize database...");
        if (initialized) {
            Log.d(TAG, "Database already initialized");
            return;
        }

        try {
            Log.d(TAG, "Checking if database exists...");
            boolean dbExists = checkDatabaseExists();
            if (!dbExists) {
                Log.d(TAG, "Database doesn't exist, creating...");
                createDatabaseDirectory();
                copyDatabaseFromAssets();
            }
            Log.d(TAG, "Opening database...");
            openDatabase();
            initialized = true;
            Log.d(TAG, "Database initialized successfully");
        } catch (IOException | SQLException e) {
            Log.e(TAG, "Database initialization failed", e);
            initialized = false;
            throw new DatabaseException("Failed to initialize database", e);
        }
    }

    private boolean checkDatabaseExists() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Database doesn't exist yet");
            return false;
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }
    }

    public void createDatabaseDirectory() {
        File dbFile = new File(dbPath);
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                Log.e(TAG, "Failed to create database directory");
            }
        }
    }

    private void copyDatabaseFromAssets() throws IOException {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = context.getAssets().open(DB_NAME);
            output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing output stream", e);
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing input stream", e);
            }
        }
    }

    public synchronized void openDatabase() throws SQLException {
        if (database != null && database.isOpen()) {
            return;
        }

        database = SQLiteDatabase.openDatabase(
                dbPath,
                null,
                SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY
        );

        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Pre-populated database — onCreate not used.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Pre-populated database — onUpgrade not used.");
    }

    @Override
    public synchronized void close() {
        try {
            if (database != null && database.isOpen()) {
                database.close();
                database = null;
            }
        } finally {
            super.close();
            initialized = false;
        }
    }

    public boolean insertPet(Pets pet) throws DatabaseException {
        checkInitialized();

        ContentValues values = new ContentValues();
        values.put("userID", 1);
        values.put("wPetName", pet.getPetName());
        values.put("wPetType", pet.getPetType());
        values.put("wPetAge", pet.getPetAge());
        values.put("wPetAgeMY", pet.getAgeUnit());
        values.put("wPetGender", pet.getPetGender());
        values.put("wPetFixed", pet.getIsFixed() ? "Yes" : "No");
        values.put("wPetActivityLvl", pet.getPetActivity());
        values.put("wPetActivity", (double) pet.getPetActivityLevel());
        values.put("wPetCurrentWeight", pet.getPetCurrentWeight());
        values.put("wPetKcalGoal", pet.getRecKcal());
        values.put("wPetProteinGoal", pet.getRecProtein());
        values.put("wPetFatsGoal", pet.getRecFats());
        values.put("wPetMoistureGoal", 75);

        if (pet.getHasGoalWeight()) {
            values.put("wPetGoalWeight", pet.getPetGoalWeight());
        } else {
            values.putNull("wPetGoalWeight");
        }

        try {
            long result = database.insert("Pets", null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Failed to insert pet", e);
            throw new DatabaseException("Failed to insert pet", e);
        }
    }

    public Cursor getAllPets() throws DatabaseException {
        checkInitialized();

        String query = "SELECT wPetName FROM Pets WHERE userID = 1";
        try {
            return database.rawQuery(query, null);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to get pets", e);
            throw new DatabaseException("Failed to get pets", e);
        }
    }

    public int getPetIdByName(String petName) throws DatabaseException {
        checkInitialized();

        if (petName == null || petName.isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty");
        }

        Cursor cursor = null;
        try {
            String query = "SELECT wPetID FROM Pets WHERE wPetName = ?";
            cursor = database.rawQuery(query, new String[]{petName});

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("wPetID");
                if (columnIndex != -1) {
                    return cursor.getInt(columnIndex);
                }
            }
            return -1;
        } catch (SQLException e) {
            Log.e(TAG, "Failed to get pet ID by name", e);
            throw new DatabaseException("Failed to get pet ID by name", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public long insertMeal(Meals meal) throws DatabaseException {
        checkInitialized();
        if (database == null || !database.isOpen()) {
            throw new DatabaseException("Database connection is not open");
        }

        ContentValues values = new ContentValues();
        values.put("petID", meal.getPetID());
        values.put("wDate", meal.getDate());
        values.put("wTime", meal.getTime());
        values.put("wDescription", meal.getDescription());
        values.put("totalKcal", meal.getTotalKcal());
        values.put("totalMoisture", meal.getTotalMoisture());
        values.put("totalProtein", meal.getTotalProtein());
        values.put("totalFats", meal.getTotalFats());
        values.put("wPetFixed", "bug");

        try {
            return database.insert("Meals", null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to insert meal", e);
            throw new DatabaseException("Failed to insert meal", e);
        }
    }

    public boolean insertMealItem(int mealId, mealItem item) throws DatabaseException {
        checkInitialized();

        ContentValues values = new ContentValues();
        values.put("mealID", mealId);
        values.put("itemType", item.getType());
        values.put("kcalCount", item.getKcal());
        values.put("moistureAmt", item.getMoisture());
        values.put("proteinAmt", item.getProtein());
        values.put("fatsAmt", item.getFats());

        try {
            long result = database.insert("MealItems", null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Failed to insert meal item", e);
            throw new DatabaseException("Failed to insert meal item", e);
        }
    }

    public List<Meals> getMealsForPet(int petId) throws DatabaseException {
        checkInitialized();

        List<Meals> mealsList = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(
                    "SELECT * FROM Meals WHERE petID = ? ORDER BY wDate DESC, wTime DESC",
                    new String[]{String.valueOf(petId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int mealId = cursor.getInt(cursor.getColumnIndexOrThrow("wMealID"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("wDate"));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow("wTime"));
                    String descr =  cursor.getString(cursor.getColumnIndexOrThrow("wDescription"));

                    List<mealItem> items = new ArrayList<>();
                    Cursor itemCursor = database.rawQuery("SELECT * FROM MealItems WHERE mealID = ?",
                            new String[]{String.valueOf(mealId)});

                    double totalKcal = 0, totalFats = 0, totalProtein = 0, totalMoisture = 0;
                    if (itemCursor != null && itemCursor.moveToFirst()) {
                        do {
                            String type = itemCursor.getString(itemCursor.getColumnIndexOrThrow("itemType"));
                            double kcal = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow("kcalCount"));
                            double fat = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow("fatsAmt"));
                            double protein = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow("proteinAmt"));
                            double moisture = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow("moistureAmt"));

                            totalKcal += kcal;
                            totalFats += fat;
                            totalProtein += protein;
                            totalMoisture += moisture;

                            mealItem item = new mealItem(type, kcal, fat, protein, moisture);
                            items.add(item);
                        } while (itemCursor.moveToNext());
                    }

                    if (itemCursor != null) itemCursor.close();

                    int itemCount = items.size();
                    double avgKcal = itemCount > 0 ? totalKcal / itemCount : 0;
                    double avgFat = itemCount > 0 ? totalFats / itemCount : 0;
                    double avgProtein = itemCount > 0 ? totalProtein / itemCount : 0;
                    double avgMoisture = itemCount > 0 ? totalMoisture / itemCount : 0;

                    Meals meal = new Meals(mealId, petId, date, time, descr, items);
                    meal.setTotalKcal(totalKcal);
                    meal.setTotalFats(totalFats);
                    meal.setTotalProtein(totalProtein);
                    meal.setTotalMoisture(totalMoisture);
                    meal.setAvgKcal(avgKcal);
                    meal.setAvgFat(avgFat);
                    meal.setAvgProtein(avgProtein);
                    meal.setAvgMoisture(avgMoisture);

                    mealsList.add(meal);
                } while (cursor.moveToNext());
            }
            return mealsList;
        } catch (SQLException e) {
            Log.e(TAG, "Failed to get meals for pet", e);
            throw new DatabaseException("Failed to get meals for pet", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Cursor getMealItemsForMeal(int mealId) throws DatabaseException {
        checkInitialized();

        try {
            String query = "SELECT * FROM MealItems WHERE mealID = ?";
            return database.rawQuery(query, new String[]{String.valueOf(mealId)});
        } catch (SQLException e) {
            Log.e(TAG, "Failed to get meal items", e);
            throw new DatabaseException("Failed to get meal items", e);
        }
    }

    public Pets getPetDetails(int petId) throws DatabaseException {
        checkInitialized();

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(
                    "SELECT wPetName, wPetType, wPetAge, wPetAgeMY, wPetGender, " +
                            "wPetActivityLvl, wPetActivity, wPetCurrentWeight, wPetGoalWeight, " +
                            "wPetKcalGoal, wPetProteinGoal, wPetMoistureGoal, wPetFatsGoal, " +
                            "wPetFixed " +
                            "FROM Pets WHERE wPetID = ?",
                    new String[]{String.valueOf(petId)}
            );

            if (cursor != null && cursor.moveToFirst()) {
                Pets pet = new Pets();
                pet.setPetName(cursor.getString(cursor.getColumnIndexOrThrow("wPetName")));
                pet.setPetType(cursor.getString(cursor.getColumnIndexOrThrow("wPetType")));
                pet.setPetAge(cursor.getInt(cursor.getColumnIndexOrThrow("wPetAge")));
                pet.setPetAgeUnit(cursor.getString(cursor.getColumnIndexOrThrow("wPetAgeMY")));
                pet.setPetGender(cursor.getString(cursor.getColumnIndexOrThrow("wPetGender")));
                pet.setIsFixed(cursor.getString(cursor.getColumnIndexOrThrow("wPetFixed")).equalsIgnoreCase("Yes"));
                pet.setActivityLvl(cursor.getInt(cursor.getColumnIndexOrThrow("wPetActivity")));
                pet.setCurrentWeight(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetCurrentWeight")));

                int goalWeightIndex = cursor.getColumnIndex("wPetGoalWeight");
                if (!cursor.isNull(goalWeightIndex)) {
                    pet.setGoalWeight(cursor.getDouble(goalWeightIndex));
                    pet.setHasGoalWeight(true);
                } else {
                    pet.setGoalWeight(0.0);
                    pet.setHasGoalWeight(false);
                }

                pet.setRecKcal(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetKcalGoal")));
                pet.setRecProtein(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetProteinGoal")));
                pet.setRecFats(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetFatsGoal")));
                pet.setRecMoisture(cursor.getDouble(cursor.getColumnIndexOrThrow("wPetMoistureGoal")));
                return pet;
            }
            return null;
        } catch (SQLException e) {
            Log.e(TAG, "Failed to get pet details", e);
            throw new DatabaseException("Failed to get pet details", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void checkInitialized() throws DatabaseException {
        if (!initialized) {
            throw new DatabaseException("Database not initialized. Call initialize() first.");
        }
    }

    public static class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }

        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
