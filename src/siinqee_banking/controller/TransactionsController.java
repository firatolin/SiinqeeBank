package siinqee_banking.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;
import siinqee_banking.model.Transaction;

public class TransactionsController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> timestampColumn;

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        loadTransactions();
    }

    private void loadTransactions() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        transactionsTable.setItems(FXCollections.observableArrayList(customer.getTransactions()));
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) transactionsTable.getScene().getWindow();
        stage.close();
    }
}
