package org.example.smart.Controllers.Menu;

import Banco.Conexao;
import DAO.DisciplinaDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import model.Disciplina;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class MenuNotasProfessorControler implements Initializable {

    @FXML private TreeTableView<Disciplina> treetablecolumprofessor;
    @FXML private TreeTableColumn<Disciplina, Integer> columdiciplinaprofessor;
    @FXML private TreeTableColumn<Disciplina, String> columnomeprofessor;
    @FXML private TreeTableColumn<Disciplina, String> columnotasprofessor;
    @FXML private TreeTableColumn<Disciplina, Integer> cursoidprofessor;

    @FXML private Button btnatualizarprofessor;
    @FXML private Button btnadicionarprofessor;
    @FXML private Button btnremoverprofessor;

    private DisciplinaDAO disciplinaDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection conexao = Conexao.getConexao();
            disciplinaDAO = new DisciplinaDAO(conexao);
            configurarColunas();
            atualizarTabela();

            btnatualizarprofessor.setOnAction(event -> atualizarTabela());
            btnadicionarprofessor.setOnAction(event -> adicionarDisciplina());
            btnremoverprofessor.setOnAction(event -> removerDisciplina());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColunas() {
        columdiciplinaprofessor.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId_Disciplina()).asObject());

        columnomeprofessor.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNomeDisciplina()));

        columnotasprofessor.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNota()));

        cursoidprofessor.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId_Curso()).asObject());
    }

    private void atualizarTabela() {
        try {
            List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();
            TreeItem<Disciplina> root = new TreeItem<>();
            for (Disciplina d : disciplinas) {
                root.getChildren().add(new TreeItem<>(d));
            }
            treetablecolumprofessor.setRoot(root);
            treetablecolumprofessor.setShowRoot(false);
            root.setExpanded(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarDisciplina() {
        // Exemplo de alerta para o botão adicionar
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adicionar");
        alert.setHeaderText("Funcionalidade ainda não implementada.");
        alert.setContentText("Você pode implementar um formulário para inserir nova disciplina.");
        alert.showAndWait();
    }

    private void removerDisciplina() {
        TreeItem<Disciplina> selectedItem = treetablecolumprofessor.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Disciplina disciplina = selectedItem.getValue();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Remover Disciplina");
            confirm.setHeaderText("Tem certeza que deseja remover a disciplina?");
            confirm.setContentText("Disciplina: " + disciplina.getNomeDisciplina());

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        disciplinaDAO.removerDisciplina(disciplina.getId_Disciplina());
                        atualizarTabela();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert error = new Alert(Alert.AlertType.ERROR, "Erro ao remover disciplina.");
                        error.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione uma disciplina para remover.");
            alert.showAndWait();
        }
    }
}
