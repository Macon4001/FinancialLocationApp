package com.app.financiallocationapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.LiveFolders;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.financiallocationapp.CalendarHelper;
import com.app.financiallocationapp.Categories;
import com.app.financiallocationapp.Currency;
import com.app.financiallocationapp.R;
import com.app.financiallocationapp.SettingsActivity;
import com.app.financiallocationapp.adapter.HighestCategoriesGraphAdapter;
import com.app.financiallocationapp.adapter.HighestCategoryGraphModel;
import com.app.financiallocationapp.budget.HighestBudgetEntriesGraphModel;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.databasehelper.ListDataSet;
import com.app.financiallocationapp.models.BudgetEntry;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GraphFragment extends Fragment {

    private Menu menu;
    private Calendar calendarStart;
    private Calendar calendarEnd;
    private User user;
    private List<BudgetEntry> walletEntryListDataSet;
    private PieChart pieChart;
    private ArrayList<HighestCategoryGraphModel> categoryModelsHome;
    private HighestCategoriesGraphAdapter adapter;
    private TextView dividerTextView;
    private ProgressBar incomesExpensesProgressBar;
    private TextView incomesTextView;
    private TextView expensesTextView;

    public static GraphFragment newInstance() {

        return new GraphFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        pieChart = view.findViewById(R.id.pie_chart);
        dividerTextView = view.findViewById(R.id.divider_textview);
        View incomesExpensesView = view.findViewById(R.id.incomes_expenses_view);
        incomesExpensesProgressBar = incomesExpensesView.findViewById(R.id.progress_bar);
        expensesTextView = incomesExpensesView.findViewById(R.id.expenses_textview);
        incomesTextView = incomesExpensesView.findViewById(R.id.incomes_textview);

        walletEntryListDataSet = new ArrayList<>();
        categoryModelsHome = new ArrayList<>();
        ListView favoriteListView = view.findViewById(R.id.favourite_categories_list_view);


        adapter = new HighestCategoriesGraphAdapter(categoryModelsHome, getActivity().getApplicationContext());
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

//        HighestBudgetEntriesGraphModel.getModel(FirebaseAuth.getInstance().getUid(), getActivity()).observe(this, new FirebaseObserver<Element<ListDataSet<BudgetEntry>>>() {
//
//            @Override
//            public void onChanged(Element<ListDataSet<BudgetEntry>> firebaseElement) {
//                if (firebaseElement.hasNoError()) {
//                    GraphFragment.this.walletEntryListDataSet = firebaseElement.getElement();
//                    Log.e("D", "onChanged: "+walletEntryListDataSet.getList() );
//                    dataUpdated();
//                }else{
//
//                    Toast.makeText(getActivity(), "SAD", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });


        ProfileModel.getModel(getTag(), getActivity()).observe(this, new FirebaseObserver<Element<User>>() {
            @Override
            public void onChanged(Element<User> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    GraphFragment.this.user = firebaseElement.getElement();

                    calendarStart = CalendarHelper.getUserPeriodStartDate(user);
                    calendarEnd = CalendarHelper.getUserPeriodEndDate(user);

                    updateCalendarIcon(false);
                    calendarUpdated();
                    dataUpdated();

                }
            }
        });

    }


    private void dataUpdated() {
        if (calendarStart != null && calendarEnd != null && walletEntryListDataSet != null) {
            //List<BudgetEntry> entryList = new ArrayList<>(walletEntryListDataSet.getList());
            List<BudgetEntry> entryList = walletEntryListDataSet;


            Log.e("ppp", "dataUpdated: +asdas"+walletEntryListDataSet.size() );
            Log.e("ppp", "dataUpdated: SAdas" );
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

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            ArrayList<Integer> pieColors = new ArrayList<>();

            for (Map.Entry<Category, Long> categoryModel : categoryModels.entrySet()) {
                float percentage = categoryModel.getValue() / (float) expensesSumInDateRange;
                final float minPercentageToShowLabelOnChart = 0.1f;
                categoryModelsHome.add(new HighestCategoryGraphModel(categoryModel.getKey(), categoryModel.getKey().getCategoryVisibleName(getContext()),
                        user.currency, categoryModel.getValue(), percentage));
                if (percentage > minPercentageToShowLabelOnChart) {
                    Drawable drawable = getContext().getDrawable(categoryModel.getKey().getIconResourceID());
                    drawable.setTint(Color.parseColor("#FFFFFF"));
                    pieEntries.add(new PieEntry(-categoryModel.getValue(), drawable));

                } else {
                    pieEntries.add(new PieEntry(-categoryModel.getValue()));
                }
                pieColors.add(categoryModel.getKey().getIconColor());
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setDrawValues(false);
            pieDataSet.setColors(pieColors);
            pieDataSet.setSliceSpace(2f);
            pieDataSet.setSelectionShift(9f);

            PieData data = new PieData(pieDataSet);
            pieChart.setData(data);
            pieChart.setTouchEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);

            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(ContextCompat.getColor(getContext(), R.color.backgroundPrimary));
            pieChart.setHoleRadius(0f);
            pieChart.setTransparentCircleRadius(30f);
            pieChart.setDrawCenterText(true);
            pieChart.setRotationAngle(270);
            pieChart.setRotationEnabled(false);
            pieChart.setHighlightPerTapEnabled(true);

            pieChart.invalidate();

            Collections.sort(categoryModelsHome, new Comparator<HighestCategoryGraphModel>() {
                @Override
                public int compare(HighestCategoryGraphModel o1, HighestCategoryGraphModel o2) {
                    return Long.compare(o1.getMoney(), o2.getMoney());
                }
            });


            adapter.notifyDataSetChanged();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");

            dividerTextView.setText("Date range: " + dateFormat.format(calendarStart.getTime())
                    + "  -  " + dateFormat.format(calendarEnd.getTime()));

            expensesTextView.setText(Currency.formatCurrency(user.currency, expensesSumInDateRange));
            incomesTextView.setText(Currency.formatCurrency(user.currency, incomesSumInDateRange));

            float progress = 100 * incomesSumInDateRange / (float) (incomesSumInDateRange - expensesSumInDateRange);
            incomesExpensesProgressBar.setProgress((int) progress);

            Log.e("dddddd", "dataUpdated: "+ categoryModelsHome.size());
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.graph_fragment_menu, menu);
        this.menu = menu;
        updateCalendarIcon(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void updateCalendarIcon(boolean updatedFromUI) {
        if (menu == null) return;
        MenuItem calendarIcon = menu.findItem(R.id.action_date_range);
        if (calendarIcon == null) return;
        if (updatedFromUI) {
            calendarIcon.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.icon_calendar_active));
        } else {
            calendarIcon.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.icon_calendar));
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date_range:
                showSelectDateRangeDialog();
                return true;
            case R.id.action_options:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSelectDateRangeDialog() {
        SmoothDateRangePickerFragment datePicker = SmoothDateRangePickerFragment.newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
            @Override
            public void onDateRangeSet(SmoothDateRangePickerFragment view, int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {
                calendarStart = Calendar.getInstance();
                calendarStart.set(yearStart, monthStart, dayStart);
                calendarStart.set(Calendar.HOUR_OF_DAY, 0);
                calendarStart.set(Calendar.MINUTE, 0);
                calendarStart.set(Calendar.SECOND, 0);

                calendarEnd = Calendar.getInstance();
                calendarEnd.set(yearEnd, monthEnd, dayEnd);
                calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarUpdated();
                updateCalendarIcon(true);
            }
        });
        datePicker.show(getActivity().getFragmentManager(), "TAG");
        //todo library doesn't respect other method than deprecated
    }


    private void calendarUpdated() {
        HighestBudgetEntriesGraphModel.getModel(getTag(), getActivity()).setDateFilter(calendarStart, calendarEnd);

    }


}
