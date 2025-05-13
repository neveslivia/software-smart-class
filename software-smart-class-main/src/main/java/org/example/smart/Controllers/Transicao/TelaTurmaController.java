package org.example.smart.Controllers.Transicao;

import Banco.Conexao;
import DAO.TurmaDAO;
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
import model.Turma;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaTurmaController implements Initializable {
    @FXML
    private Button bt_cadastrar;

    @FXML
    private Button bt_fechar;

    @FXML
    private Button bt_home;

    @FXML
    private TextField txt_anoLetivo;

    @FXML
    private TextField txt_idCurso;

    @FXML
    private TextField txt_nomeTurma;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            turmaDAO = new TurmaDAO(Conexao.getConexao());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bt_cadastrar.setOnAction(event -> cadastrarTurma());
        bt_home.setOnAction(this::irParaHome);
        bt_fechar.setOnAction(event -> fecharTela());
    }
    TurmaDAO turmaDAO;

    @FXML
    private void cadastrarTurma(){
        String nome = txt_nomeTurma.getText().trim();
        int anoLetivo= Integer.parseInt(txt_anoLetivo.getText().trim());
        int idCurso = Integer.parseInt(txt_idCurso.getText().trim());

        if (nome.isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha todos os campos antes de cadastrar.", Alert.AlertType.WARNING);
            return;
        }
        if (anoLetivo<= 0){
            mostrarAlerta("Entrada inválida!","Ano letivo deve ser maior que zero!", Alert.AlertType.WARNING);
        }

        Turma turma= new Turma();
        turma.setNome(nome);
        turma.setAnoLetivo(anoLetivo);
        turma.setIdCurso(idCurso);

        try {
            boolean sucesso = turmaDAO.criarTurma(turma);

            if (sucesso) {
                mostrarAlerta("Sucesso", "Turma cadastrada com sucesso!", Alert.AlertType.INFORMATION);
                limparCampos();
            } else {
                mostrarAlerta("Erro", "A turma não pôde ser cadastrada. Tente novamente.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            mostrarAlerta("Erro de banco", "Erro ao acessar o banco de dados: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }
    private void limparCampos() {
        txt_nomeTurma.clear();
        txt_anoLetivo.clear();
        txt_idCurso.clear();
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
