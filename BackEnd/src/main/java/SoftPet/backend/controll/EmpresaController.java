package SoftPet.backend.controll;

import SoftPet.backend.dto.EmpresaDTO;
import SoftPet.backend.dto.EmpresaExistsResponseDTO;
import SoftPet.backend.dto.EmpresaProfileResponseDTO;
import SoftPet.backend.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
@CrossOrigin("*")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/listar")
    public ResponseEntity<Object> listar() {
        try {
            List<EmpresaDTO> empresas = empresaService.listarEmpresas();
            if (empresas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(empresas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao listar empresas.");
        }
    }

    @GetMapping("/get/profile")
    public ResponseEntity<EmpresaProfileResponseDTO> getEmpresaProfile() {
        return new ResponseEntity<>(this.empresaService.getFirstEmpresa(), HttpStatus.OK);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            EmpresaDTO empresa = empresaService.buscarPorId(id);
            if (empresa == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao buscar empresa.");
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrar(@RequestBody EmpresaDTO dto) {
        try {
            empresaService.cadastrarEmpresa(dto);
            return ResponseEntity.ok("Empresa cadastrada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao cadastrar empresa.");
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody EmpresaDTO dto) {
        try {
            empresaService.atualizarEmpresa(id, dto);
            return ResponseEntity.ok("Empresa atualizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao atualizar empresa.");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        try {
            empresaService.excluirEmpresa(id);
            return ResponseEntity.ok("Empresa deletada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao deletar empresa.");
        }
    }
    @GetMapping("/existe-param")
    public ResponseEntity<Object> haveParam() {
        try {
            boolean existe = this.empresaService.existeEmpresa();
            return ResponseEntity.ok(new EmpresaExistsResponseDTO(existe));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao verificar existência de empresa parametrizada.");
        }
    }

    @PostMapping("/parametrizar")
    public ResponseEntity<Object> createEmpresa(@RequestBody EmpresaDTO dto) {
        try {
            empresaService.cadastrarEmpresa(dto);
            return ResponseEntity.ok("Empresa parametrizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao parametrizar empresa.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateEmpresa(@RequestBody EmpresaDTO dto) {
        try {
            EmpresaDTO empresaAtualizada = this.empresaService.atualizarEmpresaPorCnpj(dto);
            return ResponseEntity.ok(empresaAtualizada);
        } catch (EmpresaService.EmpresaNotFoundException | EmpresaService.InvalidCnpjException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar empresa.");
        }
    }



    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Object> deleteEmpresa(@PathVariable String cnpj) {
        try {
            boolean removida = this.empresaService.shutdownEmpresa(cnpj);
            if (removida) {
                return ResponseEntity.ok("Empresa encerrada com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Empresa não encontrada ou não pôde ser encerrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao encerrar empresa.");
        }
    }

}
