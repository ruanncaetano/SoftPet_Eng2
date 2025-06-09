package SoftPet.backend.controll;

import SoftPet.backend.model.PessoaModel;
import SoftPet.backend.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SoftPet.backend.dto.PessoaCompletoDTO;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/doador")
public class PessoaControll
{
    @Autowired
    PessoaService pessoaService;

    @GetMapping("/listar")
    public ResponseEntity<Object> getAll()
    {
        List<PessoaCompletoDTO> listDoador = pessoaService.getAllDoador();
        if(!listDoador.isEmpty())
            return ResponseEntity.ok(listDoador);
        return ResponseEntity.badRequest().body("Erro ao listar todos os doadores!");
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getDoadorCpf(@PathVariable String cpf)
    {
        PessoaCompletoDTO doador = pessoaService.getDoadorCpf(cpf);
        if(doador != null)
            return ResponseEntity.ok(doador);
        return ResponseEntity.badRequest().body("Pessoa não encontrada!");
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object> addDoador(@RequestBody PessoaCompletoDTO doador)
    {
        try
        {
            PessoaModel novoDoador = pessoaService.addDoador(doador);
            return ResponseEntity.ok(novoDoador);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("Erro ao adicionar doador!");
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Object> updateDoador(@RequestBody PessoaCompletoDTO doador)
    {
        try
        {
            pessoaService.updateDoador(doador.getPessoa().getCpf(), doador.getPessoa(), doador.getContato(), doador.getEndereco());
            return ResponseEntity.ok("Doador alterado com sucesso!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar doador: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deleteDoador(@PathVariable String cpf)
    {
        try
        {
            pessoaService.deleteDoador(cpf);
            return ResponseEntity.ok("Doador deletado com sucesso!");
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao deletar doador!");
        }
    }

}
