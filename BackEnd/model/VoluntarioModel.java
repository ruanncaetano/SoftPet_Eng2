package SoftPet.backend.model;

public class VoluntarioModel {
    private int id;
    private String cpf;
    private String nome;
    private int cargoCod;
    private int contatoCod;
    private int credenciaisCod;

    public VoluntarioModel() {}

    public VoluntarioModel(int id, String cpf, String nome, int cargoCod, int contatoCod, int credenciaisCod) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.cargoCod = cargoCod;
        this.contatoCod = contatoCod;
        this.credenciaisCod = credenciaisCod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.lang.String getCpf() {
        return cpf;
    }

    public void setCpf(java.lang.String cpf) {
        this.cpf = cpf;
    }

    public java.lang.String getNome() {
        return nome;
    }

    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }

    public int getCargoCod() {
        return cargoCod;
    }

    public void setCargoCod(int cargoCod) {
        this.cargoCod = cargoCod;
    }

    public int getContatoCod() {
        return contatoCod;
    }

    public void setContatoCod(int contatoCod) {
        this.contatoCod = contatoCod;
    }

    public int getCredenciaisCod() {
        return credenciaisCod;
    }

    public void setCredenciaisCod(int credenciaisCod) {
        this.credenciaisCod = credenciaisCod;
    }
}
