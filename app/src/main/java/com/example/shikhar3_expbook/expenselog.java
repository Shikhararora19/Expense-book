package com.example.shikhar3_expbook;

import java.util.ArrayList;

public class expenselog {
    private static ArrayList<expense> expenses = new ArrayList<expense>();

    // Constructor for the expenselog class
    public expenselog(){

    }

    // Getter method to retrieve the list of expenses
    public static ArrayList<expense> getExpenses(){
        return expenses;
    }

    // Setter method to set a new list of expenses
    public static void setExpenses(ArrayList<expense> newExpenses) {
        expenses = newExpenses;
    }

    // Get an expense object at a specific index in the list
    public expense getExpense(int index) {
        return expenses.get(index);
    }

    // Add an expense to the end of the list
    public void addExpense(expense expense) {
        expenses.add(expense);
    }

    // Add an expense at a specific index in the list
    public void addExpense(expense expense, int index) {
        expenses.add(index, expense);
    }

    // Remove an expense from the list at a specific index
    public void removeExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
        } else {
            System.out.println("Out of Bounds");
        }
    }

    // Get the number of expenses in the list
    public int size() {
        return expenses.size();
    }
}
