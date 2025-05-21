package SoftPet.backend.control;

import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.service.AdocaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("adocao")
public class AdocaoControl {
    private  final AdocaoService adocaoService;
    public AdocaoControl(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    @PostMapping("NovaAdocao")
    public ResponseEntity<Object> novaAdocao(@RequestBody AdocaoDTO adocao) {
        try
        {
            AdocaoModel novoAdocao = adocaoService.addAdocao(adocao);
            return ResponseEntity.ok(novoAdocao);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("Erro ao adicionar doação!");
        }
    }
}
