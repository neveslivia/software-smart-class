package model;

public class Curso {
    private int id;
    private String nome;
    private String descricao;
    private int idTurma;  // Novo campo para o id_turma

    // Construtores
    public Curso(String nome, String descricao, int idTurma) {
        this.nome = nome;
        this.descricao = descricao;
        this.idTurma = idTurma;
    }

    public Curso() {}

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    @Override
    public String toString() {
        return nome + " - " + descricao + " - Turma ID: " + idTurma;
    }
}
