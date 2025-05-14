package SoftPet.backend.control;

import SoftPet.backend.model.AnimalModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SoftPet.backend.service.AnimalService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/animal")
public class AnimalControl {

    private final AnimalService animalService; // Injeção de dependência

    public AnimalControl(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarAnimal(@RequestBody AnimalModel animal) {
        try {
            AnimalModel animalSalvo = animalService.cadastrarAnimal(animal);
            return ResponseEntity.ok(animalSalvo); // Retorna 200 OK com o animal cadastrado
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna 400 em caso de erro
        }
    }
}