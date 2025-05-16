package SoftPet.backend.controller;

import SoftPet.backend.model.VoluntarioModel;
import SoftPet.backend.service.VoluntarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/voluntarios")
public class VoluntarioController {
    @Autowired
    private final VoluntarioService voluntarioService;

    public VoluntarioController() {
        this.voluntarioService = new VoluntarioService();
    }

    // Cadastro de voluntário
    @PostMapping("/cadastrar")
    public VoluntarioModel cadastrarVoluntario(@RequestBody VoluntarioModel voluntario) {
        return voluntarioService.cadastrarVoluntario(voluntario);
    }

    // Buscar por CPF
    @GetMapping("/{cpf}")
    public VoluntarioModel buscarPorCPF(@PathVariable String cpf) {
        return voluntarioService.buscarPorCPF(cpf);
    }

    // Atualizar voluntário
    @PutMapping("/atualizar")
    public String atualizarVoluntario(@RequestBody VoluntarioModel voluntario) {
        boolean sucesso = voluntarioService.atualizarVoluntario(voluntario);
        return sucesso ? "Voluntário atualizado com sucesso." : "Erro ao atualizar voluntário.";
    }

    // Deletar por CPF
    @DeleteMapping("/deletar/{cpf}")
    public String deletarVoluntario(@PathVariable String cpf) {
        boolean sucesso = voluntarioService.removerPorCPF(cpf);
        return sucesso ? "Voluntário deletado com sucesso." : "Erro ao deletar voluntário.";
    }

    // Listar todos
    @GetMapping("/listar")
    public List<VoluntarioModel> listarTodos() {
        return voluntarioService.listarTodos();
    }
}
