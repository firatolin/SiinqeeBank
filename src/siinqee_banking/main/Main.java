package siinqee_banking.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import siinqee_banking.controller.LoginController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/login.fxml"));
        Parent root = loader.load();

        // Get the controller and initialize customer data
        LoginController loginController = loader.getController();
        loginController.initializeCustomerData();

        primaryStage.setTitle("Sinqee Bank Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}