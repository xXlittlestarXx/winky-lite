package com.example.winkylite.models;

import android.content.Context;

import com.example.winkylite.calculators.RecCalculator;
import com.example.winkylite.database.DBHandler;

public class Pets {
    /* VARIABLES */
    private String petName, ageUnit, petGender, petType; // --Commented out by Inspection (5/8/2025 8:57 PM):petActivity;
    private boolean isFixed, hasGoalWeight;
    private int petAge, activityLevel;
    private double petCurrentWeight, petGoalWeight;
    private double recProtein, recKcal, recFats, recMoisture;

    public Pets(String petName, String ageUnit, String petGender, String petType,
                boolean isFixed, boolean hasGoalWeight,
                int petAge, int activityLevel,
                double petCurrentWeight, double petGoalWeight) {
        this.petName = petName;
        this.ageUnit = ageUnit;
        this.petGender = petGender;
        this.petType = petType;
        this.isFixed = isFixed;
        this.hasGoalWeight = hasGoalWeight;
        this.petAge = petAge;
        this.activityLevel = activityLevel;
        this.petCurrentWeight = petCurrentWeight;
        this.petGoalWeight = petGoalWeight;
    }

    public Pets() {}

    /* GETTERS AND SETTERS */
    public String getPetName() {return petName;}
    public String getAgeUnit() {return ageUnit;}
    public String getPetGender() {return petGender;}
    public String getPetType() {return petType;}
    public boolean getIsFixed() {return isFixed;}
    public boolean getHasGoalWeight() {return hasGoalWeight;}
    public int getPetAge() {return petAge;}
    public int getPetActivityLevel() {return activityLevel;}
    public double getPetCurrentWeight() {return petCurrentWeight;}
    public double getPetGoalWeight() {return petGoalWeight;}
    public double getRecProtein() {return recProtein;}
    public double getRecKcal() {return recKcal;}
    public double getRecFats() {return recFats;}
    public double getRecMoisture() {return recMoisture;}
    public void setPetName(String s) {
        this.petName = s;
    }
    public void setPetAgeUnit(String s) {
        this.ageUnit = s;
    }
    public void setPetGender(String s) {
        this.petGender = s;
    }
    public void setPetType(String s){
        this.petType = s;
    }
    public void setIsFixed(boolean bool) {
        this.isFixed = bool;
    }
    public void setHasGoalWeight(boolean bool) {
        this.hasGoalWeight = bool;
    }
    public void setPetAge(int num) {
        this.petAge = num;
    }
    public void setActivityLvl(int num){
        this.activityLevel = num;
    }
    public void setCurrentWeight(double val){
        this.petCurrentWeight = val;
    }
    public void setGoalWeight(double val) {
        this.petGoalWeight = val;
    }
    public void setRecProtein(double val) {this.recProtein = val;}
    public void setRecKcal(double val) {this.recKcal = val;}
    public void setRecFats(double val) {this.recFats = val;}
    public void setRecMoisture(double val) {this.recMoisture = val;}

    public String getPetActivity() {
        switch(activityLevel) {
            case 0: return "Low";
            case 1: return "Medium";
            case 2: return "High";
            default: return "Medium";
        }
    }

    public void processToCalculator() {
        RecCalculator calc = new RecCalculator();
        boolean isPuppyOrKitten = ageUnit.equals("Months") ? petAge < 12 : (petAge * 12) < 12;
        calc.performCalculations(petCurrentWeight, activityLevel, petType,
                isFixed, petAge, isPuppyOrKitten, ageUnit);
        this.recKcal = calc.getRecKcal();
        this.recFats = calc.getRecFats();
        this.recProtein = calc.getRecProtein();
    }

    public boolean saveToDB(Context context) {
        try {
            DBHandler dbHandler = new DBHandler(context);
            dbHandler.initialize();
            return dbHandler.insertPet(this);
        } catch (DBHandler.DatabaseException e) {
            e.printStackTrace();
            return false;
        }
    }
}