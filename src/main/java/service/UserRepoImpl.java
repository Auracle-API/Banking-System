package service;

import Application.Controller.DBConfig;
import model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo
{
    @Override
    public User createUser(User user) {
        String sql = """
                insert into user values (?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (
             Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getIdNumber());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getName());
            ps.setString(7, user.getSurname());

            if (ps.executeUpdate() <= 0) {
                throw new BankRepositoryException("Could not create the user in the database.");
            }
            return user;
            //The most of experienced when work
        } catch (SQLException e){
            throw new BankRepositoryException("Can't connect to database");
        }
    }

    @Override
    public User getUser(String idNumber) {
        String sql  = """
                select * from user where id_number = ?;
        """;

        try (
             Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, idNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return buildUser(idNumber);
            }
            throw new BankRepositoryException("User not found");

        } catch (SQLException e){
            throw new BankRepositoryException("Can't connect to database: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String sql = """
                select * from user;
                """;

        try (
             Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ){
            while(rs.next()){
                list.add(buildUser(rs.getString("id_number")));
            }
            throw new BankRepositoryException("Users not found");

        } catch (SQLException e){
            throw new BankRepositoryException("The database could not be accessed.: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) {
        String  sql = """
                update user set password = ?, name = ?, surname = ?, email = ?, phone_number = ?, username = ? where id_number = ?;
        """;

        try (
             Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
             PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getSurname());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getUsername());
            ps.setString(7, user.getIdNumber());

            if (ps.executeUpdate() <= 0) {
                throw new BankRepositoryException("Could not update the user in the database.");
            }

        } catch (SQLException e){
            throw new BankRepositoryException("Can't connect to database: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteUser(String idNumber) {
        String sql = """
                delete from user where id_number = ?;
        """;

        try (
             Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
             PreparedStatement ps = con.prepareStatement(sql)
        ){
          if (ps.executeUpdate() == 0){
              throw new BankRepositoryException("Could not delete the user in the database.");
          }
          return true;
        } catch (SQLException e){
            throw new BankRepositoryException("Can't connect to database: " + e.getMessage());
        }
    }

    //private method to make a user object
    public static User buildUser(String idNumber){
        String sql = """
                select * from user where id_number = ?;
                """;

        try (
             Connection con = DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUsername(), DBConfig.getPassword());
             PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ){
            if(rs.next()){
                return new User(rs.getInt("user_id"),
                                null,
//                                new AccountRepoImpl().findAllAccounts(idNumber),
                                idNumber,
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("phone_number"),
                                rs.getString("surname"),
                                rs.getString("username")
                );
            }
            throw new BankRepositoryException("Could not create a user.");
        } catch (SQLException e){
            throw new BankRepositoryException("Can't connect to database: " + e.getMessage());
        }
    }
}
