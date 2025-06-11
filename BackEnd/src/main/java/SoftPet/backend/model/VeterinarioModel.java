package SoftPet.backend.model;

public class VeterinarioModel {
    private int cod;
    private String nome;
    private String crmv; // Conselho Regional de Medicina Veterinária

    public VeterinarioModel() {
    }

    public VeterinarioModel(int cod, String nome, String crmv) {
        this.cod = cod;
        this.nome = nome;
        this.crmv = crmv;
    }

    // Getters e Setters
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }
}