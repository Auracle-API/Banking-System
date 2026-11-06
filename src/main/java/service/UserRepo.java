package service;

import model.User;

import java.util.List;


public interface UserRepo {
    User createUser(User user);
    //find
    User getUser(String username);
    List<User> getAllUsers();
    //update
    void updateUser(User user);
    //delete
    boolean deleteUser(String username);
}
