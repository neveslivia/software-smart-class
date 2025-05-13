package org.example.smart.Controllers.Cadastro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LancamentoNotasADMController {

    @FXML
    private Button homeNOTASP;



    // Método existente (se houver)
    @FXML
    void initialize() {
        // Inicialização, se necessário
    }

    // Método para o botão Home (se já existir)
    @FXML
    void voltarParaHome(ActionEvent event) {
        try {
            // Substitua "homeADM.fxml" pelo nome correto do seu arquivo FXML da tela Home
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/Views/homeADM.fxml"));
            Stage stage = (Stage) homeNOTASP.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void homelancarNOTA(ActionEvent event) {

    }
}