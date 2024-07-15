package com.app.financiallocationapp.budget;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.app.financiallocationapp.models.BudgetEntriesBaseViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;


public class ExpenseModel implements ViewModelProvider.Factory {
    private String uid;

    ExpenseModel(String uid) {
        this.uid = uid;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new Model(uid);
    }

    public static Model getModel(String uid, Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity, new ExpenseModel(uid)).get(Model.class);
    }

    public static class Model extends BudgetEntriesBaseViewModel {

        private Calendar endDate;
        private Calendar startDate;

        public Model(String uid) {

            super(uid, getDefaultQuery(uid));
        }

        private static Query getDefaultQuery(String uid) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            return FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp").limitToFirst(500);
        }

        public void setDateFilter(Calendar startDate, Calendar endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            if (startDate != null && endDate != null) {
                liveData.setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("wallet-entries").child(uid).child("default").orderByChild("timestamp")
                        .startAt(-endDate.getTimeInMillis()).endAt(-startDate.getTimeInMillis()));
            } else {
                liveData.setQuery(getDefaultQuery(uid));
            }
        }

        public boolean hasDateSet() {
            return startDate != null && endDate != null;
        }

        public Calendar getStartDate() {
            return startDate;
        }

        public Calendar getEndDate() {
            return endDate;
        }
    }
}