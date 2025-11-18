package siinqee_banking.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/siinqee_banking/view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Siinqee Bank Management System");
        stage.setScene(scene);
        stage.setMaximized(true); // start full screen
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
