package com.app.financiallocationapp.models;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;


import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.databasehelper.QueryLiveDataElement;
import com.google.firebase.database.FirebaseDatabase;



public class BudgetEntryBaseViewModel extends ViewModel {
    protected final QueryLiveDataElement<BudgetEntry> liveData;
    protected final String uid;

    public BudgetEntryBaseViewModel(String uid, String walletEntryId) {
        this.uid=uid;
        liveData = new QueryLiveDataElement<>(BudgetEntry.class, FirebaseDatabase.getInstance().getReference()
                .child("wallet-entries").child(uid).child("default").child(walletEntryId));    }

    public void observe(LifecycleOwner owner, FirebaseObserver<Element<BudgetEntry>> observer) {
        if(liveData.getValue() != null) observer.onChanged(liveData.getValue());
        liveData.observe(owner, new Observer<Element<BudgetEntry>>() {
            @Override
            public void onChanged(@Nullable Element<BudgetEntry> element) {
                if(element != null) observer.onChanged(element);
            }
        });
    }

    public void removeObserver(Observer<Element<BudgetEntry>> observer) {
        liveData.removeObserver(observer);
    }


}
