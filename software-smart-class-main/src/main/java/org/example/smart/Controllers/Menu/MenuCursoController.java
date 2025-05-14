package org.example.smart.Controllers.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuCursoController {

    public void AcessarHOME_Cursos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeADM.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btAdicionarCurso(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/AdicionarCurso.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RemoverCurso(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/RemoverCurso.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bt_atualizarCurso(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/AtualizarCurso.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
