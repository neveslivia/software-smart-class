package DAO;

import model.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private Connection connection;

    // Construtor principal com injeção de conexão
    public CursoDAO(Connection connection) {
        this.connection = connection;
    }

    // Construtor vazio (opcional)
    public CursoDAO() {}

    // Adiciona um novo curso
    public boolean adicionarCurso(Curso curso) throws SQLException {
        String sql = "INSERT INTO curso (nome_curso, descricao) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Atualiza os dados de um curso pelo ID
    public void atualizarCurso(Curso curso, int id) throws SQLException {
        String sql = "UPDATE curso SET nome_curso = ?, descricao = ? WHERE id_curso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    // Remove um curso pelo ID
    public boolean removerCurso(int id) throws SQLException {
        String sql = "DELETE FROM curso WHERE id_curso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                lista.add(curso);
            }
        }
        return lista;
    }
}
