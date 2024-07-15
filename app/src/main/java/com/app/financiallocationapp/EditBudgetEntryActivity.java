package com.app.financiallocationapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.financiallocationapp.adapter.EntryCategoriesAdapter;
import com.app.financiallocationapp.budget.BudgetEntryModel;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.models.BudgetEntry;
import com.app.financiallocationapp.models.Category;
import com.app.financiallocationapp.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditBudgetEntryActivity extends BaseActivity {

    private Spinner selectCategorySpinner;
    private TextInputEditText selectNameEditText;
    private Calendar choosedDate;
    private TextInputEditText selectAmountEditText;
    private TextView chooseDayTextView;
    private TextView chooseTimeTextView;
    private User user;
    private BudgetEntry walletEntry;
    private Button removeEntryButton;
    private Button editEntryButton;
    private String walletId;
    private TextInputLayout selectAmountInputLayout;
    private TextInputLayout selectNameInputLayout;
    private Switch aSwitch;
    Button show_location;

    String lat,lng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit wallet entry");

        walletId = getIntent().getExtras().getString("wallet-entry-id");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");

        selectCategorySpinner = findViewById(R.id.select_category_spinner);
        selectNameEditText = findViewById(R.id.select_name_edittext);
        selectNameInputLayout = findViewById(R.id.select_name_inputlayout);
        editEntryButton = findViewById(R.id.edit_entry_button);
        removeEntryButton = findViewById(R.id.remove_entry_button);
        chooseTimeTextView = findViewById(R.id.choose_time_textview);
        chooseDayTextView = findViewById(R.id.choose_day_textview);
        selectAmountEditText = findViewById(R.id.select_amount_edittext);
        selectAmountInputLayout = findViewById(R.id.select_amount_inputlayout);
        aSwitch = findViewById(R.id.switchWidget);
        show_location = findViewById(R.id.show_location);
        choosedDate = Calendar.getInstance();





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



        editEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!aSwitch.isChecked()){
                    try {


                        //Expense

                        editWalletEntry(((0 * 2) - 1) * Currency.convertAmountStringToLong(selectAmountEditText.getText().toString()),
                                choosedDate.getTime(),
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

                        editWalletEntry(((1 * 2) - 1) * Currency.convertAmountStringToLong(selectAmountEditText.getText().toString()),
                                choosedDate.getTime(),
                                ((Category) selectCategorySpinner.getSelectedItem()).getCategoryID(),
                                selectNameEditText.getText().toString());
                    } catch (EmptyString e) {
                        selectNameInputLayout.setError(e.getMessage());
                    } catch (ZeroBalance e) {
                        selectAmountInputLayout.setError(e.getMessage());
                    }
                }




            }
        });

        removeEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveWalletEntryDialog();
            }

            public void showRemoveWalletEntryDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditBudgetEntryActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeWalletEntry();
                    }
                }).setNegativeButton("No", null).show();

            }
        });



        ProfileModel.getModel(getUid(), this).observe(this, new FirebaseObserver<Element<User>>() {
            @Override
            public void onChanged(Element<User> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    user = firebaseElement.getElement();
                    dataUpdated();
                }
            }
        });


        BudgetEntryModel.getModel(getUid(), walletId, this).observe(this, new FirebaseObserver<Element<BudgetEntry>>() {
            @Override
            public void onChanged(Element<BudgetEntry> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    walletEntry = firebaseElement.getElement();
                    dataUpdated();
                }
            }
        });


        show_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Maps3Activity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                startActivity(intent);

            }


        });


    }

    public void dataUpdated() {
        if (walletEntry == null || user == null) return;

        final List<Category> categories = Categories.getCategories(user);
        EntryCategoriesAdapter categoryAdapter = new EntryCategoriesAdapter(this,
                R.layout.new_entry_type, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCategorySpinner.setAdapter(categoryAdapter);

        Currency.setupAmountEditText(selectAmountEditText, user);
        choosedDate.setTimeInMillis(-walletEntry.timestamp);
        updateDate();
        selectNameEditText.setText(walletEntry.name);


        aSwitch.post(new Runnable() {
            @Override
            public void run() {
                if (walletEntry.balanceDifference < 0){
                    //selectTypeSpinner.setSelection(0);
                    aSwitch.setChecked(false);
                }
                else{ //selectTypeSpinner.setSelection(1);
                    aSwitch.setChecked(true);

                }
            }
        });

        selectCategorySpinner.post(new Runnable() {
            @Override
            public void run() {
                EntryCategoriesAdapter adapter = (EntryCategoriesAdapter) selectCategorySpinner.getAdapter();
                selectCategorySpinner.setSelection(adapter.getItemIndex(walletEntry.categoryID));
            }
        });


        long amount = Math.abs(walletEntry.balanceDifference);
        String current = Currency.formatCurrency(user.currency, amount);
        selectAmountEditText.setText(current);
        selectAmountEditText.setSelection(current.length() -
                (user.currency.left ? 0 : (user.currency.symbol.length() + (user.currency.space ? 1 : 0))));

    }


    private void updateDate() {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd");
        chooseDayTextView.setText(dataFormatter.format(choosedDate.getTime()));

        SimpleDateFormat dataFormatter2 = new SimpleDateFormat("HH:mm");
        chooseTimeTextView.setText(dataFormatter2.format(choosedDate.getTime()));
    }

    public void editWalletEntry(long balanceDifference, Date entryDate, String entryCategory, String entryName) throws EmptyString, ZeroBalance {
        if (balanceDifference == 0) {
            throw new ZeroBalance("Balance difference should not be 0");
        }

        if (entryName == null || entryName.length() == 0) {
            throw new EmptyString("Entry name length should be > 0");
        }

        long finalBalanceDifference = balanceDifference - walletEntry.balanceDifference;
        user.budget.sum += finalBalanceDifference;
        ProfileModel.saveModel(getUid(), user);

        FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(getUid())
                .child("default").child(walletId).setValue(new BudgetEntry(entryCategory, entryName, entryDate.getTime(), balanceDifference,String.valueOf("da"),String.valueOf("Ddd")));
        finish();
    }

    public void removeWalletEntry() {
        user.budget.sum -= walletEntry.balanceDifference;
        ProfileModel.saveModel(getUid(), user);

        FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(getUid())
                .child("default").child(walletId).removeValue();
        finish();
    }


    private void pickTime() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                choosedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                choosedDate.set(Calendar.MINUTE, minute);
                updateDate();

            }
        }, choosedDate.get(Calendar.HOUR_OF_DAY), choosedDate.get(Calendar.MINUTE), true).show();
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
                        choosedDate.set(year, monthOfYear, dayOfMonth);
                        updateDate();

                    }
                }, year, month, day).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

}
