package siinqee_banking.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private List<Customer> customers = new ArrayList<>() {{
        add(new Customer("user1", "123", 10000));
        add(new Customer("user2", "456", 5000));
    }};

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            // Admin login
            if(username.equals("admin") && password.equals("admin")) {
                loadFXML("/siinqee_banking/view/admin_dashboard.fxml", "Admin Dashboard - Sinqe Bank");
                return;
            }

            // Staff login
            if(username.equals("staff1") && password.equals("staff123")) {
                loadFXML("/siinqee_banking/view/staff_dashboard.fxml", "Staff Dashboard - Sinqe Bank");
                return;
            }

            // Customer login
            for(Customer customer : customers) {
                if(customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                    loadCustomerDashboard(customer);
                    return;
                }
            }

            showAlert("Login Failed", "Invalid username or password!");

        } catch(Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load dashboard. Please try again.");
        }
    }

    private void loadFXML(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();
            closeWindow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load: " + fxmlPath, e);
        }
    }

    private void loadCustomerDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/customer_dashboard.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Customer Dashboard - Sinqe Bank");
            stage.setMaximized(true);

            CustomerController controller = loader.getController();
            controller.setCustomer(customer);

            stage.show();
            closeWindow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load customer dashboard", e);
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
}