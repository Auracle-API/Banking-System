package model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Account {
    private int account;
    private String accountName;
    private double balance;
    private String accountNumber;
    private String accountType;
    private String idNumber;
}
