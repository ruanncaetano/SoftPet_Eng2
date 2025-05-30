package SoftPet.backend.model;

public class MedicamentoModel {
    private int cod;
    private String nome;
    private int quantidadeEmEstoque;
    // Outros campos que você julgar necessários (fabricante, lote, validade, etc.)

    public MedicamentoModel() {
    }

    public MedicamentoModel(int cod, String nome, int quantidadeEmEstoque) {
        this.cod = cod;
        this.nome = nome;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
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

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }
}