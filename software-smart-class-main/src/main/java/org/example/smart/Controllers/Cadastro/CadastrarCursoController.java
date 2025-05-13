package org.example.smart.Controllers.Cadastro;

import DAO.CursoDAO;
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
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CadastrarCursoController implements Initializable{

    @FXML
    private TreeTableView<Curso> TableCursos;

    @FXML
    private Button bt_cadastrar;

    @FXML
    private Button bt_fechar;

    @FXML
    private Button bt_home;

    @FXML
    private TreeTableColumn<Curso, String> colDescricao;

    @FXML
    private TreeTableColumn<Curso, Integer> colIdCurso;

    @FXML
    private TreeTableColumn<Curso, String> colNomeCurso;

    @FXML
    private TextField txt_descricao;

    @FXML
    private TextField txt_nomeCurso;


    private final CursoDAO cursoDAO = new CursoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColunas();
        try {
            carregarCursos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        bt_cadastrar.setOnAction(this::cadastrarCurso);
        bt_fechar.setOnAction(e -> fecharTela());
        bt_home.setOnAction(this::irParaHome);
    }

    private void configurarColunas() {
        colIdCurso.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        colNomeCurso.setCellValueFactory(new TreeItemPropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new TreeItemPropertyValueFactory<>("descricao"));
    }

    @FXML
    private void cadastrarCurso(ActionEvent actionEvent) {
        String nome = txt_nomeCurso.getText().trim();
        String descricao = txt_descricao.getText().trim();

        if (nome.isEmpty() || descricao.isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha todos os campos antes de cadastrar.", Alert.AlertType.WARNING);
            return;
        }

        Curso curso = new Curso();
        curso.setNome(nome);
        curso.setDescricao(descricao);


        try {
            boolean sucesso = cursoDAO.adicionarCurso(curso);
            if (sucesso) {
                mostrarAlerta("Sucesso", "Curso cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                limparCampos();
                carregarCursos(); // <- atualiza a tabela
            } else {
                mostrarAlerta("Erro", "O curso não pôde ser cadastrado. Tente novamente.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro de banco", "Erro ao acessar o banco de dados: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void carregarCursos() throws SQLException {
        List<Curso> cursos = cursoDAO.listarCursos();
        TreeItem<Curso> raiz = new TreeItem<>();
        for (Curso curso : cursos) {
            raiz.getChildren().add(new TreeItem<>(curso));
        }
        TableCursos.setRoot(raiz);
        TableCursos.setShowRoot(false);
    }

    private void limparCampos() {
        txt_nomeCurso.clear();
        txt_descricao.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void fecharTela() {
        Stage stage = (Stage) bt_fechar.getScene().getWindow();
        stage.close();
    }


    private void irParaHome(javafx.event.ActionEvent event) {
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

