package com.app.financiallocationapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BudgetEntryCategory {
    public String htmlColorCode;
    public String visibleName;

    public BudgetEntryCategory() {

    }

    public BudgetEntryCategory(String visibleName, String htmlColorCode) {
        this.htmlColorCode = htmlColorCode;
        this.visibleName = visibleName;
    }

}