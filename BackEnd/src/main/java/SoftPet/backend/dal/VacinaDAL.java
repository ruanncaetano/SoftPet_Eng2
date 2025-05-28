package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.VacinaModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class VacinaDAL {

    public VacinaModel buscarPorCod(int cod) {
        String sql = "SELECT VA_COD, VA_NOME, VA_DESC, VA_DT_APLICACAO, VA_DOSE FROM VACINAS WHERE VA_COD = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, cod);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new VacinaModel(
                        rs.getInt("VA_COD"),
                        rs.getString("VA_NOME"),
                        rs.getString("VA_DESC"),
                        rs.getDate("VA_DT_APLICACAO"),
                        rs.getString("VA_DOSE")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar vacina por código: " + e.getMessage(), e);
        }
        return null;
    }

    // Você pode adicionar um buscarPorNome(String nome) se for mais conveniente para o usuário
}