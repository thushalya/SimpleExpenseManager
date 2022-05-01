package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class TransactionTable extends SQLiteOpenHelper {
    public static final String USER_ACCOUNT_TABLE = "UserAccount";
    public static final String COLUMN_ID="ID";
    public static final String COLUMN_ACCOUNT_NO="Account_No";
    public static final String COLUMN_BANK_NAME = "BankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "AccountHolderName";
    public static final String COLUMN_BALANCE = "Balance";


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + USER_ACCOUNT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY," +COLUMN_ACCOUNT_NO + " TEXT," +
                    COLUMN_ACCOUNT_HOLDER_NAME + " TEXT," + COLUMN_BANK_NAME + " TEXT," +
                    COLUMN_BALANCE + " FLOAT)";


    public TransactionTable(@Nullable Context context) {
        super(context, "expensemanager.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
