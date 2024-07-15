package com.app.financiallocationapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.financiallocationapp.adapter.EntryCategoriesAdapter;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.models.BudgetEntry;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class AddBudgetEntryActivity extends AppCompatActivity {

    private Spinner selectCategorySpinner;
    private TextInputEditText selectNameEditText;
    private Calendar chosenDate;
    private TextInputEditText selectAmountEditText;
    private Button chooseDayTextView;
    private Button chooseTimeTextView;
    private Switch aSwitch;

    private User user;
    private TextInputLayout selectAmountInputLayout;
    private TextInputLayout selectNameInputLayout;


    FirebaseAuth auth;

    double lat,lng;
    String geohashStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Budget entry");


        if(getIntent().getStringExtra("geoHash")!=null){
            geohashStr = getIntent().getStringExtra("geoHash");
            lat = getIntent().getDoubleExtra("lat",0.0);
            lng = getIntent().getDoubleExtra("lng",0.0);

        }

        auth= FirebaseAuth.getInstance();


        selectCategorySpinner = findViewById(R.id.select_category_spinner);
        selectNameEditText = findViewById(R.id.select_name_edittext);
        selectNameInputLayout = findViewById(R.id.select_name_inputlayout);
        Button addEntryButton = findViewById(R.id.add_entry_button);
        chooseTimeTextView = findViewById(R.id.choose_time_textview);
        chooseDayTextView = findViewById(R.id.choose_day_textview);
        selectAmountEditText = findViewById(R.id.select_amount_edittext);
        selectAmountInputLayout = findViewById(R.id.select_amount_inputlayout);
        selectAmountInputLayout = findViewById(R.id.select_amount_inputlayout);
        aSwitch = findViewById(R.id.switchWidget);

        chosenDate = Calendar.getInstance();

        ProfileModel.getModel(auth.getCurrentUser().getUid(), this).observe(this, new FirebaseObserver<Element<User>>() {
            @Override
            public void onChanged(Element<User> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    user = firebaseElement.getElement();

                    dateUpdated();


                }
            }
        });



        updateDate();


        chooseDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        chooseTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });



        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(lat!=0.0){

                    if(!aSwitch.isChecked()){
                        try {


                            //Expense

                            addToWallet(((0 * 2) - 1) * Currency.convertAmountStringToLong(selectAmountEditText.getText().toString()),
                                    chosenDate.getTime(),
                                    ((Category) selectCategorySpinner.getSelectedItem()).getCategoryID(),
                                    selectNameEditText.getText().toString());
                        } catch (EmptyString e) {
                            selectNameInputLayout.setError(e.getMessage());
                        } catch (ZeroBalance e) {
                            selectAmountInputLayout.setError(e.getMessage());
                        }
                    }else{
                        try {



                            //Income

                            addToWallet(((1 * 2) - 1) * Currency.convertAmountStringToLong(selectAmountEditText.getText().toString()),
                                    chosenDate.getTime(),
                                    ((Category) selectCategorySpinner.getSelectedItem()).getCategoryID(),
                                    selectNameEditText.getText().toString());
                        } catch (EmptyString e) {
                            selectNameInputLayout.setError(e.getMessage());
                        } catch (ZeroBalance e) {
                            selectAmountInputLayout.setError(e.getMessage());
                        }
                    }
                }else{
                    Toast.makeText(AddBudgetEntryActivity.this, "Please select locoation", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }



    private void dateUpdated() {
        if (user == null) return;

        final List<Category> categories = Categories.getCategories(user);
        EntryCategoriesAdapter categoryAdapter = new EntryCategoriesAdapter(this,
                R.layout.new_entry_type, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCategorySpinner.setAdapter(categoryAdapter);

        Currency.setupAmountEditText(selectAmountEditText, user);

    }


    private void updateDate() {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd");
        chooseDayTextView.setText(dataFormatter.format(chosenDate.getTime()));

        SimpleDateFormat dataFormatter2 = new SimpleDateFormat("HH:mm");
        chooseTimeTextView.setText(dataFormatter2.format(chosenDate.getTime()));
    }

    public void addToWallet(long balanceDifference, Date entryDate, String entryCategory, String entryName) throws ZeroBalance, EmptyString {
        if (balanceDifference == 0) {
            throw new ZeroBalance("Balance difference should not be 0");
        }

        if (entryName == null || entryName.length() == 0) {
            throw new EmptyString("Entry name length should be > 0");
        }

        FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(auth.getUid())
                .child("default").push().setValue(new BudgetEntry(entryCategory, entryName, entryDate.getTime(), balanceDifference,String.valueOf(lat),String.valueOf(lng)));
        user.budget.sum += balanceDifference;
        ProfileModel.saveModel(auth.getUid(), user);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void pickTime() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                chosenDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                chosenDate.set(Calendar.MINUTE, minute);
                updateDate();

            }
        }, chosenDate.get(Calendar.HOUR_OF_DAY), chosenDate.get(Calendar.MINUTE), true).show();
    }

    private void pickDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        chosenDate.set(year, monthOfYear, dayOfMonth);
                        updateDate();

                    }
                }, year, month, day).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }


}
