package SoftPet.backend.controll;

import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.DoadorModel;
import SoftPet.backend.model.EnderecoModel;
import SoftPet.backend.service.DoadorService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SoftPet.backend.dto.DoadorCompletoDTO;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/doador")
public class DoadorControll
{
    @Autowired
    DoadorService doadorService;

    @GetMapping("/listar")
    public ResponseEntity<Object> getAll()
    {
        List<DoadorCompletoDTO> listDoador = doadorService.getAllDoador();
        if(!listDoador.isEmpty())
            return ResponseEntity.ok(listDoador);
        return ResponseEntity.badRequest().body("Erro ao listar todos os doadores!");
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getDoadorCpf(@PathVariable String cpf)
    {
        DoadorCompletoDTO doador = doadorService.getDoadorCpf(cpf);
        if(doador != null)
            return ResponseEntity.ok(doador);
        return ResponseEntity.badRequest().body("Doador não encontrado!");
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object> addDoador(@RequestBody DoadorCompletoDTO doador)
    {
        try
        {
            DoadorModel novoDoador = doadorService.addDoador(doador);
            return ResponseEntity.ok(novoDoador);
        }
        catch(Exception e)
        {
            return  ResponseEntity.badRequest().body("Erro ao adicionar doador!");
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Object> updateDoador(@RequestBody DoadorCompletoDTO doador)
    {
        try
        {
            doadorService.updateDoador(doador.getDoador().getCpf(), doador.getDoador(), doador.getContato(), doador.getEndereco());
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
            doadorService.deleteDoador(cpf);
            return ResponseEntity.ok("Doador deletado com sucesso!");
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao deletar doador!");
        }
    }

}
