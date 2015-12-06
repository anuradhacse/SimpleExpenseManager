package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHandling.DBHandle;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Anuradha Niroshan on 03/12/2015.
 */
public class SQLiteAccountDAO  implements AccountDAO{


    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> accountNumbers = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBHandle.getWritableDatabase();

            cursor = db.query(DBHandle.ACCOUNT_TABLE, new String[]{DBHandle.ACCOUNT_NO}, null, null, null, null, null); // Get account numbers from the database
            if (cursor.moveToFirst()) {
                do {

                    accountNumbers.add(cursor.getString(0));
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
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accountList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBHandle.getWritableDatabase();

            cursor = db.query(DBHandle.ACCOUNT_TABLE, null, null, null, null, null, null);// Get all accounts from the database
            if (cursor.moveToFirst()) {
                do {

                    Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)));
                    accountList.add(account);
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
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBHandle.getWritableDatabase();

            cursor = db.query(DBHandle.ACCOUNT_TABLE, null, DBHandle.ACCOUNT_NO + " = ?", new String[]{accountNo}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                return new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)));

            } else {
                String msg = "Not a valid account ";
                throw new InvalidAccountException(msg);
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
        return null;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = null;

        try {
            db = DBHandle.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBHandle.ACCOUNT_NO, account.getAccountNo()); // account number
            values.put(DBHandle.BANK_NAME, account.getBankName()); // bank name
            values.put(DBHandle.HOLDER_NAME, account.getAccountHolderName()); // holder name
            values.put(DBHandle.BALANCE, account.getBalance()); // initial balance

            // Inserting Row
            db.insert(DBHandle.ACCOUNT_TABLE, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=null;
        try{
            db = DBHandle.getWritableDatabase();
            db.delete(DBHandle.ACCOUNT_TABLE, DBHandle.ACCOUNT_NO + " = ?",
                    new String[]{String.valueOf(accountNo)});
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db=null;
        Account account=getAccount(accountNo);
        db = DBHandle.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        switch (expenseType){
            case EXPENSE:
                contentValues.put(DBHandle.BALANCE,account.getBalance()-amount);
                break;
            case INCOME:
                contentValues.put(DBHandle.BALANCE,account.getBalance()+amount);
                break;
        }
        db.update(DBHandle.ACCOUNT_TABLE,contentValues,DBHandle.ACCOUNT_NO+"= ?",new String[]{accountNo});
    }


}
