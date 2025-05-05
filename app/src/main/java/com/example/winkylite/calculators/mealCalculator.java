package com.example.winkylite.calculators;

import com.example.winkylite.models.mealItem;
import java.util.List;

public class mealCalculator {
    //public double averageKcal;
    //public double averageMoisture;
    // public double averageFats;
    //public double averageProtein;
    public static double[] calculateMeal(List<mealItem> mealItemList) {
        //double totalKcal = 0;
        //double totalMoisture = 0;
        //double totalFats = 0;
        //double totalProtein = 0;

        if (mealItemList == null || mealItemList.isEmpty()) {
            return new double[]{0, 0, 0, 0};
        }

        mealItem item = mealItemList.get(0);
        return new double[]{
                item.getKcal(),
                item.getMoisture(),
                item.getFats(),
                item.getProtein()
        };
    }
}