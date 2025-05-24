package SoftPet.backend.controll;

import SoftPet.backend.dto.DoacaoDTO;
import SoftPet.backend.model.DoacaoModel;
import SoftPet.backend.service.DoacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/doacao")
public class DoacaoControll
{
    @Autowired
    DoacaoService doacaoService;

    @GetMapping("/listar")
    public ResponseEntity<Object> getAll()
    {
        List<DoacaoDTO> listDoacao = doacaoService.getAllDoacao();
        if(!listDoacao.isEmpty())
            return ResponseEntity.ok(listDoacao);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDoacaoId(@PathVariable Long id)
    {
        DoacaoDTO doacao = doacaoService.getDoacao(id);
        if(doacao != null)
            return ResponseEntity.ok(doacao);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object> addDoacao(@RequestBody DoacaoDTO doacao)
    {
        try
        {
            DoacaoModel novoDoacao = doacaoService.addDoacao(doacao);
            return ResponseEntity.ok(novoDoacao);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("Erro ao adicionar doação!");
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Object> updateDoacao(@RequestBody DoacaoDTO doacao)
    {
        try
        {
            doacaoService.updateDoacao(doacao.getDoacao().getId(), doacao.getDoacao());
            return ResponseEntity.ok("Doação alterada com sucesso!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar doação: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDoacao(@PathVariable Long id)
    {
        try
        {
            doacaoService.deleteDoacao(id);
            return ResponseEntity.ok("Doação deletada com sucesso!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao deletar doação!");
        }
    }
}
