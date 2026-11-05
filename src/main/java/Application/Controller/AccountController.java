package Application.Controller;

import model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import service.BankingServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private final BankingServiceImpl bankingService;

    // Spring Boot automatically injects the service
    public AccountController(BankingServiceImpl bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<String> getBalance(@PathVariable("accountNumber") String accountNumber) {
        Account account = bankingService.findAccount(accountNumber);

        if (account == null) {
            return ResponseEntity.status(404).body("Account not found: " + accountNumber);
        }

        // This is now real data from your database!
        return ResponseEntity.ok("R " + account.getBalance() + " for account " + account.getAccountNumber());
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
        Account fromAccount = bankingService.findAccount(transferRequest.getFromAccount());
        Account toAccount = bankingService.findAccount(transferRequest.getToAccount());

        if (fromAccount == null || toAccount == null) {
            return ResponseEntity.status(404).body("One or both accounts not found.");
        }

        boolean success = bankingService.transferMoney(fromAccount, toAccount, transferRequest.getAmount());

        if (success) {
            return ResponseEntity.ok("Successfully transferred R" + transferRequest.getAmount() + " to " + transferRequest.getToAccount());
        } else {
            return ResponseEntity.status(400).body("Transfer failed: Insufficient funds.");
        }
    }
}

class TransferRequest {
    private String fromAccount;
    private String toAccount;
    private double amount;

    // Getters and Setters
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}