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
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class EditExpense extends AppCompatActivity{
    private EditText entry_date;
    private EditText entry_name;
    private EditText entry_charge;
    private EditText entry_comment;

    private expenselog list = new expenselog();

    private int myYear, myMonth;

    private  String buffer, month_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry_activity);

        Intent intent = getIntent();
        final int entry_pos = intent.getIntExtra("entry_pos", -1);

        entry_date = (EditText) findViewById(R.id.entry_date);
        entry_name = (EditText) findViewById(R.id.entry_name);
        entry_charge = (EditText) findViewById(R.id.entry_charge);
        entry_comment = (EditText) findViewById(R.id.entry_comment);
        Button save_entry = findViewById(R.id.button_save_entry);

        if (entry_pos >= 0 && entry_pos < list.size()) {
            expense current_expense = list.getExpense(entry_pos);
            // Populate the EditText fields with the existing expense data
            entry_name.setText(current_expense.getName());
            entry_charge.setText(String.valueOf(current_expense.getCharge()));
            entry_comment.setText(current_expense.getComment());
            entry_date.setText(current_expense.getDate() );
        } else {
            // Handle the case where entry_pos is invalid (e.g., show an error message).
            System.out.println("Out of bounds");
        }





        save_entry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Extract edited expense data from the EditText fields
                String new_date = entry_date.getText().toString();
                String new_name = entry_name.getText().toString();
                String chargeText = entry_charge.getText().toString();
                String new_comment = entry_comment.getText().toString();

                // Check if any of the fields are empty
                if (new_date.isEmpty() || new_name.isEmpty() || chargeText.isEmpty()) {
                    // Show a toast message to fill all fields
                    Toast.makeText(EditExpense.this, "Please fill in required text fields of date, name and charge", Toast.LENGTH_SHORT).show();
                } else if(isValidDate(new_date)){
                    try {
                        // All fields are filled, proceed to create and send the intent
                        Float new_charge = Float.parseFloat(chargeText);
                        // Create an updated expense object
                        expense updatedExpense = new expense(new_date, new_name, new_charge, new_comment);

                        // Pass the updated expense data back to the MainActivity
                        list.removeExpense(entry_pos);
                        list.addExpense(updatedExpense, entry_pos);
                        // Citation: https://stackoverflow.com/questions/5343544/send-a-variable-between-classes-through-the-intent
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedExpense", updatedExpense);
                        resultIntent.putExtra("entry_pos", entry_pos); // Include the position of the edited expense
                        setResult(RESULT_OK, resultIntent);
                        // Finish the EditExpense activity
                    }catch (NumberFormatException e) {
                        // Handle the case where the charge cannot be parsed as a float
                        //From Assignment 0
                        Toast.makeText(EditExpense.this, "Invalid charge value", Toast.LENGTH_SHORT).show();
                    }


                    finish();

                }
                else{
                    Toast.makeText(EditExpense.this, "Invalid date value", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    //Citation: https://www.javatpoint.com/java-regex
    private boolean isValidDate(String inputDate) {
        // Validate that the inputDate matches the "yyyy-MM" format using a regular expression.
        String regex = "^\\d{4}-\\d{2}$";
        return !TextUtils.isEmpty(inputDate) && inputDate.matches(regex);
    }
}
