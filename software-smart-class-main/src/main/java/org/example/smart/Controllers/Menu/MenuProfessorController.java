package org.example.smart.Controllers.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuProfessorController {

    @FXML
    private TextField senhaProfessor; // deve estar declarado no FXML

    public void entrarProfessor(ActionEvent event) {
        String senhaDigitada = senhaProfessor.getText().trim();

        if (senhaDigitada.isEmpty()) {
            exibirAlerta("Erro", "Digite a senha antes de continuar.");
            return;
        }

        if (verificarSenhaNoBanco(senhaDigitada)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeProfessor.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                exibirAlerta("Erro", "Erro ao carregar a tela do professor.");
            }
        } else {
            exibirAlerta("Erro de Login", "Senha incorreta! Tente novamente.");
            senhaProfessor.clear();
        }
    }

    private boolean verificarSenhaNoBanco(String senha) {
        String sql = "SELECT * FROM professor WHERE senha = ?";

        try (Connection conn = Banco.Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
