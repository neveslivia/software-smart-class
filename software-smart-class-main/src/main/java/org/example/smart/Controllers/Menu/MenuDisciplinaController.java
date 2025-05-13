package org.example.smart.Controllers.Menu;

import Banco.Conexao;
import DAO.DisciplinaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import model.Disciplina;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MenuDisciplinaController implements Initializable {

    @FXML
    private TreeTableColumn<Disciplina, String> columnomediciplinaadm;

    @FXML
    private TreeTableColumn<Disciplina, String> idcursodiciplinaadm;

    @FXML
    private TreeTableColumn<Disciplina, String> iddiciplinaadm;

    @FXML
    private Button retornaHomeDisciplina;

    @FXML
    private TreeTableView<Disciplina> treetableviewdiciplinaadm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColunas();
        carregarDisciplinas();

        retornaHomeDisciplina.setOnAction(this::retornaHomeDisciplina);
    }

    private void configurarColunas() {
        iddiciplinaadm.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId_Disciplina())));
        columnomediciplinaadm.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNomeDisciplina()));
        idcursodiciplinaadm.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId_Curso())));
    }

    private void carregarDisciplinas() {
        TreeItem<Disciplina> root = new TreeItem<>();
        root.setExpanded(true);

        try (Connection conexao = Conexao.getConexao()) {
            DisciplinaDAO dao = new DisciplinaDAO(conexao);
            List<Disciplina> lista = dao.listarDisciplinas();

            for (Disciplina d : lista) {
                TreeItem<Disciplina> item = new TreeItem<>(d);
                root.getChildren().add(item);
            }

            treetableviewdiciplinaadm.setRoot(root);
            treetableviewdiciplinaadm.setShowRoot(false);

        } catch (SQLException e) {
            exibirAlerta("Erro", "Erro ao carregar disciplinas: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void retornaHomeDisciplina(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeADM.fxml"));
            Stage stage = (Stage) retornaHomeDisciplina.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            exibirAlerta("Erro", "Erro ao carregar a tela inicial: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarAvisoNaoImplementado(String titulo) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(titulo + "\nEssa parte do sistema ainda não foi implementada.");
        alerta.showAndWait();
    }

    public void btndiciplinaadm(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/AtualizarDisciplina.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAvisoNaoImplementado("Atualizar Disciplina");
        }
    }

    public void btndiciplinasadicionaradm(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/CadastroDisciplina.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAvisoNaoImplementado("Cadastro de Disciplina");
        }
    }

    public void btnremoverdiciplinasadm(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/RemoverDisciplina.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAvisoNaoImplementado("Cadastro de Disciplina");
        }
    }
}
