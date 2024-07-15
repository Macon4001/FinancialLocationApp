package com.app.financiallocationapp.models;

import android.graphics.Color;

import com.app.financiallocationapp.R;


public  class DefaultCategories {
    private static Category[] categories = new Category[]{




            new Category(":others", "Others", R.drawable.category_default, Color.parseColor("#AC65FF")),
            new Category(":food", "Food", R.drawable.category_food, Color.parseColor("#3AB79E")),
            new Category(":gas_station", "Fuel", R.drawable.category_gas_station, Color.parseColor("#D80101")),
            new Category(":gaming", "Gaming", R.drawable.category_gaming, Color.parseColor("#B3EAB5")),
            new Category(":kids", "Kids", R.drawable.category_kids, Color.parseColor("#3F51B5")),
            new Category(":pharmacy", "Pharmacy", R.drawable.category_pharmacy, Color.parseColor("#FF5ED2")),
            new Category(":repair", "Repair", R.drawable.category_repair, Color.parseColor("#2A7781")),
            new Category(":shopping", "Shopping", R.drawable.category_shopping, Color.parseColor("#00BCD4")),
            new Category(":sport", "Sport", R.drawable.category_sport, Color.parseColor("#F44336")),
            new Category(":transport", "Transport", R.drawable.category_transport, Color.parseColor("#E91E63")),
            new Category(":work", "Work", R.drawable.category_briefcase, Color.parseColor("#009688")),
            new Category(":clothing", "Clothing", R.drawable.category_clothing, Color.parseColor("#CDDC39")),
            new Category(":gift", "Gift", R.drawable.category_gift, Color.parseColor("#FF5722")),
            new Category(":holidays", "Holidays", R.drawable.category_holidays, Color.parseColor("#FFC107")),
            new Category(":home", "Home", R.drawable.category_home, Color.parseColor("#3F51B5")),
            new Category(":transfer", "Transfer", R.drawable.category_transfer, Color.parseColor("#673AB7")),


    };

    public static Category createDefaultCategoryModel(String visibleName) {
        return new Category("default", visibleName, R.drawable.category_default,
                Color.parseColor("#26a69a"));
    }


    public static Category[] getDefaultCategories() {
        return categories;
    }
}
