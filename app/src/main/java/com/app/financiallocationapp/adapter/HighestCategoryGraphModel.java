package com.app.financiallocationapp.adapter;


import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.Currency;

public class HighestCategoryGraphModel {
    private final float percentage;
    private long money;
    private final Currency currency;
    private final Category category;

    private String categoryName;


    public HighestCategoryGraphModel(Category category, String categoryName, Currency currency, long money, float percentage) {
        this.category = category;
        this.categoryName = categoryName;
        this.currency = currency;
        this.money = money;
        this.percentage = percentage;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getMoney() {
        return money;
    }

    public Category getCategory() {
        return category;
    }

    public float getPercentage() {
        return percentage;
    }
}
