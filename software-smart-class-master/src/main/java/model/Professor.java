package model;

public class Professor {
    private int id;
    private String nome;
    private String disciplina;
    private String email;
    private String telefone;
    private String cpf;
    private String senha;

    public Professor() {
    }

    public Professor(int id, String nome, String disciplina, String email, String telefone, String cpf,String senha) {
        this.id = id;
        this.nome = nome;
        this.disciplina = disciplina;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.senha =senha;
    }

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

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }
}