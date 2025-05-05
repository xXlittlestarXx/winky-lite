package com.example.winkylite.models;

import android.content.Context;

import com.example.winkylite.calculators.mealCalculator;
import com.example.winkylite.database.DBHandler;

import java.util.List;

public class Meals {
    private int petID;
    private String date, time,  description;
    private List<mealItem> mealItems;
    /*private double kcal, moisture, fats, protein, totalKcal,
            totalMoisture,totalFats, totalProtein;
    //private List<MealItem> mealItems; */

    private double totalKcal, totalProtein, totalFats, totalMoisture;

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
    }

    public int getPetID() { return petID; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }
    public List<mealItem> getMealItems() { return mealItems; }
    public double getTotalKcal() { return totalKcal; }
    public double getTotalProtein() { return totalProtein; }
    public double getTotalFats() { return totalFats; }
    public double getTotalMoisture() { return totalMoisture; }

    public boolean saveToDB(Context context){
        DBHandler db_helper = new DBHandler(context);
        long mealID = db_helper.insertMeal(this);

        if (mealID == -1) {
            return false;
        }
        for (mealItem item : mealItems) {
            boolean itemInserted = db_helper.insertMealItem((int) mealID, item);
            if (!itemInserted) {
                return false;
            }
        }
        db_helper.close();
        return true;
    }


}

