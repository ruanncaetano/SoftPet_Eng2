package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.VacinaModel;
import org.springframework.stereotype.Repository; // Para que o Spring gerencie como um Bean

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository // Anotação para o Spring identificar esta classe como um componente de persistência
public class VacinaDAL {

    /**
     * Cria um novo registro de vacina no banco de dados.
     * vacina: O objeto VacinaModel contendo os dados a serem inseridos (sem o cod).
     * Retorna o objeto VacinaModel com o cod preenchido após a inserção.
     * Lança RuntimeException em caso de erro.
     */
    public VacinaModel criar(VacinaModel vacina) {
        String sql = "INSERT INTO VACINAS (VA_NOME, VA_DESC, VA_DT_APLICACAO, VA_DOSE) VALUES (?, ?, ?, ?)";
        // Usando try-with-resources para PreparedStatement
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vacina.getNome());
            stmt.setString(2, vacina.getDescricao()); // Descrição pode ser nula

            if (vacina.getDataReferenciaLote() != null) {
                stmt.setDate(3, new java.sql.Date(vacina.getDataReferenciaLote().getTime()));
            } else {
                // A coluna VA_DT_APLICACAO é NOT NULL no seu esquema, então este else não deveria ser atingido
                // se a validação no serviço for feita corretamente.
                // Se, por algum motivo, chegar nulo, o banco rejeitará.
                // Considerar lançar IllegalArgumentException aqui se a data for essencial e nula.
                stmt.setNull(3, Types.DATE);
            }

            stmt.setString(4, vacina.getTipoDosePadrao());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar vacina, nenhuma linha afetada.");
            }

            // Obter o ID (VA_COD) gerado pelo banco
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vacina.setCod(generatedKeys.getLong(1)); // Assume que VA_COD é INTEGER e pode ser lido como Long
                } else {
                    throw new SQLException("Falha ao criar vacina, nenhum ID (VA_COD) obtido.");
                }
            }
            return vacina;
        } catch (SQLException e) {
            // Logar a exceção pode ser útil aqui
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar vacina no banco de dados: " + e.getMessage(), e);
        }
        // ClassNotFoundException não é mais lançada pelos métodos do SingletonDB refatorado
        // se a conexão já foi estabelecida uma vez. Se for a primeira vez e o driver não for encontrado,
        // o construtor do SingletonDB já terá logado o erro.
    }

    /**
     * Busca uma vacina pelo seu código (ID).
     * cod: O código (VA_COD) da vacina a ser buscada.
     * Retorna o VacinaModel encontrado ou null se não existir.
     * Lança RuntimeException em caso de erro.
     */
    public VacinaModel buscarPorCod(Long cod) {
        if (cod == null) {
            // Considerar lançar IllegalArgumentException ou retornar null dependendo da política de erro.
            System.err.println("Tentativa de buscar vacina com código nulo.");
            return null;
        }
        String sql = "SELECT VA_COD, VA_NOME, VA_DESC, VA_DT_APLICACAO, VA_DOSE FROM VACINAS WHERE VA_COD = ?";
        // Usando try-with-resources para PreparedStatement e ResultSet
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {

            stmt.setInt(1, cod.intValue()); // Converte Long para int para a coluna VA_COD (INTEGER)

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new VacinaModel(
                            rs.getLong("VA_COD"),      // Lê como Long
                            rs.getString("VA_NOME"),
                            rs.getString("VA_DESC"),
                            rs.getDate("VA_DT_APLICACAO"), // java.sql.Date, compatível com java.util.Date
                            rs.getString("VA_DOSE")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar vacina por código " + cod + ": " + e.getMessage(), e);
        }
        return null; // Retorna null se não encontrar
    }

    /**
     * Lista todas as vacinas cadastradas, ordenadas pelo nome.
     * Retorna uma lista de VacinaModel.
     * Lança RuntimeException em caso de erro.
     */
    public List<VacinaModel> listarTodas() {
        List<VacinaModel> vacinas = new ArrayList<>();
        String sql = "SELECT VA_COD, VA_NOME, VA_DESC, VA_DT_APLICACAO, VA_DOSE FROM VACINAS ORDER BY VA_NOME";

        // Usando try-with-resources para ResultSet (que também fecha o Statement implícito do consultar)
        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            // O método consultar do SingletonDB agora retorna null em caso de erro interno.
            if (rs != null) {
                while (rs.next()) {
                    vacinas.add(new VacinaModel(
                            rs.getLong("VA_COD"),      // Lê como Long
                            rs.getString("VA_NOME"),
                            rs.getString("VA_DESC"),
                            rs.getDate("VA_DT_APLICACAO"),
                            rs.getString("VA_DOSE")
                    ));
                }
            } else {
                // Um erro ocorreu dentro do SingletonDB.consultar() e já foi logado.
                // Pode-se lançar uma exceção aqui ou retornar a lista vazia.
                System.err.println("Falha ao listar vacinas: ResultSet nulo retornado por SingletonDB.consultar().");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar todas as vacinas: " + e.getMessage(), e);
        }
        return vacinas;
    }

    /**
     * Atualiza os dados de uma vacina existente no banco.
     * vacina: O objeto VacinaModel com os dados atualizados (incluindo o cod).
     * Retorna o objeto VacinaModel atualizado.
     * Lança RuntimeException em caso de erro ou se a vacina não for encontrada.
     */
    public VacinaModel atualizar(VacinaModel vacina) {
        if (vacina.getCod() == null) {
            throw new IllegalArgumentException("O código da vacina é obrigatório para atualização.");
        }
        String sql = "UPDATE VACINAS SET VA_NOME = ?, VA_DESC = ?, VA_DT_APLICACAO = ?, VA_DOSE = ? WHERE VA_COD = ?";
        // Usando try-with-resources para PreparedStatement
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, vacina.getNome());
            stmt.setString(2, vacina.getDescricao());
            if (vacina.getDataReferenciaLote() != null) {
                stmt.setDate(3, new java.sql.Date(vacina.getDataReferenciaLote().getTime()));
            } else {
                stmt.setNull(3, Types.DATE); // VA_DT_APLICACAO é NOT NULL, então isso causaria erro no banco.
                // A validação deve ocorrer na camada de serviço.
            }
            stmt.setString(4, vacina.getTipoDosePadrao());
            stmt.setInt(5, vacina.getCod().intValue()); // Converte Long para int para VA_COD (INTEGER)

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                // Isso pode significar que a vacina com o ID fornecido não existe.
                throw new SQLException("Falha ao atualizar vacina, nenhuma linha afetada (ID: " + vacina.getCod() + " pode não existir).");
            }
            return vacina; // Retorna o objeto com os dados atualizados
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar vacina com ID " + vacina.getCod() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deleta uma vacina do banco de dados pelo seu código.
     * cod: O código (VA_COD) da vacina a ser deletada.
     * Retorna true se a deleção for bem-sucedida, false caso contrário.
     * Lança RuntimeException em caso de erro ou se a vacina estiver em uso.
     */
    public boolean deletar(Long cod) {
        if (cod == null) {
            throw new IllegalArgumentException("O código da vacina é obrigatório para deleção.");
        }

        // Verificação de dependência (opcional, mas recomendada)
        String sqlCheck = "SELECT COUNT(*) FROM ANIMAL_X_VAC WHERE VACINAS_VA_COD = ?";
        String sqlDelete = "DELETE FROM VACINAS WHERE VA_COD = ?";

        Connection conn = null; // Obter conexão do SingletonDB
        boolean sucesso = false;

        try {
            conn = SingletonDB.getConexao().getConnection(); // Obtém a conexão
            conn.setAutoCommit(false); // Inicia controle de transação manual

            // 1. Verificar dependências
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setInt(1, cod.intValue());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        conn.rollback(); // Desfaz a transação
                        throw new RuntimeException("Não é possível deletar a vacina (ID: " + cod + ") pois ela está sendo utilizada em registros de vacinação de animais.");
                    }
                }
            }

            // 2. Deletar a vacina
            try (PreparedStatement deleteStmt = conn.prepareStatement(sqlDelete)) {
                deleteStmt.setInt(1, cod.intValue()); // Converte Long para int para VA_COD (INTEGER)
                int affectedRows = deleteStmt.executeUpdate();
                if (affectedRows > 0) {
                    sucesso = true;
                } else {
                    // Se nenhuma linha foi afetada, a vacina com esse ID não existia.
                    // Pode não ser um erro, dependendo da lógica desejada.
                    System.out.println("Nenhuma vacina encontrada com o ID " + cod + " para deletar.");
                }
            }

            conn.commit(); // Se tudo correu bem, comita a transação

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Tenta reverter em caso de erro SQL
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Loga erro no rollback
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar vacina com ID " + cod + ": " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura auto-commit
                    // NÃO feche conn.close() aqui, pois é a conexão do SingletonDB.
                    // O SingletonDB é responsável por gerenciar o ciclo de vida da sua única conexão.
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return sucesso;
    }
}