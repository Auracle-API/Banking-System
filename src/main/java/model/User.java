package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private List<Account> accounts;
    private String idNumber;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String surname;
    private String username;
}
