package model;

public class Turma {

    private int id;
    private String nome;
    private int anoLetivo;
    private int idCurso;

    public Turma(String nome, int anoLetivo, int idCurso) {
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.idCurso = idCurso;
    }

    public Turma() {}

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

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
}
