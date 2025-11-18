package siinqee_banking.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BalanceController {

    @FXML
    private Label balanceLabel;

    private double balance;

    public void setBalance(double balance) {
        this.balance = balance;
        balanceLabel.setText(balance + " Birr");
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) balanceLabel.getScene().getWindow();
        stage.close();
    }
}
