package com.example.winkylite.models;

    public class mealItem {
        private final String type;
        private final double kcal;
        private final double moisture;
        private final double fats;
        private final double protein;

        public mealItem(String type, double kcal, double moisture, double fats, double protein) {
            this.type = type;
            this.kcal = kcal;
            this.moisture = moisture;
            this.fats = fats;
            this.protein = protein;
        }

        // getters
        public String getType() { return type; }
        public double getKcal() { return kcal; }
        public double getMoisture() { return moisture; }
        public double getFats() { return fats; }
        public double getProtein() { return protein; }
    }

