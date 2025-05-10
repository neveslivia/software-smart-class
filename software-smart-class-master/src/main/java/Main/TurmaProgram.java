package Main;
import DAO.TurmaDAO;
import model.Turma;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class TurmaProgram {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/sistemaacademico";
        String usuario = "root";
        String senha = null;

        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, usuario, null)) {
            TurmaDAO turmaDAO = new TurmaDAO(conn);

            System.out.println("Digite o nome da turma: ");
            String nome = sc.nextLine();
            System.out.println("Digite o ano letivo: ");
            int ano = sc.nextInt();
            System.out.println("Digite o id do curso: ");
            int idCurso = sc.nextInt();
            Turma turma = new Turma(nome, ano, idCurso);
            turmaDAO.criarTurma(turma);
            String sql = "INSERT INTO turma (nome_turma, ano_letivo,id_curso) values(?,?,?)";

            PreparedStatement sttm = conn.prepareStatement(sql);

            sttm.setString(1, nome);
            sttm.setInt(2, ano);
            sttm.setInt(3, idCurso);

            int rowsAffected = sttm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Curso adicionado com sucesso!");
            } else {
                System.out.println("Erro ao adicionar curso.");
            }



        }catch(Exception e){
            System.out.println("Erro ao ler os dados: " + e.getMessage());
        }


    }
}


