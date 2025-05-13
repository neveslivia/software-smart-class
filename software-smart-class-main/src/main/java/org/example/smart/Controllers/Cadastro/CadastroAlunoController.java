package org.example.smart.Controllers.Cadastro;

import DAO.TurmaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Banco.Conexao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.Turma;
import org.example.smart.Validador.ValidadorGERALALUNO;
import model.Aluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.List;

public class CadastroAlunoController {

    @FXML private TextField txtNomeAluno;
    @FXML private TextField txtNASCIMENTOAluno;
    @FXML private TextField txtEMAILAluno;
    @FXML private TextField txtTELEFONEAluno;
    @FXML private TextField txtCPFAluno;
    @FXML private TextField txtTURMAIDAluno;
    @FXML private Button btnCadastrarAluno;

    @FXML private TreeTableView<Turma> treeTableCursos;
    @FXML private TreeTableColumn<Turma, String> colIdTurma;
    @FXML private TreeTableColumn<Turma, String> colNomeTurma;
    @FXML private TreeTableColumn<Turma, String> colAnoLetivoTurma;
    @FXML private TreeTableColumn<Turma, String> colIdCurso;

    @FXML private Button btnAtualizarAluno;

    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        configurarFormatadores();
        configurarTabelaTurmas();
        carregarTurmas();
        btnCadastrarAluno.setOnAction(e -> cadastrarAluno());
        btnAtualizarAluno.setOnAction(this::atualizarTabela);
    }

    private void configurarFormatadores() {
        txtCPFAluno.setTextFormatter(new TextFormatter<>(change -> {
            String digits = change.getControlNewText().replaceAll("[^\\d]", "");
            if (digits.length() > 11) return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digits.length(); i++) {
                if (i == 3 || i == 6) sb.append('.');
                if (i == 9) sb.append('-');
                sb.append(digits.charAt(i));
            }
            change.setText(sb.toString());
            change.setRange(0, change.getControlText().length());
            return change;
        }));

        txtTELEFONEAluno.setTextFormatter(new TextFormatter<>(change -> {
            String digits = change.getControlNewText().replaceAll("[^\\d]", "");
            if (digits.length() > 11) return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digits.length(); i++) {
                if (i == 0) sb.append('(');
                if (i == 2) sb.append(") ");
                if (i == 7) sb.append('-');
                sb.append(digits.charAt(i));
            }
            change.setText(sb.toString());
            change.setRange(0, change.getControlText().length());
            return change;
        }));

        txtNASCIMENTOAluno.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText().replaceAll("[^\\d]", "");
            if (text.length() > 8) return null;
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (i == 2 || i == 4) formatted.append('/');
                formatted.append(text.charAt(i));
            }
            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());
            return change;
        }));

        txtEMAILAluno.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 50) return null;
            return change;
        }));

        txtNomeAluno.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 50) return null;
            return change;
        }));

        txtTURMAIDAluno.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 10) return null;
            return change;
        }));
    }

    private void cadastrarAluno() {
        try {
            String nome = txtNomeAluno.getText().trim();
            String nascimentoStr = txtNASCIMENTOAluno.getText().trim();
            String email = txtEMAILAluno.getText().trim();
            String telefone = txtTELEFONEAluno.getText().trim();
            String cpf = txtCPFAluno.getText().trim().replaceAll("[^\\d]", "");
            String turmaIdStr = txtTURMAIDAluno.getText().trim();

            if (nome.isEmpty() || nascimentoStr.isEmpty() || cpf.isEmpty() || turmaIdStr.isEmpty()) {
                mostrarAlerta("Erro", "Preencha os campos obrigatórios: Nome, Data de Nascimento, CPF e ID da Turma.");
                return;
            }

            if (!ValidadorGERALALUNO.validarCPF(cpf)) {
                mostrarAlerta("Erro", "CPF inválido.");
                return;
            }

            if (!email.isEmpty() && !ValidadorGERALALUNO.validarEmail(email)) {
                mostrarAlerta("Erro", "E-mail inválido.");
                return;
            }

            if (!telefone.isEmpty() && !ValidadorGERALALUNO.validarTelefone(telefone)) {
                mostrarAlerta("Erro", "Telefone inválido.");
                return;
            }

            int idTurma = Integer.parseInt(turmaIdStr);
            LocalDate nascimento = LocalDate.parse(nascimentoStr, inputFormatter);

            try (Connection conn = Conexao.getConexao()) {
                String sql = "INSERT INTO aluno (nome, data_nascimento, email, telefone, cpf, id_turma) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, nome);
                stmt.setDate(2, Date.valueOf(nascimento));
                stmt.setString(3, email);
                stmt.setString(4, telefone);
                stmt.setString(5, cpf);
                stmt.setInt(6, idTurma);

                stmt.executeUpdate();

                mostrarAlerta("Sucesso", "Aluno cadastrado com sucesso!");
                limparCampos();
            }

        } catch (SQLException e) {
            mostrarAlerta("Erro de Banco", "Erro ao inserir aluno: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro de Dados", "Verifique os dados informados: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void limparCampos() {
        txtNomeAluno.clear();
        txtNASCIMENTOAluno.clear();
        txtEMAILAluno.clear();
        txtTELEFONEAluno.clear();
        txtCPFAluno.clear();
        txtTURMAIDAluno.clear();
    }

    private void configurarTabelaTurmas() {
        colIdTurma.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getId())));

        colNomeTurma.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getNome()));

        colAnoLetivoTurma.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getAnoLetivo())));

        colIdCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getIdCurso())));

        treeTableCursos.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarTurmas() {
        try {
            TurmaDAO turmaDAO = new TurmaDAO(Conexao.getConexao());
            List<Turma> turmas = turmaDAO.listarTurmas();

            TreeItem<Turma> root = new TreeItem<>();
            for (Turma turma : turmas) {
                root.getChildren().add(new TreeItem<>(turma));
            }

            treeTableCursos.setRoot(root);
            treeTableCursos.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar turmas: " + e.getMessage());
        }
    }

    public void homeCadastroALUNOADM(ActionEvent event) {
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

    @FXML
    private void atualizarTabela(ActionEvent event) {
        carregarTurmas();
    }
}
