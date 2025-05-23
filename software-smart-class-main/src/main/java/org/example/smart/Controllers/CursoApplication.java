package org.example.smart.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class  CursoApplication extends Application {
    Stage janela;
    @Override
    public void start(Stage stage) throws Exception {
        janela = stage;

        Parent tela = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/smart/AdmCursos.fxml")));

        Scene scene = new Scene(tela);
        janela.setScene(scene);
        janela.show();

    }
}
