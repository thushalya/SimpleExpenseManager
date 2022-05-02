package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DataBaseHelper;

public class PersistentTransactionDAO implements TransactionDAO {
    private DataBaseHelper dataBaseHelper;

   // private final List<Transaction> transactions;

    public PersistentTransactionDAO(DataBaseHelper dataBaseHelper) {

        //transactions = new LinkedList<>();
        this.dataBaseHelper=dataBaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dataBaseHelper.logTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        return dataBaseHelper.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactions=dataBaseHelper.getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }

}
