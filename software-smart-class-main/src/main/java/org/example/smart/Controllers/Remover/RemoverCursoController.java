package org.example.smart.Controllers.Remover;

import DAO.CursoDAO;
import javafx.beans.property.SimpleIntegerProperty;
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

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoverCursoController implements Initializable {

    @FXML
    private TreeTableView<Curso> TableCursos;

    @FXML
    private TreeTableColumn<Curso, String> colDescricaoCurso;

    @FXML
    private TreeTableColumn<Curso, Integer> colIdCurso;

    @FXML
    private TreeTableColumn<Curso, String> colNome;
    @FXML
    private Button AcessarHOME_Cursos;

    @FXML
    private Button btAdicionar;

    @FXML
    private Button btRemoverCurso;

    @FXML
    private Button bt_atualizarCurso;

    @FXML
    private TextField txtIdCurso;

    private CursoDAO cursoDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cursoDAO = new CursoDAO();
        configurarColunas();
        loadCursos();

        bt_atualizarCurso.setOnAction(event -> loadCursos());
    }


    private void configurarColunas() {
        colIdCurso.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject());

        colNome.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNome()));

        colDescricaoCurso.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getDescricao()));
    }

    @FXML
    private void loadCursos() {
        try {
            List<Curso> cursos = cursoDAO.listarCursos();

            TreeItem<Curso> rootItem = new TreeItem<>();
            for (Curso curso : cursos) {
                rootItem.getChildren().add(new TreeItem<>(curso));
            }

            TableCursos.setRoot(rootItem);
            TableCursos.setShowRoot(false);
            rootItem.setExpanded(true);
            TableCursos.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception e) {
            exibirAlerta("Erro ao carregar cursos", "Erro ao carregar os cursos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void removerCurso() throws SQLException {
        TreeItem<Curso> selectedItem = TableCursos.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Curso curso = selectedItem.getValue();
            confirmarERemover(curso.getId());
        } else {
            String idTexto = txtIdCurso.getText().trim();
            if (idTexto.isEmpty()) {
                exibirAlerta("Atenção", "Informe um ID ou selecione um curso na tabela.", Alert.AlertType.WARNING);
                return;
            }

            try {
                int id = Integer.parseInt(idTexto);
                confirmarERemover(id);
            } catch (NumberFormatException | SQLException e) {
                exibirAlerta("Erro", "ID inválido. Digite um número válido.", Alert.AlertType.ERROR);
            }
        }
    }

    private void confirmarERemover(int id) throws SQLException {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Remover Curso");
        confirmacao.setContentText("Tem certeza que deseja remover o curso com ID " + id + "?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean removido = cursoDAO.removerCurso(id);
            if (removido) {
                exibirAlerta("Sucesso", "Curso removido com sucesso!", Alert.AlertType.INFORMATION);
                loadCursos();
                txtIdCurso.clear();
            } else {
                exibirAlerta("Erro", "Curso com ID " + id + " não encontrado.", Alert.AlertType.ERROR);
            }
        }
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    public void homeAlunoRemover(ActionEvent event) {
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


