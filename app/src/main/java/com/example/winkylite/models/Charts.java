package com.example.winkylite.models;

import com.example.winkylite.calculators.chartCalculator;

//import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Charts {
    private final String nutrientName;
    private final List<DataPoint> dataPoints;
    private final float recommendationValue;


    public Charts(String nutrientName, Map<String, chartCalculator.DailyAverage> dailyAverages, float recommendation) {
        this.nutrientName = nutrientName;
        this.recommendationValue = recommendation;
       this.dataPoints = new ArrayList<>();

        int index = 0;
       for (Map.Entry<String, chartCalculator.DailyAverage> entry : dailyAverages.entrySet()) {
           float value = (float) getNutrientValue(entry.getValue());
            this.dataPoints.add(new DataPoint(index++, value));
       }
    }


    private double getNutrientValue(chartCalculator.DailyAverage daily) {
        switch(nutrientName) {
            case "Kcal": return daily.averageKcal;
            case "Protein": return daily.averageProtein;
            case "Fats": return daily.averageFats;
            case "Moisture": return daily.averageMoisture;
            default: return 0;
        }
    }

    // Getters
    public List<DataPoint> getDataPoints() { return dataPoints; }
    public float getRecommendation() { return recommendationValue; }
    public String getNutrientName() { return nutrientName; }
}