package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.ProdutoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate; // Usado no seu construtor de ProdutoModel
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAL {

    public ProdutoModel buscarPorId(int id) {
        String sql = "SELECT p_cod, p_tipo, p_unidade_medida, p_data_validade, p_descricao, p_qntd_estoque FROM produtos WHERE p_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date sqlDate = rs.getDate("p_data_validade");
                    LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    return new ProdutoModel(
                            rs.getInt("p_cod"),
                            rs.getString("p_tipo"),
                            rs.getString("p_unidade_medida"),
                            localDate,
                            rs.getString("p_descricao"),
                            rs.getInt("p_qntd_estoque")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Considerar lançar RuntimeException para melhor tratamento de erro na camada de serviço
            // throw new RuntimeException("Erro ao buscar produto por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public List<ProdutoModel> getAll() {
        List<ProdutoModel> lista = new ArrayList<>();
        String sql = "SELECT p_cod, p_tipo, p_unidade_medida, p_data_validade, p_descricao, p_qntd_estoque FROM produtos ORDER BY p_nome"; // Adicionado ORDER BY p_nome (se existir p_nome) ou p_cod

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) { // Usa o consultar do SingletonDB
            if (rs != null) {
                while (rs.next()) {
                    Date sqlDate = rs.getDate("p_data_validade");
                    LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    lista.add(new ProdutoModel(
                            rs.getInt("p_cod"),
                            rs.getString("p_tipo"),
                            rs.getString("p_unidade_medida"),
                            localDate,
                            rs.getString("p_descricao"),
                            rs.getInt("p_qntd_estoque")
                    ));
                }
            } else {
                System.err.println("Falha ao consultar produtos: ResultSet nulo retornado por SingletonDB.consultar().");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao listar todos os produtos: " + e.getMessage(), e);
        }
        return lista;
    }

    public int criar(ProdutoModel produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo para criação.");
        }
        String sql = "INSERT INTO produtos (p_tipo, p_unidade_medida, p_data_validade, p_descricao, p_qntd_estoque) VALUES (?, ?, ?, ?, ?)";
        int idGerado = -1;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getTipo());
            stmt.setString(2, produto.getUnidadeMedida());
            if (produto.getDataValidade() != null) {
                stmt.setDate(3, Date.valueOf(produto.getDataValidade()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getQuantidadeEstoque());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                        produto.setId(idGerado); // Define o ID no objeto também
                    } else {
                        throw new SQLException("Falha ao criar produto, nenhum ID gerado retornado.");
                    }
                }
            } else {
                throw new SQLException("Falha ao criar produto, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar produto: " + e.getMessage(), e);
        }
        return idGerado;
    }

    public boolean update(ProdutoModel produto) {
        if (produto == null || produto.getId() == 0) { // ID 0 pode ser inválido se for auto-incremento > 0
            throw new IllegalArgumentException("Produto ou ID do produto inválido para atualização.");
        }
        String sql = "UPDATE produtos SET p_tipo = ?, p_unidade_medida = ?, p_data_validade = ?, p_descricao = ?, p_qntd_estoque = ? WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, produto.getTipo());
            stmt.setString(2, produto.getUnidadeMedida());
            if (produto.getDataValidade() != null) {
                stmt.setDate(3, Date.valueOf(produto.getDataValidade()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean delete(int id) {
        if (id <= 0) { // IDs geralmente são positivos
            throw new IllegalArgumentException("ID do produto inválido para deleção.");
        }
        // Adicionar verificação de dependências se este produto for usado em outras tabelas (ex: DOACAO_X_PRODUTOS)
        String sql = "DELETE FROM produtos WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
            return false;
        }
    }

    // --- NOVO MÉTODO PARA ATUALIZAR APENAS O ESTOQUE ---
    /**
     * Atualiza a quantidade em estoque de um produto específico.
     * produtoId: O ID do produto a ser atualizado.
     * quantidadeParaAlterar: A quantidade a ser adicionada (positiva) ou removida (negativa) do estoque.
     * Retorna o ProdutoModel atualizado se a operação for bem-sucedida, caso contrário, lança uma exceção.
     * Lança IllegalArgumentException se o produto não for encontrado ou se a operação resultar em estoque negativo.
     */
    public ProdutoModel atualizarApenasEstoque(int produtoId, int quantidadeParaAlterar) {
        ProdutoModel produto = buscarPorId(produtoId);

        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + produtoId + " não encontrado.");
        }

        int estoqueAtual = produto.getQuantidadeEstoque();
        int novoEstoque = estoqueAtual + quantidadeParaAlterar;

        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Operação inválida: O estoque do produto '" + produto.getDescricao() + "' não pode ficar negativo. Estoque atual: " + estoqueAtual + ", alteração solicitada: " + quantidadeParaAlterar);
        }

        String sql = "UPDATE produtos SET p_qntd_estoque = ? WHERE p_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, produtoId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                produto.setQuantidadeEstoque(novoEstoque); // Atualiza o objeto modelo
                return produto; // Retorna o produto com o estoque atualizado
            } else {
                // Isso não deveria acontecer se buscarPorId encontrou o produto,
                // mas é uma verificação de segurança.
                throw new SQLException("Falha ao atualizar estoque do produto ID " + produtoId + ", nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar estoque do produto ID " + produtoId + ": " + e.getMessage(), e);
        }
    }
}
