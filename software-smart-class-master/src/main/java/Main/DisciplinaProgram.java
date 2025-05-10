package Main;

import DAO.DisciplinaDAO;
import model.Disciplina;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DisciplinaProgram {
    public static void main(String[] args) {

        // Configurações do banco de dados
        String url = "jdbc:mysql://localhost:3306/sistemaacademico";
        String usuario = "root";
        String senha = null;

        // Scanner para ler a entrada do usuário
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {

            // Criação do objeto DisciplinaDAO
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO(conn);

            // Menu interativo
            while (true) {
                System.out.println("\n### Menu ###");
                System.out.println("1. Criar disciplina");
                System.out.println("2. Atualizar disciplina");
                System.out.println("3. Excluir disciplina");
                System.out.println("4. Listar disciplinas");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();  // Consumir a nova linha após a opção ser lida

                switch (opcao) {
                    case 1:
                        // Criar Disciplina
                        System.out.print("Digite o nome da disciplina: ");
                        String nome = scanner.nextLine();
                        System.out.print("Digite o ID do curso: ");
                        int idCurso = scanner.nextInt();
                        scanner.nextLine();  // Consumir a nova linha após o número
                        Disciplina novaDisciplina = new Disciplina(nome, idCurso);
                        disciplinaDAO.criarDisciplina(novaDisciplina);
                        break;

                    case 2:
                        // Atualizar Disciplina
                        System.out.print("Digite o ID da disciplina que deseja atualizar: ");
                        int idAtualizar = scanner.nextInt();
                        scanner.nextLine();  // Consumir a nova linha após o número
                        System.out.print("Digite o novo nome da disciplina: ");
                        String nomeAtualizado = scanner.nextLine();
                        System.out.print("Digite o novo ID do curso: ");
                        int idCursoAtualizado = scanner.nextInt();
                        scanner.nextLine();  // Consumir a nova linha após o número
                        Disciplina disciplinaAtualizada = new Disciplina(nomeAtualizado, idCursoAtualizado);
                        disciplinaAtualizada.setId_Disciplina(idAtualizar);
                        disciplinaDAO.atualizarDisciplina(disciplinaAtualizada);
                        break;

                    case 3:
                        // Excluir Disciplina
                        System.out.print("Digite o ID da disciplina que deseja excluir: ");
                        int idExcluir = scanner.nextInt();
                        scanner.nextLine();  // Consumir a nova linha após o número
                        disciplinaDAO.excluirDisciplina(idExcluir);
                        break;

                    case 4:
                        // Listar Disciplinas
                        List<Disciplina> disciplinas = disciplinaDAO.listarDisciplinas();
                        System.out.println("\nLista de Disciplinas:");
                        if (disciplinas.isEmpty()) {
                            System.out.println("Nenhuma disciplina cadastrada.");
                        } else {
                            for (Disciplina d : disciplinas) {
                                System.out.println("ID: " + d.getId_Disciplina() +
                                        " | Nome: " + d.getNomeDisciplina() +
                                        " | ID Curso: " + d.getId_Curso());
                            }
                        }
                        break;

                    case 5:
                        // Sair
                        System.out.println("Saindo...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro na conexão ou operação com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
