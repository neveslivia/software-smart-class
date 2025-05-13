package org.example.smart.Controllers;

import Banco.Conexao;
import DAO.AlunoDAO;
import DAO.BoletimDAO;
import DAO.DisciplinaDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Aluno;
import model.Boletim;
import model.BoletimView;
import model.Disciplina;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class LancarNotasController implements Initializable {

    @FXML private TextField idAlunoField;
    @FXML private TextField idDisciplinaField;
    @FXML private TextField notaField;
    @FXML private TextField dataLancamentoField;
    @FXML private Button confirmarButton;
    @FXML private TreeTableView<BoletimView> tabelaBoletim;
    @FXML private TreeTableColumn<BoletimView, Integer> idAlunoColumn;
    @FXML private TreeTableColumn<BoletimView, String> nomeColumn;
    @FXML private TreeTableColumn<BoletimView, Double> notaColumn;
    @FXML private TreeTableColumn<BoletimView, Integer> idCursoColumn;
    @FXML private Button homelancarNOTA;

    private Connection conexao;
    private BoletimDAO boletimDAO;
    private DisciplinaDAO disciplinaDAO;
    private AlunoDAO alunoDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Inicializando tela de lançamento de notas...");
        try {
            conexao = Conexao.getConexao();
            boletimDAO = new BoletimDAO(conexao);
            disciplinaDAO = new DisciplinaDAO(conexao);
            alunoDAO = new AlunoDAO(conexao);

            configurarTabela();
            carregarDadosTabela();

            confirmarButton.setOnAction(event -> lancarNota());

            dataLancamentoField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        } catch (SQLException e) {
            exibirAlerta("Erro de Conexão", "Não foi possível conectar ao banco de dados", e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabela() {
        idAlunoColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getIdAluno()));
        nomeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getNomeAluno()));
        notaColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getNota()));
        idCursoColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getIdCurso()));
    }

    private void carregarDadosTabela() {
        try {
            // Verificando se a tabela turma tem a coluna id_curso ou se precisamos usar outra coluna
            String sql = "SELECT b.id_boletim, a.id_aluno, a.nome as nome_aluno, b.nota, t.id_turma as id_curso " +
                    "FROM boletim b " +
                    "JOIN aluno a ON b.id_aluno = a.id_aluno " +
                    "JOIN turma t ON b.id_turma = t.id_turma " +
                    "ORDER BY b.id_boletim DESC";

            PreparedStatement pstmt = conexao.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            ObservableList<BoletimView> boletins = FXCollections.observableArrayList();

            while (rs.next()) {
                BoletimView boletimView = new BoletimView(
                        rs.getInt("id_boletim"),
                        rs.getInt("id_aluno"),
                        rs.getString("nome_aluno"),
                        rs.getDouble("nota"),
                        rs.getInt("id_curso")
                );
                boletins.add(boletimView);
            }

            System.out.println("Total de boletins carregados: " + boletins.size());

            TreeItem<BoletimView> raiz = new TreeItem<>(new BoletimView(0, 0, "Boletins", 0.0, 0));
            raiz.setExpanded(true);

            for (BoletimView boletim : boletins) {
                TreeItem<BoletimView> itemBoletim = new TreeItem<>(boletim);
                raiz.getChildren().add(itemBoletim);
            }

            tabelaBoletim.setRoot(raiz);
            tabelaBoletim.setShowRoot(false);

        } catch (SQLException e) {
            exibirAlerta("Erro", "Erro ao carregar dados da tabela", e.getMessage());
            e.printStackTrace();
        }
    }

    private void lancarNota() {
        try {
            if (idAlunoField.getText().isEmpty() || idDisciplinaField.getText().isEmpty() ||
                    notaField.getText().isEmpty() || dataLancamentoField.getText().isEmpty()) {
                exibirAlerta("Campos Vazios", "Todos os campos devem ser preenchidos", null);
                return;
            }

            int idAluno = Integer.parseInt(idAlunoField.getText().trim());
            int idDisciplina = Integer.parseInt(idDisciplinaField.getText().trim());
            double nota = Double.parseDouble(notaField.getText().trim());

            if (nota < 0 || nota > 10) {
                exibirAlerta("Nota Inválida", "A nota deve estar entre 0 e 10", null);
                return;
            }

            String dataStr = dataLancamentoField.getText().trim();
            LocalDate data;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                data = LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                exibirAlerta("Data Inválida", "Formato da data deve ser dd/MM/yyyy", null);
                return;
            }

            Aluno aluno = alunoDAO.buscarAlunoPorId(idAluno);
            if (aluno == null) {
                exibirAlerta("Aluno Não Encontrado", "O aluno com ID " + idAluno + " não existe", null);
                return;
            }

            Disciplina disciplina = disciplinaDAO.buscarDisciplinaPorId(idDisciplina);
            if (disciplina == null) {
                exibirAlerta("Disciplina Não Encontrada", "A disciplina com ID " + idDisciplina + " não existe", null);
                return;
            }

            String situacao = (nota >= 7.0) ? "APROVADO" : (nota >= 5.0) ? "RECUPERACAO" : "REPROVADO";

            Boletim boletim = new Boletim(idAluno, aluno.getIdTurma(), idDisciplina, nota, situacao);
            boletim.setDataLancamento(data);

            boletimDAO.criarBoletim(boletim);

            limparCampos();
            carregarDadosTabela();

            exibirAlerta("Sucesso", "Nota lançada com sucesso!", null);

        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Formato", "IDs e Nota devem conter valores numéricos", null);
        } catch (SQLException e) {
            exibirAlerta("Erro no Banco de Dados", "Não foi possível lançar a nota", e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        idAlunoField.clear();
        idDisciplinaField.clear();
        notaField.clear();
        dataLancamentoField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void exibirAlerta(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    @FXML
    public void homelancarNOTA(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/smart/homeADM.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homelancarNOTA.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            exibirAlerta("Erro", "Erro ao carregar a tela principal", e.getMessage());
            e.printStackTrace();
        }
    }
}