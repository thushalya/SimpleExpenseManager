package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager{
    private DataBaseHelper dataBaseHelper;

    public PersistentExpenseManager(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper =dataBaseHelper;
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/
//       DataBaseHelper dataBaseHelper =new DataBaseHelper(PersistentExpenseManager.this);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dataBaseHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dataBaseHelper);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
//    public static class DataBaseHelper extends SQLiteOpenHelper {
//        public static final String USER_ACCOUNT_TABLE = "UserAccount";
//        public static final String COLUMN_ACCOUNT_NO="Account_No";
//        public static final String COLUMN_BANK_NAME = "BankName";
//        public static final String COLUMN_ACCOUNT_HOLDER_NAME = "AccountHolderName";
//        public static final String COLUMN_BALANCE = "Balance";
//
//        public static final String SQL_CREATE_ENTRIES =
//                "CREATE TABLE " + USER_ACCOUNT_TABLE + " (" + COLUMN_ACCOUNT_NO + " INTEGER PRIMARY KEY," +
//                        COLUMN_ACCOUNT_HOLDER_NAME + " TEXT," + COLUMN_BANK_NAME + " TEXT," +
//                        COLUMN_BALANCE + " TEXT)";
//
//        public DataBaseHelper(@Nullable Context context) {
//            super(context, "expensemanager.db", null, 1);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase sqLiteDatabase) {
//            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
//
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//        }
//
//        public boolean addOne(Account account){
//            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
//            values.put(COLUMN_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
//            values.put(COLUMN_BANK_NAME,account.getBankName());
//            values.put(COLUMN_BALANCE,account.getBalance());
//
//            // Insert the new row, returning the primary key value of the new row
//            long newRowId = sqLiteDatabase.insert(USER_ACCOUNT_TABLE, null, values);
//
//            if (newRowId == -1){
//                return  false;
//            }
//            else
//                return true;
//        }
//
//
//
//
//    }
//


}
