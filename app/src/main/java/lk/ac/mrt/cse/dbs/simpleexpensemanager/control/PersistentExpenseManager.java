package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseHelper;

public class PersistentExpenseManager extends ExpenseManager {
    private final DataBaseHelper dataBaseHelper;

    public PersistentExpenseManager(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/
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

}
