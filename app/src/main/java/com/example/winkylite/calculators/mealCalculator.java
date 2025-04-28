package com.example.winkylite.calculators;

import com.example.winkylite.calculators.mealCalculator;
import com.example.winkylite.models.mealItem;
import java.util.List;

public class mealCalculator {
    public double averageKcal;
    public double averageMoisture;
    public double averageFats;
    public double averageProtein;
    public static double[] calculateMeal(List<mealItem> mealItemList) {
        double totalKcal = 0;
        double totalMoisture = 0;
        double totalFats = 0;
        double totalProtein = 0;

        if (mealItemList == null || mealItemList.isEmpty()) {
            return new double[]{0, 0, 0, 0};
        }

        for (mealItem item : mealItemList) {
            totalKcal += item.getKcal();
            totalMoisture += item.getMoisture();
            totalFats += item.getFats();
            totalProtein += item.getProtein();
        }

        int count = mealItemList.size();

        double averageKcal = totalKcal / count;
        double averageMoisture = totalMoisture / count;
        double averageFats = totalFats / count;
        double averageProtein = totalProtein / count;

        return new double[]{averageKcal, averageMoisture, averageFats, averageProtein};
    }
}