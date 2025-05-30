package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CredenciaisModel;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class CredenciaisDAL {

    /**
     * Busca credenciais pelo login.
     * login: O login a ser buscado.
     * Retorna CredenciaisModel se encontrado, caso contrário null.
     */
    public CredenciaisModel buscarPorLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            // throw new IllegalArgumentException("Login não pode ser nulo ou vazio para busca.");
            return null;
        }
        String sql = "SELECT cre_cod, cre_login, cre_senha FROM credenciais WHERE cre_login = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CredenciaisModel(
                            rs.getLong("cre_cod"), // Lê como Long
                            rs.getString("cre_login"),
                            rs.getString("cre_senha")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Considerar lançar RuntimeException
            // throw new RuntimeException("Erro ao buscar credenciais por login: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Cria novas credenciais no banco de dados.
     * O ID (cre_cod) deve ser gerado automaticamente pelo banco.
     * credenciais: O objeto CredenciaisModel com login e senha. O ID será preenchido.
     * Retorna o objeto CredenciaisModel com o ID preenchido.
     */
    public CredenciaisModel criar(CredenciaisModel credenciais) {
        if (credenciais == null || credenciais.getLogin() == null || credenciais.getLogin().trim().isEmpty() ||
                credenciais.getSenha() == null || credenciais.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Login e senha são obrigatórios para criar credenciais.");
        }

        // Opcional: Verificar se o login já existe ANTES de tentar inserir,
        // embora buscarOuCriar seja a abordagem mais completa para isso.
        // Se for chamado diretamente, e o login tiver uma constraint UNIQUE no banco,
        // uma SQLException será lançada na duplicidade.

        String sql = "INSERT INTO credenciais (cre_login, cre_senha) VALUES (?, ?)";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, credenciais.getLogin());
            stmt.setString(2, credenciais.getSenha()); // ATENÇÃO: Senhas devem ser armazenadas como HASH, não em texto plano!

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        credenciais.setId(rs.getLong(1)); // Define o ID gerado no modelo
                        return credenciais; // Retorna o objeto com ID
                    } else {
                        throw new SQLException("Falha ao criar credenciais, nenhum ID gerado retornado.");
                    }
                }
            } else {
                throw new SQLException("Falha ao criar credenciais, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            // Se o erro for de violação de constraint UNIQUE no cre_login,
            // significa que o login já existe.
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique constraint") && e.getMessage().toLowerCase().contains("cre_login")) {
                throw new RuntimeException("Erro ao criar credenciais: O login '" + credenciais.getLogin() + "' já existe.", e);
            }
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar credenciais: " + e.getMessage(), e);
        }
    }

    /**
     * Busca credenciais pelo login. Se não existir, cria novas credenciais.
     * credenciais: Objeto CredenciaisModel com login e senha.
     * Retorna o ID (Long) das credenciais existentes ou recém-criadas.
     */
    public Long buscarOuCriar(CredenciaisModel credenciais) {
        if (credenciais == null || credenciais.getLogin() == null || credenciais.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório para buscar ou criar credenciais.");
        }
        if (credenciais.getSenha() == null || credenciais.getSenha().isEmpty() ) {
            throw new IllegalArgumentException("Senha é obrigatória ao criar novas credenciais via buscarOuCriar.");
        }

        CredenciaisModel existente = buscarPorLogin(credenciais.getLogin());
        if (existente != null && existente.getId() != null) {
            // Credencial com este login já existe.
            // ATENÇÃO: Aqui você precisa decidir o que fazer se a senha fornecida
            // for diferente da senha existente para o mesmo login.
            // Por segurança, geralmente não se atualiza a senha aqui silenciosamente.
            // Poderia lançar um erro, ou apenas retornar o ID existente ignorando a nova senha.
            // Se a política é que um login só pode ter uma senha, e ela não deve ser alterada
            // por este método, apenas retornar o ID é uma opção.
            // Se a senha fornecida DEVE corresponder, adicione uma verificação aqui.
            // Ex: if (!passwordEncoder.matches(credenciais.getSenha(), existente.getSenha())) { throw new SecurityException("Senha incorreta para login existente."); }
            return (long) existente.getId();
        }

        // Se não existe, cria uma nova
        CredenciaisModel novaCredencial = criar(credenciais); // O método criar já define o ID no objeto
        return (long) novaCredencial.getId();
    }


    /**
     * Atualiza o login e/ou senha de uma credencial existente, identificado pelo ID.
     * credenciaisNovas: Objeto CredenciaisModel contendo os novos dados (login, senha).
     * idParaAtualizar: O ID (cre_cod) da credencial a ser atualizada.
     * Retorna true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean atualizar(CredenciaisModel credenciaisNovas, Long idParaAtualizar) {
        if (credenciaisNovas == null || idParaAtualizar == null) {
            throw new IllegalArgumentException("Dados de credenciais e ID são obrigatórios para atualização.");
        }
        if (credenciaisNovas.getLogin() == null || credenciaisNovas.getLogin().trim().isEmpty() ||
                credenciaisNovas.getSenha() == null || credenciaisNovas.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Login e senha são obrigatórios para atualização de credenciais.");
        }

        String sql = "UPDATE credenciais SET cre_login = ?, cre_senha = ? WHERE cre_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, credenciaisNovas.getLogin());
            stmt.setString(2, credenciaisNovas.getSenha()); // ATENÇÃO: Senha deve ser HASHED
            stmt.setInt(3, idParaAtualizar.intValue()); // CRE_COD é INTEGER no banco

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao atualizar credenciais: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deleta credenciais pelo seu ID.
     * id: O ID (Long) das credenciais a serem deletadas.
     * Retorna true se a deleção for bem-sucedida, false caso contrário.
     */
    public boolean deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID das credenciais não pode ser nulo para deleção.");
        }
        // ATENÇÃO: Antes de deletar credenciais, verifique se não estão sendo usadas
        // na tabela VOLUNTARIO (coluna CRE_COD). Se estiverem, a deleção falhará
        // devido à restrição de chave estrangeira.
        String sql = "DELETE FROM credenciais WHERE cre_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // CRE_COD é INTEGER no banco
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao deletar credenciais: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Busca credenciais pelo seu ID.
     * id: O ID (Long) das credenciais.
     * Retorna CredenciaisModel se encontrado, caso contrário null.
     */
    public CredenciaisModel findById(Long id) {
        if (id == null) {
            // throw new IllegalArgumentException("ID das credenciais não pode ser nulo para busca.");
            return null;
        }
        CredenciaisModel credenciais = null;
        String sql = "SELECT cre_cod, cre_login, cre_senha FROM credenciais WHERE cre_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // CRE_COD é INTEGER no banco
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    credenciais = new CredenciaisModel(
                            rs.getLong("cre_cod"), // Lê como Long
                            rs.getString("cre_login"),
                            rs.getString("cre_senha")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao buscar credenciais por ID: " + e.getMessage(), e);
        }
        return credenciais;
    }
}