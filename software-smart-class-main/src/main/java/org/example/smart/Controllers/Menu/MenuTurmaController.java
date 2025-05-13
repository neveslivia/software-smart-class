package org.example.smart.Controllers.Menu;

import Banco.Conexao;
import DAO.TurmaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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

public class MenuTurmaController {

    @FXML
    private TreeTableView<Turma> TreeTableViewTurma;

    @FXML
    private TreeTableColumn<Turma, String> col_IDTurma;

    @FXML
    private TreeTableColumn<Turma, String> col_nome;

    @FXML
    private TreeTableColumn<Turma, String> col_AnoLetivo;

    @FXML
    private TreeTableColumn<Turma, String> col_IDCurso;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarTurmas();
    }

    private void configurarColunas() {
        col_IDTurma.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        col_nome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        col_AnoLetivo.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getAnoLetivo())));
        col_IDCurso.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getIdCurso())));

        TreeTableViewTurma.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarTurmas() {
        try {
            Connection conn = Conexao.getConexao();
            TurmaDAO turmaDAO = new TurmaDAO(conn);
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

    public void homeTURMASP(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeProfessor.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
