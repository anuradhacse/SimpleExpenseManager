package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Anuradha Niroshan on 03/12/2015.
 */
public class SQLiteAccountDAO extends SQLiteOpenHelper implements AccountDAO{

    // Database Name
    private static final String DATABASE_NAME = "130227F";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Account";
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK = "bank";
    public static final String BANK_HOLDER = "holder";
    public static final String INITIAL_BALANCE = "initial_balance";


    public SQLiteAccountDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accoutNumberList = new ArrayList<>();
        String selectQuery = "SELECT "+ACCOUNT_NO+" FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                accoutNumberList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // return accoutNumtList list
        db.close();
        return accoutNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        String selectQuery = "SELECT "+"*"+" FROM "+TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account=new Account(null,null,null,0);
                account.setAccountNo(cursor.getString(0));
                account.setBankName(cursor.getString(1));
                account.setAccountHolderName(cursor.getString(2));
                account.setBalance(cursor.getDouble(3));
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        // return accountList list
        db.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String selectQuery = "SELECT "+BANK_HOLDER+" FROM "+TABLE_NAME+" WHERE "+ACCOUNT_NO+"="+accountNo;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account=new Account(null,null,null,0);
                account.setAccountNo(cursor.getString(0));
                account.setBankName(cursor.getString(1));
                account.setAccountHolderName(cursor.getString(2));
                account.setBalance(cursor.getDouble(3));
                db.close();
                return account;
            } while (cursor.moveToNext());
        }
        return null;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, account.getAccountNo());
        values.put(BANK, account.getBankName());
        values.put(BANK_HOLDER, account.getAccountHolderName());
        values.put(INITIAL_BALANCE, account.getBalance());
        db.insert(TABLE_NAME, null, values);
        values.clear();
        db.close(); // Closing database connection
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ACCOUNT_NO + " = ?",
                new String[] { String.valueOf(accountNo) });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String MYdatabase = "CREATE TABLE " +TABLE_NAME+ " ("
                + ACCOUNT_NO + " TEXT PRIMARY KEY, " +BANK+ " TEXT, "
                +BANK_HOLDER+ " TEXT," +INITIAL_BALANCE+" DOUBLE"+ ");";
        db.execSQL(MYdatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
