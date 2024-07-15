package com.app.financiallocationapp;

import android.graphics.Color;

import com.app.financiallocationapp.models.BudgetEntryCategory;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.DefaultCategories;
import com.app.financiallocationapp.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



public class Categories {


    public static Category searchCategory(User user, String categoryName) {



        for(Category category : DefaultCategories.getDefaultCategories()) {

            if(category.getCategoryID().equals(categoryName)) return category;
        }
        for(Map.Entry<String, BudgetEntryCategory> entry : user.customCategories.entrySet()) {

            if(entry.getKey().equals(categoryName)) {
                return new Category(categoryName, entry.getValue().visibleName, R.drawable.category_default, Color.parseColor(entry.getValue().htmlColorCode));
            }

        }
        return DefaultCategories.createDefaultCategoryModel("Others");

    }

    public static List<Category> getCategories(User user) {
        List<Category> categories = new ArrayList<>();
        categories.addAll(Arrays.asList(DefaultCategories.getDefaultCategories()));
        categories.addAll(getCustomCategories(user));
        return categories;
    }

    public static List<Category> getCustomCategories(User user) {
        ArrayList<Category> categories = new ArrayList<>();
        for(Map.Entry<String, BudgetEntryCategory> entry : user.customCategories.entrySet()) {
            String categoryName = entry.getKey();
            categories.add(new Category(categoryName, entry.getValue().visibleName, R.drawable.category_default, Color.parseColor(entry.getValue().htmlColorCode)));
        }
        return categories;
    }
}
