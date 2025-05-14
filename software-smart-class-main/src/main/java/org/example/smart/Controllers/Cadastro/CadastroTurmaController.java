package org.example.smart.Controllers.Cadastro;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import model.Curso;
import model.Turma;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CadastroTurmaController {

    // TextFields
    @FXML private TextField txt_NomeTurma;
    @FXML private TextField txt_AnoLetivo;
    @FXML private TextField txt_IDCurso;

    // Botões
    @FXML private Button btncCadastrarTurma;
    @FXML private Button btnAtualizarCadastroTurma;

    // TreeTables
    @FXML private TreeTableView<Curso> TreeTableViewCursoTurma;
    @FXML private TreeTableColumn<Curso, String> Col_ID_CursoTURMA;
    @FXML private TreeTableColumn<Curso, String> Col_Nome_CursoTURMA;
    @FXML private TreeTableColumn<Curso, String> Col_DescricaoCurso_TURMA;

    @FXML private TreeTableView<Turma> TreeTableViewTTurma;
    @FXML private TreeTableColumn<Turma, String> Col_IDCadastro_TURMA;
    @FXML private TreeTableColumn<Turma, String> Col_NomeTurma_TURMA;
    @FXML private TreeTableColumn<Turma, String> Col_AnoLetivo_TURMA;
    @FXML private TreeTableColumn<Turma, String> Col_ID_Curso_TURMA;

    private CursoDAO cursoDAO;
    private TurmaDAO turmaDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conexao.getConexao();
            cursoDAO = new CursoDAO(); // ✅ Agora usa o construtor padrão do CursoDAO
            turmaDAO = new TurmaDAO(conn);

            configurarColunas();
            carregarTabelas();

            btncCadastrarTurma.setOnAction(e -> cadastrarTurma());
            btnAtualizarCadastroTurma.setOnAction(e -> carregarTabelas());

            // Formatações de entrada
            configurarFormatadores();

        } catch (SQLException e) {
            mostrarAlerta("Erro ao conectar", e.getMessage());
        }
    }

    private void configurarFormatadores() {
        txt_AnoLetivo.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d{0,4}") ? change : null;
        }));

        txt_IDCurso.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d{0,10}") ? change : null;
        }));

        txt_NomeTurma.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().length() <= 50 ? change : null;
        }));
    }

    private void configurarColunas() {
        Col_ID_CursoTURMA.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        Col_Nome_CursoTURMA.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        Col_DescricaoCurso_TURMA.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getDescricao()));

        Col_IDCadastro_TURMA.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        Col_NomeTurma_TURMA.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        Col_AnoLetivo_TURMA.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getAnoLetivo())));
        Col_ID_Curso_TURMA.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getIdCurso())));
    }

    private void carregarTabelas() {
        try {
            List<Curso> cursos = cursoDAO.listarCursos();
            TreeItem<Curso> rootCursos = new TreeItem<>();
            for (Curso curso : cursos) {
                rootCursos.getChildren().add(new TreeItem<>(curso));
            }
            TreeTableViewCursoTurma.setRoot(rootCursos);
            TreeTableViewCursoTurma.setShowRoot(false);

            List<Turma> turmas = turmaDAO.listarTurmas();
            TreeItem<Turma> rootTurmas = new TreeItem<>();
            for (Turma turma : turmas) {
                rootTurmas.getChildren().add(new TreeItem<>(turma));
            }
            TreeTableViewTTurma.setRoot(rootTurmas);
            TreeTableViewTTurma.setShowRoot(false);

        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar dados", e.getMessage());
        }
    }

    private void cadastrarTurma() {
        try {
            String nome = txt_NomeTurma.getText().trim();
            String anoLetivoStr = txt_AnoLetivo.getText().trim();
            String idCursoStr = txt_IDCurso.getText().trim();

            if (nome.isEmpty() || anoLetivoStr.isEmpty() || idCursoStr.isEmpty()) {
                mostrarAlerta("Campos obrigatórios", "Preencha todos os campos.");
                return;
            }

            int anoLetivo = Integer.parseInt(anoLetivoStr);
            int idCurso = Integer.parseInt(idCursoStr);

            Turma novaTurma = new Turma(nome, anoLetivo, idCurso);
            boolean sucesso = turmaDAO.criarTurma(novaTurma);

            if (sucesso) {
                mostrarAlerta("Sucesso", "Turma cadastrada com sucesso!");
                limparCampos();
                carregarTabelas();
            } else {
                mostrarAlerta("Erro", "Erro ao cadastrar turma.");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro ao cadastrar", e.getMessage());
        }
    }

    private void limparCampos() {
        txt_NomeTurma.clear();
        txt_AnoLetivo.clear();
        txt_IDCurso.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public void AcessarHomeCadastroTurma(ActionEvent event) {
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
