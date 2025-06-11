package SoftPet.backend.controll;

import SoftPet.backend.dto.VacinaRequestDTO;
import SoftPet.backend.dto.VacinacaoRequestDTO; // DTO para efetuar vacinação
import SoftPet.backend.model.AnimalXVacinacaoModel; // Modelo de retorno para efetuar vacinação
import SoftPet.backend.model.VacinaModel;
import SoftPet.backend.service.VacinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import jakarta.validation.Valid; // Para Spring Boot 3+ se usar validações no DTO

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
// O RequestMapping base pode ser /api ou um mais específico se preferir separar
// as responsabilidades deste controller. Por enquanto, manterei /api/vacinas
// e adicionarei um sub-caminho para a aplicação.
@RequestMapping("/api/vacinas")
public class VacinaControl {

    private final VacinaService vacinaService;

    @Autowired
    public VacinaControl(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    // --- Endpoints CRUD para a entidade VacinaModel (gerenciar tipos de vacina) ---

    @PostMapping
    public ResponseEntity<?> adicionarVacina(/*@Valid*/ @RequestBody VacinaRequestDTO requestDTO) {
        try {
            VacinaModel novaVacina = vacinaService.adicionarVacina(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVacina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vacina: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVacinaPorId(@PathVariable Long id) {
        try {
            VacinaModel vacina = vacinaService.buscarVacinaPorId(id);
            return ResponseEntity.ok(vacina);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("não encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar vacina: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<VacinaModel>> listarTodasVacinas() {
        try {
            List<VacinaModel> vacinas = vacinaService.listarTodasVacinas();
            if (vacinas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(vacinas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVacina(@PathVariable Long id, /*@Valid*/ @RequestBody VacinaRequestDTO requestDTO) {
        try {
            VacinaModel vacinaAtualizada = vacinaService.atualizarVacina(id, requestDTO);
            return ResponseEntity.ok(vacinaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("não encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar vacina: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarVacina(@PathVariable Long id) {
        try {
            vacinaService.deletarVacina(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("não encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            if (e.getMessage() != null && e.getMessage().contains("está sendo utilizada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar vacina: " + e.getMessage());
        }
    }

    // --- NOVO ENDPOINT PARA REGISTRAR APLICAÇÃO DE VACINA ---
    /**
     * Endpoint para registrar a aplicação de uma vacina em um animal.
     * Mapeia requisições POST para /api/vacinas/aplicacoes/registrar
     * Este endpoint usa o VacinaService.efetuarVacinacao().
     *
     * vacinacaoRequestDTO: Os dados da aplicação da vacina.
     * Retorna o AnimalXVacinacaoModel registrado com status 201 (Created).
     */
    @PostMapping("/aplicacoes/registrar") // Sub-caminho para a ação de registrar aplicação
    public ResponseEntity<?> registrarAplicacaoDeVacina(@RequestBody VacinacaoRequestDTO vacinacaoRequestDTO) {
        try {
            // Validações básicas do DTO podem ser feitas aqui ou, idealmente, com anotações de validação no DTO
            if (vacinacaoRequestDTO.getDataAplicacaoAnimal() == null ||
                    vacinacaoRequestDTO.getAnimalCod() == null ||
                    vacinacaoRequestDTO.getVacinaCod() == null ||
                    vacinacaoRequestDTO.getVoluntarioRegistrandoCod() == null ||
                    vacinacaoRequestDTO.getVoluntarioAplicadorCod() == null) {
                return ResponseEntity.badRequest().body("Campos obrigatórios para registro de vacinação não foram fornecidos.");
            }

            // Chama o método efetuarVacinacao do VacinaService
            AnimalXVacinacaoModel aplicacaoRegistrada = vacinaService.efetuarVacinacao(vacinacaoRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(aplicacaoRegistrada);
        } catch (IllegalArgumentException e) { // Erros de validação do serviço
            return ResponseEntity.badRequest().body("Dados inválidos para registro de vacinação: " + e.getMessage());
        } catch (Exception e) { // Outras exceções (ex: "não encontrado" do serviço, erros de DAL)
            if (e.getMessage() != null && (e.getMessage().contains("não encontrado") || e.getMessage().contains("não encontrada"))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            e.printStackTrace(); // Logar a exceção completa no servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar registro de vacinação: " + e.getMessage());
        }
    }
}