package com.app.financiallocationapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.models.BudgetEntryCategory;
import com.app.financiallocationapp.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;


public class AddAdditionalCategoryActivity extends BaseActivity {

    private TextInputEditText selectNameEditText;
    private Button addCustomCategoryButton;
    private User user;
    private ImageView iconImageView;
    private int selectedColor = Color.parseColor("#000000");
    private TextInputLayout selectNameInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_addiotional_category);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add additional category");

        ProfileModel.getModel(getUid(), this).observe(this, new FirebaseObserver<Element<User>>() {
            @Override
            public void onChanged(Element<User> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    AddAdditionalCategoryActivity.this.user = firebaseElement.getElement();
                    dataUpdated();
                }
            }
        });


    }

    private void dataUpdated() {
        if (user == null) return;

        selectNameEditText = findViewById(R.id.select_name_edittext);
        selectNameInputLayout = findViewById(R.id.select_name_inputlayout);

        addCustomCategoryButton = findViewById(R.id.add_custom_category_button);
        addCustomCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addCustomCategory(selectNameEditText.getText().toString(), "#FF9800" );
                } catch (EmptyString e) {
                    selectNameInputLayout.setError(e.getMessage());
                }


            }
        });
    }

    private void addCustomCategory(String categoryName, String categoryHtmlCode) throws EmptyString {
        if(categoryName == null || categoryName.length() == 0)
            throw new EmptyString("Entry name length should be > 0");

        FirebaseDatabase.getInstance().getReference()
                .child("users").child(getUid()).child("customCategories").push().setValue(
                new BudgetEntryCategory(categoryName,  categoryHtmlCode));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
