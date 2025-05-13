package org.example.smart.Controllers.Remover;

import Banco.Conexao;
import DAO.DisciplinaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoverDisciplinaController implements Initializable {

    @FXML
    private Button btnremoverdiciplina;

    @FXML
    private Button btnhomediciplina;

    @FXML
    private TextField idtextfielddiciplina;

    @FXML
    private TreeTableView<Disciplina> idtreetableview;

    @FXML
    private TreeTableColumn<Disciplina, String> iddiciplinacolum;

    @FXML
    private TreeTableColumn<Disciplina, String> idnomecolum;

    @FXML
    private TreeTableColumn<Disciplina, String> idcursocolum;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColunas();
        carregarDisciplinas();

        btnremoverdiciplina.setOnAction(event -> removerDisciplina());
        btnhomediciplina.setOnAction(event -> navegarParaHome());
    }

    private void configurarColunas() {
        iddiciplinacolum.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId_Disciplina())));
        idnomecolum.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNomeDisciplina()));
        idcursocolum.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId_Curso())));
    }

    private void carregarDisciplinas() {
        TreeItem<Disciplina> root = new TreeItem<>();
        root.setExpanded(true);

        try (Connection conexao = Conexao.getConexao()) {
            DisciplinaDAO dao = new DisciplinaDAO(conexao
            );
            List<Disciplina> lista = dao.listarDisciplinas();

            for (Disciplina d : lista) {
                TreeItem<Disciplina> item = new TreeItem<>(d);
                root.getChildren().add(item);
            }

            idtreetableview.setRoot(root);
            idtreetableview.setShowRoot(false);

        } catch (SQLException e) {
            exibirAlerta("Erro", "Erro ao carregar disciplinas: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void removerDisciplina() {
        TreeItem<Disciplina> selectedItem = idtreetableview.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            confirmarERemover(selectedItem.getValue().getId_Disciplina());
        } else {
            String idText = idtextfielddiciplina.getText().trim();
            if (idText.isEmpty()) {
                exibirAlerta("Atenção", "Informe o ID da disciplina ou selecione uma da tabela.", Alert.AlertType.WARNING);
            } else {
                try {
                    int id = Integer.parseInt(idText);
                    confirmarERemover(id);
                } catch (NumberFormatException e) {
                    exibirAlerta("Erro", "ID inválido. Digite apenas números.", Alert.AlertType.ERROR);
                }
            }
        }
    }

    private void confirmarERemover(int idDisciplina) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Remover Disciplina");
        confirmacao.setContentText("Deseja realmente remover a disciplina com ID " + idDisciplina + "?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conexao = Conexao.getConexao()) {
                DisciplinaDAO dao = new DisciplinaDAO(conexao);
                dao.excluirDisciplina(idDisciplina);

                exibirAlerta("Sucesso", "Disciplina removida com sucesso!", Alert.AlertType.INFORMATION);
                carregarDisciplinas();
                idtextfielddiciplina.clear();

            } catch (SQLException e) {
                exibirAlerta("Erro", "Erro ao remover disciplina: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void navegarParaHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeADM.fxml"));
            Stage stage = (Stage) btnhomediciplina.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            exibirAlerta("Erro", "Erro ao carregar tela inicial: " + e.getMessage(), Alert.AlertType.ERROR);
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
}
