package SoftPet.backend.service;

import SoftPet.backend.dal.ProdutoDAL;
import SoftPet.backend.model.ProdutoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoDAL produtoDAL;

    public int criarProduto(ProdutoModel produto) {
        return produtoDAL.criar(produto);
    }

    public ProdutoModel buscarPorId(int id) {
        return produtoDAL.buscarPorId(id);
    }

    public List<ProdutoModel> listarProdutos() {
        return produtoDAL.getAll();
    }   

    public boolean atualizarProduto(ProdutoModel produto) {
        return produtoDAL.update(produto);
    }

    public boolean deletarProduto(int id) {
        return produtoDAL.delete(id);
    }
}
