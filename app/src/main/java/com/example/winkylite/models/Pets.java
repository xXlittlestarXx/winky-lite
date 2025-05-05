package com.example.winkylite.models;

import android.content.Context;

import com.example.winkylite.calculators.RecCalculator;
import com.example.winkylite.database.DBHandler;

import java.io.IOException;

public class Pets {

    /* VARIABLES */
    private String petName, ageUnit, petGender, petType, petActivity;
    /* months / years  ; male/female  ; cat/dog */
    private boolean isFixed, hasGoalWeight;
    /* Yes/No */
    private int petAge, activityLevel;
    /* 0, 1, or 2 */
    private double petCurrentWeight, petGoalWeight;

    /* FOR CALCULATIONS */
    private double recProtein, recKcal, recFats, recMoisture;

    /* CONSTRUCTOR */
    public Pets (String petName, String ageUnit, String petGender, String petType,
                 boolean isFixed, boolean hasGoalWeight,
                 int petAge, int activityLevel,
                 double petCurrentWeight, double petGoalWeight){
        /*Strings*/
        this.petName = petName;
        this.ageUnit = ageUnit;
        this.petGender = petGender;
        this.petType = petType;

        /*booleans*/
        this.isFixed = isFixed;
        this.hasGoalWeight = hasGoalWeight;

        /*ints*/
        this.petAge = petAge;
        this.activityLevel = activityLevel;

        /*doubles*/
        this.petCurrentWeight = petCurrentWeight;
        this.petGoalWeight = petGoalWeight;


    }


    /* GETTERS */

    /* STRINGS */
    public String getPetName () {return petName;}
    public String getAgeUnit() {return ageUnit;}
    public String getPetGender () {return petGender;}
    public String getPetType () {return petType;}

    /* BOOLEANS */
    public boolean getIsFixed () {return isFixed;}
    public boolean getHasGoalWeight () {return hasGoalWeight;}


    /* INTS */
    public int getPetAge () {return petAge;}
    public int getPetActivityLevel () {return activityLevel;}

    /* DOUBLES */

    public double getPetCurrentWeight() {return petCurrentWeight;}
    public double getPetGoalWeight() {return petGoalWeight;}

    public double getRecProtein() {return recProtein;}
    public double getRecKcal() {return recKcal;}
    public double getRecFats() {return recFats;}
    public double getRecMoisture() { return recMoisture; }

    public void setRecProtein(double val) { this.recProtein = val; }
    public void setRecKcal(double val) { this.recKcal = val; }
    public void setRecFats(double val) { this.recFats = val; }
    public void setRecMoisture(double val) { this.recMoisture = val; }
    public String getPetActivity() {
        switch(activityLevel){
            case 0: return "Low";
            case 1: return "Medium";
            case 2: return "High";
            default: return "Medium";
        }
    }

    public void processToCalculator(){
        RecCalculator calc = new RecCalculator ();

        boolean isPuppyOrKitten;
        if (ageUnit.equals("Months")) {
            isPuppyOrKitten = petAge < 12;
        } else{
            isPuppyOrKitten = (petAge * 12) < 12;
        }

        calc.performCalculations(petCurrentWeight, activityLevel, petType,
                isFixed, petAge, isPuppyOrKitten, ageUnit);
        this.recKcal = calc.getRecKcal();
        this.recFats = calc.getRecFats();
        this.recProtein = calc.getRecProtein();
    }

    public boolean saveToDB(Context context){
        DBHandler db_helper = new DBHandler(context);
        try {
            db_helper.createDatabase();
        } catch (IOException e){
            return false;
        }
        db_helper.openDatabase();
        return db_helper.insertPet(this);
    }

}


