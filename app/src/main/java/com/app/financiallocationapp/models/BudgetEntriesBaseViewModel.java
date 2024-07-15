package com.app.financiallocationapp.models;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.databasehelper.ListDataSet;
import com.app.financiallocationapp.databasehelper.QueryLiveDataSet;
import com.google.firebase.database.Query;



public class BudgetEntriesBaseViewModel extends ViewModel {
    protected final QueryLiveDataSet<BudgetEntry> liveData;
    protected final String uid;

    public BudgetEntriesBaseViewModel(String uid, Query query) {
        this.uid=uid;
        liveData = new QueryLiveDataSet<>(BudgetEntry.class, query);
    }

    public void observe(LifecycleOwner owner, FirebaseObserver<Element<ListDataSet<BudgetEntry>>> observer) {
        observer.onChanged(liveData.getValue());
        liveData.observe(owner, new Observer<Element<ListDataSet<BudgetEntry>>>() {
            @Override
            public void onChanged(@Nullable Element<ListDataSet<BudgetEntry>> element) {
                if(element != null) observer.onChanged(element);
            }
        });
    }

    public void removeObserver(Observer<Element<ListDataSet<BudgetEntry>>> observer) {
        liveData.removeObserver(observer);
    }


}
