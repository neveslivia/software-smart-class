package org.example.smart.Controllers.Transicao;

import DAO.CursoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Curso;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaCursoController implements Initializable {
    @FXML
    private Button bt_cadastrar;

    @FXML
    private Button bt_fechar;

    @FXML
    private Button bt_home;

    @FXML
    private TextField txt_descricao;

    @FXML
    private TextField txt_nomeCurso;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cursoDAO = new CursoDAO();
        bt_cadastrar.setOnAction(event -> cadastrarCurso());
        bt_fechar.setOnAction(event -> fecharTela());
        bt_home.setOnAction(this::irParaHome);

    }
    CursoDAO cursoDAO;

    @FXML
    private void cadastrarCurso() {
        String nome = txt_nomeCurso.getText().trim();
        String descricao = txt_descricao.getText().trim();

        if (nome.isEmpty() || descricao.isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha todos os campos antes de cadastrar.", Alert.AlertType.WARNING);
            return;
        }

        Curso curso = new Curso();
        curso.setNome(nome);
        curso.setDescricao(descricao);

        boolean sucesso = cursoDAO.adicionarCurso(curso);

        if (sucesso) {
            mostrarAlerta("Sucesso", "Curso cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
        } else {
            mostrarAlerta("Erro", "O curso não pôde ser cadastrado. Tente novamente.", Alert.AlertType.ERROR);
        }

    }
    private void limparCampos() {
        txt_nomeCurso.clear();
        txt_descricao.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
    public void irParaHome(ActionEvent event) {
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
    @FXML
    private void fecharTela() {
        Stage stage = (Stage) bt_fechar.getScene().getWindow();
        stage.close();
    }
}
