package siinqee_banking.model;

public class User {
    private String userId;
    private String username;
    private String password;
    private String role; // ADMIN, STAFF
    private String fullName;
    private String email;
    private String phone;

    public User(String userId, String username, String password, String role, String fullName, String email, String phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}