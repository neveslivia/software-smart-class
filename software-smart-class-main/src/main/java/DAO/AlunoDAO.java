package DAO;

import Banco.Conexao;
import model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    private Connection conexao;

    public AlunoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public AlunoDAO() {
        // Construtor vazio para quando não for necessário inicializar com conexão
    }

    public Aluno buscarAlunoPorId(int idAluno) throws SQLException {
        String sql = "SELECT * FROM aluno WHERE id_aluno = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id_aluno"));
                aluno.setNome(rs.getString("nome"));

                // Converter Date para LocalDate se não for nulo
                Date dataNascimento = rs.getDate("data_nascimento");
                if (dataNascimento != null) {
                    aluno.setDataNascimento(dataNascimento.toLocalDate());
                }

                aluno.setEmail(rs.getString("email"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setCpf(rs.getString("cpf"));
                aluno.setIdTurma(rs.getInt("id_turma"));

                return aluno;
            }
        }
        return null;
    }


    public List<Aluno> buscarTodos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getInt("id_aluno"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento").toLocalDate(), // CORREÇÃO AQUI
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getInt("id_turma")
                );
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunos;
    }

    public boolean removerPorId(int id) {
        String sql = "DELETE FROM aluno WHERE id_aluno = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
