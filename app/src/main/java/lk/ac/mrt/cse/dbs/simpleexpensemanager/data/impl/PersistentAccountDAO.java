package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseHelper;

public class PersistentAccountDAO implements AccountDAO {
    private final DataBaseHelper dataBaseHelper;

    public PersistentAccountDAO(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return (dataBaseHelper.getAccountNumbersList());
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
        this.getAccount(accountNo);
        dataBaseHelper.deleteOne(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!dataBaseHelper.checkAccount(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = dataBaseHelper.getAccount(accountNo);
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
