package org.example.smart.Controllers.Menu;

import Banco.Conexao;
import DAO.CursoDAO;
import DAO.TurmaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import model.Curso;
import model.Turma;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MenuCursoController {


    @FXML
    private TreeTableView<Curso> TreeTableViewCursoMenu;

    @FXML
    private TreeTableColumn<Curso, String> descricaoCurso;

    @FXML
    private TreeTableColumn<Curso, String> idCurso;

    @FXML
    private TreeTableColumn<Curso, String> nomeCurso;



    private CursoDAO cursoDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conexao.getConexao();
            cursoDAO = new CursoDAO(conn);

            configurarColunas();
            carregarCursos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configurarColunas() {
        idCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getId())));
        nomeCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getNome()));
        descricaoCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getDescricao())));

        TreeTableViewCursoMenu.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarCursos() {

            List<Curso> cursos =cursoDAO.listarCursos();
            TreeItem<Curso> root = new TreeItem<>();
            for (Curso curso: cursos) {
                root.getChildren().add(new TreeItem<>(curso));
            }
            TreeTableViewCursoMenu.setRoot(root);
            TreeTableViewCursoMenu.setShowRoot(false);

    }


    public void retornarHomeCursos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/smart/homeADM.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarAtualizarCurso(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/smart/AtualizarCurso.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarAdicionarCurso(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/smart/AdicionarCurso.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarRemoverCurso(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/smart/RemoverCurso.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
