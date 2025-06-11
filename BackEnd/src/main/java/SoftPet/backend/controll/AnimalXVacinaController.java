package SoftPet.backend.controll;

import SoftPet.backend.dal.AnimalXVacinacaoDAL;
import SoftPet.backend.dto.AnimalXVacinacaoDTO;
import SoftPet.backend.model.AnimalXVacinacaoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animal-x-vacinacao")
@CrossOrigin(origins = "*") // Libera para frontend acessar
public class AnimalXVacinaController {

    @Autowired
    private AnimalXVacinacaoDAL dal;

    public AnimalXVacinaController(AnimalXVacinacaoDAL dal) {
        this.dal = dal;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody AnimalXVacinacaoDTO dto) {

        System.out.println("Recebido: " + dto.getCodAnimal());
        if (dto.getCodAnimal() == null) {
            throw new IllegalArgumentException("O código do animal é obrigatório para o registro de vacinação.");
        }
        AnimalXVacinacaoModel model = new AnimalXVacinacaoModel();
        model.setAnimalCod(dto.getCodAnimal());
        model.setVacinaCod(dto.getCodVacina());
        model.setVoluntarioAplicadorCod(dto.getCodAplicdor());
        model.setDataAplicacao(dto.getDataAplicacao());
        model.setDoseNumerica(dto.getDose());
        model.setObservacao(dto.getDescricao());

        dal.registrarAplicacaoVacina(model);
        return ResponseEntity.ok().build();
    }
}
