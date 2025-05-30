package SoftPet.backend.service;

import SoftPet.backend.dal.CredenciaisDAL;
import SoftPet.backend.model.CredenciaisModel;
// Importe um CredenciaisRequestDTO se você o criar para receber dados do controller
// import SoftPet.backend.dto.CredenciaisRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import javax.persistence.EntityNotFoundException; // ou jakarta.persistence.EntityNotFoundException

@Service
public class CredenciaisService {

    private final CredenciaisDAL credenciaisDAL;

    @Autowired // Injeção de dependência via construtor (recomendado)
    public CredenciaisService(CredenciaisDAL credenciaisDAL) {
        this.credenciaisDAL = credenciaisDAL;
    }

    /**
     * Busca ou cria uma credencial.
     * Esta é a forma preferida de lidar com a criação para evitar logins duplicados.
     * credenciais: O objeto CredenciaisModel com pelo menos login e senha.
     * Retorna o ID (Long) da credencial existente ou recém-criada.
     */
    public Long buscarOuCriarCredenciais(CredenciaisModel credenciais) {
        // Validações básicas
        if (credenciais == null || credenciais.getLogin() == null || credenciais.getLogin().trim().isEmpty() ||
                credenciais.getSenha() == null || credenciais.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Login e senha são obrigatórios para buscar ou criar credenciais.");
        }
        // ATENÇÃO: A senha deve ser "hasheada" aqui antes de ser passada para o DAL,
        // usando um serviço de criptografia (ex: BCryptPasswordEncoder do Spring Security).
        // Exemplo: credenciais.setSenha(passwordEncoder.encode(credenciais.getSenha()));

        return credenciaisDAL.buscarOuCriar(credenciais);
    }

    /**
     * Busca uma credencial pelo seu ID.
     * id: O ID da credencial.
     * Retorna o CredenciaisModel encontrado.
     * Lança uma exceção se a credencial não for encontrada.
     */
    public CredenciaisModel buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para busca não pode ser nulo.");
        }
        CredenciaisModel credenciais = credenciaisDAL.findById(id);
        if (credenciais == null) {
            // Lançar uma exceção mais específica é uma boa prática
            throw new RuntimeException("Credenciais com ID " + id + " não encontradas."); // Ou EntityNotFoundException
        }
        return credenciais;
    }

    /**
     * Atualiza uma credencial existente.
     * id: O ID da credencial a ser atualizada.
     * credenciaisNovas: Objeto CredenciaisModel com os novos dados de login e senha.
     * Retorna true se a atualização for bem-sucedida.
     * Lança uma exceção se a credencial não for encontrada.
     */
    public boolean atualizarCredenciais(Long id, CredenciaisModel credenciaisNovas) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para atualização é obrigatório.");
        }
        // Garante que a credencial existe antes de tentar atualizar
        buscarPorId(id);

        // ATENÇÃO: A senha deve ser "hasheada" aqui também antes de passar para o DAL
        // Exemplo: credenciaisNovas.setSenha(passwordEncoder.encode(credenciaisNovas.getSenha()));

        return credenciaisDAL.atualizar(credenciaisNovas, id);
    }

    /**
     * Deleta uma credencial pelo seu ID.
     * id: O ID da credencial a ser deletada.
     * Retorna true se a deleção for bem-sucedida.
     * Lança uma exceção se a credencial não for encontrada ou se não puder ser deletada.
     */
    public boolean deletarCredenciais(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para deleção é obrigatório.");
        }
        // Garante que a credencial existe antes de tentar deletar
        buscarPorId(id);

        boolean deletado = credenciaisDAL.deletar(id);
        if (!deletado) {
            // O DAL pode retornar false se a deleção falhar por uma restrição de chave estrangeira
            // que não lançou uma SQLException (embora geralmente lance).
            // Lançar uma exceção aqui é mais consistente.
            throw new RuntimeException("Falha ao deletar credenciais com ID " + id + ". Verifique se não está em uso.");
        }
        return true;
    }
}