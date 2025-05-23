package SoftPet.backend.controll;

import SoftPet.backend.model.VoluntarioModel;
import SoftPet.backend.service.VoluntarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/voluntarios")
public class VoluntarioController {

    @Autowired
    private VoluntarioService voluntarioService;

    // Cadastro de voluntário
    @PostMapping("/cadastrar")
    public VoluntarioModel cadastrarVoluntario(@RequestBody VoluntarioModel voluntario) {
        return voluntarioService.cadastrarVoluntario(voluntario);
    }

    // Atualizar voluntário por ID
    @PutMapping("/{id}")
    public ResponseEntity<VoluntarioModel> atualizarVoluntario(@PathVariable int id, @RequestBody VoluntarioModel voluntarioAtualizado) {
        VoluntarioModel voluntarioExistente = voluntarioService.buscarPorId(id);

        if (voluntarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Ajusta IDs relacionados para evitar inconsistência
        voluntarioAtualizado.setId(id);
        voluntarioAtualizado.setContatoCod(voluntarioExistente.getContatoCod());
        voluntarioAtualizado.setCredenciaisCod(voluntarioExistente.getCredenciaisCod());

        boolean atualizado = voluntarioService.atualizarVoluntario(voluntarioAtualizado);

        if (atualizado) {
            return ResponseEntity.ok(voluntarioAtualizado);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar por CPF
    @GetMapping("/{cpf}")
    public VoluntarioModel buscarPorCPF(@PathVariable String cpf) {
        return voluntarioService.buscarPorCPF(cpf);
    }

    // Atualizar voluntário (via PUT, sem ID na URL)
    @PutMapping("/atualizar")
    public String atualizarVoluntario(@RequestBody VoluntarioModel voluntario) {
        boolean sucesso = voluntarioService.atualizarVoluntario(voluntario);
        return sucesso ? "Voluntário atualizado com sucesso." : "Erro ao atualizar voluntário.";
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<VoluntarioModel> buscarPorId(@PathVariable int id) {
        VoluntarioModel voluntario = voluntarioService.buscarPorId(id);
        if (voluntario != null) {
            return ResponseEntity.ok(voluntario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar por CPF
    @DeleteMapping("/deletar/{cpf}")
    public String deletarVoluntario(@PathVariable String cpf) {
        boolean sucesso = voluntarioService.removerPorCPF(cpf);
        return sucesso ? "Voluntário deletado com sucesso." : "Erro ao deletar voluntário.";
    }

    // Listar todos os voluntários
    @GetMapping("/listar")
    public List<VoluntarioModel> listarTodos() {
        return voluntarioService.listarTodos();
    }
}
