package com.example.shikhar3_expbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewEntryExpense extends AppCompatActivity {
    private EditText entry_date;
    private EditText entry_name;
    private EditText entry_charge;
    private EditText entry_comment;

    private expenselog list = new expenselog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry_activity);

        // Initialize UI elements
        entry_date = (EditText) findViewById(R.id.entry_date);
        entry_name = (EditText) findViewById(R.id.entry_name);
        entry_charge = (EditText) findViewById(R.id.entry_charge);
        entry_comment = (EditText) findViewById(R.id.entry_comment);
        Button save_entry = (Button) findViewById(R.id.button_save_entry);

        // Set a click listener for the "Save" button
        save_entry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                // Extract data from EditText fields
                String new_date = entry_date.getText().toString();
                String new_name = entry_name.getText().toString();
                String new_chargeText = entry_charge.getText().toString();
                String new_comment = entry_comment.getText().toString();

                // Check if any of the fields are empty
                if (new_date.isEmpty() || new_name.isEmpty() || new_chargeText.isEmpty()) {
                    // Display a toast message if any field is empty
                    Toast.makeText(NewEntryExpense.this, "Please fill in required text fields of date, name, and charge", Toast.LENGTH_SHORT).show();
                } else if (isValidDate(new_date)){
                    try {
                        // Parse the charge as a float
                        Float new_charge = Float.parseFloat(new_chargeText);

                        // Create a new expense object
                        expense newExpense = new expense(new_date, new_name, new_charge, new_comment);
                        list.addExpense(newExpense);

                        // Create an intent to pass back the new expense data
                        //Citation: https://stackoverflow.com/questions/5343544/send-a-variable-between-classes-through-the-intent
                        Intent intent = new Intent();
                        intent.putExtra("newExpense", newExpense);

                        setResult(Activity.RESULT_OK, intent);

                        finish();
                    } catch (NumberFormatException e) {
                        // Handle the case where the charge cannot be parsed as a float
                        Toast.makeText(NewEntryExpense.this, "Invalid charge value", Toast.LENGTH_SHORT).show();
                    }

                    finish();

                }
                else{
                    // Show a toast message for invalid date format
                    Toast.makeText(NewEntryExpense.this, "Invalid date value", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Helper method to validate the date format (yyyy-MM)
    private boolean isValidDate(String inputDate) {
        // Validate that the inputDate matches the "yyyy-MM" format using a regular expression.
        String regex = "^\\d{4}-\\d{2}$";
        return !TextUtils.isEmpty(inputDate) && inputDate.matches(regex);
    }
}
