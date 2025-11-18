package siinqee_banking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, ACCOUNT_OPENING
    private String description;
    private double amount;
    private LocalDateTime timestamp;
    private String transactionId;

    public Transaction(String type, String description, double amount, LocalDateTime timestamp) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.timestamp = timestamp;
        this.transactionId = generateTransactionId();
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    // Getters
    public String getType() { return type; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getTransactionId() { return transactionId; }

    // Formatted getters for display
    public String getFormattedAmount() {
        if (amount >= 0) {
            return "+ETB" + String.format("%.2f", amount);
        } else {
            return "-ETB" + String.format("%.2f", Math.abs(amount));
        }
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }

    public String getTypeIcon() {
        switch (type) {
            case "DEPOSIT": return "📥";
            case "WITHDRAWAL": return "📤";
            case "TRANSFER_IN": return "➡️💰";
            case "TRANSFER_OUT": return "💰➡️";
            case "ACCOUNT_OPENING": return "🏦";
            default: return "💳";
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s: %s (%s)",
                getTypeIcon(),
                getFormattedAmount(),
                description,
                getFormattedTimestamp());
    }
}