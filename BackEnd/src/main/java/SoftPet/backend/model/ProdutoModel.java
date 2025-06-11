package SoftPet.backend.model;

import java.time.LocalDate;

public class ProdutoModel {
    private Long id;
    private String tipo;
    private String unidadeMedida;
    private LocalDate dataValidade;
    private String descricao;
    private int quantidadeEstoque;

    public ProdutoModel(Long id, String tipo, String unidadeMedida, LocalDate dataValidade, String descricao, int quantidadeEstoque) {
        this.id = id;
        this.tipo = tipo;
        this.unidadeMedida = unidadeMedida;
        this.dataValidade = dataValidade;
        this.descricao = descricao;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public ProdutoModel(String tipo, String unidadeMedida, LocalDate dataValidade, String descricao, int quantidadeEstoque) {
        this(null, tipo, unidadeMedida, dataValidade, descricao, quantidadeEstoque);
    }

    public ProdutoModel() {
        this(null, "", "", null, "", 0);
    }


    public long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
}
