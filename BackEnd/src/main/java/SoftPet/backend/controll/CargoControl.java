package SoftPet.backend.controll;

import SoftPet.backend.dal.CargoDAL;
import SoftPet.backend.model.CargoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cargos")
public class CargoControl {

    @Autowired
    private CargoDAL cargoDAL;

    // Listar todos os cargos
    @GetMapping
    public List<CargoModel> listarTodos() {
        return cargoDAL.getAll();
    }

    // Criar um novo cargo (se não existir, caso contrário retorna o existente)
    @PostMapping
    public ResponseEntity<CargoModel> criarOuRetornarCargo(@RequestBody CargoModel cargo) {
        CargoModel existente = cargoDAL.buscarPorNome(cargo.getNome());
        if (existente != null) {
            // Já existe - retorna com status 200 OK
            return ResponseEntity.ok(existente);
        }

        // Não existe - cria um novo cargo usando o método criar
        Long idGerado = cargoDAL.criar(cargo);
        cargo.setId(idGerado);
        return ResponseEntity.status(201).body(cargo);
    }

    // Buscar cargo por ID
    @GetMapping("/{id}")
    public ResponseEntity<CargoModel> buscarPorId(@PathVariable int id) {
        CargoModel cargo = cargoDAL.findById(id);
        if (cargo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cargo);
    }

    // Atualizar cargo
    @PutMapping("/{id}")
    public ResponseEntity<CargoModel> atualizar(@PathVariable Long id, @RequestBody CargoModel cargo) {
        cargo.setId(id);
        boolean atualizado = cargoDAL.update(cargo);
        if (!atualizado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cargo);
    }

    // Deletar cargo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        boolean deletado = cargoDAL.delete(id);
        if (!deletado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
