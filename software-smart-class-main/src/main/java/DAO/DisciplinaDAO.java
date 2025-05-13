package DAO;

import model.Disciplina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {
    private Connection connection;

    public DisciplinaDAO(Connection connection) {
        this.connection = connection;
    }

    // Criar Disciplina
    public void criarDisciplina(Disciplina disciplina) throws SQLException {
        String sql = "INSERT INTO disciplina (nome_disciplina, id_curso) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, disciplina.getNomeDisciplina());
            stmt.setInt(2, disciplina.getId_Curso());
            stmt.executeUpdate();
            System.out.println("Disciplina '" + disciplina.getNomeDisciplina() + "' criada com sucesso!");
        }
    }

    // Atualizar Disciplina
    public void atualizarDisciplina(Disciplina disciplina) throws SQLException {
        String sql = "UPDATE disciplina SET nome_disciplina = ?, id_curso = ? WHERE id_disciplina = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, disciplina.getNomeDisciplina());
            stmt.setInt(2, disciplina.getId_Curso());
            stmt.setInt(3, disciplina.getId_Disciplina());
            stmt.executeUpdate();
            System.out.println("Disciplina com ID " + disciplina.getId_Disciplina() + " atualizada com sucesso!");
        }
    }

    // Excluir Disciplina
    public void excluirDisciplina(int idDisciplina) throws SQLException {
        String sql = "DELETE FROM disciplina WHERE id_disciplina = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDisciplina);
            stmt.executeUpdate();
            System.out.println("Disciplina com ID " + idDisciplina + " excluída com sucesso!");
        }
    }

    // Listar Disciplinas
    public List<Disciplina> listarDisciplinas() throws SQLException {
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT * FROM disciplina";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId_Disciplina(rs.getInt("id_disciplina"));
                disciplina.setNomeDisciplina(rs.getString("nome_disciplina"));
                disciplina.setId_Curso(rs.getInt("id_curso"));
                lista.add(disciplina);
            }
        }
        return lista;
    }

    public Disciplina buscarDisciplinaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM disciplina WHERE id_disciplina = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId(rs.getInt("id_disciplina")); // CORRIGIDO
                disciplina.setNome(rs.getString("nome_disciplina")); // CORRIGIDO
                disciplina.setIdCurso(rs.getInt("id_curso")); // CORRIGIDO
                return disciplina;
            }
        }
        return null;
    }

    public void removerDisciplina(int idDisciplina) {
    }
}
