package org.example.smart.Controllers.Cadastro;

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
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import model.Curso;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CadastrarCursoController implements Initializable{

    @FXML
    private TreeTableColumn<Curso, String> Col_DescricaoCurso;

    @FXML
    private TreeTableColumn<Curso, String> Col_IDCurso;

    @FXML
    private TreeTableColumn<Curso, String> Col_NomeCurso;

    @FXML
    private TreeTableView<Curso> TreeTableViewCurso;

    @FXML
    private Button btnAtualizarCadastroCurso;

    @FXML
    private Button btnCadastrarCurso;

    @FXML
    private TextField txtDescricaoCurso;

    @FXML
    private TextField txtNomeCurso;

    CursoDAO cursoDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection conn = Conexao.getConexao();
            cursoDAO = new CursoDAO(conn);


            configurarColunas();
            carregarTabelas();

            btnCadastrarCurso.setOnAction(e -> cadastrarCurso());
            btnAtualizarCadastroCurso.setOnAction(e -> carregarTabelas());

        } catch (SQLException e) {
            mostrarAlerta("Erro ao conectar", e.getMessage());
        }
    }

    private void configurarColunas() {
        Col_IDCurso.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        Col_NomeCurso.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        Col_DescricaoCurso.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getDescricao()));

    }


    private void carregarTabelas() {
        List<Curso> cursos = cursoDAO.listarCursos();
        TreeItem<Curso> rootCursos = new TreeItem<>();
        for (Curso curso : cursos) {
            rootCursos.getChildren().add(new TreeItem<>(curso));
        }
        TreeTableViewCurso.setRoot(rootCursos);
        TreeTableViewCurso.setShowRoot(false);


    }


    @FXML
    private void cadastrarCurso() {
        try {
            String nomeCurso = txtNomeCurso.getText().trim();
            String descricaoCurso = txtDescricaoCurso.getText().trim();

            if (nomeCurso.isEmpty() || descricaoCurso.isEmpty()) {
                mostrarAlerta("Campos obrigatórios", "Preencha todos os campos.");
                return;
            }


            Curso novoCurso = new Curso(nomeCurso,descricaoCurso);
            boolean sucesso = cursoDAO.adicionarCurso(novoCurso);

            if (sucesso) {
                mostrarAlerta("Sucesso", "Curso cadastrado com sucesso!");
                limparCampos();
                carregarTabelas();
            } else {
                mostrarAlerta("Erro", "Erro ao cadastrar curso.");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro ao cadastrar", e.getMessage());
        }

    }

    private void limparCampos() {
        txtNomeCurso.clear();
        txtDescricaoCurso.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }



    @FXML
    public void AcessarHomeCadastroCurso(ActionEvent event) {
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
}

