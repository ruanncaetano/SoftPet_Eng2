package SoftPet.backend.dto;

import SoftPet.backend.model.ProdutoModel;

public class ProdutoDTO {
    private ProdutoModel produto;

    public ProdutoDTO() {}

    public ProdutoDTO(ProdutoModel produto) {
        this.produto = produto;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }
}

