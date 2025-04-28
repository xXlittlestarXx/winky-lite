package com.example.winkylite.models;

    public class mealItem {
        private double kcal;
        private double moisture;
        private double fats;
        private double protein;

        public mealItem(double kcal, double moisture, double fats, double protein) {
            this.kcal = kcal;
            this.moisture = moisture;
            this.fats = fats;
            this.protein = protein;
        }

        // getters
        public double getKcal() { return kcal; }
        public double getMoisture() { return moisture; }
        public double getFats() { return fats; }
        public double getProtein() { return protein; }
    }

