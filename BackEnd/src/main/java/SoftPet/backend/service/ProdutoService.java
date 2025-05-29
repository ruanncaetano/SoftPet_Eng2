package SoftPet.backend.service;

import SoftPet.backend.dal.ProdutoDAL;
import SoftPet.backend.model.ProdutoModel;
// Se o EstoqueUpdateRequestDTO for usado aqui, importe-o.
// Por enquanto, o serviço receberá os parâmetros diretamente.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoDAL produtoDAL; // Alterado para injeção via construtor

    @Autowired // Injeção de dependência via construtor (recomendado)
    public ProdutoService(ProdutoDAL produtoDAL) {
        this.produtoDAL = produtoDAL;
    }

    public int criarProduto(ProdutoModel produto) {
        // Adicionar validações de negócio aqui se necessário antes de chamar o DAL
        if (produto == null) {
            throw new IllegalArgumentException("Dados do produto não podem ser nulos.");
        }
        // Ex: if (produto.getQuantidadeEstoque() < 0) { throw new IllegalArgumentException("Estoque inicial não pode ser negativo."); }
        return produtoDAL.criar(produto);
    }

    public ProdutoModel buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do produto deve ser positivo.");
        }
        ProdutoModel produto = produtoDAL.buscarPorId(id);
        if (produto == null) {
            // Lançar uma exceção mais específica é uma boa prática
            throw new RuntimeException("Produto com ID " + id + " não encontrado.");
        }
        return produto;
    }

    public List<ProdutoModel> listarProdutos() {
        return produtoDAL.getAll();
    }

    public boolean atualizarProduto(ProdutoModel produto) {
        if (produto == null || produto.getId() == 0) { // Assumindo que ID 0 é inválido
            throw new IllegalArgumentException("Dados do produto ou ID inválidos para atualização.");
        }
        // Garante que o produto existe antes de tentar atualizar
        buscarPorId(produto.getId());
        return produtoDAL.update(produto);
    }

    public boolean deletarProduto(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do produto deve ser positivo para deleção.");
        }
        // Garante que o produto existe antes de tentar deletar
        buscarPorId(id);
        return produtoDAL.delete(id);
    }

    // --- NOVO MÉTODO PARA ATUALIZAR APENAS O ESTOQUE ---
    /**
     * Ajusta a quantidade em estoque de um produto específico.
     * produtoId: O ID do produto a ser atualizado.
     * quantidadeParaAlterar: A quantidade a ser adicionada (positiva) ou removida (negativa) do estoque.
     * Retorna o ProdutoModel atualizado.
     * Lança exceções se o produto não for encontrado ou se a operação for inválida (ex: estoque negativo).
     */
    public ProdutoModel ajustarEstoqueProduto(int produtoId, int quantidadeParaAlterar) {
        if (produtoId <= 0) {
            throw new IllegalArgumentException("ID do produto deve ser positivo para ajuste de estoque.");
        }
        // A validação de quantidadeParaAlterar == 0 pode ser feita aqui ou no controller,
        // ou permitir e o DAL simplesmente não fará nada (mas é melhor ser explícito).
        // O método atualizarApenasEstoque no DAL já lida com a busca do produto e a lógica de estoque negativo.

        return produtoDAL.atualizarApenasEstoque(produtoId, quantidadeParaAlterar);
    }
}
