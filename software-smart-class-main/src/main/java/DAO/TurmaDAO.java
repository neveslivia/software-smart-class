package DAO;

import model.Turma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    private Connection connection;

    public TurmaDAO(Connection connection) {
        this.connection = connection;
    }

    public TurmaDAO() {
    }

    public boolean criarTurma(Turma turma) throws SQLException {
        String sql = "INSERT INTO turma (nome_turma, ano_letivo, id_curso) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getAnoLetivo());
            stmt.setInt(3, turma.getIdCurso());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void atualizarTurma(Turma turma, int id) throws SQLException {
        String sql = "UPDATE turma SET nome_turma = ?, ano_letivo = ?, id_curso = ? WHERE id_turma = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getAnoLetivo());
            stmt.setInt(3, turma.getIdCurso());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public void excluirTurma(int id) throws SQLException {
        String sql = "DELETE FROM turma WHERE id_turma = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Turma> listarTurmas() throws SQLException {
        List<Turma> lista = new ArrayList<>();
        String sql = "SELECT * FROM turma";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId(rs.getInt("id_turma"));
                turma.setNome(rs.getString("nome_turma"));
                turma.setAnoLetivo(rs.getInt("ano_letivo"));
                turma.setIdCurso(rs.getInt("id_curso"));
                lista.add(turma);
            }
        }
        return lista;
    }
}
