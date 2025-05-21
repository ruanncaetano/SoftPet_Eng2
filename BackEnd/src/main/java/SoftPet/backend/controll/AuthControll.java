package SoftPet.backend.controll;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dal.UserDAL;
import SoftPet.backend.model.UserModel;
import SoftPet.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SoftPet.backend.service.AuthService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthControll
{

    @Autowired
    AuthService authService;

    @GetMapping("/teste")
    public ResponseEntity<Object> getAll()
    {
        List <UserModel> listUSer = authService.getAllLogins();
        if(!listUSer.isEmpty())
            return ResponseEntity.ok(listUSer);
        return ResponseEntity.badRequest().body("Erro ao listas os usuarios cadastrado");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel user)
    {
        try
        {
            var result = authService.login(user.getCpf(), user.getSenha());
            if(result.isPresent())
                return ResponseEntity.ok(result.get());
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user)
    {
        try
        {
            UserModel novoUser = authService.register(user.getCpf(), user.getSenha());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUser);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/senha")
    public ResponseEntity<Object> updateSenha(@RequestBody UserModel user)
    {
        try
        {
            authService.updateSenha(user.getCpf(), user.getSenha());
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deleteConta(@PathVariable String cpf)
    {
        try
        {
            authService.deleteConta(cpf);
            return ResponseEntity.ok("Conta excluída com sucesso.");
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
