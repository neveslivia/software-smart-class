package org.example.smart.Controllers.Remover;

import Banco.Conexao;
import DAO.DisciplinaDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import model.Disciplina;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class RemoverNotasADMINController implements Initializable {

    @FXML private TreeTableView<Disciplina> treeTablenotasadm1;
    @FXML private TreeTableColumn<Disciplina, Integer> colIdnotasadmDisciplina1;
    @FXML private TreeTableColumn<Disciplina, String> colNomenotasadmDisciplina1;
    @FXML private TreeTableColumn<Disciplina, Integer> colIdnotasadmCurso1;
    @FXML private Button btnAtualizarnotasadm1;

    private DisciplinaDAO disciplinaDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection conexao = Conexao.getConexao(); // agora usando sua classe Conexao
            disciplinaDAO = new DisciplinaDAO(conexao);
            configurarColunas();
            atualizarTabela(); // carrega ao iniciar

            // Ação do botão de atualizar
            btnAtualizarnotasadm1.setOnAction(event -> atualizarTabela());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColunas() {
        colIdnotasadmDisciplina1.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId_Disciplina()).asObject());

        colNomenotasadmDisciplina1.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getNomeDisciplina()));

        colIdnotasadmCurso1.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getId_Curso()).asObject());
    }

    @FXML
    private void atualizarTabela() {
        try {
            List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();
            TreeItem<Disciplina> root = new TreeItem<>();
            for (Disciplina d : disciplinas) {
                root.getChildren().add(new TreeItem<>(d));
            }
            treeTablenotasadm1.setRoot(root);
            treeTablenotasadm1.setShowRoot(false);
            root.setExpanded(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
