package SoftPet.backend.service;

import SoftPet.backend.dal.ProdutoDAL;
import SoftPet.backend.dto.ProdutoDTO;
import SoftPet.backend.model.ProdutoModel;
import SoftPet.backend.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoDAL produtoDAL;

    public ProdutoModel addProduto(ProdutoDTO produtoDTO) throws Exception {
        ProdutoModel produto = produtoDTO.getProduto();

        if (!Validation.validarTextoProduto(produto.getTipo()))
            throw new IllegalArgumentException("Tipo do produto inválido!");

        if (!Validation.validarTextoProduto(produto.getDescricao()))
            throw new IllegalArgumentException("Descrição do produto inválida!");

        if (!Validation.validarUnidadeMedida(produto.getUnidadeMedida()))
            throw new IllegalArgumentException("Unidade de medida inválida!");

        if (!Validation.validarQuantidadeEstoque(produto.getQuantidadeEstoque()))
            throw new IllegalArgumentException("Quantidade em estoque inválida!");

        if (!Validation.validarDataValidade(produto.getDataValidade()))
            throw new IllegalArgumentException("Data de validade inválida!");

        return produtoDAL.addProduto(produto);
    }

    public ProdutoDTO getProduto(Long id) {
        return produtoDAL.findByProduto(id);
    }

    public void updateProduto(Long id, ProdutoModel produto) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID do produto inválido!");

        if (!Validation.validarTextoProduto(produto.getTipo()))
            throw new IllegalArgumentException("Tipo do produto inválido!");

        if (!Validation.validarTextoProduto(produto.getDescricao()))
            throw new IllegalArgumentException("Descrição do produto inválida!");

        if (!Validation.validarUnidadeMedida(produto.getUnidadeMedida()))
            throw new IllegalArgumentException("Unidade de medida inválida!");

        if (!Validation.validarQuantidadeEstoque(produto.getQuantidadeEstoque()))
            throw new IllegalArgumentException("Quantidade em estoque inválida!");

        if (!Validation.validarDataValidade(produto.getDataValidade()))
            throw new IllegalArgumentException("Data de validade inválida!");

        ProdutoDTO produtoExistente = produtoDAL.findByProduto(id);
        if (produtoExistente == null)
            throw new Exception("Produto com esse ID não foi encontrado!");

        produto.setId(id);
        produtoDAL.updateProduto(produto);
    }

    public void deleteProduto(Long id) throws Exception {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID do produto inválido!");

        ProdutoDTO produtoDelete = produtoDAL.findByProduto(id);

        if (produtoDelete == null)
            throw new Exception("Produto com esse ID não existe!");

        if (!produtoDAL.deleteByProduto(id))
            throw new Exception("Erro ao deletar o produto!");
    }

    public List<ProdutoDTO> getAllProdutos() {
        return produtoDAL.getAllProdutos();
    }
    public List<ProdutoDTO> buscarProdutosPorTipo(String tipo) {
        return produtoDAL.getProdutosPorTipo(tipo);
    }
}
