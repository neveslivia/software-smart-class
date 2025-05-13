package org.example.smart.Controllers.Atualizar;

import Banco.Conexao;
import DAO.ProfessorDAO;
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
import javafx.stage.Stage;
import model.Professor;

import java.sql.Connection;
import java.util.List;

public class AtualizarProfessorController {

    @FXML private TextField txtID;
    @FXML private TextField txtNome;
    @FXML private TextField txtDisciplina;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtCPF;

    @FXML private Button btnFinalizar;
    @FXML private Button btnAtualizar;
    @FXML private Button AcessarHomeAtualizaProfessor;

    @FXML private TreeTableView<Professor> treeTableViewProfessor;
    @FXML private TreeTableColumn<Professor, String> col_ID;
    @FXML private TreeTableColumn<Professor, String> col_Nome;
    @FXML private TreeTableColumn<Professor, String> col_Disciplina;
    @FXML private TreeTableColumn<Professor, String> col_Email;
    @FXML private TreeTableColumn<Professor, String> col_Telefone;
    @FXML private TreeTableColumn<Professor, String> col_CPF;

    private ProfessorDAO professorDAO;

    @FXML
    public void initialize() {
        professorDAO = new ProfessorDAO();
        configurarColunas();
        configurarFormatadores();
        carregarTabela();

        btnFinalizar.setOnAction(this::atualizarProfessor);
        btnAtualizar.setOnAction(e -> carregarTabela());
    }

    private void configurarColunas() {
        col_ID.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getValue().getId())));
        col_Nome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getNome()));
        col_Disciplina.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getDisciplina()));
        col_Email.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getEmail()));
        col_Telefone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getTelefone()));
        col_CPF.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getCpf()));
    }

    private void configurarFormatadores() {
        txtID.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d{0,10}") ? change : null));

        txtNome.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 100 ? change : null));

        txtDisciplina.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 100 ? change : null));

        txtEmail.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 100 ? change : null));

        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String digits = change.getControlNewText().replaceAll("[^\\d]", "");
            if (digits.length() > 11) return null;
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < digits.length(); i++) {
                if (i == 0) formatted.append('(');
                if (i == 2) formatted.append(") ");
                if (i == 7) formatted.append('-');
                formatted.append(digits.charAt(i));
            }
            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());
            return change;
        }));

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
    }

    private void carregarTabela() {
        try {
            List<Professor> professores = professorDAO.listarTodos();
            TreeItem<Professor> root = new TreeItem<>();
            for (Professor p : professores) {
                root.getChildren().add(new TreeItem<>(p));
            }
            treeTableViewProfessor.setRoot(root);
            treeTableViewProfessor.setShowRoot(false);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao carregar professores: " + e.getMessage());
        }
    }

    private void atualizarProfessor(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtID.getText().trim());
            String nome = txtNome.getText().trim();
            String disciplina = txtDisciplina.getText().trim();
            String email = txtEmail.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String cpf = txtCPF.getText().trim().replaceAll("[^\\d]", "");

            Professor existente = professorDAO.buscarPorId(id);
            if (existente == null) {
                mostrarAlerta("Erro", "ID não encontrado.");
                return;
            }

            Professor duplicadoCPF = professorDAO.buscarPorCPF(cpf);
            if (duplicadoCPF != null && duplicadoCPF.getId() != id) {
                mostrarAlerta("Erro", "CPF já cadastrado para outro professor.");
                return;
            }

            Professor atualizado = new Professor();
            atualizado.setId(id);
            atualizado.setNome(nome);
            atualizado.setDisciplina(disciplina);
            atualizado.setEmail(email);
            atualizado.setTelefone(telefone);
            atualizado.setCpf(cpf);

            boolean sucesso = professorDAO.atualizar(atualizado);
            if (sucesso) {
                mostrarAlerta("Sucesso", "Professor atualizado com sucesso!");
                carregarTabela();
            } else {
                mostrarAlerta("Erro", "Nenhuma linha foi atualizada.");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao atualizar professor: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    public void AcessarHomeAtualizaProfessor(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeADM.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}