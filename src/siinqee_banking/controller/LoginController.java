package siinqee_banking.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Shared customer list for all controllers
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        try {
            // Admin login
            if (username.equals("admin") && password.equals("admin")) {
                loadAdminDashboard();
                return;
            }

            // Customer login
            Customer authenticatedCustomer = null;
            for (Customer customer : allCustomers) {
                System.out.println("Checking: " + customer.getFirstName() + " with password: " + customer.getPassword());
                if (customer.getFirstName().equalsIgnoreCase(username) &&
                        customer.getPassword().equals(password)) {
                    authenticatedCustomer = customer;
                    break;
                }
            }

            if (authenticatedCustomer != null) {
                System.out.println("Login successful for: " + authenticatedCustomer.getFullName());
                loadCustomerDashboard(authenticatedCustomer);
            } else {
                System.out.println("Login failed for username: " + username);
                showAlert("Login Failed", "Invalid username or password!");
                passwordField.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load dashboard. Please try again: " + e.getMessage());
        }
    }

    // This method should be called when the application starts to initialize customer data
    public void initializeCustomerData() {
        // Only initialize if empty (to avoid duplicates when returning from admin)
        if (allCustomers.isEmpty()) {
            // Initialize with some sample customers using the simple constructor
            allCustomers.addAll(
                    new Customer("John", "john123", 10000.0),
                    new Customer("Alice", "alice123", 5000.0),
                    new Customer("Bob", "bob123", 7500.0),
                    new Customer("Emma", "emma123", 4200.0)
            );

            System.out.println("Initialized " + allCustomers.size() + " customers in LoginController");
            for (Customer customer : allCustomers) {
                System.out.println(" - " + customer.getFirstName() + " | Phone: " + customer.getPhone() + " | Address: " + customer.getAddress());
            }
        }
    }

    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/admin_dashboard.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard - Sinqee Bank");
            stage.setMaximized(true);

            // Pass the shared customer data to admin controller
            AdminController adminController = loader.getController();
            adminController.setCustomers(allCustomers);

            stage.show();
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load admin dashboard: " + e.getMessage());
        }
    }

    private void loadCustomerDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/customer_dashboard.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Customer Dashboard - " + customer.getFullName());
            stage.setMaximized(true);

            CustomerController controller = loader.getController();
            controller.setCustomer(customer);

            // Pass the shared list of all customers for transfer functionality
            controller.setAllCustomers(allCustomers);

            stage.show();
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load customer dashboard: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Static getter for other controllers to access the shared customer list
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
}