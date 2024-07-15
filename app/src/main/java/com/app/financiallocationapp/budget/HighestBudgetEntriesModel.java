package com.app.financiallocationapp.budget;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.app.financiallocationapp.models.BudgetEntriesBaseViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class HighestBudgetEntriesModel implements ViewModelProvider.Factory {
    private String uid;

    HighestBudgetEntriesModel(String uid) {
        this.uid = uid;

        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new Model(uid);
    }

    public static Model getModel(String uid, Activity activity) {
        return ViewModelProviders.of((FragmentActivity) activity, new HighestBudgetEntriesModel(uid)).get(Model.class);
    }

    public static class Model extends BudgetEntriesBaseViewModel {

        public Model(String uid) {
            super(uid, FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp"));
        }

        //new changes
        public void setDateFilter(Calendar startDate, Calendar endDate) {
            liveData.setQuery(FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp")
                    .startAt(-endDate.getTimeInMillis()).endAt(-startDate.getTimeInMillis()));
        }
    }
}