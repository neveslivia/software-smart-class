package org.example.smart.Controllers.Remover;

import Banco.Conexao;
import DAO.TurmaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import model.Turma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RemoverTurmaController {

    @FXML private TreeTableView<Turma> TreeTableViewRemoverTurma;
    @FXML private TreeTableColumn<Turma, String> Col_IDTurmaT;
    @FXML private TreeTableColumn<Turma, String> Col_NomeTurmaT;
    @FXML private TreeTableColumn<Turma, String> Col_AnoLetivoT;
    @FXML private TreeTableColumn<Turma, String> Col_IDCursoT;

    @FXML private Button btnRemoverTurma;

    private TurmaDAO turmaDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conexao.getConexao();
            turmaDAO = new TurmaDAO(conn);

            configurarColunas();
            carregarTabela();

            btnRemoverTurma.setOnAction(this::removerTurmaSelecionada);

        } catch (SQLException e) {
            mostrarAlerta("Erro ao conectar", e.getMessage());
        }
    }

    private void configurarColunas() {
        Col_IDTurmaT.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        Col_NomeTurmaT.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        Col_AnoLetivoT.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getAnoLetivo())));
        Col_IDCursoT.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getIdCurso())));
    }

    private void carregarTabela() {
        try {
            List<Turma> turmas = turmaDAO.listarTurmas();
            TreeItem<Turma> root = new TreeItem<>();
            for (Turma turma : turmas) {
                root.getChildren().add(new TreeItem<>(turma));
            }
            TreeTableViewRemoverTurma.setRoot(root);
            TreeTableViewRemoverTurma.setShowRoot(false);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar dados", e.getMessage());
        }
    }

    private void removerTurmaSelecionada(ActionEvent event) {
        TreeItem<Turma> itemSelecionado = TreeTableViewRemoverTurma.getSelectionModel().getSelectedItem();

        if (itemSelecionado == null) {
            mostrarAlerta("Nenhuma seleção", "Selecione uma turma na tabela para remover.");
            return;
        }

        Turma turma = itemSelecionado.getValue();
        int idTurma = turma.getId();

        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText("Tem certeza que deseja remover a turma?");
        confirmacao.setContentText(
                "Todos os boletins e alunos relacionados a esta turma também serão removidos.\n\n"
                        + "Turma: " + turma.getNome()
                        + "\nID: " + idTurma
        );

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                excluirTurmaComDependencias(idTurma);
                mostrarAlerta("Sucesso", "Turma e dependências removidas com sucesso.");
                carregarTabela();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao remover", e.getMessage());
            }
        }
    }

    private void excluirTurmaComDependencias(int idTurma) throws SQLException {
        Connection conn = Conexao.getConexao();

        try {
            conn.setAutoCommit(false);

            // Excluir boletins
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM boletim WHERE id_turma = ?")) {
                stmt.setInt(1, idTurma);
                stmt.executeUpdate();
            }

            // Excluir alunos da turma
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM aluno WHERE id_turma = ?")) {
                stmt.setInt(1, idTurma);
                stmt.executeUpdate();
            }

            // Excluir a turma
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM turma WHERE id_turma = ?")) {
                stmt.setInt(1, idTurma);
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    public void AcessarHOMERemoverTurma(ActionEvent event) {
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
