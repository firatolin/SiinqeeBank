package siinqee_banking.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import siinqee_banking.model.Customer;
import siinqee_banking.model.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminController {

    @FXML private Label welcomeLabel;
    @FXML private Label customerCountLabel;
    @FXML private Label totalBalanceLabel;
    @FXML private StackPane contentArea;

    // Sidebar buttons
    @FXML private Button addCustomerBtn;
    @FXML private Button viewAccountsBtn;
    @FXML private Button viewTransactionsBtn;
    @FXML private Button systemReportsBtn;
    @FXML private Button accountTransactionsBtn;

    // Use ObservableList for automatic updates
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private Set<String> usedAccountNumbers = new HashSet<>();
    private Random random = new Random();

    // Properties for binding
    private IntegerProperty customerCount = new SimpleIntegerProperty();
    private DoubleProperty totalBalance = new SimpleDoubleProperty();

    // Current customer for transactions
    private Customer currentCustomer;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Administrator");

        // Initialize with sample data
        customers.addAll(
                new Customer("John", "Doe", "Male", "1234567890", "123 Main St, Addis Ababa", "password123",
                        generateUniqueAccountNumber(), 10000, "Regular", LocalDate.now()),
                new Customer("Jane", "Smith", "Female", "0987654321", "456 Oak Ave, Dire Dawa", "password456",
                        generateUniqueAccountNumber(), 5000, "Student", LocalDate.now())
        );

        // Bind labels to properties
        customerCountLabel.textProperty().bind(Bindings.concat("Customers: ", customerCount));
        totalBalanceLabel.textProperty().bind(Bindings.concat("Balance: ", totalBalance.asString("%.0fK ETB")));

        updateSidebarStats();

        // Show add customer form by default and highlight the button
        showAddCustomer();
        highlightActiveButton(addCustomerBtn);
    }

    // Generate unique 10-digit account number starting with "s"
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            // Generate 9 random digits and prepend with "s"
            int randomNum = 100000000 + random.nextInt(900000000);
            accountNumber = "s" + randomNum;
        } while (usedAccountNumbers.contains(accountNumber));

        usedAccountNumbers.add(accountNumber);
        return accountNumber;
    }

    // Update the sidebar stats
    private void updateSidebarStats() {
        customerCount.set(customers.size());
        double balance = customers.stream().mapToDouble(Customer::getBalance).sum();
        totalBalance.set(balance / 1000); // Convert to K format
    }

    // Method to highlight active button and remove highlight from others
    private void highlightActiveButton(Button activeButton) {
        // Reset all buttons to default style
        addCustomerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 15; -fx-alignment: CENTER_LEFT; -fx-border-color: #F4941C; -fx-border-width: 0 0 0 3;");
        viewAccountsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 15; -fx-alignment: CENTER_LEFT; -fx-border-color: #F4941C; -fx-border-width: 0 0 0 3;");
        viewTransactionsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 15; -fx-alignment: CENTER_LEFT; -fx-border-color: #F4941C; -fx-border-width: 0 0 0 3;");
        systemReportsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 15; -fx-alignment: CENTER_LEFT; -fx-border-color: #F4941C; -fx-border-width: 0 0 0 3;");
        accountTransactionsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 15; -fx-alignment: CENTER_LEFT; -fx-border-color: #F4941C; -fx-border-width: 0 0 0 3;");

        // Highlight the active button
        activeButton.setStyle("-fx-background-color: #F4941C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 15; -fx-alignment: CENTER_LEFT;");
    }

    @FXML
    private void showAddCustomer() {
        try {
            VBox addCustomerContent = createAddCustomerContent();
            loadContentInScrollPane(addCustomerContent);
            highlightActiveButton(addCustomerBtn);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load add customer form.");
        }
    }

    @FXML
    private void showViewAccounts() {
        try {
            VBox accountsContent = createAccountsContent();
            loadContentInScrollPane(accountsContent);
            highlightActiveButton(viewAccountsBtn);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load accounts view.");
        }
    }

    @FXML
    private void showViewTransactions() {
        try {
            VBox transactionsContent = createTransactionsContent();
            loadContentInScrollPane(transactionsContent);
            highlightActiveButton(viewTransactionsBtn);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load transactions view.");
        }
    }

    @FXML
    private void showSystemReports() {
        try {
            VBox reportsContent = createReportsContent();
            loadContentInScrollPane(reportsContent);
            highlightActiveButton(systemReportsBtn);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load reports view.");
        }
    }

    @FXML
    private void showAccountTransactions() {
        try {
            VBox transactionContent = createTransactionContent();
            loadContentInScrollPane(transactionContent);
            highlightActiveButton(accountTransactionsBtn);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load account transactions.");
        }
    }

    // Method to load content in a scrollable pane
    private void loadContentInScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        contentArea.getChildren().setAll(scrollPane);
    }

    // Create Add Customer Content with all new fields
    private VBox createAddCustomerContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30; -fx-alignment: top-center;");

        // Header
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("👥 Add New Customer");
        titleLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Create new customer accounts for banking services");
        subtitleLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-size: 14px;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Container
        VBox formContainer = new VBox(15);
        formContainer.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-background-radius: 10;");
        formContainer.setPrefWidth(650);

        // Create the form with input fields
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        // Column constraints
        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setPrefWidth(150);
        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setPrefWidth(350);
        form.getColumnConstraints().addAll(col1, col2);

        // Personal Information Section
        Label personalInfoLabel = new Label("Personal Information");
        personalInfoLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 0 0 10 0;");
        GridPane.setColumnSpan(personalInfoLabel, 2);
        form.add(personalInfoLabel, 0, 0);

        // First Name
        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        firstNameField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        form.add(firstNameLabel, 0, 1);
        form.add(firstNameField, 1, 1);

        // Last Name
        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");
        lastNameField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        form.add(lastNameLabel, 0, 2);
        form.add(lastNameField, 1, 2);

        // Gender (Radio Buttons)
        Label genderLabel = new Label("Gender:");
        genderLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");

        HBox genderBox = new HBox(15);
        genderBox.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup genderGroup = new ToggleGroup();

        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        maleRadio.setStyle("-fx-text-fill: #333;");

        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);
        femaleRadio.setStyle("-fx-text-fill: #333;");

        genderBox.getChildren().addAll(maleRadio, femaleRadio);

        form.add(genderLabel, 0, 3);
        form.add(genderBox, 1, 3);

        // Phone Number
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter phone number");
        phoneField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        form.add(phoneLabel, 0, 4);
        form.add(phoneField, 1, 4);

        // Address
        Label addressLabel = new Label("Address:");
        addressLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        TextArea addressField = new TextArea();
        addressField.setPromptText("Enter full address");
        addressField.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-pref-height: 60;");
        addressField.setWrapText(true);
        form.add(addressLabel, 0, 5);
        form.add(addressField, 1, 5);

        // Account Information Section
        Label accountInfoLabel = new Label("Account Information");
        accountInfoLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10 0 10 0;");
        GridPane.setColumnSpan(accountInfoLabel, 2);
        form.add(accountInfoLabel, 0, 6);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        form.add(passwordLabel, 0, 7);
        form.add(passwordField, 1, 7);

        // Account Type
        Label accountTypeLabel = new Label("Account Type:");
        accountTypeLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        ComboBox<String> accountTypeComboBox = new ComboBox<>();
        accountTypeComboBox.getItems().addAll("Children", "Regular", "Student", "Women");
        accountTypeComboBox.setPromptText("Select account type");
        accountTypeComboBox.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        form.add(accountTypeLabel, 0, 8);
        form.add(accountTypeComboBox, 1, 8);

        // Opening Date
        Label openingDateLabel = new Label("Opening Date:");
        openingDateLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        DatePicker openingDatePicker = new DatePicker();
        openingDatePicker.setValue(LocalDate.now()); // Set default to today
        openingDatePicker.setStyle("-fx-background-radius: 5;");
        openingDatePicker.setPrefWidth(350);
        form.add(openingDateLabel, 0, 9);
        form.add(openingDatePicker, 1, 9);

        // Account Number (Auto-generated, display only)
        Label accountNumberLabel = new Label("Account Number:");
        accountNumberLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        Label accountNumberDisplay = new Label("Will be generated automatically");
        accountNumberDisplay.setStyle("-fx-text-fill: #666; -fx-font-style: italic; -fx-padding: 8; -fx-background-color: #e9ecef; -fx-background-radius: 5;");
        accountNumberDisplay.setPrefWidth(350);
        accountNumberDisplay.setPrefHeight(35);
        accountNumberDisplay.setAlignment(Pos.CENTER_LEFT);
        form.add(accountNumberLabel, 0, 10);
        form.add(accountNumberDisplay, 1, 10);

        // Opening Balance
        Label balanceLabel = new Label("Opening Balance:");
        balanceLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        TextField balanceField = new TextField();
        balanceField.setPromptText("0.00");
        balanceField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        form.add(balanceLabel, 0, 11);
        form.add(balanceField, 1, 11);

        // Create Account Button
        Button createAccountBtn = new Button("➕ Create Customer Account");
        createAccountBtn.setStyle("-fx-background-color: #F4941C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; -fx-font-size: 14px;");

        // Add action to the button
        createAccountBtn.setOnAction(event -> {
            // Get selected gender from radio buttons
            String selectedGender = "";
            if (maleRadio.isSelected()) selectedGender = "Male";
            else if (femaleRadio.isSelected()) selectedGender = "Female";

            String accountNumber = generateUniqueAccountNumber();
            handleAddCustomer(
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    selectedGender,
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    passwordField.getText().trim(),
                    accountNumber,
                    accountTypeComboBox.getValue(),
                    openingDatePicker.getValue(),
                    balanceField.getText().trim()
            );

            // Clear form after successful creation
            firstNameField.clear();
            lastNameField.clear();
            genderGroup.selectToggle(null);
            phoneField.clear();
            addressField.clear();
            passwordField.clear();
            accountTypeComboBox.setValue(null);
            openingDatePicker.setValue(LocalDate.now());
            balanceField.clear();
        });

        GridPane.setColumnSpan(createAccountBtn, 2);
        GridPane.setHalignment(createAccountBtn, javafx.geometry.HPos.CENTER);
        form.add(createAccountBtn, 0, 12);

        formContainer.getChildren().add(form);

        // Tips
        VBox tips = new VBox(10);
        tips.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label tipsTitle = new Label("💡 Quick Tips");
        tipsTitle.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");

        Label tip1 = new Label("• All fields are required");
        tip1.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        Label tip2 = new Label("• Account number is automatically generated");
        tip2.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        Label tip3 = new Label("• Password should be secure");
        tip3.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        tips.getChildren().addAll(tipsTitle, tip1, tip2, tip3);

        content.getChildren().addAll(header, formContainer, tips);
        return content;
    }

    // Updated handleAddCustomer to accept all parameters
    private void handleAddCustomer(String firstName, String lastName, String gender,
                                   String phone, String address, String password,
                                   String accountNumber, String accountType,
                                   LocalDate openingDate, String balanceText) {
        // Validate all fields
        if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() ||
                phone.isEmpty() || address.isEmpty() || password.isEmpty() ||
                accountType == null || openingDate == null || balanceText.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

        // Validate phone number (basic validation)
        if (!phone.matches("\\d+")) {
            showAlert("Error", "Phone number should contain only digits.");
            return;
        }

        try {
            double openingBalance = Double.parseDouble(balanceText);
            if (openingBalance < 0) {
                showAlert("Error", "Opening balance cannot be negative.");
                return;
            }

            // Create new customer with all details
            Customer newCustomer = new Customer(firstName, lastName, gender, phone,
                    address, password, accountNumber,
                    openingBalance, accountType, openingDate);
            customers.add(newCustomer);

            updateSidebarStats();
            showSuccessAlert(firstName + " " + lastName, accountNumber, accountType, openingDate, openingBalance);

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount for opening balance.");
        }
    }

    // Custom success alert with branding
    private void showSuccessAlert(String customerName, String accountNumber, String accountType, LocalDate openingDate, double balance) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);

        // Create custom dialog pane with branding
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-alignment: center;");

        // Bank branding header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        Label bankIcon = new Label("🏦");
        bankIcon.setStyle("-fx-font-size: 24px;");
        Label bankName = new Label("SINQEE BANK");
        bankName.setStyle("-fx-text-fill: #2f4411; -fx-font-size: 18px; -fx-font-weight: bold;");
        header.getChildren().addAll(bankIcon, bankName);

        // Success icon and message
        VBox successBox = new VBox(10);
        successBox.setAlignment(Pos.CENTER);
        Label successIcon = new Label("✅");
        successIcon.setStyle("-fx-font-size: 48px;");
        Label successTitle = new Label("Account Created Successfully!");
        successTitle.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 20px; -fx-font-weight: bold;");
        successBox.getChildren().addAll(successIcon, successTitle);

        // Customer details
        VBox detailsBox = new VBox(8);
        detailsBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-alignment: center-left;");
        detailsBox.setPrefWidth(350);

        Label nameLabel = new Label("👤 Customer: " + customerName);
        nameLabel.setStyle("-fx-text-fill: #2f4411; -fx-font-weight: bold;");

        Label accountLabel = new Label("🔢 Account Number: " + accountNumber);
        accountLabel.setStyle("-fx-text-fill: #2f4411; -fx-font-weight: bold;");

        Label typeLabel = new Label("📋 Account Type: " + accountType);
        typeLabel.setStyle("-fx-text-fill: #2f4411; -fx-font-weight: bold;");

        Label dateLabel = new Label("📅 Opening Date: " + openingDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        dateLabel.setStyle("-fx-text-fill: #2f4411; -fx-font-weight: bold;");

        Label balanceLabel = new Label("💰 Opening Balance: " + String.format("%.2f ETB", balance));
        balanceLabel.setStyle("-fx-text-fill: #2f4411; -fx-font-weight: bold;");

        detailsBox.getChildren().addAll(nameLabel, accountLabel, typeLabel, dateLabel, balanceLabel);

        // Footer message
        Label footer = new Label("Welcome to Sinqee Bank Family! 🎉");
        footer.setStyle("-fx-text-fill: #666; -fx-font-size: 12px; -fx-font-style: italic;");

        content.getChildren().addAll(header, successBox, detailsBox, footer);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefSize(400, 400);
        alert.showAndWait();
    }

    // Create Accounts Content
    private VBox createAccountsContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30; -fx-alignment: top-center;");

        // Header
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("📊 All Customer Accounts");
        titleLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("View and manage all customer accounts");
        subtitleLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-size: 14px;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Accounts Table
        VBox accountsTable = new VBox(15);
        accountsTable.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-background-radius: 10;");
        accountsTable.setPrefWidth(1000);

        Label tableTitle = new Label("Customer Accounts (" + customers.size() + " accounts)");
        tableTitle.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold; -fx-font-size: 16px;");

        // Create a table view with all customer details
        TableView<Customer> tableView = new TableView<>();
        tableView.setPrefHeight(400);

        // Add columns for all customer information
        TableColumn<Customer, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(100);

        TableColumn<Customer, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(100);

        TableColumn<Customer, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setPrefWidth(80);

        TableColumn<Customer, String> accountCol = new TableColumn<>("Account No.");
        accountCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accountCol.setPrefWidth(120);

        TableColumn<Customer, String> typeCol = new TableColumn<>("Account Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        typeCol.setPrefWidth(100);

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(100);

        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(200);
        addressCol.setCellFactory(col -> new TableCell<Customer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item.length() > 30 ? item.substring(0, 30) + "..." : item);
                    setTooltip(new javafx.scene.control.Tooltip(item));
                }
            }
        });

        TableColumn<Customer, LocalDate> dateCol = new TableColumn<>("Opening Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("openingDate"));
        dateCol.setPrefWidth(120);
        dateCol.setCellFactory(col -> new TableCell<Customer, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                }
            }
        });

        TableColumn<Customer, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balanceCol.setPrefWidth(100);
        balanceCol.setCellFactory(col -> new TableCell<Customer, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f ETB", item));
                }
            }
        });

        tableView.getColumns().addAll(firstNameCol, lastNameCol, genderCol, accountCol, typeCol, phoneCol, addressCol, dateCol, balanceCol);
        tableView.setItems(customers);

        accountsTable.getChildren().addAll(tableTitle, tableView);
        content.getChildren().addAll(header, accountsTable);

        return content;
    }

    // Create Transactions Content - FIXED: Now shows actual transactions
    private VBox createTransactionsContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30; -fx-alignment: top-center;");

        // Header
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("💳 Transaction History");
        titleLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("View all banking transactions across all accounts");
        subtitleLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-size: 14px;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Transactions Content
        VBox transactionsBox = new VBox(15);
        transactionsBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-background-radius: 10;");
        transactionsBox.setPrefWidth(900);

        // Create table for transactions
        TableView<Transaction> transactionsTable = new TableView<>();
        transactionsTable.setPrefHeight(400);

        // Columns for transactions
        TableColumn<Transaction, String> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(cellData -> {
            // Find which customer this transaction belongs to
            for (Customer customer : customers) {
                if (customer.getTransactions().contains(cellData.getValue())) {
                    return new SimpleStringProperty(customer.getAccountNumber() + " - " + customer.getFullName());
                }
            }
            return new SimpleStringProperty("Unknown");
        });
        accountCol.setPrefWidth(180);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);

        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setPrefWidth(200);

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedAmount()));
        amountCol.setPrefWidth(120);

        TableColumn<Transaction, String> timestampCol = new TableColumn<>("Date/Time");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
        timestampCol.setPrefWidth(150);

        transactionsTable.getColumns().addAll(accountCol, typeCol, descriptionCol, amountCol, timestampCol);

        // Get all transactions from all customers
        ObservableList<Transaction> allTransactions = FXCollections.observableArrayList();
        for (Customer customer : customers) {
            allTransactions.addAll(customer.getTransactions());
        }

        // Sort by timestamp (newest first)
        allTransactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));

        transactionsTable.setItems(allTransactions);

        if (allTransactions.isEmpty()) {
            Label noTransactionsLabel = new Label("No transactions available yet.");
            noTransactionsLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
            transactionsBox.getChildren().add(noTransactionsLabel);
        } else {
            transactionsBox.getChildren().add(transactionsTable);
        }

        content.getChildren().addAll(header, transactionsBox);
        return content;
    }

    // Create Reports Content
    private VBox createReportsContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30; -fx-alignment: top-center;");

        // Header
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("📈 System Reports");
        titleLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Bank performance and analytics");
        subtitleLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-size: 14px;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Reports Content
        VBox reportsBox = new VBox(20);
        reportsBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-background-radius: 10;");
        reportsBox.setPrefWidth(600);

        // Stats cards - First Row
        HBox statsContainer1 = new HBox(15);
        statsContainer1.setAlignment(Pos.CENTER);

        // Total Customers
        VBox customerStats = new VBox(5);
        customerStats.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-alignment: center;");
        customerStats.setPrefWidth(250);
        Label customerLabel = new Label("Total Customers");
        customerLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
        Label customerCount = new Label(String.valueOf(this.customers.size()));
        customerCount.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 28px; -fx-font-weight: bold;");
        customerStats.getChildren().addAll(customerLabel, customerCount);

        // Total Balance
        VBox balanceStats = new VBox(5);
        balanceStats.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-alignment: center;");
        balanceStats.setPrefWidth(250);
        Label balanceLabel = new Label("Total Bank Balance");
        balanceLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
        double totalBalance = customers.stream().mapToDouble(Customer::getBalance).sum();
        Label balanceValue = new Label(String.format("%.2f ETB", totalBalance));
        balanceValue.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 28px; -fx-font-weight: bold;");
        balanceStats.getChildren().addAll(balanceLabel, balanceValue);

        statsContainer1.getChildren().addAll(customerStats, balanceStats);

        // Account Type Statistics - Second Row
        HBox statsContainer2 = new HBox(15);
        statsContainer2.setAlignment(Pos.CENTER);

        // Calculate account type distribution
        Map<String, Long> accountTypeCounts = customers.stream()
                .collect(Collectors.groupingBy(Customer::getAccountType, Collectors.counting()));

        // Account Type Distribution
        VBox accountTypeStats = new VBox(5);
        accountTypeStats.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-alignment: center;");
        accountTypeStats.setPrefWidth(250);
        Label accountTypeLabel = new Label("Account Types");
        accountTypeLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        VBox accountTypeDetails = new VBox(5);
        accountTypeDetails.setAlignment(Pos.CENTER_LEFT);

        for (Map.Entry<String, Long> entry : accountTypeCounts.entrySet()) {
            HBox typeRow = new HBox(10);
            typeRow.setAlignment(Pos.CENTER_LEFT);
            Label typeName = new Label(entry.getKey() + ":");
            typeName.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
            Label typeCount = new Label(String.valueOf(entry.getValue()));
            typeCount.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 16px; -fx-font-weight: bold;");
            typeRow.getChildren().addAll(typeName, typeCount);
            accountTypeDetails.getChildren().add(typeRow);
        }

        accountTypeStats.getChildren().addAll(accountTypeLabel, accountTypeDetails);

        // Gender Distribution
        VBox genderStats = new VBox(5);
        genderStats.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8; -fx-alignment: center;");
        genderStats.setPrefWidth(250);
        Label genderLabel = new Label("Gender Distribution");
        genderLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        Map<String, Long> genderCounts = customers.stream()
                .collect(Collectors.groupingBy(Customer::getGender, Collectors.counting()));

        VBox genderDetails = new VBox(5);
        genderDetails.setAlignment(Pos.CENTER_LEFT);

        for (Map.Entry<String, Long> entry : genderCounts.entrySet()) {
            HBox genderRow = new HBox(10);
            genderRow.setAlignment(Pos.CENTER_LEFT);
            Label genderName = new Label(entry.getKey() + ":");
            genderName.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
            Label genderCount = new Label(String.valueOf(entry.getValue()));
            genderCount.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 16px; -fx-font-weight: bold;");
            genderRow.getChildren().addAll(genderName, genderCount);
            genderDetails.getChildren().add(genderRow);
        }

        genderStats.getChildren().addAll(genderLabel, genderDetails);

        statsContainer2.getChildren().addAll(accountTypeStats, genderStats);

        reportsBox.getChildren().addAll(statsContainer1, statsContainer2);
        content.getChildren().addAll(header, reportsBox);

        return content;
    }

    // Create Transaction Content for account operations
    private VBox createTransactionContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30; -fx-alignment: top-center;");

        // Header
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("💳 Account Transactions");
        titleLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Search accounts and perform transactions");
        subtitleLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-size: 14px;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Transaction Container
        VBox transactionContainer = new VBox(15);
        transactionContainer.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 25; -fx-background-radius: 10;");
        transactionContainer.setPrefWidth(800);

        // Search Section
        VBox searchSection = new VBox(10);
        Label searchLabel = new Label("Search Account by Account Number");
        searchLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold; -fx-font-size: 16px;");

        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter account number (e.g., s123456789)");
        searchField.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-pref-width: 300;");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("s\\d{0,9}")) {
                searchField.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-pref-width: 300; -fx-border-color: #ced4da;");
            } else {
                searchField.setStyle("-fx-background-radius: 5; -fx-padding: 10; -fx-pref-width: 300; -fx-border-color: #dc3545;");
            }
        });

        Button searchBtn = new Button("🔍 Search");
        searchBtn.setStyle("-fx-background-color: #F4941C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        Button clearBtn = new Button("🔄 Clear");
        clearBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        searchBox.getChildren().addAll(searchField, searchBtn, clearBtn);

        // Customer Info Display (initially hidden)
        VBox customerInfoDisplay = new VBox(15);
        customerInfoDisplay.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 8;");
        customerInfoDisplay.setVisible(false);

        Label customerInfoLabel = new Label("Account Holder Information");
        customerInfoLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-weight: bold; -fx-font-size: 16px;");

        GridPane customerGrid = new GridPane();
        customerGrid.setHgap(15);
        customerGrid.setVgap(10);

        // Column constraints
        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setPrefWidth(120);
        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setPrefWidth(200);
        customerGrid.getColumnConstraints().addAll(col1, col2);

        // Customer details
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        Label nameValue = new Label();
        nameValue.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");

        Label accountLabel = new Label("Account Number:");
        accountLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        Label accountValue = new Label();
        accountValue.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");

        Label balanceLabel = new Label("Current Balance:");
        balanceLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        Label balanceValue = new Label();
        balanceValue.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setStyle("-fx-text-fill: rgb(63, 86, 21); -fx-font-weight: bold;");
        Label phoneValue = new Label();
        phoneValue.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");

        // Add to grid
        customerGrid.add(nameLabel, 0, 0);
        customerGrid.add(nameValue, 1, 0);
        customerGrid.add(accountLabel, 0, 1);
        customerGrid.add(accountValue, 1, 1);
        customerGrid.add(balanceLabel, 0, 2);
        customerGrid.add(balanceValue, 1, 2);
        customerGrid.add(phoneLabel, 0, 3);
        customerGrid.add(phoneValue, 1, 3);

        customerInfoDisplay.getChildren().addAll(customerInfoLabel, customerGrid);

        // Transaction Operations
        VBox transactionOperations = new VBox(20);
        transactionOperations.setAlignment(Pos.CENTER);
        transactionOperations.setVisible(false);

        // Deposit Section
        VBox depositSection = new VBox(10);
        depositSection.setStyle("-fx-background-color: #e8f5e8; -fx-padding: 15; -fx-background-radius: 8;");
        depositSection.setPrefWidth(250);

        Label depositLabel = new Label("💰 Deposit Funds");
        depositLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 16px;");

        TextField depositField = new TextField();
        depositField.setPromptText("Amount (ETB)");
        depositField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Button depositBtn = new Button("✅ Deposit");
        depositBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15;");

        depositSection.getChildren().addAll(depositLabel, depositField, depositBtn);

        // Withdraw Section
        VBox withdrawSection = new VBox(10);
        withdrawSection.setStyle("-fx-background-color: #fde8e8; -fx-padding: 15; -fx-background-radius: 8;");
        withdrawSection.setPrefWidth(250);

        Label withdrawLabel = new Label("💳 Withdraw Funds");
        withdrawLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 16px;");

        TextField withdrawField = new TextField();
        withdrawField.setPromptText("Amount (ETB)");
        withdrawField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Button withdrawBtn = new Button("📤 Withdraw");
        withdrawBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15;");

        withdrawSection.getChildren().addAll(withdrawLabel, withdrawField, withdrawBtn);

        // Transfer Section
        VBox transferSection = new VBox(10);
        transferSection.setStyle("-fx-background-color: #e8f0f8; -fx-padding: 15; -fx-background-radius: 8;");
        transferSection.setPrefWidth(250);

        Label transferLabel = new Label("🔄 Transfer Funds");
        transferLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold; -fx-font-size: 16px;");

        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient Account No.");
        recipientField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        TextField transferField = new TextField();
        transferField.setPromptText("Amount (ETB)");
        transferField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Button transferBtn = new Button("➡️ Transfer");
        transferBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15;");

        transferSection.getChildren().addAll(transferLabel, recipientField, transferField, transferBtn);

        // Arrange operations in a row
        HBox operationsRow = new HBox(20);
        operationsRow.setAlignment(Pos.CENTER);
        operationsRow.getChildren().addAll(depositSection, withdrawSection, transferSection);

        transactionOperations.getChildren().add(operationsRow);

        // Add event handlers
        searchBtn.setOnAction(e -> {
            String accountNumber = searchField.getText().trim();
            handleSearchAccount(accountNumber, customerInfoDisplay, transactionOperations,
                    nameValue, accountValue, balanceValue, phoneValue);
        });

        clearBtn.setOnAction(e -> {
            searchField.clear();
            recipientField.clear();
            depositField.clear();
            withdrawField.clear();
            transferField.clear();
            customerInfoDisplay.setVisible(false);
            transactionOperations.setVisible(false);
            currentCustomer = null;
        });

        depositBtn.setOnAction(e -> {
            String amountText = depositField.getText().trim();
            handleDeposit(amountText, depositField, balanceValue);
        });

        withdrawBtn.setOnAction(e -> {
            String amountText = withdrawField.getText().trim();
            handleWithdraw(amountText, withdrawField, balanceValue);
        });

        transferBtn.setOnAction(e -> {
            String recipientAccount = recipientField.getText().trim();
            String amountText = transferField.getText().trim();
            handleTransfer(recipientAccount, amountText, recipientField, transferField, balanceValue);
        });

        searchSection.getChildren().addAll(searchLabel, searchBox);
        transactionContainer.getChildren().addAll(searchSection, customerInfoDisplay, transactionOperations);
        content.getChildren().addAll(header, transactionContainer);

        return content;
    }

    // Transaction handling methods
    private void handleSearchAccount(String accountNumber, VBox customerInfoDisplay, VBox transactionOperations,
                                     Label nameLabel, Label accountLabel, Label balanceLabel, Label phoneLabel) {
        if (accountNumber.isEmpty()) {
            showAlert("Error", "Please enter an account number.");
            return;
        }

        if (!accountNumber.matches("s\\d{9}")) {
            showAlert("Error", "Please enter a valid account number (e.g., s123456789).");
            return;
        }

        currentCustomer = findCustomerByAccountNumber(accountNumber);
        if (currentCustomer != null) {
            // Display customer information
            nameLabel.setText(currentCustomer.getFullName());
            accountLabel.setText(currentCustomer.getAccountNumber());
            balanceLabel.setText(String.format("ETB%.2f", currentCustomer.getBalance()));
            phoneLabel.setText(currentCustomer.getPhone());

            customerInfoDisplay.setVisible(true);
            transactionOperations.setVisible(true);
        } else {
            showAlert("Not Found", "Account number not found: " + accountNumber);
            customerInfoDisplay.setVisible(false);
            transactionOperations.setVisible(false);
        }
    }

    private void handleDeposit(String amountText, TextField depositField, Label balanceLabel) {
        if (currentCustomer == null) {
            showAlert("Error", "Please search for an account first.");
            return;
        }

        if (amountText.isEmpty()) {
            showAlert("Error", "Please enter deposit amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Error", "Amount must be positive.");
                return;
            }

            currentCustomer.deposit(amount);
            balanceLabel.setText(String.format("ETB%.2f", currentCustomer.getBalance()));
            depositField.clear();

            showTransactionSuccessAlert("Deposit Successful", "💰",
                    String.format("ETB%.2f deposited to %s's account", amount, currentCustomer.getFullName()),
                    String.format("New Balance: ETB%.2f", currentCustomer.getBalance()));

            updateSidebarStats();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.");
        }
    }

    private void handleWithdraw(String amountText, TextField withdrawField, Label balanceLabel) {
        if (currentCustomer == null) {
            showAlert("Error", "Please search for an account first.");
            return;
        }

        if (amountText.isEmpty()) {
            showAlert("Error", "Please enter withdrawal amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (currentCustomer.withdraw(amount)) {
                balanceLabel.setText(String.format("ETB%.2f", currentCustomer.getBalance()));
                withdrawField.clear();

                showTransactionSuccessAlert("Withdrawal Successful", "💳",
                        String.format("ETB%.2f withdrawn from %s's account", amount, currentCustomer.getFullName()),
                        String.format("Remaining Balance: ETB%.2f", currentCustomer.getBalance()));

                updateSidebarStats();
            } else {
                showAlert("Error", "Insufficient funds or invalid amount.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.");
        }
    }

    private void handleTransfer(String recipientAccount, String amountText, TextField recipientField,
                                TextField transferField, Label balanceLabel) {
        if (currentCustomer == null) {
            showAlert("Error", "Please search for an account first.");
            return;
        }

        if (recipientAccount.isEmpty() || amountText.isEmpty()) {
            showAlert("Error", "Please enter recipient account and amount.");
            return;
        }

        if (recipientAccount.equals(currentCustomer.getAccountNumber())) {
            showAlert("Error", "Cannot transfer to the same account.");
            return;
        }

        Customer recipient = findCustomerByAccountNumber(recipientAccount);
        if (recipient == null) {
            showAlert("Error", "Recipient account not found: " + recipientAccount);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (currentCustomer.transfer(recipient, amount)) {
                balanceLabel.setText(String.format("ETB%.2f", currentCustomer.getBalance()));
                recipientField.clear();
                transferField.clear();

                showTransactionSuccessAlert("Transfer Successful", "🔄",
                        String.format("ETB%.2f transferred from %s to %s",
                                amount, currentCustomer.getFullName(), recipient.getFullName()),
                        String.format("Your Balance: ETB%.2f\nRecipient Balance: ETB%.2f",
                                currentCustomer.getBalance(), recipient.getBalance()));

                updateSidebarStats();
            } else {
                showAlert("Error", "Insufficient funds or invalid amount.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.");
        }
    }

    private Customer findCustomerByAccountNumber(String accountNumber) {
        for (Customer customer : customers) {
            if (customer.getAccountNumber().equals(accountNumber)) {
                return customer;
            }
        }
        return null;
    }

    // Custom transaction success alert with branding
    private void showTransactionSuccessAlert(String title, String icon, String message, String details) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Create custom dialog pane with branding
        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-alignment: center;");

        // Bank branding header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        Label bankIcon = new Label("🏦");
        bankIcon.setStyle("-fx-font-size: 24px;");
        Label bankName = new Label("SINQEE BANK");
        bankName.setStyle("-fx-text-fill: #2f4411; -fx-font-size: 18px; -fx-font-weight: bold;");
        header.getChildren().addAll(bankIcon, bankName);

        // Success icon and message
        VBox successBox = new VBox(10);
        successBox.setAlignment(Pos.CENTER);
        Label successIcon = new Label(icon);
        successIcon.setStyle("-fx-font-size: 48px;");
        Label successTitle = new Label(title);
        successTitle.setStyle("-fx-text-fill: #F4941C; -fx-font-size: 20px; -fx-font-weight: bold;");
        successBox.getChildren().addAll(successIcon, successTitle);

        // Transaction details
        VBox detailsBox = new VBox(8);
        detailsBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-alignment: center;");
        detailsBox.setPrefWidth(350);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: #2f4411; -fx-font-weight: bold; -fx-font-size: 14px; -fx-alignment: center;");
        messageLabel.setWrapText(true);

        Label detailsLabel = new Label(details);
        detailsLabel.setStyle("-fx-text-fill: #F4941C; -fx-font-weight: bold; -fx-font-size: 16px; -fx-alignment: center;");
        detailsLabel.setWrapText(true);

        detailsBox.getChildren().addAll(messageLabel, detailsLabel);

        // Footer message
        Label footer = new Label("Transaction completed successfully! ✅");
        footer.setStyle("-fx-text-fill: #666; -fx-font-size: 12px; -fx-font-style: italic;");

        content.getChildren().addAll(header, successBox, detailsBox, footer);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefSize(400, 400);
        alert.showAndWait();
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sinqee Bank Login");
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Logout failed: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getter for customers list
    public ObservableList<Customer> getCustomers() {
        return customers;
    }
}