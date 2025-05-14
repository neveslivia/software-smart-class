package org.example.smart.Controllers.Atualizar;

import DAO.CursoDAO;
import DAO.TurmaDAO;
import Banco.Conexao;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Curso;
import model.Turma;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AtualizarTurmaController {

    // Campos de entrada
    @FXML private TextField txtIDTurma_Atualizar;
    @FXML private TextField txt_NomeTurma_Atualizar;
    @FXML private TextField txtAnoLetivo_Turma_Atualizar;
    @FXML private TextField txtIDCurso_Turma_Atualizar;

    // Botões
    @FXML private Button btn_FinalizarAtualizar_Turma;
    @FXML private Button btn_AtualizarBanco_AtTurma;

    // Tabela de Turmas
    @FXML private TreeTableView<Turma> treeTableTurmaAtualizar;
    @FXML private TreeTableColumn<Turma, String> Col_IDTurmaAtualizar;
    @FXML private TreeTableColumn<Turma, String> Col_NomeTurma_AtualizarT;
    @FXML private TreeTableColumn<Turma, String> Col_AnoLetivo_AtualizarTurma;
    @FXML private TreeTableColumn<Turma, String> Col_IDCurso_AtualizarTurma;

    // Tabela de Cursos
    @FXML private TreeTableView<Curso> treeTableTurmaAtualizar1;
    @FXML private TreeTableColumn<Curso, String> Col_IDCursoAtualizar;
    @FXML private TreeTableColumn<Curso, String> Col_NomeCursoAtualizar;
    @FXML private TreeTableColumn<Curso, String> Col_DescricaoAtualizar;

    private TurmaDAO turmaDAO;
    private CursoDAO cursoDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conexao.getConexao();
            turmaDAO = new TurmaDAO(conn);
            cursoDAO = new CursoDAO(); // ✅ Agora usa o construtor padrão da CursoDAO

            configurarColunas();
            carregarTabelas();

            treeTableTurmaAtualizar.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
                if (novo != null) preencherCamposComTurma(novo.getValue());
            });

            btn_FinalizarAtualizar_Turma.setOnAction(event -> finalizarAtualizacao());
            btn_AtualizarBanco_AtTurma.setOnAction(event -> carregarTabelas());

            txtAnoLetivo_Turma_Atualizar.setTextFormatter(new TextFormatter<>(change -> {
                String novoTexto = change.getControlNewText();
                if (!novoTexto.matches("\\d{0,4}")) {
                    return null;
                }
                return change;
            }));

        } catch (SQLException e) {
            mostrarAlerta("Erro de Conexão", "Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    private void configurarColunas() {
        Col_IDTurmaAtualizar.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        Col_NomeTurma_AtualizarT.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        Col_AnoLetivo_AtualizarTurma.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getAnoLetivo())));
        Col_IDCurso_AtualizarTurma.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getIdCurso())));

        Col_IDCursoAtualizar.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        Col_NomeCursoAtualizar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        Col_DescricaoAtualizar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getDescricao()));

        treeTableTurmaAtualizar.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableTurmaAtualizar1.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarTabelas() {
        try {
            // Turmas
            List<Turma> turmas = turmaDAO.listarTurmas();
            TreeItem<Turma> rootTurmas = new TreeItem<>();
            for (Turma turma : turmas) {
                rootTurmas.getChildren().add(new TreeItem<>(turma));
            }
            treeTableTurmaAtualizar.setRoot(rootTurmas);
            treeTableTurmaAtualizar.setShowRoot(false);

            // Cursos
            List<Curso> cursos = cursoDAO.listarCursos();
            TreeItem<Curso> rootCursos = new TreeItem<>();
            for (Curso curso : cursos) {
                rootCursos.getChildren().add(new TreeItem<>(curso));
            }
            treeTableTurmaAtualizar1.setRoot(rootCursos);
            treeTableTurmaAtualizar1.setShowRoot(false);

        } catch (SQLException e) {
            mostrarAlerta("Erro ao Carregar", "Não foi possível carregar os dados: " + e.getMessage());
        }
    }

    private void preencherCamposComTurma(Turma turma) {
        txtIDTurma_Atualizar.setText(String.valueOf(turma.getId()));
        txt_NomeTurma_Atualizar.setText(turma.getNome());
        txtAnoLetivo_Turma_Atualizar.setText(String.valueOf(turma.getAnoLetivo()));
        txtIDCurso_Turma_Atualizar.setText(String.valueOf(turma.getIdCurso()));
    }

    private void finalizarAtualizacao() {
        try {
            Alert confirmacao = new Alert(AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Atualização");
            confirmacao.setHeaderText("Deseja atualizar esta turma?");
            confirmacao.setContentText("Verifique os dados preenchidos antes de continuar.");
            Optional<ButtonType> resultado = confirmacao.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                int id = Integer.parseInt(txtIDTurma_Atualizar.getText());
                String nome = txt_NomeTurma_Atualizar.getText();
                int anoLetivo = Integer.parseInt(txtAnoLetivo_Turma_Atualizar.getText());
                int idCurso = Integer.parseInt(txtIDCurso_Turma_Atualizar.getText());

                Turma turmaAtualizada = new Turma(nome, anoLetivo, idCurso);
                turmaDAO.atualizarTurma(turmaAtualizada, id);

                mostrarAlerta("Sucesso", "Turma atualizada com sucesso!");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao atualizar turma: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public void AcessarHomeAtualizarTurma(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/admTurmas.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
