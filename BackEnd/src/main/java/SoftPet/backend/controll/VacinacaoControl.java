package SoftPet.backend.controll;

import SoftPet.backend.dto.VacinacaoRequestDTO;
import SoftPet.backend.model.AnimalXVacinacaoModel;
import SoftPet.backend.service.VacinacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vacinacoes") // Endpoint para o recurso de vacinação (aplicações)
public class VacinacaoControl {

    @Autowired
    private VacinacaoService vacinacaoService;

    @PostMapping("/registrarAplicacao")
    public ResponseEntity<?> registrarAplicacaoVacina(@RequestBody VacinacaoRequestDTO vacinacaoRequestDTO) {
        try {
            // Validar o DTO de entrada (ex: campos obrigatórios) - pode ser feito com anotações no DTO
            if (vacinacaoRequestDTO.getDataAplicacaoAnimal() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de aplicação é obrigatória.");
            }
            // Outras validações básicas podem ser adicionadas aqui

            AnimalXVacinacaoModel aplicacaoRegistrada = vacinacaoService.efetuarVacinacao(vacinacaoRequestDTO);
            // Retorna o registro da aplicação, que contém as chaves estrangeiras e os dados da aplicação.
            return ResponseEntity.status(HttpStatus.CREATED).body(aplicacaoRegistrada);
        } catch (RuntimeException e) { // Captura específica para RuntimeException (ex: falha de PK)
            if (e.getMessage() != null && e.getMessage().contains("Esta vacina já foi registrada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            e.printStackTrace(); // Logar a exceção completa no servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao processar registro da vacinação: " + e.getMessage());
        }
        catch (Exception e) {
            // Tratar exceções de "não encontrado" ou regras de negócio específicas
            String mensagemErro = e.getMessage() != null ? e.getMessage() : "Erro desconhecido.";
            if (mensagemErro.contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
            }
            // Outras exceções de negócio podem ter HttpStatus.BAD_REQUEST
            e.printStackTrace(); // Logar a exceção completa no servidor
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao registrar aplicação da vacina: " + mensagemErro);
        }
    }
}