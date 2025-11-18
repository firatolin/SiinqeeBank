package siinqee_banking.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;
import javafx.scene.control.Alert;

public class WithdrawController {

    @FXML
    private TextField amountField;

    private Customer currentCustomer;

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    @FXML
    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());

            if (currentCustomer.withdraw(amount)) {
                showAlert("Withdrawal Successful", "You withdrew " + amount + " Birr\nRemaining balance: " + currentCustomer.getBalance());
                Stage stage = (Stage) amountField.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Failed", "Insufficient balance or invalid amount!");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
