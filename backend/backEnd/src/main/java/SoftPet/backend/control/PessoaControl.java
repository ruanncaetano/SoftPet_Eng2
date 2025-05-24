package SoftPet.backend.control;

import SoftPet.backend.dal.PessoaDAL;
import SoftPet.backend.dto.AdotanteCompletoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pessoa")
public class PessoaControl {

    private PessoaDAL pessoaDAL;

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getDoadorCpf(@PathVariable String cpf)
    {
        AdotanteCompletoDTO doador = pessoaDAL.findByDoador(cpf);
        if(doador != null)
            return ResponseEntity.ok(doador);
        return ResponseEntity.badRequest().body("Doador não encontrado!");
    }
}
