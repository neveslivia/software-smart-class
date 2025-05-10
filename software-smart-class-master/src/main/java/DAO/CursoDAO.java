package DAO;

import model.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private Connection connection;

    public CursoDAO(Connection connection) {
        this.connection = connection;
    }

    public CursoDAO() {}

    // Adiciona um novo curso
    public void adicionarCurso(Curso curso) throws SQLException {
        String sql = "INSERT INTO curso (nome_curso, descricao, id_turma) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, curso.getIdTurma());
            stmt.executeUpdate();
        }
    }

    // Atualiza um curso
    public void atualizarCurso(Curso curso, int id) throws SQLException {
        String sql = "UPDATE curso SET nome_curso = ?, descricao = ?, id_turma = ? WHERE id_curso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, curso.getIdTurma());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    // Remove um curso
    public void removerCurso(int id) throws SQLException {
        String sql = "DELETE FROM curso WHERE id_curso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Lista todos os cursos
    public List<Curso> listarCursos() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM curso";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id_curso"));
                curso.setNome(rs.getString("nome_curso"));
                curso.setDescricao(rs.getString("descricao"));
                curso.setIdTurma(rs.getInt("id_turma"));
                lista.add(curso);
            }
        }
        return lista;
    }
}
