package org.example.smart.Controllers.Menu;

import DAO.AlunoDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import model.Aluno;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuAlunosController implements Initializable {

    @FXML private TreeTableView<Aluno> TabelaProfessorAluno;
    @FXML private TreeTableColumn<Aluno, Integer> col_IDALUNOP;
    @FXML private TreeTableColumn<Aluno, String> col_NOMEALUNOP;
    @FXML private TreeTableColumn<Aluno, Integer> col_IDturmaALUNOP;

    private AlunoDAO alunoDAO = new AlunoDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColunas();
        carregarDados();
    }

    private void configurarColunas() {
        col_IDALUNOP.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject());

        col_NOMEALUNOP.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNome()));

        col_IDturmaALUNOP.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getIdTurma()).asObject());
    }

    private void carregarDados() {
        List<Aluno> alunos = alunoDAO.buscarTodos();
        TreeItem<Aluno> root = new TreeItem<>();

        for (Aluno aluno : alunos) {
            TreeItem<Aluno> item = new TreeItem<>(aluno);
            root.getChildren().add(item);
        }

        TabelaProfessorAluno.setRoot(root);
        TabelaProfessorAluno.setShowRoot(false);
    }

    // Método para voltar para a tela do professor
    public void homeALUNOP(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/org/example/smart/homeProfessor.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
