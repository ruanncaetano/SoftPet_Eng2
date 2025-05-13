package SoftPet.backend.controll;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dal.UserDAL;
import SoftPet.backend.model.UserModel;
import SoftPet.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SoftPet.backend.service.AuthService;

import java.util.List;
//import seu.pacote.dto.LoginRequest;
//import seu.pacote.dto.RegisterRequest;
//import seu.pacote.dto.UpdateSenhaRequest;

@RestController
@RequestMapping("/auth")
public class AuthControll
{

    private final AuthService authService;

    public AuthControll() {
        this.authService = new AuthService();
    }

    @GetMapping("/teste")
    public ResponseEntity<Object> getAll()
    {
        List <UserModel> listUSer = authService.getAllLogins();
        if(!listUSer.isEmpty())
            return ResponseEntity.ok(listUSer);
        return ResponseEntity.badRequest().body("Erro ao listas os usuarios cadastrado");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel request)
    {
        try
        {
            var result = authService.login(request.getCpf(), request.getSenha());
            if (result.isPresent()) {
                return ResponseEntity.ok(result.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel request) {
        try {
            var user = authService.register(request.getCpf(), request.getSenha());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/senha")
    public ResponseEntity<?> updateSenha(@RequestBody UserModel request) {
        try {
            authService.updateSenha(request.getCpf(), request.getSenha());
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<?> deleteConta(@PathVariable String cpf) {
        try {
            authService.deleteConta(cpf);
            return ResponseEntity.ok("Conta excluída com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
