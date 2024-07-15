package com.app.financiallocationapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financiallocationapp.Categories;
import com.app.financiallocationapp.Currency;
import com.app.financiallocationapp.EditBudgetEntryActivity;
import com.app.financiallocationapp.R;
import com.app.financiallocationapp.budget.ExpenseModel;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.databasehelper.ListDataSet;
import com.app.financiallocationapp.models.BudgetEntry;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.User;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseHolder> {


    private User user;
    private boolean firstUserSync = false;
    private final String uid;
    private final FragmentActivity fragmentActivity;
    private ListDataSet<BudgetEntry> budgetEntryListDataSet;




    public ExpenseAdapter(FragmentActivity fragmentActivity, String uid) {
        this.fragmentActivity = fragmentActivity;
        this.uid = uid;


        ProfileModel.getModel(uid,fragmentActivity).observe(fragmentActivity, new FirebaseObserver<Element<User>>() {
            @Override
            public void onChanged(Element<User> element) {
                if(!element.hasNoError()) return;

                ExpenseAdapter.this.user = element.getElement();

                if(!firstUserSync) {
                    ExpenseModel.getModel(uid, fragmentActivity).observe(fragmentActivity, new FirebaseObserver<Element<ListDataSet<BudgetEntry>>>() {
                        @Override
                        public void onChanged(Element<ListDataSet<BudgetEntry>> element) {
                            if(element.hasNoError()) {

                                budgetEntryListDataSet = element.getElement();

                                element.getElement().notifyRecycler(ExpenseAdapter.this);

                            }
                        }
                    });
                }
                notifyDataSetChanged();
                firstUserSync = true;
            }
        });

    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentActivity);
        View view = inflater.inflate(R.layout.expense_item, parent, false);
        return new ExpenseHolder(view);
    }

    @Override

    public void onBindViewHolder(ExpenseHolder holder, int position) {
        String id = budgetEntryListDataSet.getIDList().get(position);
        BudgetEntry budgetEntry = budgetEntryListDataSet.getList().get(position);



        Category category = Categories.searchCategory(user, budgetEntry.categoryID);
        holder.iconImageView.setImageResource(category.getIconResourceID());
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));
        holder.categoryTextView.setText(category.getCategoryVisibleName(fragmentActivity));
        holder.nameTextView.setText(budgetEntry.name);

        Date date = new Date(-budgetEntry.timestamp);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        holder.dateTextView.setText(dateFormat.format(date));

        holder.moneyTextView.setText(Currency.formatCurrency(user.currency, budgetEntry.balanceDifference));
        holder.moneyTextView.setTextColor(ContextCompat.getColor(fragmentActivity, budgetEntry.balanceDifference < 0 ? R.color.primary_text_expense : R.color.primary_text_income));

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createDeleteDialog(id, uid, budgetEntry.balanceDifference, fragmentActivity);
                return false;
            }
        });



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragmentActivity, EditBudgetEntryActivity.class);
                intent.putExtra("wallet-entry-id", id);
                intent.putExtra("lat", budgetEntry.lat);
                intent.putExtra("lng", budgetEntry.lng);

                fragmentActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (budgetEntryListDataSet == null) return 0;
        return budgetEntryListDataSet.getList().size();
    }


    //new function
    private void createDeleteDialog(String id, String uid, long balanceDifference, Context context) {
        new AlertDialog.Builder(context)
                .setMessage("Do you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(uid).child("default").child(id).removeValue();
                        user.budget.sum -= balanceDifference;
                        ProfileModel.saveModel(uid, user);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create().show();

    }

    public void setDateRange(Calendar calendarStart, Calendar calendarEnd) {
        ExpenseModel.getModel(uid, fragmentActivity).setDateFilter(calendarStart, calendarEnd);
    }


}