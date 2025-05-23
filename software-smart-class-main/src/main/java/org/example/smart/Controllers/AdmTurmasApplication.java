package org.example.smart.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdmTurmasApplication extends Application {
    Stage janela;
    @Override
    public void start(Stage stage) throws Exception {
        janela = stage;

        Parent tela = FXMLLoader.load(getClass().getResource("/org/example/smart/AdmTurmas.fxml"));

        Scene scene = new Scene(tela);
        janela.setScene(scene);
        janela.show();

    }
}
