package com.example.winkylite.models;

import android.content.Context;

import com.example.winkylite.calculators.mealCalculator;
import com.example.winkylite.database.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class Meals {
    private int mealID;
    private int petID;
    private String date, time, description;
    private List<mealItem> mealItems;

    private double totalKcal, totalProtein, totalFats, totalMoisture;
    private double avgKcal, avgFat, avgProtein, avgMoisture;

    // Constructor for creating new Meals with item list (used when adding a meal)
    public Meals(int petID, String date, String time, String description, List<mealItem> mealItems) {
        this.petID = petID;
        this.date = date;
        this.time = time;
        this.description = description;
        this.mealItems = mealItems;

        double[] totals = mealCalculator.calculateMeal(mealItems);
        this.totalKcal = totals[0];
        this.totalMoisture = totals[1];
        this.totalFats = totals[2];
        this.totalProtein = totals[3];

        this.avgKcal = totalKcal / mealItems.size();
        this.avgFat = totalFats / mealItems.size();
        this.avgProtein = totalProtein / mealItems.size();
        this.avgMoisture = totalMoisture / mealItems.size();
    }

    // Constructor for loading meals from database
    public Meals(int mealID, int petID, String date, String time, String description, double avgKcal, double avgFat, double avgProtein, double avgMoisture) {
        this.mealID = mealID;
        this.petID = petID;
        this.date = date;
        this.time = time;
        this.description = description;
        this.avgKcal = avgKcal;
        this.avgFat = avgFat;
        this.avgProtein = avgProtein;
        this.avgMoisture = avgMoisture;
        this.mealItems = new ArrayList<>(); // Will be populated later
    }

    // Save meal and its items to DB
    public boolean saveToDB(Context context) throws DBHandler.DatabaseException {
        DBHandler dbHelper = new DBHandler(context);
        long insertedMealID = dbHelper.insertMeal(this);

        if (insertedMealID == -1) {
            dbHelper.close();
            return false;
        }

        for (mealItem item : mealItems) {
            boolean itemInserted = dbHelper.insertMealItem((int) insertedMealID, item);
            if (!itemInserted) {
                dbHelper.close();
                return false;
            }
        }

        dbHelper.close();
        return true;
    }

    // Getters
    public int getMealID() { return mealID; }
    public int getPetID() { return petID; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }
    public List<mealItem> getMealItems() { return mealItems; }

    public double getTotalKcal() { return totalKcal; }
    public double getTotalProtein() { return totalProtein; }
    public double getTotalFats() { return totalFats; }
    public double getTotalMoisture() { return totalMoisture; }

    public double getAvgKcal() { return avgKcal; }
    public double getAvgFat() { return avgFat; }
    public double getAvgProtein() { return avgProtein; }
    public double getAvgMoisture() { return avgMoisture; }
}
