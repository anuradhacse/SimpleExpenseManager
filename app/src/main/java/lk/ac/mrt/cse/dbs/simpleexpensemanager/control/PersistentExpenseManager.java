package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.MyContext;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Anuradha Niroshan on 03/12/2015.
 */
public class PersistentExpenseManager extends  ExpenseManager {

    public PersistentExpenseManager() throws ExpenseManagerException {
        setup();
    }


    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO sqlite_transactions = new SQLiteTransactionDAO();
        setTransactionsDAO(sqlite_transactions);

        AccountDAO sqlite_account = new SQLiteAccountDAO();
        setAccountsDAO(sqlite_account);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);
    }
}
