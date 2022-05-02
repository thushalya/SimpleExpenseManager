package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "UserAccount";
    public static final String USER_ACCOUNT_NO = "AccountNo";
    public static final String USER_BANK_NAME = "BankName";
    public static final String USER_ACCOUNT_HOLDER_NAME = "AccountHolderName";
    public static final String USER_BALANCE = "Balance";

    public static final String TRANSACTION_TABLE = "TransactionTable";
    public static final String TRANSACTION_ID = "ID";
    public static final String TRANSACTION_DATE = "Date";
    public static final String TRANSACTION_ACCOUNT_NO = "AccountNo";
    public static final String TRANSACTION_EXPENSE_TYPE = "ExpenseType";
    public static final String TRANSACTION_AMOUNT = "Amount";


    public static final String SQL_CREATE_USER_ACCOUNT_ENTRIES =
            "CREATE TABLE " + USER_TABLE + " (" + USER_ACCOUNT_NO + " TEXT PRIMARY KEY," + USER_BANK_NAME + " TEXT," +
                    USER_ACCOUNT_HOLDER_NAME + " TEXT," + USER_BALANCE + " TEXT)";

    public static final String SQL_CREATE_TRANSACTION_ENTRIES =
            "CREATE TABLE " + TRANSACTION_TABLE + " (" + TRANSACTION_ID + " INT PRIMARY KEY ," + TRANSACTION_DATE + " TIMESTAMP," +
                    TRANSACTION_ACCOUNT_NO + " TEXT," + TRANSACTION_EXPENSE_TYPE + " TEXT," +
                    TRANSACTION_AMOUNT + " TEXT," + " FOREIGN KEY" + " (" + USER_ACCOUNT_NO + ")" + " REFERENCES " + USER_TABLE + "(" + USER_ACCOUNT_NO + ")" + ")";

    public static final String SQL_DROP_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + USER_TABLE;
    public static final String SQL_DROP_TRANSACTION_ENTRIES =
            "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "190676J", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_USER_ACCOUNT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_TRANSACTION_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_USER_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DROP_TRANSACTION_ENTRIES);
        onCreate(sqLiteDatabase);

    }
    ///////////////////////////////////////////////////////////////////////////////
    //TRANSACTION TABLE FUNCTIONS

    public void logTransaction(Transaction transaction) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANSACTION_DATE, transaction.getDate().getTime());
        values.put(TRANSACTION_ACCOUNT_NO, transaction.getAccountNo());
        values.put(TRANSACTION_EXPENSE_TYPE, transaction.getExpenseType().name());
        values.put(TRANSACTION_AMOUNT, transaction.getAmount());
        sqLiteDatabase.insert(TRANSACTION_TABLE, null, values);
    }


    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionLogsList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        while (cursor.moveToNext()) {

            long time = cursor.getLong(cursor.getColumnIndexOrThrow(TRANSACTION_DATE));
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_ACCOUNT_NO));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_EXPENSE_TYPE));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_AMOUNT));

            Transaction newtransaction = new Transaction(new Date(time), accountNO, ExpenseType.valueOf(expenseType), Double.parseDouble(amount));
            transactionLogsList.add(newtransaction);

        }
        cursor.close();
        sqLiteDatabase.close();
        return transactionLogsList;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TRANSACTION_TABLE, null, null, null, null, null, null, Integer.toString(limit));
        List<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            long time = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TRANSACTION_DATE));
            String accountNo = cursor.getString(
                    cursor.getColumnIndexOrThrow(TRANSACTION_ACCOUNT_NO));
            String expenseType = cursor.getString(
                    cursor.getColumnIndexOrThrow(TRANSACTION_EXPENSE_TYPE));
            String amount = cursor.getString(
                    cursor.getColumnIndexOrThrow(TRANSACTION_AMOUNT));
            transactions.add(new Transaction(new Date(time), accountNo, ExpenseType.valueOf(expenseType), Double.parseDouble(amount)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return transactions;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //USER ACCOUNT TABLE FUNCTIONS

    public void addOne(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ACCOUNT_NO, account.getAccountNo());
        values.put(USER_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(USER_BANK_NAME, account.getBankName());
        values.put(USER_BALANCE, account.getBalance());

        sqLiteDatabase.insert(USER_TABLE, null, values);


    }

    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_NO));
                String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_HOLDER_NAME));
                String bankName = cursor.getString(cursor.getColumnIndexOrThrow(USER_BANK_NAME));
                String balance = cursor.getString(cursor.getColumnIndexOrThrow(USER_BALANCE));

                Account newAccount = new Account(accountNO, accountHolderName, bankName, Double.parseDouble(balance));
                accountList.add(newAccount);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }

    public ArrayList<String> getAccountNumbersList() {
        ArrayList<String> accountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, new String[]{USER_ACCOUNT_NO}, null, null, null, null, null, null);


        while (cursor.moveToNext()) {
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_NO));
            accountList.add(accountNO);
        }

        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }

    public Account getAccount(String accountNo) {

        String selection = USER_ACCOUNT_NO + " = ?";
        String[] selectionArgs = {accountNo};

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_NO));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_HOLDER_NAME));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow(USER_BANK_NAME));
            String balance = cursor.getString(cursor.getColumnIndexOrThrow(USER_BALANCE));
            cursor.close();
            sqLiteDatabase.close();
            return new Account(accountNO, accountHolderName, bankName, Double.parseDouble(balance));
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return null;
        }

    }

    public Boolean checkAccount(String accountNo) {

        String selection = USER_ACCOUNT_NO + " = ?";
        String[] selectionArgs = {accountNo};

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        } else {
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
    }

    public void deleteOne(String accountNO) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(USER_TABLE, USER_ACCOUNT_NO + " = ? ", new String[]{accountNO});

    }

    public void updateBalance(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_BALANCE, account.getBalance());

        sqLiteDatabase.update(USER_TABLE, values, USER_ACCOUNT_NO + " = ?", new String[]{account.getAccountNo()});

    }

}
