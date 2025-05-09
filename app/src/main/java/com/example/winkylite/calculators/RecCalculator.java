package com.example.winkylite.calculators;


public class RecCalculator {

    double recKcal, recProtein, recFat;

    public void performCalculations(double weight, int activityLevel,
                                    String petType, boolean isFixed, int petAge,
                                    boolean isPuppyOrKitten, String ageUnit) {
        recKcal = calcRecKcal(weight, petType, isFixed, petAge, isPuppyOrKitten);
        recProtein = calcRecProtein(petType, ageUnit, petAge, recKcal);
        recFat = calcRecFats(activityLevel, petType, isPuppyOrKitten);
    }

    private double calcRecFats(int activityLevel, String petType, boolean isPuppyorKitten) {
        if (petType.equals("Dog")) {
            if (isPuppyorKitten) {
                recFat = 25;
            } else {
                switch (activityLevel) {
                    case 2:
                        recFat = 15;
                        break;
                    case 1:
                        recFat = 10;
                        break;
                    default:
                        recFat = 8;
                        break;
                }
            }
        } else if (petType.equals("Cat")) {
            if (isPuppyorKitten) {
                recFat = 20;
            } else {
                recFat = 10;
            }

        }
        return recFat;
    }

    private double calcRecProtein(String petType, String ageUnit, int petAge,
                                  double recKcal) {
        double gramsPer1000Kcal = 0;

        if (petType.equals("Dog")) {
            if (ageUnit.equals("Months")) {
                if (petAge <= 4) {
                    gramsPer1000Kcal = 45;
                } else {
                    gramsPer1000Kcal = 35;
                }
            } else {
                gramsPer1000Kcal = 20;
            }
        } else if (petType.equals("Cat")) {
            if (ageUnit.equals("Months") && petAge < 12) {
                gramsPer1000Kcal = 45;
            } else {
                gramsPer1000Kcal = 40;
            }
        }
        return (gramsPer1000Kcal /1000) * recKcal;
    }


   private double calcRER(double weight) {
        return 70 * Math.pow(weight, 0.75);
    }

    private double calcRecKcal(double weight, String petType, boolean isFixed,
                               int petAge, boolean isPuppyOrKitten) {
        double RER = calcRER(weight);

        if (petType.equals("Dog")) {
            if (isPuppyOrKitten) {
                if (petAge < 4) {
                    recKcal = 3 * RER;
                } else {
                    recKcal = 2 * RER;
                }
            } else {
                if (isFixed) {
                    recKcal = 1.6 * RER;
                } else {
                    recKcal = 1.8 * RER;
                }
            }
        } else if (petType.equals("Cat")) {
            if (isPuppyOrKitten) {
                recKcal = 2.5 * RER;
            } else {
                if (isFixed) {
                    recKcal = 1.2 * RER;
                } else {
                    recKcal = 1.4 * RER;
                }
            }
        }
        return recKcal;
    }

    public double getRecKcal() {
        return recKcal;
    }

    public double getRecProtein() {
        return recProtein;
    }

    public double getRecFats() {
        return recFat;
    }
    
}