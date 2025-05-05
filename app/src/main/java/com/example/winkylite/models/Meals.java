package com.example.winkylite.models;

import android.content.Context;

import com.example.winkylite.database.DBHandler;

import java.util.List;

public class Meals {
    private int petID;
    private String date, time,  description, itemType;
    private double kcal, moisture, fats, protein, totalKcal,
            totalMoisture,totalFats, totalProtein;
    //private List<MealItem> mealItems;

    public Meals (int petID, String date, String time, String description, String itemType,
                  double kcal, double moisture, double fats, double protein, double totalKcal,
                  double  totalMoisture,double totalFats,double totalProtein){

        this.petID = petID;

        this.date= date;
        this.time =time;
        this.itemType = itemType;
        this.description = description;

        this.kcal =  kcal;
        this.moisture = moisture;
        this.fats = fats;
        this.protein = protein;

        this.totalKcal = kcal;
        this.totalMoisture = moisture;
        this.totalFats = fats;
        this.totalProtein = protein;
    }

    public int getPetID() { return petID;}
    public String getDate() { return date;}
    public String getTime() { return time;}
    public String getItemType() {return itemType;}
    public String getDescription() { return description;}
    public double getKcal() { return kcal;}
    public double getMoisture() { return moisture;}
    public double getFats() { return fats;}
    public double getProtein() { return protein;}
    public double getTotalKcal() { return totalKcal;}
    public double getTotalMoisture() { return totalMoisture;}
    public double getTotalFats() { return totalFats;}
    public double getTotalProtein() { return totalProtein;}

    public void saveToDB(Context context){
        DBHandler db_helper = new DBHandler(context);
        db_helper.insertMeal(this);

    }
}

