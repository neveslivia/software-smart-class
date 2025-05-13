package org.example.smart.Controllers.Menu;

import DAO.AlunoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import model.Aluno;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuAlunosADMController {

    // TABELA DE ALUNOS
    @FXML private TreeTableView<Aluno> treeTableAlunosMenu;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_ID;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_NOME;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_DataNascimento;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_Email;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_Telefone;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_CPF;
    @FXML private TreeTableColumn<Aluno, String> col_MenuALUNO_ID_TURMA;

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        configurarColunas();
        carregarAlunos();
    }

    private void configurarColunas() {
        col_MenuALUNO_ID.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
        col_MenuALUNO_NOME.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getNome()));
        col_MenuALUNO_DataNascimento.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getDataNascimento().format(formatter)));
        col_MenuALUNO_Email.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEmail()));
        col_MenuALUNO_Telefone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTelefone()));
        col_MenuALUNO_CPF.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCpf()));
        col_MenuALUNO_ID_TURMA.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getIdTurma())));
    }

    private void carregarAlunos() {
        List<Aluno> alunos = alunoDAO.buscarTodos();
        TreeItem<Aluno> root = new TreeItem<>();

        for (Aluno aluno : alunos) {
            root.getChildren().add(new TreeItem<>(aluno));
        }

        treeTableAlunosMenu.setRoot(root);
        treeTableAlunosMenu.setShowRoot(false);
    }

    // DADOS ALUNO ADMIN (RETORNOS)

    public void retornaHomeAluno(ActionEvent event) {
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

    // DADOS ADMALUNO Acessar

    public void AcessaAdicionarAluno(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/CadastroAlunos.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessaAtualizadoAluno(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/AtualizarAluno.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AcessaRemoverAluno(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/RemoverAluno.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}