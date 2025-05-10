package DAO;

import model.Boletim;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoletimDAO {
    private Connection connection;

    public BoletimDAO(Connection connection) {
        this.connection = connection;
    }

    // Criar Boletim
    public void criarBoletim(Boletim boletim) throws SQLException {
        String sql = "INSERT INTO boletim (id_aluno, id_turma, id_disciplina, nota, situacao) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boletim.getIdAluno());
            stmt.setInt(2, boletim.getIdTurma());
            stmt.setInt(3, boletim.getIdDisciplina());
            stmt.setDouble(4, boletim.getNota());
            stmt.setString(5, boletim.getSituacao());
            stmt.executeUpdate();
            System.out.println("Boletim criado com sucesso!");
        }
    }

    // Atualizar Boletim
    public void atualizarBoletim(Boletim boletim) throws SQLException {
        String sql = "UPDATE boletim SET id_aluno = ?, id_turma = ?, id_disciplina = ?, nota = ?, situacao = ? WHERE id_boletim = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boletim.getIdAluno());
            stmt.setInt(2, boletim.getIdTurma());
            stmt.setInt(3, boletim.getIdDisciplina());
            stmt.setDouble(4, boletim.getNota());
            stmt.setString(5, boletim.getSituacao());
            stmt.setInt(6, boletim.getId());
            stmt.executeUpdate();
            System.out.println("Boletim com ID " + boletim.getId() + " atualizado com sucesso!");
        }
    }

    // Excluir Boletim
    public void excluirBoletim(int id) throws SQLException {
        String sql = "DELETE FROM boletim WHERE id_boletim = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Boletim com ID " + id + " excluído com sucesso!");
        }
    }

    // Listar Boletins
    public List<Boletim> listarBoletins() throws SQLException {
        List<Boletim> lista = new ArrayList<>();
        String sql = "SELECT * FROM boletim";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Boletim boletim = new Boletim();
                boletim.setId(rs.getInt("id_boletim"));
                boletim.setIdAluno(rs.getInt("id_aluno"));
                boletim.setIdTurma(rs.getInt("id_turma"));
                boletim.setIdDisciplina(rs.getInt("id_disciplina"));
                boletim.setNota(rs.getDouble("nota"));
                boletim.setSituacao(rs.getString("situacao"));
                lista.add(boletim);
            }
        }
        return lista;
    }
}
