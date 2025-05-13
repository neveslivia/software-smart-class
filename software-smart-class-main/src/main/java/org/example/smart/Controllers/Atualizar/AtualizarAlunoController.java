package org.example.smart.Controllers.Atualizar;

import DAO.AlunoDAO;
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
import model.Aluno;
import org.example.smart.Validador.ValidadorGERALALUNO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AtualizarAlunoController {

    @FXML private TextField ID_Aluno;
    @FXML private TextField txtNomeAluno;
    @FXML private TextField txtDataNascimentoAluno;
    @FXML private TextField txtEmailAluno;
    @FXML private TextField txtTelefoneAluno;
    @FXML private TextField txtCPFAluno;
    @FXML private TextField txtID_TurmaAluno;

    @FXML private Button btnFinalizarAtualizarALUNO;
    @FXML private Button btn_AtualizarALUNO;

    @FXML private TreeTableView<Aluno> treeTableAlunosAtualizar;
    @FXML private TreeTableColumn<Aluno, String> col_IDALUNO;
    @FXML private TreeTableColumn<Aluno, String> col_NOMEALUNO;
    @FXML private TreeTableColumn<Aluno, String> col_DATANASCIMENTOALUNO;
    @FXML private TreeTableColumn<Aluno, String> col_EMAILALUNO;
    @FXML private TreeTableColumn<Aluno, String> col_TelefoneALUNO;
    @FXML private TreeTableColumn<Aluno, String> col_CPFALUNO;
    @FXML private TreeTableColumn<Aluno, String> col_IDTURMAALUNO;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final AlunoDAO alunoDAO = new AlunoDAO();

    @FXML
    public void initialize() {
        configurarFormatadores();
        configurarTabela();
        carregarAlunos();
        btn_AtualizarALUNO.setOnAction(e -> carregarAlunos());
        btnFinalizarAtualizarALUNO.setOnAction(this::atualizarAluno);
    }

    private void configurarTabela() {
        col_IDALUNO.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
        col_NOMEALUNO.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getNome()));
        col_DATANASCIMENTOALUNO.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDataNascimento().format(formatter)));
        col_EMAILALUNO.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEmail()));
        col_TelefoneALUNO.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTelefone()));
        col_CPFALUNO.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCpf()));
        col_IDTURMAALUNO.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getIdTurma())));
        treeTableAlunosAtualizar.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarAlunos() {
        List<Aluno> alunos = alunoDAO.buscarTodos();
        TreeItem<Aluno> root = new TreeItem<>();
        for (Aluno aluno : alunos) {
            root.getChildren().add(new TreeItem<>(aluno));
        }
        treeTableAlunosAtualizar.setRoot(root);
        treeTableAlunosAtualizar.setShowRoot(false);
    }

    private void configurarFormatadores() {
        txtCPFAluno.setTextFormatter(new TextFormatter<>(change -> formatarCPF(change)));
        txtTelefoneAluno.setTextFormatter(new TextFormatter<>(change -> formatarTelefone(change)));
        txtDataNascimentoAluno.setTextFormatter(new TextFormatter<>(change -> formatarData(change)));
    }

    private TextFormatter.Change formatarCPF(TextFormatter.Change change) {
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
    }

    private TextFormatter.Change formatarTelefone(TextFormatter.Change change) {
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
    }

    private TextFormatter.Change formatarData(TextFormatter.Change change) {
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
    }

    private void atualizarAluno(ActionEvent event) {
        try {
            int id = Integer.parseInt(ID_Aluno.getText().trim());
            String nome = txtNomeAluno.getText().trim();
            String dataNascimento = txtDataNascimentoAluno.getText().trim();
            String email = txtEmailAluno.getText().trim();
            String telefone = txtTelefoneAluno.getText().trim();
            String cpf = txtCPFAluno.getText().replaceAll("[^\\d]", "").trim();
            int idTurma = Integer.parseInt(txtID_TurmaAluno.getText().trim());

            if (!ValidadorGERALALUNO.validarCPF(cpf)) {
                mostrarAlerta("Erro", "CPF inválido.", AlertType.ERROR);
                return;
            }

            LocalDate data = LocalDate.parse(dataNascimento, formatter);

            Alert confirmacao = new Alert(AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmação de Atualização");
            confirmacao.setHeaderText("Deseja atualizar os dados deste aluno?");
            confirmacao.setContentText("ID: " + id);
            Optional<ButtonType> resultado = confirmacao.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try (Connection conn = Conexao.getConexao()) {
                    String sql = "UPDATE aluno SET nome=?, data_nascimento=?, email=?, telefone=?, cpf=?, id_turma=? WHERE id_aluno=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nome);
                    stmt.setDate(2, java.sql.Date.valueOf(data));
                    stmt.setString(3, email);
                    stmt.setString(4, telefone);
                    stmt.setString(5, cpf);
                    stmt.setInt(6, idTurma);
                    stmt.setInt(7, id);
                    stmt.executeUpdate();
                    mostrarAlerta("Sucesso", "Aluno atualizado com sucesso!", AlertType.INFORMATION);
                    carregarAlunos();
                }
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao atualizar aluno: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public void AcessarHOMEAtualizarAluno(ActionEvent event) {
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