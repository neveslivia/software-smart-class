package org.example.smart.Controllers.Menu;

import DAO.TurmaDAO;
import Banco.Conexao;
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
import model.Turma;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MenuTurmasADMController {

    @FXML private TreeTableView<Turma> TreeTableViewTurma;
    @FXML private TreeTableColumn<Turma, String> ID_Turma;
    @FXML private TreeTableColumn<Turma, String> Nome_Turma;
    @FXML private TreeTableColumn<Turma, String> AnoLetivo_Turma;
    @FXML private TreeTableColumn<Turma, String> CURSOTurma;

    private TurmaDAO turmaDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conexao.getConexao();
            turmaDAO = new TurmaDAO(conn);

            configurarColunas();
            carregarTurmas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configurarColunas() {
        ID_Turma.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getId())));
        Nome_Turma.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getNome()));
        AnoLetivo_Turma.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getAnoLetivo())));
        CURSOTurma.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getIdCurso())));

        TreeTableViewTurma.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarTurmas() {
        try {
            List<Turma> turmas = turmaDAO.listarTurmas();
            TreeItem<Turma> root = new TreeItem<>();
            for (Turma turma : turmas) {
                root.getChildren().add(new TreeItem<>(turma));
            }
            TreeTableViewTurma.setRoot(root);
            TreeTableViewTurma.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void retornaHomeTurma(ActionEvent event) {
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

    public void AcessarAtualizarTurma(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/AtualizarTurma.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarAdicionarTurma(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/CadastroTurma.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessarRemoverTurma(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/RemoverTurmas.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
