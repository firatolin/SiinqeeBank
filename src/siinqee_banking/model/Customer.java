package siinqee_banking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String address;
    private String password;
    private String accountNumber;
    private double balance;
    private String accountType;
    private LocalDate openingDate;
    private List<Transaction> transactions;

    // New constructor with all fields
    public Customer(String firstName, String lastName, String gender, String phone,
                    String address, String password, String accountNumber, double balance,
                    String accountType, LocalDate openingDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.openingDate = openingDate;
        this.transactions = new ArrayList<>();

        // Add initial transaction for opening balance
        addTransaction("ACCOUNT_OPENING", "Account opened", balance, LocalDateTime.now());
    }

    // Old constructor for backward compatibility
    public Customer(String username, String password, double balance) {
        this.firstName = username;
        this.lastName = "";
        this.gender = "";
        this.phone = "";
        this.address = "";
        this.password = password;
        this.accountNumber = "s" + (100000000 + (int)(Math.random() * 900000000));
        this.balance = balance;
        this.accountType = "Regular";
        this.openingDate = LocalDate.now();
        this.transactions = new ArrayList<>();

        // Add initial transaction for opening balance
        addTransaction("ACCOUNT_OPENING", "Account opened", balance, LocalDateTime.now());
    }

    // Banking methods
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            addTransaction("DEPOSIT", "Cash deposit", amount, LocalDateTime.now());
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            addTransaction("WITHDRAWAL", "Cash withdrawal", -amount, LocalDateTime.now());
            return true;
        }
        return false;
    }

    public boolean transfer(Customer recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            recipient.deposit(amount);

            // Add transaction for sender
            addTransaction("TRANSFER_OUT", "Transfer to " + recipient.getAccountNumber(), -amount, LocalDateTime.now());

            // Add transaction for recipient
            recipient.addTransaction("TRANSFER_IN", "Transfer from " + this.getAccountNumber(), amount, LocalDateTime.now());

            return true;
        }
        return false;
    }

    public boolean transfer(Customer recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            recipient.deposit(amount);

            // Add transaction for sender
            this.addTransaction("TRANSFER_OUT", "Transfer to " + recipient.getAccountNumber(), -amount, LocalDateTime.now());

            // Add transaction for recipient
            recipient.addTransaction("TRANSFER_IN", "Transfer from " + this.getAccountNumber(), amount, LocalDateTime.now());

            return true;
        }
        return false;
    }

    // Transaction management
    public void addTransaction(String type, String description, double amount, LocalDateTime timestamp) {
        Transaction transaction = new Transaction(type, description, amount, timestamp);
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Return a copy to prevent external modification
    }

    public List<Transaction> getRecentTransactions(int count) {
        int startIndex = Math.max(0, transactions.size() - count);
        return new ArrayList<>(transactions.subList(startIndex, transactions.size()));
    }

    // Getters and setters for all fields
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }

    // For backward compatibility - get username (uses first name)
    public String getUsername() {
        return firstName;
    }

    // For display purposes
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Utility methods
    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                '}';
    }

    // Method to check if password matches
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}