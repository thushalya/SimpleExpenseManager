package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentAccountDAO implements AccountDAO {
    private DataBaseHelper dataBaseHelper;

    public PersistentAccountDAO(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper=dataBaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return new ArrayList<>(dataBaseHelper.getAccountNumbersList());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(dataBaseHelper.getAccountsList());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (dataBaseHelper.checkAccount(accountNo)) {
            return dataBaseHelper.getAccount(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        
        dataBaseHelper.addOne(account);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!dataBaseHelper.deleteOne(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        //accounts.remove(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!dataBaseHelper.checkAccount(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = dataBaseHelper.getAccount(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        dataBaseHelper.updateBalance(account);
    }
}
