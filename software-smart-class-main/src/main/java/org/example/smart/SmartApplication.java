package org.example.smart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SmartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SmartApplication.class.getResource("telainicial.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500); // Tamanho ajustado para tela inicial
        stage.setTitle("Smart Class");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
