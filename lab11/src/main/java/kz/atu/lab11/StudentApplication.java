package kz.atu.lab11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                StudentApplication.class.getResource("student-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Каталог студентов");
        stage.setScene(scene);
        stage.setMinWidth(640);
        stage.setMinHeight(460);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
