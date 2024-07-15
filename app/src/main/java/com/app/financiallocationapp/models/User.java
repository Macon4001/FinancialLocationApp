package com.app.financiallocationapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public Currency currency = new Currency("$", true, true);
    public UserSettings userSettings = new UserSettings();
    public Budget budget = new Budget();
    public Map<String, BudgetEntryCategory> customCategories = new HashMap<>();

    public User() {

    }
}