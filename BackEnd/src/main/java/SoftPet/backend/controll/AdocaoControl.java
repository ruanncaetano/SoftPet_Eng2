package SoftPet.backend.controll;

import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;
import SoftPet.backend.service.AdocaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/buscarAdocao")
    public ResponseEntity<Object> buscarAdocao(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        try {
            List<AdocaoDTO> lista = adocaoService.buscarAdocoes(cpf, dataInicio, dataFim);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }
    @PostMapping("/upcontrato")
    public ResponseEntity<Object> upcontrato(@RequestParam(value = "id") Long id,@RequestParam(value = "contrato") MultipartFile contrato)
    {
        try {
           boolean adocao = adocaoService.upContrato(id,contrato.getBytes());
            return ResponseEntity.ok(adocao);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }
    @GetMapping("/{id}/contrato")
    public ResponseEntity<byte[]> buscarContratoPorIdAdocao(@PathVariable Long id) {
        try {
            byte[] contrato = adocaoService.buscarContratoPorIdAdocao(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Ou o tipo apropriado para seu contrato
            headers.setContentDispositionFormData("attachment", "contrato-adocao-" + id + ".pdf");

            return new ResponseEntity<>(contrato, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
