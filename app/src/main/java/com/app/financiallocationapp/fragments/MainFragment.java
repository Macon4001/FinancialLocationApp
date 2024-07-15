package com.app.financiallocationapp.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.financiallocationapp.CalendarHelper;
import com.app.financiallocationapp.Categories;
import com.app.financiallocationapp.Currency;
import com.app.financiallocationapp.R;
import com.app.financiallocationapp.SettingsActivity;
import com.app.financiallocationapp.adapter.MainCategoriesAdapter;
import com.app.financiallocationapp.adapter.MainCategoryListModel;
import com.app.financiallocationapp.budget.HighestBudgetEntriesModel;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.databasehelper.ListDataSet;
import com.app.financiallocationapp.lib.CircleGauge;
import com.app.financiallocationapp.models.BudgetEntry;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainFragment extends Fragment {
    private User user;
    private List<BudgetEntry> walletEntryListDataSet;

    private CircleGauge gauge;
    private MainCategoriesAdapter adapter;
    private ArrayList<MainCategoryListModel> categoryModelsHome;
    private TextView totalBalanceTextView;
    private TextView gaugeLeftBalanceTextView;
    private TextView gaugeLeftLine1TextView;
    private TextView gaugeLeftLine2TextView;
    private TextView gaugeRightBalanceTextView;
    private TextView gaugeRightLine1TextView;
    private TextView gaugeRightLine2TextView;
    private TextView gaugeBalanceLeftTextView;
    FirebaseRecyclerAdapter firebaseAdapter;
    ListView favoriteListView;
    public static MainFragment newInstance() {

        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        walletEntryListDataSet = new ArrayList<>();
        categoryModelsHome = new ArrayList<>();

        gauge = view.findViewById(R.id.gauge);
        gauge.setValue(50);

        totalBalanceTextView = view.findViewById(R.id.total_balance_textview);
        gaugeLeftBalanceTextView = view.findViewById(R.id.gauge_left_balance_text_view);
        gaugeLeftLine1TextView = view.findViewById(R.id.gauge_left_line1_textview);
        gaugeLeftLine2TextView = view.findViewById(R.id.gauge_left_line2_textview);
        gaugeRightBalanceTextView = view.findViewById(R.id.gauge_right_balance_text_view);
        gaugeRightLine1TextView = view.findViewById(R.id.gauge_right_line1_textview);
        gaugeRightLine2TextView = view.findViewById(R.id.gauge_right_line2_textview);
        gaugeBalanceLeftTextView = view.findViewById(R.id.left_balance_textview);


        favoriteListView = view.findViewById(R.id.favourite_categories_list_view);
//        favoriteListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MainCategoriesAdapter(categoryModelsHome, getActivity().getApplicationContext());
        favoriteListView.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("default")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                            BudgetEntry object = dataSnap.getValue(BudgetEntry.class);

                            walletEntryListDataSet.add(object);

                            // Use your object as needed
                        }
                        dataUpdated();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//        HighestBudgetEntriesModel.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), getActivity()).observe(this, new FirebaseObserver<Element<ListDataSet<BudgetEntry>>>() {
//
//            @Override
//            public void onChanged(Element<ListDataSet<BudgetEntry>> firebaseElement) {
//                if (firebaseElement.hasNoError()) {
//                    MainFragment.this.walletEntryListDataSet = firebaseElement.getElement();
//                    dataUpdated();
//                }
//            }
//        });




        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                MainFragment.this.user = snapshot.getValue(User.class);
                dataUpdated();

                Calendar startDate = CalendarHelper.getUserPeriodStartDate(user);
                Calendar endDate = CalendarHelper.getUserPeriodEndDate(user);

                // Log.e("dede", "onChanged: ", startDate.getTimeInMillis());


                HighestBudgetEntriesModel.getModel(getTag(), getActivity()).setDateFilter(startDate, endDate);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        ProfileModel.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), (FragmentActivity) getActivity()).observe(this, new FirebaseObserver<Element<User>>() {
//            @Override
//            public void onChanged(Element<User> firebaseElement) {
//                if (firebaseElement.hasNoError()) {
//                    MainFragment.this.user = firebaseElement.getElement();
//                    dataUpdated();
//
//                    Calendar startDate = CalendarHelper.getUserPeriodStartDate(user);
//                    Calendar endDate = CalendarHelper.getUserPeriodEndDate(user);
//
//                   // Log.e("dede", "onChanged: ", startDate.getTimeInMillis());
//
//
//                    HighestBudgetEntriesModel.getModel(getTag(), getActivity()).setDateFilter(startDate, endDate);
//
//                }
//            }
//        });

        //setupRecyclerView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_options:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dataUpdated() {
        if (user == null || walletEntryListDataSet == null) return;

        List<BudgetEntry> entryList = walletEntryListDataSet;

        Calendar startDate = CalendarHelper.getUserPeriodStartDate(user);
        Calendar endDate = CalendarHelper.getUserPeriodEndDate(user);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM");


        long expensesSumInDateRange = 0;
        long incomesSumInDateRange = 0;

        HashMap<Category, Long> categoryModels = new HashMap<>();
        for (BudgetEntry walletEntry : entryList) {
            if (walletEntry.balanceDifference > 0) {
                incomesSumInDateRange += walletEntry.balanceDifference;
                continue;
            }
            expensesSumInDateRange += walletEntry.balanceDifference;
            Category category = Categories.searchCategory(user, walletEntry.categoryID);
            if (categoryModels.get(category) != null)
                categoryModels.put(category, categoryModels.get(category) + walletEntry.balanceDifference);
            else
                categoryModels.put(category, walletEntry.balanceDifference);

        }

        categoryModelsHome.clear();
        for (Map.Entry<Category, Long> categoryModel : categoryModels.entrySet()) {
            categoryModelsHome.add(new MainCategoryListModel(categoryModel.getKey(), categoryModel.getKey().getCategoryVisibleName(getContext()),
                    user.currency, categoryModel.getValue()));
        }

        Collections.sort(categoryModelsHome, new Comparator<MainCategoryListModel>() {
            @Override
            public int compare(MainCategoryListModel o1, MainCategoryListModel o2) {
                return Long.compare(o1.getMoney(), o2.getMoney());
            }
        });


       // adapter.notifyDataSetChanged();
        totalBalanceTextView.setText(Currency.formatCurrency(user.currency, user.budget.sum));


            gaugeLeftBalanceTextView.setText(Currency.formatCurrency(user.currency, incomesSumInDateRange));
            gaugeLeftLine1TextView.setText("Incomes");
            gaugeLeftLine2TextView.setVisibility(View.INVISIBLE);
            gaugeRightBalanceTextView.setText(Currency.formatCurrency(user.currency, expensesSumInDateRange));
            gaugeRightLine1TextView.setText("Expenses");
            gaugeRightLine2TextView.setVisibility(View.INVISIBLE);

            gauge.setPointStartColor(ContextCompat.getColor(getContext(), R.color.gauge_income));
            gauge.setPointEndColor(ContextCompat.getColor(getContext(), R.color.gauge_income));
            gauge.setStrokeColor(ContextCompat.getColor(getContext(), R.color.gauge_expense));
            if (incomesSumInDateRange - expensesSumInDateRange != 0)
                gauge.setValue((int) (incomesSumInDateRange * 100 / (double) (incomesSumInDateRange - expensesSumInDateRange)));

            gaugeBalanceLeftTextView.setText(dateFormat.format(startDate.getTime()) + "          -          " + dateFormat.format(endDate.getTime()));

    }



    public void setupRecyclerView(){

        //query

        String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(UserId).child("default");
        com.google.firebase.database.Query firebaseSearchQuery = databaseReference;



        //firebase recyclerview
        FirebaseRecyclerOptions<BudgetEntry> options =
                new FirebaseRecyclerOptions.Builder<BudgetEntry>()
                        .setQuery(firebaseSearchQuery, BudgetEntry.class)
                        .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<BudgetEntry, DiaryViewHolder>(options) {
            @NonNull
            @Override
            public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                // attach layout to RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);

                return new DiaryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(DiaryViewHolder holder, int position, BudgetEntry model) {


                Category category = Categories.searchCategory(user, model.categoryID);
                holder.iconImageView.setImageResource(category.getIconResourceID());
                holder.categoryTextView.setText(category.getCategoryVisibleName(getActivity()));


                holder.moneyTextView.setText(Currency.formatCurrency(user.currency, model.getBalanceDifference()));
                holder.moneyTextView.setTextColor(ContextCompat.getColor(getActivity(), model.getBalanceDifference() < 0 ? R.color.primary_text_expense : R.color.primary_text_income));



            }
        };


        firebaseAdapter.startListening();
        // add adapter to recyclerview

       // favoriteListView.setAdapter(firebaseAdapter);
    }


    public static class DiaryViewHolder extends RecyclerView.ViewHolder{
        final ImageView iconImageView;
        public View view;
        final TextView moneyTextView;
        final TextView categoryTextView;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            moneyTextView = itemView.findViewById(R.id.item_sum);
            categoryTextView = itemView.findViewById(R.id.item_category);
            iconImageView = itemView.findViewById(R.id.icon_imageview);

        }

    }


}
