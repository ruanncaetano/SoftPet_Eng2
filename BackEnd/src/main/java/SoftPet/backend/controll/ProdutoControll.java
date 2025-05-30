package SoftPet.backend.controll;

import SoftPet.backend.dto.ProdutoDTO;
import SoftPet.backend.model.ProdutoModel;
import SoftPet.backend.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoControll {

    @Autowired
    ProdutoService produtoService;

    @GetMapping("/listar/{tipo}")
    public ResponseEntity<List<ProdutoDTO>> listarPorTipo(@PathVariable String tipo) {
        List<ProdutoDTO> produtos = produtoService.buscarProdutosPorTipo(tipo);
        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtos);
    }


    @GetMapping("/listar")
    public ResponseEntity<Object> getAll() {
        List<ProdutoDTO> listProdutos = produtoService.getAllProdutos();
        if (!listProdutos.isEmpty())
            return ResponseEntity.ok(listProdutos);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProdutoId(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.getProduto(id);
        if (produto != null)
            return ResponseEntity.ok(produto);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object> addProduto(@RequestBody ProdutoDTO produto) {
        try {
            ProdutoModel novoProduto = produtoService.addProduto(produto);
            return ResponseEntity.ok(novoProduto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao adicionar produto!");
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Object> updateProduto(@RequestBody ProdutoDTO produto) {
        try {
            produtoService.updateProduto(produto.getProduto().getId(), produto.getProduto());
            return ResponseEntity.ok("Produto alterado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable Long id) {
        try {
            produtoService.deleteProduto(id);
            return ResponseEntity.ok("Produto deletado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao deletar produto!");
        }
    }


}
