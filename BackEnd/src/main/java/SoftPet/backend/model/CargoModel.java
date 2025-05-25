package SoftPet.backend.model;

public class CargoModel {
    private Long id;
    private String nome;

    public CargoModel() {
    }

    public CargoModel(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CargoModel(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
