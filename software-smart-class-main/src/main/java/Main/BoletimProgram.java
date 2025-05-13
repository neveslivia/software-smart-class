package Main;

import DAO.BoletimDAO;
import model.Boletim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BoletimProgram {
    public static void main(String[] args) {

        // Configurações do banco de dados
        String url = "jdbc:mysql://localhost:3306/sistemaacademico";
        String usuario = "root";
        String senha = null;

        // Scanner para ler a entrada do usuário
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            // Criação do objeto BoletimDAO
            BoletimDAO boletimDAO = new BoletimDAO(conn);

            // Menu interativo
            while (true) {
                System.out.println("\n### Menu ###");
                System.out.println("1. Criar boletim");
                System.out.println("2. Atualizar boletim");
                System.out.println("3. Excluir boletim");
                System.out.println("4. Listar boletins");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = sc.nextInt();
                sc.nextLine();  // Consumir a nova linha após a opção ser lida

                switch (opcao) {
                    case 1:
                        // Criar Boletim
                        System.out.print("Digite o ID do aluno: ");
                        int idAluno = sc.nextInt();
                        System.out.print("Digite o ID da turma: ");
                        int idTurma = sc.nextInt();
                        System.out.print("Digite o ID da disciplina: ");
                        int idDisciplina = sc.nextInt();
                        System.out.print("Digite a nota: ");
                        double nota = sc.nextDouble();
                        sc.nextLine();  // Consumir a nova linha após a nota
                        System.out.print("Digite a situação (APROVADO, REPROVADO, RECUPERACAO): ");
                        String situacao = sc.nextLine();

                        Boletim boletim = new Boletim(idAluno, idTurma, idDisciplina, nota, situacao);
                        boletimDAO.criarBoletim(boletim);
                        break;

                    case 2:
                        // Atualizar Boletim
                        System.out.print("Digite o ID do boletim que deseja atualizar: ");
                        int idAtualizar = sc.nextInt();
                        sc.nextLine();  // Consumir a nova linha após o número

                        // Verificar se o boletim existe antes de tentar atualizar
                        List<Boletim> boletins = boletimDAO.listarBoletins();
                        boolean boletimEncontrado = false;
                        for (Boletim b : boletins) {
                            if (b.getId() == idAtualizar) {
                                boletimEncontrado = true;
                                break;
                            }
                        }

                        if (!boletimEncontrado) {
                            System.out.println("Boletim com ID " + idAtualizar + " não encontrado.");
                            break;
                        }

                        // Caso o boletim exista, atualize os dados
                        System.out.print("Digite o novo ID do aluno: ");
                        int novoIdAluno = sc.nextInt();
                        System.out.print("Digite o novo ID da turma: ");
                        int novoIdTurma = sc.nextInt();
                        System.out.print("Digite o novo ID da disciplina: ");
                        int novoIdDisciplina = sc.nextInt();
                        System.out.print("Digite a nova nota: ");
                        double novaNota = sc.nextDouble();
                        sc.nextLine();  // Consumir a nova linha após a nota
                        System.out.print("Digite a nova situação (APROVADO, REPROVADO, RECUPERACAO): ");
                        String novaSituacao = sc.nextLine();

                        // Criar o objeto boletim atualizado
                        Boletim boletimAtualizado = new Boletim(novoIdAluno, novoIdTurma, novoIdDisciplina, novaNota, novaSituacao);
                        boletimAtualizado.setId(idAtualizar);

                        // Atualizar o boletim no banco de dados
                        boletimDAO.atualizarBoletim(boletimAtualizado);
                        System.out.println("Boletim com ID " + idAtualizar + " atualizado com sucesso!");
                        break;

                    case 3:
                        // Excluir Boletim
                        System.out.print("Digite o ID do boletim que deseja excluir: ");
                        int idExcluir = sc.nextInt();
                        boletimDAO.excluirBoletim(idExcluir);
                        break;

                    case 4:
                        // Listar Boletins
                        List<Boletim> listaBoletins = boletimDAO.listarBoletins();
                        System.out.println("\nLista de Boletins:");
                        if (listaBoletins.isEmpty()) {
                            System.out.println("Nenhum boletim encontrado.");
                        } else {
                            for (Boletim b : listaBoletins) {
                                System.out.println("ID: " + b.getId() +
                                        " | ID Aluno: " + b.getIdAluno() +
                                        " | ID Turma: " + b.getIdTurma() +
                                        " | ID Disciplina: " + b.getIdDisciplina() +
                                        " | Nota: " + b.getNota() +
                                        " | Situação: " + b.getSituacao());
                            }
                        }
                        break;

                    case 5:
                        // Sair
                        System.out.println("Saindo...");
                        sc.close();
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
