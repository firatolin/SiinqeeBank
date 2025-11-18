package siinqee_banking.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;

public class CustomerController {

    @FXML
    private Button checkBalanceBtn, depositBtn, withdrawBtn, viewTransactionsBtn, logoutBtn;

    @FXML
    private Label welcomeLabel;

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        welcomeLabel.setText("Welcome, " + customer.getUsername());
    }

    @FXML
    private void handleCheckBalance(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/balance_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Balance - " + customer.getUsername());

            BalanceController controller = loader.getController();
            controller.setBalance(customer.getBalance());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/deposit_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Deposit - " + customer.getUsername());

            DepositController controller = loader.getController();
            controller.setCustomer(customer);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/withdraw_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Withdraw - " + customer.getUsername());

            WithdrawController controller = loader.getController();
            controller.setCustomer(customer);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewTransactions(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/transactions_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Transactions - " + customer.getUsername());

            TransactionsController controller = loader.getController();
            controller.setCustomer(customer);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Siinqee Bank Login");
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}