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
    private final List<mealItem> mealItems;

    private double totalKcal, totalProtein, totalFats, totalMoisture;
    private double avgKcal,avgFat, avgProtein, avgMoisture;

    // Constructor for creating new Meals with item list (used when adding a meal)
    public Meals(int mealID, int petID, String date, String time, String description, List<mealItem> mealItems) {
        this.mealID = mealID;
        this.petID = petID;
        this.date = date;
        this.time = time;
        this.description = description;
        this.mealItems = mealItems;

        this.totalKcal = 0;
        this.totalMoisture = 0;
        this.totalFats = 0;
        this.totalProtein = 0;

        for (mealItem item : mealItems) {
            this.totalKcal += item.getKcal();
            this.totalMoisture += item.getMoisture();
            this.totalFats += item.getFats();
            this.totalProtein += item.getProtein();
        }

        int itemCount = mealItems.size();
        this.avgKcal = itemCount > 0 ? totalKcal / itemCount : 0;

       this.avgFat = itemCount > 0 ? totalFats / itemCount : 0;
        this.avgProtein = itemCount > 0 ? totalProtein / itemCount : 0;
     this.avgMoisture = itemCount > 0 ? totalMoisture / itemCount : 0;
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
        dbHelper.initialize();
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

    //Setters
    public void setMealID(int mealID) {this.mealID = mealID;}
    public void setPetID(int petID) {this.petID = petID;}
    public void setDate(String date) {this.date = date;}
    public void setTime(String time) {this.time = time;}
    public void setDescription(String description) {this.description = description;}
    //public void setMealItems(List<mealItem> mealItems) {this.mealItems = mealItems;}
    public void setTotalKcal(double totalKcal) {this.totalKcal = totalKcal;}
    public void setTotalProtein(double totalProtein) {this.totalProtein = totalProtein;}
    public void setTotalFats(double totalFats) {this.totalFats = totalFats;}
    public void setTotalMoisture(double totalMoisture) {this.totalMoisture = totalMoisture;}
    public void setAvgKcal(double avgKcal) {this.avgKcal = avgKcal;}
    public void setAvgFat(double avgFat) {this.avgFat = avgFat;}

  public void setAvgProtein(double avgProtein) {this.avgProtein = avgProtein;}
    public void setAvgMoisture(double avgMoisture) {this.avgMoisture = avgMoisture;}


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

    public void setMealItems(List<mealItem> mealItems) {
        recalculateTotals();
    }

    private void recalculateTotals() {
        totalKcal = 0;
        totalMoisture = 0;
        totalFats = 0;
        totalProtein = 0;

        for (mealItem item : mealItems) {
            totalKcal += item.getKcal();
            totalMoisture += item.getMoisture();
            totalFats += item.getFats();
            totalProtein += item.getProtein();
        }

        int count = mealItems.size();
        avgKcal = count > 0 ? totalKcal / count : 0;
        avgFat = count > 0 ? totalFats / count : 0;
        avgProtein = count > 0 ? totalProtein / count : 0;
        avgMoisture = count > 0 ? totalMoisture / count : 0;
    }
}
