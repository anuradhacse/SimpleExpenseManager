package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHandling.DBHandle;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Anuradha Niroshan on 03/12/2015.
 */
public class SQLiteTransactionDAO implements TransactionDAO {



    public  SQLiteTransactionDAO(){

    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = null;

        try {
            db = DBHandle.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBHandle.TRANSACTION_ACCOUNT_NO, accountNo);
            values.put(DBHandle.TRANSACTION_DATE, DBHandle.getTimeAsString(date));
            switch (expenseType) {
                case EXPENSE:
                    values.put(DBHandle.TRANSACTION_TYPE, "E");
                    break;
                case INCOME:
                    values.put(DBHandle.TRANSACTION_TYPE, "I");
                    break;
            }
            values.put(DBHandle.TRANSACTION_AMOUNT, amount);

            db.insert(DBHandle.TRANSACTION_TABLE, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBHandle.getWritableDatabase();

            cursor = db.query(DBHandle.TRANSACTION_TABLE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {

                    Transaction transaction = new Transaction(DBHandle.getTimeAsValue(cursor.getString(2)), cursor.getString(1), null, Double.parseDouble(cursor.getString(4)));
                    switch (cursor.getString(3)) {
                        case "E":
                            transaction.setExpenseType(ExpenseType.EXPENSE);
                            break;
                        case "I":
                            transaction.setExpenseType(ExpenseType.INCOME);
                            break;
                    }
                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBHandle.getWritableDatabase();

            cursor = db.query(DBHandle.TRANSACTION_TABLE, null, null, null, null, null, DBHandle.TRANSACTION_ID + " DESC", String.valueOf(limit));
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction(DBHandle.getTimeAsValue(cursor.getString(2)), cursor.getString(1), null, Double.parseDouble(cursor.getString(4)));
                    switch (cursor.getString(3)) {
                        case "E":
                            transaction.setExpenseType(ExpenseType.EXPENSE);
                            break;
                        case "I":
                            transaction.setExpenseType(ExpenseType.INCOME);
                            break;
                    }
                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return transactionsList;
    }

}
