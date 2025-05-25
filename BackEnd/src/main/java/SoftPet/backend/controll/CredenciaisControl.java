package SoftPet.backend.controller;

import SoftPet.backend.model.CredenciaisModel;
import SoftPet.backend.service.CredenciaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/credenciais")
public class CredenciaisControl {

    @Autowired
    private CredenciaisService credenciaisService;

    @PostMapping("/criar")
    public ResponseEntity<Integer> criarCredenciais(@RequestBody CredenciaisModel credenciais) {
        int id = credenciaisService.criarCredenciais(credenciais);
        if (id > 0) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredenciaisModel> buscarCredenciais(@PathVariable int id) {
        CredenciaisModel credenciais = credenciaisService.buscarPorId(id);
        if (credenciais != null) {
            return ResponseEntity.ok(credenciais);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizarCredenciais(@RequestBody CredenciaisModel credenciais) {
        boolean atualizado = credenciaisService.atualizarCredenciais(credenciais);
        if (atualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCredenciais(@PathVariable int id) {
        boolean deletado = credenciaisService.deletarCredenciais(id);
        if (deletado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
