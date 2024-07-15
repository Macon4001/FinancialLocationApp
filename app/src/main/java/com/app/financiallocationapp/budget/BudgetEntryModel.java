package com.app.financiallocationapp.budget;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.app.financiallocationapp.models.BudgetEntryBaseViewModel;


public class BudgetEntryModel implements ViewModelProvider.Factory {
    private final String entryId;
    private final String uid;

    private BudgetEntryModel(String uid, String entryId) {
        this.uid = uid;
        this.entryId = entryId;

    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new BudgetEntryBaseViewModel(uid, entryId);
    }

    public static BudgetEntryBaseViewModel getModel(String uid, String entryId, FragmentActivity activity) {
        return ViewModelProviders.of(activity, new BudgetEntryModel(uid, entryId)).get(BudgetEntryBaseViewModel.class);
    }


}