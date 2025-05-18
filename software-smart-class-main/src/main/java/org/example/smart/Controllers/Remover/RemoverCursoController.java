package org.example.smart.Controllers.Remover;

import Banco.Conexao;
import DAO.CursoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RemoverCursoController {

    @FXML
    private Button btRemoverCurso;

    @FXML
    private TreeTableColumn<Curso, String> colDescricao;

    @FXML
    private TreeTableColumn<Curso,String > colIdCurso;

    @FXML
    private TreeTableColumn<Curso, String> colNomeCurso;

    @FXML
    private TreeTableView<Curso> TreeTableViewRemoverCurso;



    private CursoDAO cursoDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conexao.getConexao();
            cursoDAO= new CursoDAO(conn);

            configurarColunas();
            carregarTabela();

            btRemoverCurso.setOnAction(this::removerCursoSelecionado);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao conectar", e.getMessage());
        }
    }

    private void configurarColunas() {
        colIdCurso.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        colNomeCurso.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        colDescricao.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getDescricao())));
    }

    private void carregarTabela() {
        List<Curso> cursos = cursoDAO.listarCursos();
        TreeItem<Curso> root = new TreeItem<>();
        for (Curso curso: cursos) {
            root.getChildren().add(new TreeItem<>(curso));
        }
        TreeTableViewRemoverCurso.setRoot(root);
        TreeTableViewRemoverCurso.setShowRoot(false);
    }

    @FXML
    private void removerCursoSelecionado(ActionEvent event) {
        TreeItem<Curso> itemSelecionado = TreeTableViewRemoverCurso.getSelectionModel().getSelectedItem();

        if (itemSelecionado == null) {
            mostrarAlerta("Nenhuma seleção", "Selecione um curso na tabela para remover.");
            return;
        }

       Curso curso= itemSelecionado.getValue();
        int idCurso = curso.getId();

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText("Tem certeza que deseja remover o curso?");
        confirmacao.setContentText(
                "Todos os boletins e alunos relacionados a este curso também serão removidos.\n\n"
                        + "Curso: " + curso.getNome()
                        + "\nID: " + idCurso
        );

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                excluirCursoComDependencias(idCurso);
                mostrarAlerta("Sucesso", "Curso e dependências removidas com sucesso.");
                carregarTabela();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao remover", e.getMessage());
            }
        }
    }

    private void excluirCursoComDependencias(int idCurso) throws SQLException {
        Connection conn = Conexao.getConexao();

        try {
            conn.setAutoCommit(false);


            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM turma WHERE id_curso = ?")) {
                stmt.setInt(1, idCurso);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM disciplina WHERE id_curso = ?")) {
                stmt.setInt(1, idCurso);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM curso WHERE id_curso = ?")) {
                stmt.setInt(1, idCurso);
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
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    public void AcessarHomeRemoverCursos(ActionEvent event) {
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



