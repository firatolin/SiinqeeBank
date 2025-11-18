package siinqee_banking.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import siinqee_banking.model.Customer;
import siinqee_banking.model.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CustomerController {

    // Color constants with your branding
    private static final String PRIMARY_COLOR = "#3f5615";    // rgb(63, 86, 21)
    private static final String HIGHLIGHT_COLOR = "#FFD700";  // Gold
    private static final String SECONDARY_COLOR = "#F4941C";  // Orange

    @FXML private Label welcomeLabel;
    @FXML private Label accountNumberLabel; // Header account number
    @FXML private Label balanceLabel;

    // Account overview labels
    @FXML private Label fullNameLabel;
    @FXML private Label accountTypeLabel;
    @FXML private Label accountNumberDetailLabel; // Detailed account number in overview
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label openingDateLabel;
    @FXML private Label genderLabel; // Added gender label

    // Header section with better spacing
    @FXML private HBox headerBox;
    @FXML private VBox welcomeBox;
    @FXML private VBox balanceBox;
    @FXML private VBox logoutBox;

    // Transfer fields
    @FXML private TextField recipientAccountField;
    @FXML private TextField transferAmountField;
    @FXML private Label recipientNameLabel;
    @FXML private VBox recipientInfoBox;

    // Transactions table
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> descriptionColumn;
    @FXML private TableColumn<Transaction, String> amountColumn;
    @FXML private TableColumn<Transaction, String> dateColumn;

    private Customer customer;
    private ObservableList<Customer> allCustomers;

    // Initialize method to check FXML injection
    @FXML
    public void initialize() {
        System.out.println("CustomerController initialized!");
        System.out.println("welcomeLabel: " + (welcomeLabel != null ? "INJECTED" : "NULL"));
        System.out.println("fullNameLabel: " + (fullNameLabel != null ? "INJECTED" : "NULL"));
        System.out.println("accountTypeLabel: " + (accountTypeLabel != null ? "INJECTED" : "NULL"));
        System.out.println("accountNumberDetailLabel: " + (accountNumberDetailLabel != null ? "INJECTED" : "NULL"));
        System.out.println("phoneLabel: " + (phoneLabel != null ? "INJECTED" : "NULL"));
        System.out.println("addressLabel: " + (addressLabel != null ? "INJECTED" : "NULL"));
        System.out.println("openingDateLabel: " + (openingDateLabel != null ? "INJECTED" : "NULL"));
        System.out.println("genderLabel: " + (genderLabel != null ? "INJECTED" : "NULL"));
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        System.out.println("Setting customer: " + (customer != null ? customer.getFullName() : "NULL"));
        if (customer != null) {
            System.out.println("Customer details - Account: " + customer.getAccountNumber() +
                    ", Phone: " + customer.getPhone() +
                    ", Address: " + customer.getAddress() +
                    ", Gender: " + customer.getGender());
        }
        updateCustomerInfo();
        loadRecentTransactions();
        setupHeaderSpacing();
    }

    public void setAllCustomers(ObservableList<Customer> allCustomers) {
        this.allCustomers = allCustomers;
        System.out.println("Set " + (allCustomers != null ? allCustomers.size() : 0) + " customers for transfers");
    }

    private void setupHeaderSpacing() {
        // Add proper spacing between header elements
        if (headerBox != null) {
            headerBox.setSpacing(40);
            headerBox.setStyle("-fx-padding: 20px; -fx-alignment: center;");
        }

        // Style individual boxes for better appearance with correct colors
        if (welcomeBox != null) {
            welcomeBox.setStyle("-fx-padding: 15px; -fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        }

        if (balanceBox != null) {
            balanceBox.setStyle("-fx-padding: 15px; -fx-background-color: " + PRIMARY_COLOR + "; -fx-background-radius: 10;");
        }

        if (logoutBox != null) {
            logoutBox.setStyle("-fx-padding: 10px; -fx-alignment: center;");
        }
    }

    private void updateCustomerInfo() {
        if (customer != null) {
            System.out.println("Updating customer info for: " + customer.getFullName());
            System.out.println("Phone: " + customer.getPhone());
            System.out.println("Address: " + customer.getAddress());
            System.out.println("Gender: " + customer.getGender());
            System.out.println("Account Number: " + customer.getAccountNumber());

            // Safe updates with null checks
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + customer.getFirstName() + "!");
            }
            if (accountNumberLabel != null) {
                accountNumberLabel.setText("Account: " + customer.getAccountNumber());
            }
            if (balanceLabel != null) {
                balanceLabel.setText(String.format("ETB %.2f", customer.getBalance()));
            }
            if (fullNameLabel != null) {
                fullNameLabel.setText(customer.getFullName());
            }
            if (accountTypeLabel != null) {
                accountTypeLabel.setText(customer.getAccountType());
            }
            if (accountNumberDetailLabel != null) {
                accountNumberDetailLabel.setText(customer.getAccountNumber());
            }

            if (openingDateLabel != null) {
                openingDateLabel.setText(customer.getOpeningDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            }
            if (genderLabel != null) {
                String gender = customer.getGender();
                genderLabel.setText(gender != null && !gender.isEmpty() ? gender : "Not provided");
            }
        } else {
            System.out.println("Customer is null in updateCustomerInfo!");
        }
    }

    private void loadRecentTransactions() {
        if (customer != null && transactionsTable != null) {
            // Configure table columns
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            amountColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getFormattedAmount()));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));

            // Get recent transactions (last 20)
            ObservableList<Transaction> recentTransactions =
                    FXCollections.observableArrayList(customer.getRecentTransactions(20));
            transactionsTable.setItems(recentTransactions);
        }
    }

    @FXML
    private void handleCheckBalance() {
        if (customer != null) {
            showBrandedAlert("Balance Information",
                    "Current Balance: " + String.format("ETB %.2f", customer.getBalance()),
                    "💰", PRIMARY_COLOR);
        }
    }

    @FXML
    private void handleSearchRecipient() {
        String recipientAccount = recipientAccountField.getText().trim();

        if (recipientAccount.isEmpty()) {
            showBrandedAlert("Error", "Please enter recipient account number.", "❌", "#dc3545");
            return;
        }

        if (recipientAccount.equals(customer.getAccountNumber())) {
            showBrandedAlert("Error", "Cannot transfer to your own account.", "⚠️", SECONDARY_COLOR);
            recipientInfoBox.setVisible(false);
            return;
        }

        // Search for recipient in all customers
        Customer recipient = findCustomerByAccountNumber(recipientAccount);
        if (recipient != null) {
            recipientNameLabel.setText(recipient.getFullName() + " (" + recipient.getAccountType() + ")");
            recipientInfoBox.setVisible(true);
            recipientInfoBox.setStyle("-fx-background-color: #e8f5e8; -fx-padding: 10px; -fx-background-radius: 5;");
        } else {
            showBrandedAlert("Not Found", "Account number not found: " + recipientAccount, "🔍", "#6c757d");
            recipientInfoBox.setVisible(false);
        }
    }

    @FXML
    private void handleTransfer() {
        String recipientAccount = recipientAccountField.getText().trim();
        String amountText = transferAmountField.getText().trim();

        if (recipientAccount.isEmpty() || amountText.isEmpty()) {
            showBrandedAlert("Error", "Please enter recipient account and amount.", "❌", "#dc3545");
            return;
        }

        if (recipientAccount.equals(customer.getAccountNumber())) {
            showBrandedAlert("Error", "Cannot transfer to your own account.", "⚠️", SECONDARY_COLOR);
            return;
        }

        Customer recipient = findCustomerByAccountNumber(recipientAccount);
        if (recipient == null) {
            showBrandedAlert("Error", "Recipient account not found: " + recipientAccount, "🔍", "#6c757d");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showBrandedAlert("Error", "Amount must be positive.", "❌", "#dc3545");
                return;
            }

            if (amount > customer.getBalance()) {
                showBrandedAlert("Error", "Insufficient funds for this transfer.", "💸", "#dc3545");
                return;
            }

            // Show branded transfer confirmation
            if (showBrandedTransferConfirmation(recipient, amount)) {
                if (customer.transfer(recipient, amount)) {
                    showBrandedTransferSuccess(recipient, amount);

                    // Clear fields and refresh data
                    recipientAccountField.clear();
                    transferAmountField.clear();
                    recipientInfoBox.setVisible(false);

                    // Refresh customer info and transactions
                    updateCustomerInfo();
                    loadRecentTransactions();
                } else {
                    showBrandedAlert("Error", "Transfer failed. Please try again.", "❌", "#dc3545");
                }
            }

        } catch (NumberFormatException e) {
            showBrandedAlert("Error", "Please enter a valid amount.", "❌", "#dc3545");
        }
    }

    private boolean showBrandedTransferConfirmation(Customer recipient, double amount) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Transfer");
        alert.setHeaderText(null);

        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-alignment: center;");

        // Header with bank branding
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER);
        Label bankIcon = new Label("🏦");
        bankIcon.setStyle("-fx-font-size: 28px;");
        Label bankName = new Label("SINQEE BANK");
        bankName.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-size: 20px; -fx-font-weight: bold;");
        header.getChildren().addAll(bankIcon, bankName);

        // Confirmation icon
        Label confirmIcon = new Label("🔒");
        confirmIcon.setStyle("-fx-font-size: 40px;");

        // Title
        Label title = new Label("TRANSFER CONFIRMATION");
        title.setStyle("-fx-text-fill: " + SECONDARY_COLOR + "; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Transfer details
        VBox detailsBox = new VBox(12);
        detailsBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        detailsBox.setPrefWidth(400);

        Label amountLabel = new Label("Amount: " + String.format("ETB %.2f", amount));
        amountLabel.setStyle("-fx-text-fill: " + SECONDARY_COLOR + "; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label fromLabel = new Label("From: " + customer.getFullName());
        fromLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");

        Label toLabel = new Label("To: " + recipient.getFullName());
        toLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");

        Label accountLabel = new Label("Account: " + recipient.getAccountNumber());
        accountLabel.setStyle("-fx-text-fill: #666;");

        Label warningLabel = new Label("This action cannot be undone.");
        warningLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-style: italic; -fx-font-size: 12px;");

        detailsBox.getChildren().addAll(amountLabel, fromLabel, toLabel, accountLabel, warningLabel);

        content.getChildren().addAll(header, confirmIcon, title, detailsBox);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefSize(450, 400);

        // Style the dialog buttons
        alert.getDialogPane().getButtonTypes().forEach(buttonType -> {
            Button button = (Button) alert.getDialogPane().lookupButton(buttonType);
            if (buttonType.getButtonData().isDefaultButton()) {
                button.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                button.setStyle("-fx-background-color: " + SECONDARY_COLOR + "; -fx-text-fill: white; -fx-font-weight: bold;");
            }
        });

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showBrandedTransferSuccess(Customer recipient, double amount) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transfer Successful");
        alert.setHeaderText(null);

        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-alignment: center;");

        // Header with bank branding
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER);
        Label bankIcon = new Label("🏦");
        bankIcon.setStyle("-fx-font-size: 28px;");
        Label bankName = new Label("SINQEE BANK");
        bankName.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-size: 20px; -fx-font-weight: bold;");
        header.getChildren().addAll(bankIcon, bankName);

        // Success icon
        Label successIcon = new Label("✅");
        successIcon.setStyle("-fx-font-size: 48px;");

        // Success title
        Label successTitle = new Label("TRANSFER SUCCESSFUL");
        successTitle.setStyle("-fx-text-fill: #28a745; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Transfer details
        VBox detailsBox = new VBox(15);
        detailsBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        detailsBox.setPrefWidth(400);

        Label amountLabel = new Label(String.format("ETB %.2f", amount));
        amountLabel.setStyle("-fx-text-fill: " + SECONDARY_COLOR + "; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label fromLabel = new Label("From: " + customer.getFullName());
        fromLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");

        Label toLabel = new Label("To: " + recipient.getFullName());
        toLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");

        Label newBalanceLabel = new Label("New Balance: " + String.format("ETB %.2f", customer.getBalance()));
        newBalanceLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-weight: bold;");

        detailsBox.getChildren().addAll(amountLabel, fromLabel, toLabel, newBalanceLabel);

        // Footer message
        Label footer = new Label("Funds transferred successfully! 🎉");
        footer.setStyle("-fx-text-fill: #666; -fx-font-size: 12px; -fx-font-style: italic;");

        content.getChildren().addAll(header, successIcon, successTitle, detailsBox, footer);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefSize(450, 450);
        alert.showAndWait();
    }

    private void showBrandedAlert(String title, String message, String icon, String color) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        VBox content = new VBox(15);
        content.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-alignment: center;");

        // Header with bank branding
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        Label bankIcon = new Label("🏦");
        bankIcon.setStyle("-fx-font-size: 24px;");
        Label bankName = new Label("SINQEE BANK");
        bankName.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-size: 16px; -fx-font-weight: bold;");
        header.getChildren().addAll(bankIcon, bankName);

        // Alert icon and message
        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.CENTER);
        Label alertIcon = new Label(icon);
        alertIcon.setStyle("-fx-font-size: 32px;");
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        messageBox.getChildren().addAll(alertIcon, messageLabel);

        content.getChildren().addAll(header, messageBox);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefSize(350, 200);
        alert.showAndWait();
    }

    @FXML
    private void handleViewAllTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/transactions_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("All Transactions - " + customer.getFullName());

            TransactionsController controller = loader.getController();
            controller.setCustomer(customer);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showBrandedAlert("Error", "Could not load transactions.", "❌", "#dc3545");
        }
    }

    @FXML
    private void handleViewAccountDetails() {
        showBrandedAlert("Account Information",
                "You are currently viewing your account details.\n\n" +
                        "Name: " + customer.getFullName() + "\n" +
                        "Account Number: " + customer.getAccountNumber() + "\n" +
                        "Account Type: " + customer.getAccountType() + "\n" +
                        "Gender: " + (customer.getGender() != null ? customer.getGender() : "Not provided") + "\n" +
                        "Phone: " + (customer.getPhone() != null ? customer.getPhone() : "Not provided") + "\n" +
                        "Address: " + (customer.getAddress() != null ? customer.getAddress() : "Not provided") + "\n" +
                        "Opening Date: " + customer.getOpeningDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + "\n" +
                        "Current Balance: " + String.format("ETB %.2f", customer.getBalance()),
                "👤", PRIMARY_COLOR);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Show confirmation before logout
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Confirm Logout");
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/login.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Siinqee Bank Login");
                stage.setMaximized(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showBrandedAlert("Error", "Logout failed: " + e.getMessage(), "❌", "#dc3545");
        }
    }

    private Customer findCustomerByAccountNumber(String accountNumber) {
        if (allCustomers != null) {
            for (Customer c : allCustomers) {
                if (c.getAccountNumber().equals(accountNumber)) {
                    return c;
                }
            }
        }
        return null;
    }
}