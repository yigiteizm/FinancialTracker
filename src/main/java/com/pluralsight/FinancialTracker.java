package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Scanner;

import static java.time.temporal.TemporalAdjuster.*;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {

        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] parts = input.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double price = Double.parseDouble(parts[4]);

                transactions.add(new Transaction(date, time, description, vendor, price));

            }

        } catch (Exception e) {

                System.err.println("Related file does not exist.");
                e.printStackTrace();

            }
        }





    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.


        System.out.println("Please enter the date of the deposit according to format(yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Please enter the time of the deposit according to format(HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Please enter the description of the deposit:");
        String description = scanner.nextLine();

        System.out.println("Please enter the vendor of the deposit:");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount of the deposit:");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Invalid input:Deposit amount must be greater than zero. ");
            return;
        }
        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(transaction);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();

            System.out.println("Thank you for deposit of $" + amount);
        } catch (Exception e) {
            System.out.println("An error occurred while processing the transaction: ");
        }


    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number than transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        System.out.println("Please enter the date of the payment according to format(yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Please enter the time of the payment according to format(HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Please enter the description of the payment:");
        String description = scanner.nextLine();

        System.out.println("Please enter the vendor of the payment:");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount of the payment:");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("In order to make a payment, the amount must be positive. Please try again.");
            return;
        }
        amount = -Math.abs(amount);
        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(transaction);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();

            System.out.println("Thank you for deposit of $" + amount);
        } catch (Exception e) {
            System.out.println("An error occurred while processing the transaction: ");
        }

    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {

        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("list of your All Transactions");
        System.out.println("date | time | description | vendor | amount");

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }

    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.

        System.out.println("List of your Deposit Transactions:");
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction listOfDeposit : transactions) {
            if (listOfDeposit.getAmount()>0 ){
                System.out.println(listOfDeposit.toString());
            }

        }


    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.

        System.out.println("List of your Payment Transaction:");
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction listOfPayments : transactions){
            if ((listOfPayments.getAmount()<0)){
                System.out.println(listOfPayments.toString());
            }

        }


    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();
            LocalDate currentDay = LocalDate.now();
            switch (input) {
                case "1":

                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.

                    LocalDate firstDayOfMonth = currentDay.with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate lastDayOfMonth = currentDay;
                    filterTransactionsByDate(firstDayOfMonth,lastDayOfMonth);

                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate firstDayOfPreviousMonth = currentDay.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate lastDayOfPreviousMonth = currentDay.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                    filterTransactionsByDate(firstDayOfPreviousMonth,lastDayOfPreviousMonth);


                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.


                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.

        boolean found = false;
        System.out.println("Date | Time | Description | Vendor | Amount");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Result has not been found.");
        }
    }


    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.


        boolean found = false;
        System.out.println("Date | Time | Description | Vendor | Amount");

        for (Transaction transaction : transactions) {

            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions were found for the selected vendor.");
        }
    }
    }
