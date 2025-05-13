package model;

import java.time.LocalDate;

public class Boletim {
    private int id;
    private int idAluno;
    private int idTurma;
    private int idDisciplina;
    private double nota;
    private String situacao;
    private LocalDate dataLancamento;

    // Construtor vazio
    public Boletim() {
        this.dataLancamento = LocalDate.now(); // Data padrão é hoje
    }

    // Construtor para criação de novo boletim (sem ID, que será gerado pelo banco)
    public Boletim(int idAluno, int idTurma, int idDisciplina, double nota, String situacao) {
        this.idAluno = idAluno;
        this.idTurma = idTurma;
        this.idDisciplina = idDisciplina;
        this.nota = nota;
        this.situacao = situacao;
        this.dataLancamento = LocalDate.now(); // Data padrão é hoje
    }

    // Construtor completo (usado ao recuperar do banco de dados)
    public Boletim(int id, int idAluno, int idTurma, int idDisciplina, double nota, String situacao, LocalDate dataLancamento) {
        this.id = id;
        this.idAluno = idAluno;
        this.idTurma = idTurma;
        this.idDisciplina = idDisciplina;
        this.nota = nota;
        this.situacao = situacao;
        this.dataLancamento = dataLancamento;
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

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "Boletim{" +
                "id=" + id +
                ", idAluno=" + idAluno +
                ", idTurma=" + idTurma +
                ", idDisciplina=" + idDisciplina +
                ", nota=" + nota +
                ", situacao='" + situacao + '\'' +
                ", dataLancamento=" + dataLancamento +
                '}';
    }
}