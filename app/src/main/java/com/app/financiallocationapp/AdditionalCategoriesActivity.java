package com.app.financiallocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.app.financiallocationapp.adapter.AdditionalCategoriesAdapter;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.User;

import java.util.ArrayList;



public class AdditionalCategoriesActivity extends BaseActivity {

    private User user;

    private AdditionalCategoriesAdapter customCategoriesAdapter;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_categories);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Additional categories");

        ProfileModel.getModel(getUid(), this).observe(this, new FirebaseObserver<Element<User>>() {
            @Override
            public void onChanged(Element<User> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    AdditionalCategoriesActivity.this.user = firebaseElement.getElement();
                    dataUpdated();
                }
            }
        });

        ListView listView = findViewById(R.id.additional_categories_list_view);

        categories = new ArrayList<>();


        customCategoriesAdapter = new AdditionalCategoriesAdapter(this, categories, getApplicationContext());

        listView.setAdapter(customCategoriesAdapter);

        findViewById(R.id.add_addtional_category_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdditionalCategoriesActivity.this, AddAdditionalCategoryActivity.class));
            }
        });

    }

    private void dataUpdated() {
        if (user == null) return;
        categories.clear();
        categories.addAll(Categories.getCustomCategories(user));
        customCategoriesAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
