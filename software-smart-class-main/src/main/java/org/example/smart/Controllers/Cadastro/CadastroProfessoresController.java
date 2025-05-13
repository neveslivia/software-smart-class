package org.example.smart.Controllers.Cadastro;

import DAO.DisciplinaDAO;
import DAO.ProfessorDAO;
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
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import model.Disciplina;
import model.Professor;
import org.example.smart.Validador.ValidadorGERALALUNO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CadastroProfessoresController {

    @FXML private TextField txtNome;
    @FXML private TextField txtDisciplina;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtCPF;
    @FXML private TextField txtSenha;

    @FXML private Button btnCadastrar;
    @FXML private Button btnAtualizar;
    @FXML private Button AcessarHOME;

    @FXML private TreeTableView<Professor> treeTableViewProfessor;
    @FXML private TreeTableColumn<Professor, String> col_ID;
    @FXML private TreeTableColumn<Professor, String> col_nome;
    @FXML private TreeTableColumn<Professor, String> col_disciplina;
    @FXML private TreeTableColumn<Professor, String> col_email;
    @FXML private TreeTableColumn<Professor, String> col_telefone;
    @FXML private TreeTableColumn<Professor, String> col_cpf;
    @FXML private TreeTableColumn<Professor, String> col_senha;

    @FXML private TreeTableView<Disciplina> treeTableViewDisciplina;
    @FXML private TreeTableColumn<Disciplina, String> col_IDdisciplina;
    @FXML private TreeTableColumn<Disciplina, String> col_nomeDisciplina;
    @FXML private TreeTableColumn<Disciplina, String> col_IDcurso;

    private final ProfessorDAO professorDAO = new ProfessorDAO();

    @FXML
    public void initialize() {
        configurarFormatacaoCampos();
        configurarColunasTabela();
        carregarTabelas();

        btnCadastrar.setOnAction(e -> cadastrarProfessor());
        btnAtualizar.setOnAction(e -> carregarTabelas());
    }

    private void configurarFormatacaoCampos() {
        txtCPF.setTextFormatter(new TextFormatter<>(change -> {
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

        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
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
    }

    private void configurarColunasTabela() {
        col_ID.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        col_nome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        col_disciplina.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getDisciplina()));
        col_email.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getEmail()));
        col_telefone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getTelefone()));
        col_cpf.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getCpf()));
        col_senha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getSenha()));

        col_IDdisciplina.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId_Disciplina())));
        col_nomeDisciplina.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNomeDisciplina()));
        col_IDcurso.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId_Curso())));

        treeTableViewProfessor.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableViewDisciplina.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarTabelas() {
        try {
            List<Professor> professores = professorDAO.listarTodos();
            TreeItem<Professor> rootProf = new TreeItem<>();
            for (Professor p : professores) {
                rootProf.getChildren().add(new TreeItem<>(p));
            }
            treeTableViewProfessor.setRoot(rootProf);
            treeTableViewProfessor.setShowRoot(false);

            DisciplinaDAO disciplinaDAO = new DisciplinaDAO(Conexao.getConexao());
            List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();
            TreeItem<Disciplina> rootDisc = new TreeItem<>();
            for (Disciplina d : disciplinas) {
                rootDisc.getChildren().add(new TreeItem<>(d));
            }
            treeTableViewDisciplina.setRoot(rootDisc);
            treeTableViewDisciplina.setShowRoot(false);
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void cadastrarProfessor() {
        try {
            String nome = txtNome.getText();
            String disciplina = txtDisciplina.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            String cpf = txtCPF.getText().replaceAll("[^\\d]", "");
            String senha = txtSenha.getText();

            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty()) {
                mostrarAlerta("Erro", "Preencha os campos obrigatórios: Nome, CPF e Senha.");
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

            Professor p = new Professor();
            p.setNome(nome);
            p.setDisciplina(disciplina);
            p.setEmail(email);
            p.setTelefone(telefone);
            p.setCpf(cpf);
            p.setSenha(senha);

            professorDAO.inserir(p);
            mostrarAlerta("Sucesso", "Professor cadastrado com sucesso!");
            limparCampos();
            carregarTabelas();

        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao cadastrar professor: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtDisciplina.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtCPF.clear();
        txtSenha.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public void AcessarHOME(ActionEvent event) {
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