package service;

import model.Account;
import model.User;

interface BankingServicesRepo {
    //User security handling
    boolean register(User user);
    boolean login(String username, String password);
    Account createAccount(Account account, User user);
    boolean updatePassword(String oldPassword, String newPassword, User user);
    //operations of the program
    boolean withdrawMoney(Account account, double amount);
    boolean depositMoney(Account account, double amount);
    boolean transferMoney(Account fromAccount, Account toAccount, double amount);
    Account findAccount(String accountNumber);


}