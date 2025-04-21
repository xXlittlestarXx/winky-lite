package com.example.winkylite.models;

public class Pets {

    /* VARIABLES */
    private String petName, ageUnit, petGender, petType;
    /* months / years  ; male/female  ; cat/dog */
    private boolean isFixed, hasGoalWeight;
    /* Yes/No */
    private int petAge, activityLevel;
    /* 0, 1, or 2 */
    private double petCurrentWeight, petGoalWeight;

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


}


