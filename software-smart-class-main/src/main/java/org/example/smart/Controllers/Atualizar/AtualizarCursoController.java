package org.example.smart.Controllers.Atualizar;

import Banco.Conexao;
import DAO.CursoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Curso;
import model.Turma;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AtualizarCursoController implements Initializable {


    @FXML
    private Button btAtualizarCurso;

    @FXML
    private Button btFinalizarCurso;

    @FXML
    private TreeTableColumn<Curso, String> colDescricao;

    @FXML
    private TreeTableColumn<Curso, String> colIdCurso;


    @FXML
    private TreeTableColumn<Curso, String> colNomeCurso;


    @FXML
    private TreeTableView<Curso> TreeTableViewAtualizarCurso;


    @FXML
    private TextField txNomeCurso;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtIDCurso;



    CursoDAO cursoDAO;
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection conn = Conexao.getConexao();
            cursoDAO = new CursoDAO(conn);


            carregarTabelas();
            configurarColunas();
            try {
                carregarCursos();
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Falha ao carregar cursos: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }

            btAtualizarCurso.setOnAction(actionEvent -> AtualizarCurso());

            btFinalizarCurso.setOnAction(actionEvent -> finalizarCurso());


            TreeTableViewAtualizarCurso.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Curso cursoSelecionado = newSelection.getValue();
                    txtIDCurso.setText(String.valueOf(cursoSelecionado.getId()));
                    txNomeCurso.setText(cursoSelecionado.getNome());
                    txtDescricao.setText(cursoSelecionado.getDescricao());
                }
            });

        } catch (SQLException e) {
            mostrarAlertaSemTipo("Erro", "Falha ao carregar cursos: " + e.getMessage());
            e.printStackTrace();
        }


    }


    private void configurarColunas() {
        colIdCurso.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
        colNomeCurso.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getNome()));
        colDescricao.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDescricao()));

        TreeTableViewAtualizarCurso.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarCursos() throws SQLException {
        List<Curso> cursos = cursoDAO.listarCursos();
        TreeItem<Curso> root = new TreeItem<>();
        for (Curso curso : cursos) {
            root.getChildren().add(new TreeItem<>(curso));
        }
        TreeTableViewAtualizarCurso.setRoot(root);
        TreeTableViewAtualizarCurso.setShowRoot(false);
    }

    private void carregarTabelas() {

        List<Curso> cursos = cursoDAO.listarCursos();
        TreeItem<Curso> rootCursos = new TreeItem<>();
        for (Curso curso : cursos) {
            rootCursos.getChildren().add(new TreeItem<>(curso));
        }
        TreeTableViewAtualizarCurso.setRoot(rootCursos);
        TreeTableViewAtualizarCurso.setShowRoot(false);

    }

    @FXML
    private void AtualizarCurso() {
        try {
            int id = Integer.parseInt(txtIDCurso.getText().trim());
            String nome = txNomeCurso.getText().trim();
            String descricao = txtDescricao.getText().trim();

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmação de Atualização");
            confirmacao.setHeaderText("Deseja atualizar os dados deste curso?");
            confirmacao.setContentText("ID: " + id + "\nNome: " + nome);

            Optional<ButtonType> resultado = confirmacao.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try (Connection conn = Conexao.getConexao()) {
                    String sql = "UPDATE curso SET nome_curso=?, descricao=? WHERE id_curso=?";
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

    private void mostrarAlertaSemTipo(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
    @FXML
    public void AcessarHomeAtualizarCurso(ActionEvent event) {
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
    private void finalizarCurso() {
        txtIDCurso.clear();
        txNomeCurso.clear();
        txtDescricao.clear();
        TreeTableViewAtualizarCurso.getSelectionModel().clearSelection();
        mostrarAlerta("Info", "Campos limpos!", Alert.AlertType.INFORMATION);
    }


}





