package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

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
    public static final String USER_ID ="ID";
    public static final String USER_ACCOUNT_NO ="AccountNo";
    public static final String USER_BANK_NAME = "BankName";
    public static final String USER_ACCOUNT_HOLDER_NAME = "AccountHolderName";
    public static final String USER_BALANCE = "Balance";

    public static final String TRANSACTION_TABLE = "TransactionTable";
    public static final String TRANSACTION_ID = "ID";
    public static final String TRANSACTION_DATE ="Date";
    public static final String TRANSACTION_ACCOUNT_NO = "AccountNo";
    public static final String TRANSACTION_EXPENSE_TYPE = "ExpenseType";
    public static final String TRANSACTION_AMOUNT = "Amount";

    public static final String SQL_CREATE_USER_ACCOUNT_ENTRIES =
//            "CREATE TABLE " + USER_TABLE + " (" + USER_ID + " INT PRIMARY KEY,"   + USER_BANK_NAME + " TEXT," +
//                    USER_ACCOUNT_HOLDER_NAME + " TEXT," + USER_BANK_NAME + " TEXT," +
//                    USER_BALANCE + " FLOAT)";
            String.format("CREATE TABLE %s(%s text PRIMARY KEY, %s text, %s text, %s real);",
                    USER_TABLE, USER_ACCOUNT_NO,
                    USER_BANK_NAME,
                    USER_ACCOUNT_HOLDER_NAME,
                    USER_BALANCE);


    //DATE
    //FLOAT
    //AUTOINCREMENT

    public static final String SQL_CREATE_TRANSACTION_ENTRIES =
            "CREATE TABLE " + TRANSACTION_TABLE + " (" + TRANSACTION_ID + " INT PRIMARY KEY ," + TRANSACTION_DATE + " TIMESTAMP," +
                    TRANSACTION_ACCOUNT_NO + " TEXT," + TRANSACTION_EXPENSE_TYPE + " TEXT," +
                    TRANSACTION_AMOUNT + " REAL)";

    public static final String SQL_DROP_USER_ENTRIES =
       "DROP TABLE IF EXISTS " + USER_TABLE;
    public static final String SQL_DROP_TRANSACTION_ENTRIES =
            "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "expensemanager.db", null, 1);
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
    public boolean logTransaction(Transaction transaction){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //DATE,ENUM
        values.put(TRANSACTION_DATE,transaction.getDate().getTime());
        values.put(TRANSACTION_ACCOUNT_NO,transaction.getAccountNo());
        values.put(TRANSACTION_EXPENSE_TYPE,transaction.getExpenseType().name());
        values.put(TRANSACTION_AMOUNT,transaction.getAmount());

        long newRowId = sqLiteDatabase.insert(TRANSACTION_TABLE, null, values);

        if (newRowId == -1){
            return  false;
        }
        else
            return true;
    }


    public List <Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionLogsList =new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString,null);

        while(cursor.moveToNext());{

            long time = cursor.getLong(cursor.getColumnIndexOrThrow(TRANSACTION_DATE));
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_ACCOUNT_NO));
            String expenseType =cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_EXPENSE_TYPE));
            double amount = cursor.getDouble( cursor.getColumnIndexOrThrow(TRANSACTION_AMOUNT));

            Transaction newtransaction = new Transaction(new Date(time),accountNO,ExpenseType.valueOf(expenseType),amount);
            transactionLogsList.add(newtransaction);
        }

        cursor.close();
        sqLiteDatabase.close();
        return transactionLogsList;
    }


    //USER ACCOUNT TABLE

    public boolean addOne(Account account){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ACCOUNT_NO, account.getAccountNo());
        values.put(USER_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(USER_BANK_NAME,account.getBankName());
        values.put(USER_BALANCE,account.getBalance());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = sqLiteDatabase.insert(USER_TABLE, null, values);

        if (newRowId == -1){
            return  false;
        }
        else
            return true;
    }

    public List <Account> getAccountsList(){
        List<Account> accountList =new ArrayList<>();

        String queryString = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do{ int columnID =cursor.getInt(0);
                String accountNO = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                String bankName =cursor.getString(3);
                double balance = cursor.getDouble(4);

                Account newAccount = new Account(accountNO,accountHolderName,bankName,balance);
                accountList.add(newAccount);

            }
            while(cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }

    public ArrayList getAccountNumbersList(){
        ArrayList accountList =new ArrayList<>();

        String queryString = "SELECT "+ USER_ACCOUNT_NO +" FROM " + USER_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString,null);


        while(cursor.moveToNext());{
                String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_NO));
                accountList.add(accountNO);
        }

        cursor.close();
        sqLiteDatabase.close();
        return accountList;
    }

    public Account getAccount(String accountNo){

        String selection = USER_ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE,null,selection,selectionArgs,null,null,null,null);

        if (cursor.moveToFirst()){
            String accountNO = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_NO));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(USER_ACCOUNT_HOLDER_NAME));
            String bankName =cursor.getString(cursor.getColumnIndexOrThrow(USER_BANK_NAME));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(USER_BALANCE));
            cursor.close();
            sqLiteDatabase.close();
            return new Account(accountNO,accountHolderName,bankName,balance);
        }
        else {
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

    public boolean deleteOne (String accountNO){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String  queryString ="DELETE FROM "+ USER_TABLE +" WHERE "+ USER_ACCOUNT_NO +" = " + accountNO;
        Cursor cursor =sqLiteDatabase.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }
        else {
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }

    }

    public void updateBalance(Account account){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_BALANCE, account.getBalance());

        sqLiteDatabase.update(USER_TABLE, values,
                USER_ACCOUNT_NO + " = ?", new String[]{account.getAccountNo()});


    }







}
