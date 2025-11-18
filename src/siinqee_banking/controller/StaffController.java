package siinqee_banking.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class StaffController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalCustomersLabel;
    @FXML private Label totalBalanceLabel;
    @FXML private TextField customerUsernameField;
    @FXML private TextField depositAmountField;
    @FXML private TextField withdrawAmountField;
    @FXML private TextField newCustomerUsernameField;
    @FXML private TextField newCustomerPasswordField;
    @FXML private TextField initialBalanceField;

    // Simple customer list for staff
    private List<Customer> customers = new ArrayList<>() {{
        add(new Customer("user1", "123", 10000));
        add(new Customer("user2", "456", 5000));
    }};

    @FXML
    public void initialize() {
        updateStats();
        welcomeLabel.setText("Welcome, Staff Member");
    }

    private void updateStats() {
        totalCustomersLabel.setText(String.valueOf(customers.size()));

        double totalBalance = customers.stream()
                .mapToDouble(Customer::getBalance)
                .sum();
        totalBalanceLabel.setText(String.format("ETB%.2f", totalBalance));
    }

    @FXML
    private void handleDeposit() {
        String username = customerUsernameField.getText().trim();
        String amountText = depositAmountField.getText().trim();

        if (username.isEmpty() || amountText.isEmpty()) {
            showAlert("Error", "Please enter customer username and amount.");
            return;
        }

        Customer customer = findCustomer(username);
        if (customer == null) {
            showAlert("Error", "Customer not found: " + username);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Error", "Amount must be positive.");
                return;
            }

            customer.deposit(amount);
            showAlert("Success",
                    String.format("ETB%.2f deposited to %s's account!\nNew Balance: ETB%.2f",
                            amount, customer.getUsername(), customer.getBalance()));

            depositAmountField.clear();
            updateStats();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.");
        }
    }

    @FXML
    private void handleWithdraw() {
        String username = customerUsernameField.getText().trim();
        String amountText = withdrawAmountField.getText().trim();

        if (username.isEmpty() || amountText.isEmpty()) {
            showAlert("Error", "Please enter customer username and amount.");
            return;
        }

        Customer customer = findCustomer(username);
        if (customer == null) {
            showAlert("Error", "Customer not found: " + username);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (customer.withdraw(amount)) {
                showAlert("Success",
                        String.format("ETB%.2f withdrawn from %s's account!\nRemaining Balance: ETB%.2f",
                                amount, customer.getUsername(), customer.getBalance()));

                withdrawAmountField.clear();
                updateStats();
            } else {
                showAlert("Error", "Insufficient funds or invalid amount.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.");
        }
    }

    @FXML
    private void handleAddCustomer() {
        String username = newCustomerUsernameField.getText().trim();
        String password = newCustomerPasswordField.getText().trim();
        String balanceText = initialBalanceField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || balanceText.isEmpty()) {
            showAlert("Error", "Please fill all customer details.");
            return;
        }

        if (findCustomer(username) != null) {
            showAlert("Error", "Customer username already exists.");
            return;
        }

        try {
            double initialBalance = Double.parseDouble(balanceText);
            if (initialBalance < 0) {
                showAlert("Error", "Initial balance cannot be negative.");
                return;
            }

            Customer newCustomer = new Customer(username, password, initialBalance);
            customers.add(newCustomer);

            showAlert("Success",
                    String.format("New customer '%s' added successfully!\nInitial Balance: ETB%.2f",
                            username, initialBalance));

            newCustomerUsernameField.clear();
            newCustomerPasswordField.clear();
            initialBalanceField.clear();
            updateStats();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid balance amount.");
        }
    }

    @FXML
    private void handleViewCustomers() {
        StringBuilder customerList = new StringBuilder();
        customerList.append("=== CUSTOMER LIST ===\n\n");

        for (Customer customer : customers) {
            customerList.append(String.format("Username: %s\nBalance: ETB%.2f\n\n",
                    customer.getUsername(), customer.getBalance()));
        }

        showAlert("Customer List", customerList.toString());
    }

    @FXML
    private void handleViewTransactions() {
        String username = customerUsernameField.getText().trim();

        if (username.isEmpty()) {
            showAlert("Error", "Please enter customer username to view transactions.");
            return;
        }

        Customer customer = findCustomer(username);
        if (customer == null) {
            showAlert("Error", "Customer not found: " + username);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/transactions_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Transactions - " + customer.getUsername());

            TransactionsController controller = loader.getController();
            controller.setCustomer(customer);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load transactions window.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sinqe Bank Login");
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Customer findCustomer(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}