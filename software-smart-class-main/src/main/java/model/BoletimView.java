package model;

/**
 * Esta classe representa uma visão do boletim para exibição na interface gráfica.
 * Modificada para exibir informações do aluno ao invés da disciplina.
 */
public class BoletimView {

    private int id;
    private int idAluno;           // Changed from idDisciplina
    private String nomeAluno;      // Changed from nomeDisciplina
    private double nota;
    private int idCurso;

    public BoletimView(int id, int idAluno, String nomeAluno, double nota, int idCurso) {
        this.id = id;
        this.idAluno = idAluno;
        this.nomeAluno = nomeAluno;
        this.nota = nota;
        this.idCurso = idCurso;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
}