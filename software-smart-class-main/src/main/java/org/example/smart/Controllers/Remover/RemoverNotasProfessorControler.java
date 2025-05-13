package org.example.smart.Controllers.Remover;

import Banco.Conexao;
import DAO.BoletimDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import model.Boletim;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class RemoverNotasProfessorControler implements Initializable {

    @FXML
    private Button AcessarRemoverNotasADM;

    @FXML
    private TreeTableColumn<Boletim, Number> columidalunonotasprofessor;

    @FXML
    private TreeTableColumn<Boletim, Number> columidturmanotasprofessor;

    @FXML
    private TreeTableColumn<Boletim, Number> columiddiciplinanotasprofessor;

    @FXML
    private TreeTableColumn<Boletim, Number> columnotasboletim;

    @FXML
    private TreeTableColumn<Boletim, String> col_notaprofessornotas;

    @FXML
    private TreeTableColumn<Boletim, String> col_situacaoprofessornotas;

    @FXML
    private TreeTableView<Boletim> treetablecolumprofessornotas;

    @FXML
    private Button homeNOTASP;

    @FXML
    private Button sbtnLancamentoNota;

    @FXML
    private Button btnatualizarprofessor;

    @FXML
    private Button btnadicionarprofessor;

    @FXML
    private Button btnremoverprofessor;

    private BoletimDAO boletimDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection conexao = Conexao.getConexao();
            boletimDAO = new BoletimDAO(conexao);
            configurarColunas();
            atualizarTabela();

            btnatualizarprofessor.setOnAction(event -> atualizarTabela());
            btnadicionarprofessor.setOnAction(event -> adicionarNota());
            btnremoverprofessor.setOnAction(event -> removerNota());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColunas() {
        columidalunonotasprofessor.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getIdAluno()));

        columidturmanotasprofessor.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getIdTurma()));

        columiddiciplinanotasprofessor.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getIdDisciplina()));

        columnotasboletim.setCellValueFactory(param ->
                new SimpleDoubleProperty(param.getValue().getValue().getNota()));

        col_notaprofessornotas.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getValue().getNota())));

        col_situacaoprofessornotas.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getSituacao()));
    }

    private void atualizarTabela() {
        try {
            List<Boletim> boletins = boletimDAO.listarBoletins();
            TreeItem<Boletim> root = new TreeItem<>();
            for (Boletim b : boletins) {
                root.getChildren().add(new TreeItem<>(b));
            }
            treetablecolumprofessornotas.setRoot(root);
            treetablecolumprofessornotas.setShowRoot(false);
            root.setExpanded(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarNota() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adicionar Nota");
        alert.setHeaderText("Funcionalidade ainda não implementada.");
        alert.setContentText("Você pode implementar um formulário para inserir uma nova nota.");
        alert.showAndWait();
    }

    private void removerNota() {
        TreeItem<Boletim> selectedItem = treetablecolumprofessornotas.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Boletim boletim = selectedItem.getValue();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Remover Nota");
            confirm.setHeaderText("Tem certeza que deseja remover esta nota?");
            confirm.setContentText("ID do Aluno: " + boletim.getIdAluno() + "\nNota: " + boletim.getNota());

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        boletimDAO.excluirBoletim(boletim.getId());
                        atualizarTabela();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert error = new Alert(Alert.AlertType.ERROR, "Erro ao remover nota.");
                        error.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione uma nota para remover.");
            alert.showAndWait();
        }
    }
}
