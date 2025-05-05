package com.example.winkylite.calculators;

import com.example.winkylite.models.Meals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class chartCalculator {
    public static Map<String, DailyAverage> calculateDailyAverages(List<Meals> meals) {
        Map<String, DailyAverage> dailyData = new HashMap<>();

        for (Meals meal : meals) {
            String date = meal.getDate();
            DailyAverage day = dailyData.getOrDefault(date, new DailyAverage());

            day.totalKcal += meal.getTotalKcal();
            day.totalProtein += meal.getTotalProtein();
            day.totalFats += meal.getTotalFats();
            day.totalMoisture += meal.getTotalMoisture();
            day.mealCount++;

            dailyData.put(date, day);
        }
        for (Map.Entry<String, DailyAverage> entry : dailyData.entrySet()) {
            DailyAverage day = entry.getValue();
            day.averageKcal = day.totalKcal / day.mealCount;
            day.averageProtein = day.totalProtein / day.mealCount;
            day.averageFats = day.totalFats / day.mealCount;
            day.averageMoisture = day.totalMoisture / day.mealCount;
        }

        return dailyData;
    }

    public static class DailyAverage {
        public int mealCount = 0;
        public double totalKcal;
        public double totalProtein;
        public double totalFats;
        public double totalMoisture;
        public double averageKcal;
        public double averageProtein;
        public double averageFats;
        public double averageMoisture;
    }

    public static String compareWithRecommendation(double actual, double recommended) {
        double difference = actual - recommended;
        double percentageDiff = (difference / recommended) * 100;

        if (percentageDiff < -10) {
            return "Low: " + String.format("%.1f%% below recommendation", Math.abs(percentageDiff));
        } else if (Math.abs(percentageDiff) <= 10) {
            return "Good: Within 10% of recommendation";
        } else {
            return "High: " + String.format("%.1f%% above recommendation", percentageDiff);
        }
    }

    public static String generateChartURL(List<Double> values, double recommendation, String label) {
        String baseURL = "https://image-charts.com/chart?";

        String chs = "600x200";
        String cht = "lc";
        String chd = "t:" + getFormattedValues(values);
        String chco = "4A89DC";
        String chdl = label;
        String chxt = "x,y";
        String chxl = "0:|" + getDayLabels(values.size());
        String chm = getRecommendationMarker(recommendation);

        return baseURL + "chs=" + chs +
                "&cht=" + cht +
                "&chd=" + chd +
                "&chco=" + chco +
                "&chdl=" + chdl +
                "&chxt=" + chxt +
                "&chxl=" + chxl +
                "&chm=" + chm;
    }
    private static String getFormattedValues(List<Double> values) {
        StringBuilder sb = new StringBuilder();
        for (Double value : values) {
            sb.append(String.format("%.1f", value)).append(",");
        }
        return sb.toString().replaceAll(",$", "");
    }

    private static String getDayLabels(int days) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= days; i++) {
            sb.append("Day ").append(i).append("|");
        }
        return sb.toString().replaceAll("\\|$", "");
    }
    private static String getRecommendationMarker(double recommendation) {
        return "r,FF0000,0," + recommendation + ",0.5";
    }
}
