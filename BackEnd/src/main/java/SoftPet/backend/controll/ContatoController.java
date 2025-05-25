package SoftPet.backend.controller;

import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @PostMapping("/criar")
    public ResponseEntity<Integer> criarContato(@RequestBody ContatoModel contato) {
        int id = contatoService.criarContato(contato);
        if (id > 0) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoModel> buscarContato(@PathVariable int id) {
        ContatoModel contato = contatoService.buscarPorId(id);
        if (contato != null) {
            return ResponseEntity.ok(contato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizarContato(@RequestBody ContatoModel contato) {
        boolean atualizado = contatoService.atualizarContato(contato);
        if (atualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarContato(@PathVariable int id) {
        boolean deletado = contatoService.deletarContato(id);
        if (deletado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
