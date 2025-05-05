package com.example.winkylite.calculators;

import com.example.winkylite.models.mealItem;
import java.util.List;

public class mealCalculator {
    public static double[] calculateMeal(List<mealItem> mealItemList) {
        if (mealItemList == null || mealItemList.isEmpty()) {
            return new double[]{0, 0, 0, 0};
        }

        double totalKcal = 0, totalMoisture = 0, totalFats = 0, totalProtein = 0;

        for (mealItem item : mealItemList) {
            totalKcal += item.getKcal();
            totalMoisture += item.getMoisture();
            totalFats += item.getFats();
            totalProtein += item.getProtein();
        }

        int count = mealItemList.size();

        return new double[]{
                totalKcal / count,
                totalMoisture / count,
                totalFats / count,
                totalProtein / count
        };
    }
}