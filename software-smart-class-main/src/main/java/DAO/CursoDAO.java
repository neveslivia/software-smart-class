package DAO;

import Banco.Conexao;
import model.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private Connection connection;

    public CursoDAO(Connection connection) {
        this.connection = connection;
    }



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

    public void atualizarCurso(Curso curso, int id) throws SQLException {
        String sql = "UPDATE curso SET nome_curso = ?, descricao = ? WHERE id_curso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public boolean removerCurso(int id) throws SQLException {
        String sql = "DELETE FROM curso WHERE id_curso = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;


    }

    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM curso";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("id_curso"),
                        rs.getString("nome_curso"),
                        rs.getString("descricao")

                );
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return cursos;
    }
}
