package com.example.winkylite.calculators;

import android.net.Uri;
import android.util.Log;

import com.example.winkylite.models.Meals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public static String generateChartURL(List<Double> values, double recommendation, String label) throws UnsupportedEncodingException {
        try {
            String formattedValues = getFormattedValues(values);
            return new Uri.Builder()
                    .scheme("https")
                    .authority("image-charts.com")
                    .path("chart")
                    .appendQueryParameter("chs", "600x200")
                    .appendQueryParameter("cht", "lc")
                    .appendQueryParameter("chd", "a:" + formattedValues)
                    .appendQueryParameter("chco", "4A89DC")
                    .appendQueryParameter("chdl", label)
                    .appendQueryParameter("chxt", "x,y")
                    .appendQueryParameter("chxl", "0:|" + getDayLabels(values.size()))
                    .appendQueryParameter("chm", getRecommendationMarker(recommendation))
                    .build()
                    .toString();
        } catch (Exception e) {
            Log.e("ChartURL", "Error generating chart URL", e);
            return null;
        }
    }
    private static String getFormattedValues(List<Double> values) {
        StringBuilder sb = new StringBuilder();
        for (Double value : values) {
            sb.append(String.format("%.0f", value)).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
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
