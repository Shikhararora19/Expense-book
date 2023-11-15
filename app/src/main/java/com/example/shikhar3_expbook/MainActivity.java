package com.example.shikhar3_expbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView log_list;
    private TextView total;

    private ArrayAdapter<expense> expense_adapter;

    private ArrayList<expense> dataList;
    private float totalCost;
    private ActivityResultLauncher<Intent> launchNewEntryExpenseActivity;
    private ActivityResultLauncher<Intent> launchEditExpenseActivity;
    private int selected_position = -1;
    private expenselog list = new expenselog();

    private static final int REQUEST_NEW_EXPENSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        Button addExpenseButton = findViewById(R.id.btnAddExpense);
        log_list = (ListView) findViewById(R.id.log_list);
        total = (TextView) findViewById(R.id.total);

        // Initialize data list and adapter for expenses
        dataList = new ArrayList<>();
        expense_adapter = new ArrayAdapter<>(this, R.layout.view_expense, R.id.expenseTextView, dataList);
        log_list.setAdapter(expense_adapter);

        // Set a click listener to handle selecting expenses
        log_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // Store the selected position for future reference
                selected_position = position;
            }
        });

        // Handle adding new expenses
        //Took from ListyCity Lab code
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the NewEntryExpense activity using the ActivityResultLauncher
                Intent intent = new Intent(MainActivity.this, NewEntryExpense.class);
                launchNewEntryExpenseActivity.launch(intent);
            }
        });

        // Handle editing expenses
        Button editExpenseButton = findViewById(R.id.edit_button);
        editExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditExpense.class);
                intent.putExtra("entry_pos", selected_position);
                selected_position = -1;
                launchEditExpenseActivity.launch(intent);
            }
        });

        // Handle deleting expenses
        Button deleteExpenseButton = findViewById(R.id.delete_expense);
        deleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_position >= 0 && selected_position < dataList.size()) {
                    // Update the total cost and remove the selected expense
                    totalCost = totalCost - dataList.get(selected_position).getCharge();
                    total.setText(String.format("$ %.2f", totalCost));
                    list.removeExpense(selected_position);
                    dataList.remove(selected_position);
                    selected_position = -1;
                    expense_adapter.notifyDataSetChanged();
                }
            }
        });
        //Launch methods inspired by:https://developer.android.com/training/basics/intents/result#java
        // and https://stackoverflow.com/questions/68405703/how-to-use-registerforactivityresult-correctly-getting-lifecycleowners-must-ca


        // Initialize ActivityResultLaunchers for handling activity results
        launchNewEntryExpenseActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                // Handle the result of adding a new expense
                                expense newExpense = data.getParcelableExtra("newExpense");
                                dataList.add(newExpense);
                                totalCost = totalCost + newExpense.getCharge();
                                total.setText(String.format("$ %.2f", totalCost));
                                expense_adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        launchEditExpenseActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                // Handle the result of editing an expense
                                int position = data.getIntExtra("entry_pos", -1);
                                expense updatedExpense = data.getParcelableExtra("updatedExpense");
                                totalCost = totalCost + updatedExpense.getCharge() - dataList.get(position).getCharge();
                                total.setText(String.format("$ %.2f", totalCost));

                                if (position != -1) {
                                    // Update the expense in the data list and refresh the adapter
                                    dataList.set(position, updatedExpense);
                                    expense_adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }
}
