package SoftPet.backend.control;

import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;
import SoftPet.backend.service.AdocaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/adocao")
public class AdocaoControl {
    private  final AdocaoService adocaoService;
    public AdocaoControl(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    @PostMapping("NovaAdocao")
    public ResponseEntity<Object> novaAdocao(
            @RequestParam("ado_dt") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data,
            @RequestParam(value = "ado_contrato", required = false) String contrato,
            @RequestParam("pe_cod") Long pessoaId,
            @RequestParam("an_cod") int animalId) {

        try {
            System.out.println("Dados recebidos:");
            System.out.println("Data: " + data);
            System.out.println("ID Pessoa: " + pessoaId);
            System.out.println("ID Animal: " + animalId);

//            // Converter a data
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date dataAdocao = sdf.parse(data);

            // Converter contrato se necessário
            byte[] contratoBytes = contrato != null ? contrato.getBytes() : null;

            // Criar DTO manualmente
            AdocaoDTO adocaoDTO = new AdocaoDTO();

            AdocaoModel adocaoModel = new AdocaoModel();
            adocaoModel.setContrato(contratoBytes);
            adocaoModel.setAdo_dt(data);
            adocaoDTO.setAdocao(adocaoModel);
            PessoaModel pessoa = new PessoaModel();
            pessoa.setId(pessoaId);
            adocaoDTO.setPessoa(pessoa);

            AnimalModel animal = new AnimalModel();
            animal.setCod(animalId);
            adocaoDTO.setAnimal(animal);

            AdocaoModel novaAdocao = adocaoService.addAdocao(adocaoDTO);
            return ResponseEntity.ok(novaAdocao);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao processar adoção: " + e.getMessage());
        }
    }

    @GetMapping("/bucarAdocao")
    public ResponseEntity<Object> buscarAdocao(@RequestParam String cpf)
    {
        AdocaoDTO adocao = null;
        try
        {
            adocao= adocaoService.getAdocao(cpf);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(adocao);
    }
}
