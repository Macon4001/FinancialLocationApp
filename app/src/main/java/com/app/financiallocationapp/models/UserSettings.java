package com.app.financiallocationapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserSettings {

    public int dayOfMonthStart = 0;
    public int dayOfWeekStart = 0;

    public int homeCounterPeriod = UserSettings.HOME_COUNTER_PERIOD_MONTHLY;

    public static final int HOME_COUNTER_PERIOD_MONTHLY = 0;
    public static final int HOME_COUNTER_PERIOD_WEEKLY = 1;


    public UserSettings() {

    }

}
