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
    private static final int DB_VERSION = 1;
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
            // Don't call getReadableDatabase(); just make sure the dir exists
            File dbFile = new File(DB_PATH);
            dbFile.getParentFile().mkdirs();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
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

    private void copyDatabase() throws IOException {
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

    public void openDatabase() throws SQLException {
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
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
        // Handle upgrades if needed
    }

    public Cursor queryData(String query) {
        return myDataBase.rawQuery(query, null);
    }

    public void insertPet(Pets Pets) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase
                (DB_PATH,null, SQLiteDatabase.OPEN_READWRITE);

        ContentValues values = new ContentValues();

        values.put("wUserID", 1);
        values.put("wPetName", Pets.getPetName());
        values.put("wPetType", Pets.getPetType());
        values.put("wPetAge", Pets.getPetAge());
        values.put("wPetAgeMY", Pets.getAgeUnit());
        values.put("wPetGender", Pets.getPetGender());
        values.put("wPetFixed", Pets.getIsFixed() ? "Yes" : "No");
        values.put("wPetActivityLvl", Pets.getPetActivity());
        values.put("wPetCurrentWeight", Pets.getPetCurrentWeight());
        values.put("wPetGoalWeight", Pets.getHasGoalWeight() ? Pets.getPetGoalWeight() : null);
        values.put("wPetKcalGoal", Pets.getRecKcal());
        values.put("wPetProteinGoal",Pets.getRecProtein());
        values.put("wPetFatsGoal", Pets.getRecFats());
        values.put("wPetMoistureGoal", 75);
        values.put("wPetActivityLvl", Pets.getPetActivityLevel());

        db.insert("wPets", null, values);

        db.close();

    }

    public Cursor getAllPets() {
        if(myDataBase == null || !myDataBase.isOpen()){
            openDatabase();
        }

        String query = "SELECT wPetName FROM wPets WHERE userID = 1";
        return myDataBase.rawQuery(query, null);
    }
}