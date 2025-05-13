package org.example.smart.Controllers.Menu;

import DAO.ProfessorDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import model.Professor;

import java.sql.SQLException;
import java.util.List;

public class MenuProfessorADMController {

    @FXML
    private TreeTableView<Professor> TreeTableViewProfessores;
    @FXML
    private TreeTableColumn<Professor, String> col_ID;
    @FXML
    private TreeTableColumn<Professor, String> col_Nome;
    @FXML
    private TreeTableColumn<Professor, String> col_Disciplina;
    @FXML
    private TreeTableColumn<Professor, String> col_Email;
    @FXML
    private TreeTableColumn<Professor, String> col_Telefone;
    @FXML
    private TreeTableColumn<Professor, String> col_CPF;

    private final ProfessorDAO professorDAO = new ProfessorDAO();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarProfessores();
    }

    private void configurarColunas() {
        col_ID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getId())));
        col_Nome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getNome()));
        col_Disciplina.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getDisciplina()));
        col_Email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getEmail()));
        col_Telefone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTelefone()));
        col_CPF.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getCpf()));
        TreeTableViewProfessores.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarProfessores() {
        try {
            List<Professor> professores = professorDAO.listarTodos();
            TreeItem<Professor> root = new TreeItem<>();
            for (Professor professor : professores) {
                root.getChildren().add(new TreeItem<>(professor));
            }
            TreeTableViewProfessores.setRoot(root);
            TreeTableViewProfessores.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AcessarREMOVERPROFESSOR(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/RemoverProfessores.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarAdicionarProfessor(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/CadastroProfessores.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarAtualizarProfessor(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/AtualizarProfessor.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarHomeProfessor(ActionEvent event) {
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
}