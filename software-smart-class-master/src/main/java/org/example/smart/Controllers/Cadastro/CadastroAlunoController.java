package org.example.smart.Controllers.Cadastro;

import DAO.CursoDAO;
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
import model.Curso;
import org.example.smart.Validador.ValidadorGERALALUNO;
import model.Aluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.List;
import java.util.function.UnaryOperator;

public class CadastroAlunoController {

    @FXML private TextField txtNomeAluno;
    @FXML private TextField txtNASCIMENTOAluno;
    @FXML private TextField txtEMAILAluno;
    @FXML private TextField txtTELEFONEAluno;
    @FXML private TextField txtCPFAluno;
    @FXML private TextField txtTURMAIDAluno;
    @FXML private Button btnCadastrarAluno;

    @FXML private TreeTableView<Curso> treeTableCursos;
    @FXML private TreeTableColumn<Curso, String> colIdCurso;
    @FXML private TreeTableColumn<Curso, String> colNomeCurso;
    @FXML private TreeTableColumn<Curso, String> colDescricao;
    @FXML private TreeTableColumn<Curso, String> colIdTurma;
    @FXML private Button btnAtualizarAluno;

    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        configurarFormatadores();
        configurarTabelaCursos();  // Configura a tabela de cursos
        carregarCursos();  // Carrega os cursos ao inicializar a tela
        btnCadastrarAluno.setOnAction(e -> cadastrarAluno());
        btnAtualizarAluno.setOnAction(this::atualizarTabela);  // Associa o botão de atualização
    }

    private void configurarFormatadores() {
        // CPF: 000.000.000-00 (limite de 14 caracteres)
        txtCPFAluno.setTextFormatter(new TextFormatter<>(change -> {
            String digits = change.getControlNewText().replaceAll("[^\\d]", "");
            if (digits.length() > 11) return null;  // Limitar a 11 dígitos
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

        // Telefone: (00) 00000-0000 (limite de 15 caracteres)
        txtTELEFONEAluno.setTextFormatter(new TextFormatter<>(change -> {
            String digits = change.getControlNewText().replaceAll("[^\\d]", "");
            if (digits.length() > 11) return null;  // Limitar a 11 dígitos
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

        // Data de nascimento: dd/MM/yyyy (limite de 10 caracteres)
        txtNASCIMENTOAluno.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText().replaceAll("[^\\d]", "");
            if (text.length() > 8) return null;  // Limitar a 8 dígitos (ddMMyyyy)
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (i == 2 || i == 4) formatted.append('/');
                formatted.append(text.charAt(i));
            }
            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());
            return change;
        }));

        // Email: limite de 50 caracteres
        txtEMAILAluno.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 50) return null;  // Limitar a 50 caracteres
            return change;
        }));

        // Nome: limite de 50 caracteres
        txtNomeAluno.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 50) return null;  // Limitar a 50 caracteres
            return change;
        }));

        // ID da Turma: limite de 10 caracteres
        txtTURMAIDAluno.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 10) return null;  // Limitar a 10 caracteres
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

            // Converte data de nascimento (formato dd/MM/yyyy)
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

    private void configurarTabelaCursos() {
        colIdCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getId())));

        colNomeCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getNome()));

        colDescricao.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getDescricao()));

        colIdTurma.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getIdTurma())));

        treeTableCursos.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarCursos() {
        try {
            CursoDAO cursoDAO = new CursoDAO(Conexao.getConexao());
            List<Curso> cursos = cursoDAO.listarCursos();

            TreeItem<Curso> root = new TreeItem<>();
            for (Curso curso : cursos) {
                root.getChildren().add(new TreeItem<>(curso));
            }

            treeTableCursos.setRoot(root);
            treeTableCursos.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar cursos: " + e.getMessage());
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

    // Método para atualizar a tabela após remoção
    @FXML
    private void atualizarTabela(ActionEvent event) {
        carregarCursos();  // Recarrega a lista de cursos
    }
}
