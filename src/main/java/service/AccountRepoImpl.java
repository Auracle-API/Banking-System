package service;

import Application.Controller.DBConfig;
import model.Account;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepoImpl implements AccountRepo {

 private final int accNumber = 0000;
    @Override
    public Account createAccount(Account account) {
        String sql = """
                Insert into account values (?, ?, ?, ?);
                """;

        try (Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
             PreparedStatement ps = con.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getAccountName());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getIdNumber());
            ps.setString(4, account.getAccountType());

            if (ps.executeUpdate() <= 0) {
                throw new BankRepositoryException("Error creating account");
            }
            return account;
        }catch (SQLException e){
            throw new BankRepositoryException("Cannot connect to database: " + e.getMessage());
        }
    }

    @Override
    public Optional<Account> findAccountByNumber(String accountNumber) {
        String sql = """
                select * from account where account_number = ?;
                """;

        try (Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
            PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setString(1, accountNumber);

            ResultSet rs =  ps.executeQuery();
            if (rs.next()){
                return Optional.of(build(accountNumber));
            }
            throw new BankRepositoryException("Account not found");
        } catch (SQLException e){
            throw new BankRepositoryException("Cannot connect to database: " + e.getMessage());
        }
    }

    @Override
    public List<Account> findAllAccounts(String idNumber) {
        List<Account> accounts = new ArrayList<>();
        String sql = """
                select * from account where user_id = ?;
                """;

        try(Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ){
            ps.setString(1, idNumber);

            while (rs.next()){
                accounts.add(build(rs.getString("account_number")));
            }

            return accounts;
        } catch (SQLException e){
            throw new BankRepositoryException("Cannot connect to database: " + e.getMessage());
        }
    }

    @Override
    public void updateName(String accountName) {
        String sql = """
                update account set account_handler = ? where account_handler = ?;
                """;

        try (Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
            PreparedStatement ps = con.prepareStatement(sql))
        {
            if (ps.executeUpdate() == 0) {
                throw new BankRepositoryException("Database connected! but there were no rows affected");
            }
        }catch (SQLException e){
            throw new BankRepositoryException("Cannot connect to database: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteAccount(Account account) {
        String sql = """
                delete from account where account_number = ?;
                """;

        try (Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
        PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, account.getAccountNumber());
            if (ps.executeUpdate() == 0) {
                throw new BankRepositoryException("Database connected! but there were no rows affected");
            }
            return true;
        } catch (SQLException e){
            throw new BankRepositoryException("Could not connect to the database: " + e.getMessage());
        }
    }

    //private helper method for making an account
    private static Account build(String accountNumber) {
        try (Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
            PreparedStatement ps = con.prepareStatement("select * from account where account_number = ?;")
        ){
            ps.setString(1, accountNumber);
            try (ResultSet rs =  ps.executeQuery()){
                if (rs.next()){
                    return new Account(rs.getInt("account"),
                                        rs.getString("account_handler"),
                                        rs.getDouble("balance"),
                                        rs.getString("account_number"),
                                        rs.getString("account_type"),
                                        rs.getString("id_number"));
                }
                throw new BankRepositoryException("Could not find account with account_number: " + accountNumber);
            }

        }catch (SQLException e){
            throw new BankRepositoryException("Account couldn't be created: " + e.getMessage());
        }
    }
}
