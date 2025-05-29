package SoftPet.backend.service;

import SoftPet.backend.dal.UserDAL;
import SoftPet.backend.model.UserModel;
import SoftPet.backend.util.cpfValidator;
import org.mindrot.jbcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService
{
    @Autowired
    private UserDAL userDAL;

    private static final int SALT_ROUNDS = 10;
    private static final String JWT_SECRET = "Gh1020!";

    public Optional<LoginResult> login(String cpf, String senha) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        UserModel user = userDAL.findByCPF(cpf);
        if(user != null && BCrypt.checkpw(senha, user.getSenha()))
        {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            String token = JWT.create()
                    .withClaim("cpf", user.getCpf())
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 3600 * 1000)) //1h
                    .sign(algorithm);
            return Optional.of(new LoginResult(user, token));
        }
        return Optional.empty();
    }

    public UserModel register(String cpf, String senha) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        if(userDAL.findByCPF(cpf) != null)
            throw new Exception("Usuário já cadastrado com este CPF.");

        String hashed = BCrypt.hashpw(senha, BCrypt.gensalt(SALT_ROUNDS));
        return userDAL.create(new UserModel(cpf, hashed));
    }

    public void updateSenha(String cpf, String novaSenha) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        if(userDAL.findByCPF(cpf) == null)
            throw new Exception("Não existe esse usuário!");

        String hashed = BCrypt.hashpw(novaSenha, BCrypt.gensalt(SALT_ROUNDS));
        userDAL.updateSenha(cpf, hashed);
    }

    public void deleteConta(String cpf) throws Exception
    {
        if (!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        if (userDAL.findByCPF(cpf) == null)
            throw new Exception("Não existe esse usuário!");

        userDAL.deleteByCPF(cpf);
    }

    public List<UserModel> getAllLogins() throws SQLException, ClassNotFoundException {
        return userDAL.getAll();
    }

    public static class LoginResult
    {
        public final UserModel user;
        public final String token;

        public LoginResult(UserModel user, String token)
        {
            this.user = user;
            this.token = token;
        }
    }
}
