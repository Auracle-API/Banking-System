package service;

import model.Account;
import model.User;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class BankingServiceImpl implements BankingServicesRepo{
    private final UserRepo userRepo;
    private final AccountRepo accountRepo;
    private final Object flag = new Object();
    private static int STRIKES = 3;
    private static long accountNumber = 000000000;

    @Autowired
    public BankingServiceImpl(AccountRepo accountRepo, UserRepo userRepo) {

        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }
    @Override
    public boolean register(User user) {
        if (userRepo.getUser(user.getUsername()) == null){
            throw new BankRepositoryException("User already exists");
        }

        userRepo.createUser(user);
        return true;
    }

    @Override
    public boolean login(String  username, String password) {
        while (STRIKES != 0) {
            if (username.equalsIgnoreCase(userRepo.getUser(username).getUsername()) && password.equalsIgnoreCase(userRepo.getUser(username).getPassword())) return true;
            STRIKES--;
        }
        return false;
    }

    @Override
    public Account createAccount(Account account, User user) {
        account.setAccountNumber("BOE"+accountNumber++);
        account.setIdNumber(user.getIdNumber());
       return accountRepo.createAccount(account);
    }

    @Override
    public boolean updatePassword(String oldPassword, String newPassword, User user) {
        if (oldPassword.equalsIgnoreCase(userRepo.getUser(user.getUsername()).getPassword())){
            user.setPassword(newPassword);
            userRepo.updateUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean withdrawMoney(Account account, double amount) {
        synchronized (flag) {
            if (account.getBalance() < amount) {
                return false;
            }
            double newBalance = account.getBalance() - amount;
            changeBalance(account, newBalance);
        }
        return true;
    }

    @Override
    public boolean depositMoney(Account account, double amount) {
        synchronized (flag) {
            double newBalance = account.getBalance() + amount;
            changeBalance(account, newBalance);
            return true;
        }
    }

    @Override
    public boolean transferMoney(Account fromAccount, Account toAccount, double amount) {
        if (fromAccount.getBalance() < amount) {
            return false;
        }
        double newBalance = fromAccount.getBalance() - amount;
        changeBalance(fromAccount, newBalance);
        double newBalance1 = toAccount.getBalance() + amount;
        changeBalance(toAccount, newBalance1);
        return true;
    }

    @Override
    public Account findAccount(String accountNumber) {
      return  accountRepo.findAccountByNumber(accountNumber).orElse(null);
    }

    private static boolean changeBalance(Account account, double newBalance) {
        String sql = """
                UPDATE account SET balance = ? WHERE account = ?;
                """;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank");
             PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setDouble(1, newBalance);
            ps.setInt(2, account.getAccount());

            if (ps.executeUpdate() != 0){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return  false;
    }
}
