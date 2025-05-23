package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.ProdutoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAL {

    public ProdutoModel buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE p_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ProdutoModel(
                        rs.getInt("p_cod"),
                        rs.getString("p_tipo"),
                        rs.getString("p_unidade_medida"),
                        rs.getDate("p_data_validade").toLocalDate(),
                        rs.getString("p_descricao"),
                        rs.getInt("p_qntd_estoque")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProdutoModel> getAll() {
        List<ProdutoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new ProdutoModel(
                        rs.getInt("p_cod"),
                        rs.getString("p_tipo"),
                        rs.getString("p_unidade_medida"),
                        rs.getDate("p_data_validade").toLocalDate(),
                        rs.getString("p_descricao"),
                        rs.getInt("p_qntd_estoque")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int criar(ProdutoModel produto) {
        String sql = "INSERT INTO produtos (p_tipo, p_unidade_medida, p_data_validade, p_descricao, p_qntd_estoque) VALUES (?, ?, ?, ?, ?)";
        int idGerado = -1;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getTipo());
            stmt.setString(2, produto.getUnidadeMedida());
            stmt.setDate(3, Date.valueOf(produto.getDataValidade()));
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getQuantidadeEstoque());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGerado;
    }

    public boolean update(ProdutoModel produto) {
        String sql = "UPDATE produtos SET p_tipo = ?, p_unidade_medida = ?, p_data_validade = ?, p_descricao = ?, p_qntd_estoque = ? WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, produto.getTipo());
            stmt.setString(2, produto.getUnidadeMedida());
            stmt.setDate(3, Date.valueOf(produto.getDataValidade()));
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM produtos WHERE p_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
