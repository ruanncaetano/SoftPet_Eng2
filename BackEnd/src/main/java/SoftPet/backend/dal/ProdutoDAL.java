package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.ProdutoDTO;
import SoftPet.backend.model.ProdutoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAL {

    public ProdutoDTO findByProduto(Long id) {
        ProdutoDTO produtoDTO = null;
        String sql = "SELECT * FROM produtos WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ProdutoModel produto = new ProdutoModel(
                        rs.getLong("p_cod"),
                        rs.getString("p_tipo"),
                        rs.getString("p_unidade_medida"),
                        rs.getDate("p_data_validade").toLocalDate(),
                        rs.getString("p_descricao"),
                        rs.getInt("p_qntd_estoque")
                );
                produtoDTO = new ProdutoDTO(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtoDTO;
    }

    public List<ProdutoDTO> getAllProdutos() {
        List<ProdutoDTO> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs.next()) {
                ProdutoModel produto = new ProdutoModel(
                        rs.getLong("p_cod"),
                        rs.getString("p_tipo"),
                        rs.getString("p_unidade_medida"),
                        rs.getDate("p_data_validade").toLocalDate(),
                        rs.getString("p_descricao"),
                        rs.getInt("p_qntd_estoque")
                );
                produtos.add(new ProdutoDTO(produto));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public ProdutoModel addProduto(ProdutoModel produto) {
        String sql = "INSERT INTO produtos (p_tipo, p_unidade_medida, p_data_validade, p_descricao, p_qntd_estoque) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getTipo());
            stmt.setString(2, produto.getUnidadeMedida());
            stmt.setDate(3, Date.valueOf(produto.getDataValidade()));
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getQuantidadeEstoque());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                    produto.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar produto: " + e.getMessage(), e);
        }

        return produto;
    }

    public boolean updateProduto(ProdutoModel produto) {
        String sql = "UPDATE produtos SET p_tipo = ?, p_unidade_medida = ?, p_data_validade = ?, p_descricao = ?, p_qntd_estoque = ? WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, produto.getTipo());
            stmt.setString(2, produto.getUnidadeMedida());
            stmt.setDate(3, Date.valueOf(produto.getDataValidade()));
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setLong(6, produto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByProduto(Long id) {
        String sql = "DELETE FROM produtos WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProdutoDTO> getProdutosPorTipo(String tipo) {
        List<ProdutoDTO> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE LOWER(p_tipo) = LOWER(?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProdutoModel produto = new ProdutoModel(
                        rs.getLong("p_cod"),
                        rs.getString("p_tipo"),
                        rs.getString("p_unidade_medida"),
                        rs.getDate("p_data_validade").toLocalDate(),
                        rs.getString("p_descricao"),
                        rs.getInt("p_qntd_estoque")
                );
                produtos.add(new ProdutoDTO(produto));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }
}
