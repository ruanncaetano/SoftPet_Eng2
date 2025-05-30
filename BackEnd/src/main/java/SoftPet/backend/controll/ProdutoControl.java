package SoftPet.backend.controll;

import SoftPet.backend.dto.EstoqueUpdateRequestDTO; // Importa o novo DTO
import SoftPet.backend.model.ProdutoModel;
import SoftPet.backend.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos") // Alterei o RequestMapping base para /api/produtos para consistência
public class ProdutoControl {

    private final ProdutoService produtoService; // Alterado para injeção via construtor

    @Autowired // Injeção de dependência via construtor
    public ProdutoControl(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarProduto(@RequestBody ProdutoModel produto) {
        try {
            int id = produtoService.criarProduto(produto);
            // Se o DAL.criar agora retorna o ProdutoModel com ID, podemos retornar o objeto.
            // Por enquanto, mantendo o retorno do ID se for > 0.
            if (id > 0) {
                // Para retornar o objeto completo, você faria:
                // ProdutoModel produtoCriado = produtoService.buscarPorId(id);
                // return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
                return ResponseEntity.status(HttpStatus.CREATED).body(id);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar produto, ID não gerado.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao criar produto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable int id) {
        try {
            ProdutoModel produto = produtoService.buscarPorId(id);
            return ResponseEntity.ok(produto);
        } catch (IllegalArgumentException e) { // Captura ID inválido do serviço
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // Captura "não encontrado" do serviço
            if (e.getMessage() != null && e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao buscar produto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> listarTodos() {
        try {
            List<ProdutoModel> produtos = produtoService.listarProdutos();
            if (produtos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para atualização completa do produto
    @PutMapping("/atualizar/{id}") // Adicionado {id} ao path para clareza
    public ResponseEntity<?> atualizarProduto(@PathVariable int id, @RequestBody ProdutoModel produto) {
        try {
            if (produto.getId() == 0) { // Se o ID não vier no corpo, usa o do path
                produto.setId(id);
            } else if (produto.getId() != id) {
                return ResponseEntity.badRequest().body("O ID do produto no corpo da requisição não corresponde ao ID na URL.");
            }

            boolean atualizado = produtoService.atualizarProduto(produto);
            if (atualizado) {
                ProdutoModel produtoAtualizado = produtoService.buscarPorId(id); // Retorna o produto atualizado
                return ResponseEntity.ok(produtoAtualizado);
            } else {
                // Isso pode acontecer se o produto não for encontrado para atualização pelo DAL,
                // mas o buscarPorId no serviço já deveria ter lançado exceção.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto com ID " + id + " não encontrado para atualização ou nenhuma alteração foi feita.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao atualizar produto: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable int id) {
        try {
            boolean deletado = produtoService.deletarProduto(id);
            if (deletado) {
                return ResponseEntity.noContent().build(); // 204 No Content é apropriado para delete
            } else {
                // Se deletarProduto no serviço lançar exceção para "não encontrado", este else não será atingido.
                // Se o DAL.delete retornar false sem lançar exceção, este pode ser o caso.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto com ID " + id + " não encontrado para deleção.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao deletar produto: " + e.getMessage());
        }
    }

    // --- NOVO ENDPOINT PARA ATUALIZAR APENAS O ESTOQUE ---
    /**
     * Endpoint para ajustar o estoque de um produto específico.
     * id: O ID do produto (da URL).
     * requestDTO: Contém a 'quantidade' a ser adicionada (positiva) ou removida (negativa).
     * Retorna o ProdutoModel atualizado.
     */
    @PutMapping("/{id}/estoque") // Método PUT ou PATCH podem ser usados. PUT geralmente implica substituir o recurso ou parte dele.
    public ResponseEntity<?> ajustarEstoqueProduto(@PathVariable int id, @RequestBody EstoqueUpdateRequestDTO requestDTO) {
        try {
            if (requestDTO.getQuantidade() == null) {
                return ResponseEntity.badRequest().body("A 'quantidade' para alteração do estoque é obrigatória no corpo da requisição.");
            }
            ProdutoModel produtoAtualizado = produtoService.ajustarEstoqueProduto(id, requestDTO.getQuantidade());
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Captura erros de validação do serviço ou DAL
        } catch (RuntimeException e) { // Captura "não encontrado" ou outras falhas
            if (e.getMessage() != null && e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao ajustar estoque: " + e.getMessage());
        }
    }
}
