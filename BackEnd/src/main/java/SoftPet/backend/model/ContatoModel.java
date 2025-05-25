package SoftPet.backend.model;

public class ContatoModel {
    private int id;
    private Long telefone;
    private String email;

    public ContatoModel() {}

    public ContatoModel(int id, Long telefone, String email) {
        this.id = id;
        this.telefone = telefone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
