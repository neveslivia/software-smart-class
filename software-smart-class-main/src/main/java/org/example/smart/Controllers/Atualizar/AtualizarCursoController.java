package org.example.smart.Controllers.Atualizar;

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

public class AtualizarCursoController {


        @FXML
        private Button AcessarHomeCurso;

        @FXML
        private Button btAtualizarCurso;

        @FXML
        private Button btFinalizarCurso;

        @FXML
        private TreeTableColumn<Curso, String> colDescricao;

        @FXML
        private TreeTableColumn<Curso, String> colDescricaoAtualizada;

        @FXML
        private TreeTableColumn<Curso, String> colIdCurso;

        @FXML
        private TreeTableColumn<Curso, String> colIdCursoAtualizar;

        @FXML
        private TreeTableColumn<Curso, String> colNomeCurso;

        @FXML
        private TreeTableColumn<Curso, String> colNomeCursoAtualizar;

        @FXML
        private TreeTableView<Curso> treeTableTurmaAtualizar;

        @FXML
        private TreeTableView<Curso> TableCursosAtualizar;

        @FXML
        private TextField txrNomeCurso;

        @FXML
        private TextField txtDescricao;

        @FXML
        private TextField txtIDCurso;

        private final CursoDAO cursoDAO = new CursoDAO();

        @FXML
        public void initialize() throws SQLException {
            configurarTabela();
            carregarCursos();
            btAtualizarCurso.setOnAction(e -> {
                try {
                    carregarCursos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            btFinalizarCurso.setOnAction(this::AtualizarCurso);
            AcessarHomeCurso.setOnAction( this::AcessarHomeAtualizarTurma);
        }

        private void configurarTabela() {
            colIdCursoAtualizar.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
            colNomeCursoAtualizar.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getNome()));
            colDescricaoAtualizada.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDescricao()));

            TableCursosAtualizar.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        }

        private void carregarCursos() throws SQLException {
            List<Curso> cursos = cursoDAO.listarCursos();
            TreeItem<Curso> root = new TreeItem<>();
            for (Curso curso : cursos) {
                root.getChildren().add(new TreeItem<>(curso));
            }
            TableCursosAtualizar.setRoot(root);
            TableCursosAtualizar.setShowRoot(false);
        }

        private void AtualizarCurso(ActionEvent event) {
            try {
                int id = Integer.parseInt(txtIDCurso.getText().trim());
                String nome = txrNomeCurso.getText().trim();
                String descricao = txtDescricao.getText().trim();

                Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacao.setTitle("Confirmação de Atualização");
                confirmacao.setHeaderText("Deseja atualizar os dados deste curso?");
                confirmacao.setContentText("ID: " + id + "\nNome: " + nome);

                Optional<ButtonType> resultado = confirmacao.showAndWait();

                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    try (Connection conn = Conexao.getConexao()) {
                        String sql = "UPDATE curso SET nome=?, descricao=? WHERE id_curso=?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, nome);
                        stmt.setString(2, descricao);
                        stmt.setInt(3, id);
                        stmt.executeUpdate();
                        mostrarAlerta("Sucesso", "Curso atualizado com sucesso!", Alert.AlertType.INFORMATION);
                        carregarCursos();
                    }
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao atualizar curso: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }

        private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensagem);
            alerta.showAndWait();
        }


        public void AcessarHomeAtualizarTurma(ActionEvent event) {
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


