package model;

public class Boletim {

    private int id; // id_boletim
    private int idAluno;
    private int idTurma;
    private int idDisciplina;
    private double nota;
    private String situacao; // 'APROVADO', 'REPROVADO', 'RECUPERACAO'

    public Boletim() {
    }

    public Boletim(int idAluno, int idTurma, int idDisciplina, double nota, String situacao) {
        this.idAluno = idAluno;
        this.idTurma = idTurma;
        this.idDisciplina = idDisciplina;
        this.nota = nota;
        this.situacao = situacao;
    }

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

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
