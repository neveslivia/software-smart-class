package org.example.smart.Controllers.Remover;

import model.Professor;
import DAO.ProfessorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoverProfessorController implements Initializable {

    @FXML
    private TextField tfId;

    @FXML
    private Button btnRemover;

    @FXML
    private Button btnHome;

    @FXML
    private TreeTableView<Professor> treeTableProfessores;

    @FXML
    private TreeTableColumn<Professor, Integer> colId;

    @FXML
    private TreeTableColumn<Professor, String> colNome;

    @FXML
    private TreeTableColumn<Professor, String> colDisciplina;

    @FXML
    private TreeTableColumn<Professor, String> colEmail;

    @FXML
    private TreeTableColumn<Professor, String> colTelefone;

    @FXML
    private TreeTableColumn<Professor, String> colCpf;

    private ProfessorDAO professorDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa o DAO
        professorDAO = new ProfessorDAO();

        // Configurar as colunas da TreeTableView
        configureColumns();

        // Carregar os dados dos professores
        loadProfessors();

        // Configurar ação do botão remover
        btnRemover.setOnAction(event -> removerProfessor());

        // Configurar ação do botão home
        btnHome.setOnAction(event -> navegarParaHome());
    }

    private void configureColumns() {
        // Usando Callback personalizado em vez de TreeItemPropertyValueFactory
        colId.setCellValueFactory(cellData -> {
            if (cellData.getValue().getValue() != null) {
                return new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValue().getId()).asObject();
            } else {
                return null;
            }
        });

        colNome.setCellValueFactory(cellData -> {
            if (cellData.getValue().getValue() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().getNome());
            } else {
                return null;
            }
        });

        colDisciplina.setCellValueFactory(cellData -> {
            if (cellData.getValue().getValue() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().getDisciplina());
            } else {
                return null;
            }
        });

        colEmail.setCellValueFactory(cellData -> {
            if (cellData.getValue().getValue() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().getEmail());
            } else {
                return null;
            }
        });

        colTelefone.setCellValueFactory(cellData -> {
            if (cellData.getValue().getValue() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().getTelefone());
            } else {
                return null;
            }
        });

        colCpf.setCellValueFactory(cellData -> {
            if (cellData.getValue().getValue() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().getCpf());
            } else {
                return null;
            }
        });
    }

    private void loadProfessors() {
        try {
            // Buscar todos os professores do banco de dados
            List<Professor> professores = professorDAO.listarTodos();

            if (professores.isEmpty()) {
                // Se não houver professores, exibir mensagem
                System.out.println("Nenhum professor encontrado no banco de dados");
                return;
            }

            // Criar o item raiz
            TreeItem<Professor> rootItem = new TreeItem<>();

            // Adicionar cada professor como um item na árvore
            for (Professor professor : professores) {
                TreeItem<Professor> item = new TreeItem<>(professor);
                rootItem.getChildren().add(item);
            }

            // Configurar a tabela
            treeTableProfessores.setRoot(rootItem);
            treeTableProfessores.setShowRoot(false);

            // Expande todos os itens para garantir que sejam visíveis
            rootItem.setExpanded(true);

            // Verificação extra para debug
            System.out.println("Carregados " + professores.size() + " professores");

        } catch (SQLException e) {
            exibirAlerta("Erro ao Carregar Dados",
                    "Não foi possível carregar os dados dos professores: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void removerProfessor() {
        // Primeiro, verificar se um item está selecionado na tabela
        TreeItem<Professor> selectedItem = treeTableProfessores.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Se um item está selecionado, usar esse professor
            Professor professor = selectedItem.getValue();
            confirmarERemover(professor.getId());
        } else {
            // Se nenhum item selecionado, tentar usar o ID do campo de texto
            String idText = tfId.getText().trim();

            if (idText.isEmpty()) {
                exibirAlerta("Atenção",
                        "Por favor, informe o ID do professor ou selecione um na tabela",
                        Alert.AlertType.WARNING);
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                confirmarERemover(id);
            } catch (NumberFormatException e) {
                exibirAlerta("Erro",
                        "ID inválido. Por favor, informe um número inteiro",
                        Alert.AlertType.ERROR);
            }
        }
    }

    private void confirmarERemover(int id) {
        // Confirmação antes de remover
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Remover Professor");
        confirmacao.setContentText("Tem certeza que deseja remover o professor com ID " + id + "?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                boolean removido = professorDAO.excluir(id);

                if (removido) {
                    exibirAlerta("Sucesso",
                            "Professor removido com sucesso!",
                            Alert.AlertType.INFORMATION);
                    loadProfessors(); // Recarregar a tabela
                    tfId.clear();
                } else {
                    exibirAlerta("Erro",
                            "Professor com ID " + id + " não encontrado",
                            Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                exibirAlerta("Erro",
                        "Erro ao remover professor: " + e.getMessage(),
                        Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void navegarParaHome() {
        try {
            // Corrigir o caminho do recurso FXML
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/smart/homeADM.fxml"));
            Stage stage = (Stage) btnHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            exibirAlerta("Erro",
                    "Erro ao navegar para a tela inicial: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}