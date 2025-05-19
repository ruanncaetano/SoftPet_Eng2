package SoftPet.backend.control;

import SoftPet.backend.model.AnimalModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SoftPet.backend.service.AnimalService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/animal")
public class AnimalControl
{

    private final AnimalService animalService; // Injeção de dependência

    public AnimalControl(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarAnimal( @RequestParam("nome") String nome,
                                                   @RequestParam("idade") int idade,
                                                   @RequestParam("tipo") String tipo,
                                                   @RequestParam("sexo") String sexo,
                                                   @RequestParam("porte") String porte,
                                                   @RequestParam("raca") String raca,
                                                   @RequestParam("pelagem") String pelagem,
                                                   @RequestParam("peso") int peso,
                                                   @RequestParam("baia") String baia,
                                                   @RequestParam("resgate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date resgate,
                                                   @RequestParam("adocao") boolean adocao,
                                                   @RequestParam("castrado") boolean castrado,
                                                   @RequestParam("ativo") boolean ativo,
                                                   @RequestParam(value = "foto", required = false) MultipartFile foto,
                                                   @RequestParam(value = "obs", required = false) String obs) throws IOException {

        AnimalModel animal = new AnimalModel(nome,idade,tipo,sexo,porte,raca,pelagem,peso,baia,resgate,adocao,castrado,obs,ativo);
        if (foto != null && !foto.isEmpty()) {
            animal.setFoto(foto.getBytes()); // ← CONVERSÃO PARA byte[]   -> passando por fora do contrutor
        }
        try {
            AnimalModel animalSalvo = animalService.cadastrarAnimal(animal);
            return ResponseEntity.ok(animalSalvo); // Retorna 200 OK com o animal cadastrado
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna 400 em caso de erro
        }
    }

    @GetMapping("/filtro")
    public ResponseEntity<Object> filtrar(@RequestParam(required = false) String nome,
                                          @RequestParam(required = false)String porte,
                                          @RequestParam(required = false) String tipo,
                                          @RequestParam(required = false) String sexo,
                                          @RequestParam(required = false) boolean status)
    {
        //required = false é para deixar opcional
        List<AnimalModel> animais=animalService.buscarAnimais(nome,porte,tipo,sexo,status);
        return ResponseEntity.ok(animais);
    }
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> getFoto(@PathVariable Long id) {
        byte[] foto = animalService.getFoto(id); // busca no banco

        if (foto == null || foto.length == 0) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // ou IMAGE_PNG conforme o tipo real

        return new ResponseEntity<>(foto, headers, HttpStatus.OK);
    }
}
