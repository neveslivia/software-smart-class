package Main;

import DAO.CursoDAO;
import DAO.TurmaDAO;
import model.Curso;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CursoProgram {
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/sistemaacademico";
        String usuario = "root";
        String senha = null;
        Scanner sc = new Scanner(System.in);

        // Adicionando um novo curso
        try (Connection conn= DriverManager.getConnection(url, usuario, senha)) {
            CursoDAO cursoDAO = new CursoDAO(conn);
            TurmaDAO turmaDAO = new TurmaDAO(conn);

            System.out.println("Digite o nome do curso: ");
            String nome = sc.nextLine();
            System.out.println("Digite a descrição do curso: ");
            String descricao = sc.nextLine();

            System.out.println("Digite o id da turma: ");
            int idTurma = sc.nextInt();  // Adicionando o ID da turma
            sc.nextLine();  // Limpa o buffer

            // Criando o curso
            Curso curso = new Curso(nome, descricao, idTurma);
            cursoDAO.adicionarCurso(curso);

            System.out.println("Curso adicionado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir curso: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // Atualizando um curso
        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            CursoDAO cursoDAO = new CursoDAO(conn);
            TurmaDAO turmaDAO = new TurmaDAO(conn);

            System.out.println("Digite o id do curso que deseja alterar: ");
            int id = sc.nextInt();
            sc.nextLine();  // Limpa o buffer

            System.out.println("Digite o novo nome do curso: ");
            String nome = sc.nextLine();

            System.out.println("Digite a nova descrição do curso: ");
            String descricao = sc.nextLine();

            System.out.println("Digite o novo id da turma: ");
            int idTurma = sc.nextInt();
            sc.nextLine();  // Limpa o buffer

            Curso curso = new Curso(nome, descricao, idTurma);
            cursoDAO.atualizarCurso(curso, id);

            System.out.println("Curso atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar curso: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
