/* package com.example.winkylite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class dbUpdater {
    private static final String TAG = "DatabaseUpdater";
    private  static final int LATEST_DB_VERSION = 3;

    public static void update(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            switch (version) {
                case 1:
                    runV1Update(db);
                    break;
                case 2:
                    runV2Update(db);
                    break;
                case 3:
                    runV3Update(db);
                    break;
                default:
                    throw new IllegalStateException("Unexpected version: " + version);
            }
        }
    }

    private static void runV1Update(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Pets (" +
                    "wPetID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userID INTEGER NOT NULL," +
                    "wPetName TEXT NOT NULL," +
                    "wPetType TEXT," +
                    "wPetAge INTEGER," +
                    "wPetAgeMY TEXT," +
                    "wPetGender TEXT," +
                    "wPetFixed TEXT," +
                    "wPetActivityLvl TEXT," +
                    "wPetActivity REAL," +
                    "wPetCurrentWeight REAL," +
                    "wPetGoalWeight REAL," +
                    "wPetKcalGoal REAL," +
                    "wPetProteinGoal REAL," +
                    "wPetFatsGoal REAL," +
                    "wPetMoistureGoal REAL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS Meals (" +
                    "wMealID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "petID INTEGER NOT NULL," +
                    "wDate TEXT NOT NULL," +
                    "wTime TEXT NOT NULL," +
                    "wDescription TEXT," +
                    "totalKcal REAL NOT NULL," +
                    "totalMoisture REAL NOT NULL," +
                    "totalProtein REAL NOT NULL," +
                    "totalFats REAL NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS MealItems (" +
                    "wItemID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "mealID INTEGER NOT NULL," +
                    "itemType TEXT NOT NULL," +
                    "kcal REAL NOT NULL," +
                    "moisture REAL NOT NULL," +
                    "protein REAL NOT NULL," +
                    "fats REAL NOT NULL);");
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e(TAG, "V1 Migration failed: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    private static void runV2Update(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("ALTER TABLE Meals ADD COLUMN wNotes TEXT DEFAULT ''");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_meals_date ON Meals(wDate)");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "V2 Migration failed: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    private static void runV3Update(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("ALTER TABLE Meals ADD COLUMN wTimestamp INTEGER DEFAULT 0");

            db.execSQL("CREATE TEMPORARY TABLE TempMeals AS SELECT * FROM Meals");
            db.execSQL("DROP TABLE Meals");

            db.execSQL("CREATE TABLE Meals (" +
                    "wMealID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "petID INTEGER NOT NULL," +
                    "wTimestamp INTEGER NOT NULL," +
                    "wDescription TEXT," +
                    "totalKcal REAL NOT NULL," +
                    "totalMoisture REAL NOT NULL," +
                    "totalProtein REAL NOT NULL," +
                    "totalFats REAL NOT NULL," +
                    "wNotes TEXT DEFAULT '');");
            db.execSQL("INSERT INTO Meals SELECT " +
                    "wMealID, petID, 0, wDescription, " +
                    "totalKcal, totalMoisture, totalProtein, totalFats, wNotes " +
                    "FROM TempMeals");

            db.execSQL("DROP TABLE TempMeals");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "V3 Migration failed: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
    public static void deleteDatabase(Context context) {
        context.deleteDatabase("myDBupdated.db");
    }

    public static int getCurrentVersion(Context context) {
        return context.getDatabasePath("myDBupdated.db").exists() ?
                SQLiteDatabase.openDatabase(
                        context.getDatabasePath("myDBupdated.db").getPath(),
                        null,
                        SQLiteDatabase.OPEN_READONLY
                ).getVersion() : 0;
    }
}
*/