package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.ContatoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContatoDAL {

    /**
     * Busca um contato pelo seu ID.
     * id: O ID (Long) do contato a ser buscado.
     * Retorna o ContatoModel encontrado ou null se não existir.
     */
    public ContatoModel FindById(Long id) {
        if (id == null) {
            // throw new IllegalArgumentException("ID do contato não pode ser nulo.");
            return null; // Ou lançar exceção
        }
        ContatoModel contato = null;
        String sql = "SELECT con_cod, con_telefone, con_email FROM contato WHERE con_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // CON_COD é INTEGER no banco, converter Long para int
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contato = new ContatoModel(
                            rs.getLong("con_cod"), // Lê como Long para o modelo
                            rs.getString("con_telefone"),
                            rs.getString("con_email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Considerar lançar uma RuntimeException para melhor tratamento de erro
            // throw new RuntimeException("Erro ao buscar contato por ID: " + e.getMessage(), e);
        }
        return contato;
    }

    /**
     * Adiciona um novo contato ao banco de dados.
     * O ID do contato (con_cod) deve ser gerado automaticamente pelo banco.
     * contato: O objeto ContatoModel contendo os dados a serem inseridos (sem o ID).
     * Retorna o objeto ContatoModel com o ID preenchido após a inserção.
     */
    public ContatoModel addContato(ContatoModel contato) {
        if (contato == null) {
            throw new IllegalArgumentException("Objeto ContatoModel não pode ser nulo.");
        }
        // Validações básicas podem ser adicionadas aqui para telefone/email se necessário

        String sql = "INSERT INTO contato (con_telefone, con_email) VALUES (?, ?)";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contato.getTelefone());
            stmt.setString(2, contato.getEmail());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        contato.setId(rs.getLong(1)); // Define o ID gerado no modelo
                    } else {
                        throw new SQLException("Falha ao criar contato, nenhum ID gerado retornado.");
                    }
                }
            } else {
                throw new SQLException("Falha ao criar contato, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar contato: " + e.getMessage(), e);
        }
        return contato;
    }

    /**
     * Atualiza os dados de um contato existente.
     * contato: O objeto ContatoModel com o ID do contato a ser atualizado e os novos dados.
     * Retorna true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean updateContato(ContatoModel contato) {
        if (contato == null || contato.getId() == null) {
            throw new IllegalArgumentException("ID do contato é obrigatório para atualização.");
        }
        String sql = "UPDATE contato SET con_telefone = ?, con_email = ? WHERE con_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, contato.getTelefone());
            stmt.setString(2, contato.getEmail());
            stmt.setInt(3, contato.getId().intValue()); // CON_COD é INTEGER, converter Long para int

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao atualizar contato: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deleta um contato pelo seu ID.
     * id: O ID (Long) do contato a ser deletado.
     * Retorna true se a deleção for bem-sucedida, false caso contrário.
     */
    public Boolean deleteByContato(Long id) { // Retorno alterado para boolean (minúsculo) para consistência
        if (id == null) {
            throw new IllegalArgumentException("ID do contato não pode ser nulo para deleção.");
        }
        // ATENÇÃO: Antes de deletar um contato, verifique se ele não está sendo usado
        // nas tabelas PESSOA ou VOLUNTARIO (coluna CON_COD). Se estiver, a deleção falhará
        // devido à restrição de chave estrangeira.
        String sql = "DELETE FROM contato WHERE con_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // CON_COD é INTEGER, converter Long para int
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao deletar contato: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Lista todos os contatos cadastrados.
     * Retorna uma lista de ContatoModel.
     */
    public List<ContatoModel> getAll() {
        List<ContatoModel> list = new ArrayList<>();
        String sql = "SELECT con_cod, con_telefone, con_email FROM contato ORDER BY con_cod"; // Adicionado ORDER BY
        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) { // Usa o consultar do SingletonDB
            if (rs != null) {
                while (rs.next()) {
                    ContatoModel contato = new ContatoModel(
                            rs.getLong("con_cod"),
                            rs.getString("con_telefone"),
                            rs.getString("con_email")
                    );
                    list.add(contato);
                }
            } else {
                System.err.println("Falha ao consultar contatos: ResultSet nulo retornado por SingletonDB.consultar().");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao listar todos os contatos: " + e.getMessage(), e);
        }
        return list;
    }
}