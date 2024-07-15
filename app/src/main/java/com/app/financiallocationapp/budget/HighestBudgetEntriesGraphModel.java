package com.app.financiallocationapp.budget;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.app.financiallocationapp.models.BudgetEntriesBaseViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class HighestBudgetEntriesGraphModel implements ViewModelProvider.Factory {
    private Calendar endDate;
    private Calendar startDate;
    private String uid;

    HighestBudgetEntriesGraphModel(String uid) {
        this.uid = uid;

        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
    public void setDate(Calendar startDate, Calendar endDate){
        this.startDate=startDate;
        this.endDate=endDate;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new Model(uid);
    }

    public static Model getModel(String uid, FragmentActivity activity) {
        return ViewModelProviders.of(activity, new HighestBudgetEntriesGraphModel(uid)).get(Model.class);
    }

    public static class Model extends BudgetEntriesBaseViewModel {

        public Model(String uid) {
            super(uid, FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp"));
        }

        public void setDateFilter(Calendar startDate, Calendar endDate) {
            liveData.setQuery(FirebaseDatabase.getInstance().getReference()
                    .child("wallet-entries").child(uid).child("default").orderByChild("timestamp")
                    .startAt(-endDate.getTimeInMillis()).endAt(-startDate.getTimeInMillis()));
        }
    }
}