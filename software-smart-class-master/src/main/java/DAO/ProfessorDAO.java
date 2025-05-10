package DAO;

import Banco.Conexao;
import model.Professor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    private Connection conexao;

    public ProfessorDAO() {
        try {
            this.conexao = Conexao.getConexao();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }

    public void inserir(Professor professor) throws SQLException {
        String sql = "INSERT INTO professor (nome, disciplina, email, telefone, cpf, senha) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDisciplina());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getTelefone());
            stmt.setString(5, professor.getCpf());
            stmt.setString(6, professor.getSenha());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir professor, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    professor.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Falha ao inserir professor, nenhum ID obtido.");
                }
            }
        }
    }

    public Professor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM professor WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Professor professor = new Professor();
                    professor.setId(rs.getInt("id"));
                    professor.setNome(rs.getString("nome"));
                    professor.setDisciplina(rs.getString("disciplina"));
                    professor.setEmail(rs.getString("email"));
                    professor.setTelefone(rs.getString("telefone"));
                    professor.setCpf(rs.getString("cpf"));
                    // Não retornamos a senha por segurança
                    return professor;
                }
            }
        }
        return null;
    }

    public Professor buscarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT * FROM professor WHERE cpf = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Professor professor = new Professor();
                    professor.setId(rs.getInt("id"));
                    professor.setNome(rs.getString("nome"));
                    professor.setDisciplina(rs.getString("disciplina"));
                    professor.setEmail(rs.getString("email"));
                    professor.setTelefone(rs.getString("telefone"));
                    professor.setCpf(rs.getString("cpf"));
                    return professor;
                }
            }
        }
        return null;
    }

    public List<Professor> listarTodos() throws SQLException {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT * FROM professor";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Professor professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setNome(rs.getString("nome"));
                professor.setDisciplina(rs.getString("disciplina"));
                professor.setEmail(rs.getString("email"));
                professor.setTelefone(rs.getString("telefone"));
                professor.setCpf(rs.getString("cpf"));

                professores.add(professor);
            }
        }

        return professores;
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM professor WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean atualizar(Professor professor) throws SQLException {
        String sql = "UPDATE professor SET nome = ?, disciplina = ?, email = ?, telefone = ?, cpf = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDisciplina());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getTelefone());
            stmt.setString(5, professor.getCpf());
            stmt.setInt(6, professor.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}