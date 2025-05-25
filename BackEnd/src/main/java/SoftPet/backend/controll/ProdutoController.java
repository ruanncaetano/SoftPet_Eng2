package SoftPet.backend.controller;

import SoftPet.backend.model.ProdutoModel;
import SoftPet.backend.service  .ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/criar")
    public ResponseEntity<Integer> criarProduto(@RequestBody ProdutoModel produto) {
        int id = produtoService.criarProduto(produto);
        if (id > 0) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoModel> buscarProduto(@PathVariable int id) {
        ProdutoModel produto = produtoService.buscarPorId(id);
        if (produto != null) {
            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> listarTodos() {
        List<ProdutoModel> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizarProduto(@RequestBody ProdutoModel produto) {
        boolean atualizado = produtoService.atualizarProduto(produto);
        if (atualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable int id) {
        boolean deletado = produtoService.deletarProduto(id);
        if (deletado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
