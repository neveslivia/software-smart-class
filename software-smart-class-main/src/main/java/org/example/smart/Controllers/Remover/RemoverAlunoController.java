package org.example.smart.Controllers.Remover;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Aluno;
import DAO.AlunoDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoverAlunoController implements Initializable {

    @FXML private TextField tfID_ALUNO;
    @FXML private Button btnRemoverALUNO;
    @FXML private Button btnAtualizarALUNOS; // NOVO BOTÃO

    @FXML private TreeTableView<Aluno> treeTableAlunos;  // Corrigido para Alunos
    @FXML private TreeTableColumn<Aluno, Integer> col_ID_ALUNO;
    @FXML private TreeTableColumn<Aluno, String> colnomeALUNO;
    @FXML private TreeTableColumn<Aluno, String> coldata_nascimento;
    @FXML private TreeTableColumn<Aluno, String> colEmail_ALUNO;
    @FXML private TreeTableColumn<Aluno, String> colTelefone_ALUNO;
    @FXML private TreeTableColumn<Aluno, String> colCpf_ALUNO;
    @FXML private TreeTableColumn<Aluno, Integer> col_ID_Turma;

    private AlunoDAO alunoDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alunoDAO = new AlunoDAO();
        configurarColunas();
        loadAlunos();

        btnRemoverALUNO.setOnAction(event -> removerAluno());
        btnAtualizarALUNOS.setOnAction(event -> loadAlunos()); // AÇÃO DO NOVO BOTÃO
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private void configurarColunas() {
        col_ID_ALUNO.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject());

        colnomeALUNO.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNome()));
        colnomeALUNO.setPrefWidth(160);
        colnomeALUNO.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setTooltip(item != null ? new Tooltip(item) : null);
            }
        });

        coldata_nascimento.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getDataNascimento().format(formatter)));
        coldata_nascimento.setPrefWidth(100);

        colEmail_ALUNO.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getEmail()));
        colEmail_ALUNO.setPrefWidth(200);
        colEmail_ALUNO.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setTooltip(item != null ? new Tooltip(item) : null);
            }
        });

        colTelefone_ALUNO.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getTelefone()));
        colTelefone_ALUNO.setPrefWidth(120);

        colCpf_ALUNO.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getCpf()));
        colCpf_ALUNO.setPrefWidth(140);
        colCpf_ALUNO.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setTooltip(item != null ? new Tooltip(item) : null);
            }
        });

        col_ID_Turma.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getIdTurma()).asObject());
        col_ID_Turma.setPrefWidth(80);
    }

    private void loadAlunos() {
        try {
            List<Aluno> alunos = alunoDAO.buscarTodos();

            TreeItem<Aluno> rootItem = new TreeItem<>();
            for (Aluno aluno : alunos) {
                TreeItem<Aluno> item = new TreeItem<>(aluno);
                rootItem.getChildren().add(item);
            }

            treeTableAlunos.setRoot(rootItem);  // Corrigido para treeTableAlunos
            treeTableAlunos.setShowRoot(false);
            rootItem.setExpanded(true);
            treeTableAlunos.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception e) {
            exibirAlerta("Erro ao carregar dados", "Erro ao carregar os dados dos alunos: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void removerAluno() {
        TreeItem<Aluno> selectedItem = treeTableAlunos.getSelectionModel().getSelectedItem();  // Corrigido para treeTableAlunos

        if (selectedItem != null) {
            Aluno aluno = selectedItem.getValue();
            confirmarERemover(aluno.getId());
        } else {
            String idTexto = tfID_ALUNO.getText().trim();
            if (idTexto.isEmpty()) {
                exibirAlerta("Atenção", "Informe um ID ou selecione um aluno na tabela.", AlertType.WARNING);
                return;
            }

            try {
                int id = Integer.parseInt(idTexto);
                confirmarERemover(id);
            } catch (NumberFormatException e) {
                exibirAlerta("Erro", "ID inválido. Digite um número válido.", AlertType.ERROR);
            }
        }
    }

    private void confirmarERemover(int id) {
        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Remover Aluno");
        confirmacao.setContentText("Tem certeza que deseja remover o aluno com ID " + id + "?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean removido = alunoDAO.removerPorId(id);
            if (removido) {
                exibirAlerta("Sucesso", "Aluno removido com sucesso!", AlertType.INFORMATION);
                loadAlunos();
                tfID_ALUNO.clear();
            } else {
                exibirAlerta("Erro", "Aluno com ID " + id + " não encontrado.", AlertType.ERROR);
            }
        }
    }

    private void exibirAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }


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
