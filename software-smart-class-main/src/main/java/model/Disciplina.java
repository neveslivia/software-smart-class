package model;

public class Disciplina {

    private int id_Disciplina;
    private String nomeDisciplina;
    private int id_Curso;
    private  int idCurso;
    private  int idDisciplina;
    private  String nome;


    public Disciplina() {
    }

    public Disciplina(String nomeDisciplina, int idCurso) {
        this.nomeDisciplina = nomeDisciplina;
        this.id_Curso = idCurso;
    }

    public int getId_Disciplina() {
        return id_Disciplina;
    }

    public void setId_Disciplina(int id_Disciplina) {
        this.id_Disciplina = id_Disciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public int getId_Curso() {
        return 0;
    }

    public void setId_Curso(int id_Curso) {
        this.id_Curso = id_Curso;
    }

    public String getNota() {
        return "";
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public void setId(int idDisciplina) {
        this.idDisciplina =idDisciplina;
    }


    public void setNome(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
}
