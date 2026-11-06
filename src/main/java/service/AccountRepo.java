package service;

import model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepo {

    Account createAccount(Account account);
    //find account by the user's name
    Optional<Account> findAccountByNumber(String Number);
    List<Account> findAllAccounts(String idNumber);

    //update the account
    void updateName(String accountName);

    //delete the account
    boolean deleteAccount(Account account);

    
}
