package SoftPet.backend.model;

public class CargoModel {
    private int id;
    private String nome;

    public CargoModel() {
    }

    public CargoModel(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CargoModel(String nome) {
        this.nome = nome;
    }

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
}
