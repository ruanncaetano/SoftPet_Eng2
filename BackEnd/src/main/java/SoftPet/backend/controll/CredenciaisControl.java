package SoftPet.backend.controll;

import SoftPet.backend.model.CredenciaisModel;
import SoftPet.backend.service.CredenciaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/credenciais")
public class CredenciaisControl {

    @Autowired
    private CredenciaisService credenciaisService;

    @PostMapping("/criar")
    public ResponseEntity<Long> criarCredenciais(@RequestBody CredenciaisModel credenciais) {
        Long id = credenciaisService.buscarOuCriarCredenciais(credenciais);
        if (id > 0) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredenciaisModel> buscarCredenciais(@PathVariable Long id) {
        CredenciaisModel credenciais = credenciaisService.buscarPorId(id);
        if (credenciais != null) {
            return ResponseEntity.ok(credenciais);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar") // Ou "/atualizar/{id}" se o ID vier da URL
    public ResponseEntity<Void> atualizarCredenciais(@RequestBody CredenciaisModel credenciais) {
        boolean atualizado = false;
        try {
            if (credenciais.getId() == null) {
                // Se o ID não estiver no corpo da requisição, você precisa de outra forma de obtê-lo
                // ou retornar um erro.
                // Considerar se o ID deveria vir como @PathVariable se o endpoint for /atualizar/{id}
                throw new IllegalArgumentException("O ID da credencial é obrigatório para atualização e não foi fornecido no corpo da requisição.");
            }
            // Chama o serviço com o ID e o objeto de credenciais
            atualizado = credenciaisService.atualizarCredenciais(credenciais.getId(), credenciais);

            if (atualizado) {
                return ResponseEntity.ok().build();
            } else {
                // Isso pode acontecer se o DAL.atualizar retornar false (ex: nenhuma linha afetada, mas não lançou exceção)
                // ou se o buscarPorId no serviço não encontrar a credencial (embora deva lançar exceção antes).
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou um erro mais específico
            }
        } catch (IllegalArgumentException e) {
            // Se o ID for nulo ou outras validações do serviço falharem
            // Idealmente, o corpo da resposta teria a mensagem de erro e.getMessage()
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            // Para outras exceções, como "não encontrado" vindo do buscarPorId no serviço
            if (e.getMessage() != null && e.getMessage().contains("não encontradas")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            // Para outros erros inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCredenciais(@PathVariable Long id) {
        boolean deletado = credenciaisService.deletarCredenciais(id);
        if (deletado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
