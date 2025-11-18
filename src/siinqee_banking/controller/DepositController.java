package siinqee_banking.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import siinqee_banking.model.Customer;

public class DepositController {

    @FXML
    private TextField amountField;

    private Customer currentCustomer;

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());

            if (currentCustomer != null) {
                currentCustomer.deposit(amount);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deposit Successful");
            alert.setHeaderText(null);
            alert.setContentText("You deposited: " + amount + " Birr\nNew Balance: "
                    + (currentCustomer != null ? currentCustomer.getBalance() : "N/A"));
            alert.showAndWait();

            // Close the deposit window
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid number!");
            alert.showAndWait();
        }
    }
}
