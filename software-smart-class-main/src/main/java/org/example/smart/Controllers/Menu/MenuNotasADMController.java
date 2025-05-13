package org.example.smart.Controllers.Menu;

import Banco.Conexao;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuNotasADMController implements Initializable {

    @FXML
    private Button homeNOTAS;

    @FXML
    private Button sbtnLancamentoNota;

    @FXML
    private Button AcessarRemoverNotasADM;

    @FXML
    private TableView<Boletim> tableNotas;

    @FXML
    private TableColumn<Boletim, Number> columdiciplinaprofessor;

    @FXML
    private TableColumn<Boletim, Number> columnomeprofessor;

    @FXML
    private TableColumn<Boletim, Number> columnotasprofessor;

    @FXML
    private TableColumn<Boletim, Number> cursoidprofessor;

    @FXML
    private TableColumn<Boletim, Number> col_nota;

    @FXML
    private TableColumn<Boletim, String> col_situacao;

    public static class Boletim {
        private final SimpleIntegerProperty idBoletim;
        private final SimpleIntegerProperty idAluno;
        private final SimpleIntegerProperty idTurma;
        private final SimpleIntegerProperty idDisciplina;
        private final SimpleDoubleProperty nota;
        private final SimpleStringProperty situacao;

        public Boletim(int idBoletim, int idAluno, int idTurma, int idDisciplina, double nota, String situacao) {
            this.idBoletim = new SimpleIntegerProperty(idBoletim);
            this.idAluno = new SimpleIntegerProperty(idAluno);
            this.idTurma = new SimpleIntegerProperty(idTurma);
            this.idDisciplina = new SimpleIntegerProperty(idDisciplina);
            this.nota = new SimpleDoubleProperty(nota);
            this.situacao = new SimpleStringProperty(situacao);
        }

        public int getIdBoletim() {
            return idBoletim.get();
        }

        public SimpleIntegerProperty idBoletimProperty() {
            return idBoletim;
        }

        public int getIdAluno() {
            return idAluno.get();
        }

        public SimpleIntegerProperty idAlunoProperty() {
            return idAluno;
        }

        public int getIdTurma() {
            return idTurma.get();
        }

        public SimpleIntegerProperty idTurmaProperty() {
            return idTurma;
        }

        public int getIdDisciplina() {
            return idDisciplina.get();
        }

        public SimpleIntegerProperty idDisciplinaProperty() {
            return idDisciplina;
        }

        public double getNota() {
            return nota.get();
        }

        public SimpleDoubleProperty notaProperty() {
            return nota;
        }

        public String getSituacao() {
            return situacao.get();
        }

        public SimpleStringProperty situacaoProperty() {
            return situacao;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar colunas da TableView
        configurarColunas();

        // Carregar dados do banco para a TableView
        carregarDadosDoBoletim();
    }

    private void configurarColunas() {
        // Associar as colunas da TableView com as propriedades da classe Boletim
        columdiciplinaprofessor.setCellValueFactory(param -> param.getValue().idBoletimProperty());
        columnomeprofessor.setCellValueFactory(param -> param.getValue().idAlunoProperty());
        columnotasprofessor.setCellValueFactory(param -> param.getValue().idTurmaProperty());
        cursoidprofessor.setCellValueFactory(param -> param.getValue().idDisciplinaProperty());
        col_nota.setCellValueFactory(param -> param.getValue().notaProperty());
        col_situacao.setCellValueFactory(param -> param.getValue().situacaoProperty());
    }

    private void carregarDadosDoBoletim() {
        // Lista para armazenar os dados do boletim
        ObservableList<Boletim> boletins = FXCollections.observableArrayList();

        // SQL para buscar dados do boletim
        String sql = "SELECT id_boletim, id_aluno, id_turma, id_disciplina, nota, situacao FROM boletim";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorrer os resultados e adicionar à lista
            while (rs.next()) {
                int idBoletim = rs.getInt("id_boletim");
                int idAluno = rs.getInt("id_aluno");
                int idTurma = rs.getInt("id_turma");
                int idDisciplina = rs.getInt("id_disciplina");
                double nota = rs.getDouble("nota");
                String situacao = rs.getString("situacao");

                Boletim boletim = new Boletim(idBoletim, idAluno, idTurma, idDisciplina, nota, situacao);
                boletins.add(boletim);
            }

            // Definir os itens da TableView
            tableNotas.setItems(boletins);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar dados do boletim: " + e.getMessage());
        }
    }

    @FXML
    void homeNOTAS(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/smart/TelaInicial.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeNOTAS.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar para a tela inicial: " + e.getMessage());
        }
    }

    @FXML
    void abrirLancamentoNotas(ActionEvent event) {
        try {
            // Correção do caminho do arquivo FXML para corresponder à estrutura do projeto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/smart/LançarNotas.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) sbtnLancamentoNota.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir tela de lançamento de notas: " + e.getMessage());
        }
    }

    @FXML
    void AcessarRemoverNotasADM(ActionEvent event) {
        mostrarAlerta("Info", "Funcionalidade de remoção de notas ainda não implementada.");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}