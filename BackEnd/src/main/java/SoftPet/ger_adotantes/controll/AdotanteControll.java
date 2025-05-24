package SoftPet.ger_adotantes.controll;

import SoftPet.ger_adotantes.model.AdotanteModel;
import SoftPet.ger_adotantes.service.AdotanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SoftPet.ger_adotantes.dto.AdotanteCompletoDTO;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/adotante")
public class AdotanteControll
{
    @Autowired
    AdotanteService adotanteService;

    @GetMapping("/listar")
    public ResponseEntity<Object> getAll()
    {
        List<AdotanteCompletoDTO> listAdotante = adotanteService.getAllAdotante();
        if(!listAdotante.isEmpty())
            return ResponseEntity.ok(listAdotante);
        return ResponseEntity.badRequest().body("Erro ao listar todos os adotantes!");
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getAdotanteCpf(@PathVariable String cpf)
    {
        AdotanteCompletoDTO adotante = adotanteService.getAdotanteCpf(cpf);
        if(adotante != null)
            return ResponseEntity.ok(adotante);
        return ResponseEntity.badRequest().body("Adotante não encontrado!");
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object> addAdotante(@RequestBody AdotanteCompletoDTO adotante)
    {
        try
        {
            AdotanteModel novoAdotante = adotanteService.addAdotante(adotante);
            return ResponseEntity.ok(novoAdotante);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("Erro ao adicionar adotante!");
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Object> updateAdotante(@RequestBody AdotanteCompletoDTO adotante)
    {
        try
        {
            adotanteService.updateAdotante(adotante.getAdotante().getCpf(), adotante.getAdotante(), adotante.getContato(), adotante.getEndereco());
            return ResponseEntity.ok("Adotante alterado com sucesso!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar adotante: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deleteAdotante(@PathVariable String cpf)
    {
        try
        {
            adotanteService.deleteAdotante(cpf);
            return ResponseEntity.ok("Adotante deletado com sucesso!");
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao deletar adotante!");
        }
    }

}
