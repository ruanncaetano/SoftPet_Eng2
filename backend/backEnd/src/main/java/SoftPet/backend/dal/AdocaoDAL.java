package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.AdocaoModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
@Repository
public class AdocaoDAL {
    public AdocaoModel NovaAdocao(AdocaoModel adocao) {

        if (adocao == null || adocao.getAdo_dt() == null || adocao.getPe_cod() == null || adocao.getAn_cod() == 0) {
            throw new IllegalArgumentException("Dados de adoção inválidos ou incompletos");
        }
        String sql = "INSERT INTO adocao("+
                "ado_dt, ado_contrato, pe_cod, an_cod)"+
        "VALUES (?, ?, ?, ?);";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setDate(1, new java.sql.Date(adocao.getAdo_dt().getTime()));
            stmt.setBytes(2,adocao.getContrato());
            stmt.setLong(3,adocao.getPe_cod());
            stmt.setInt(4,adocao.getAn_cod());

            int linhasMod = stmt.executeUpdate();
            if(linhasMod > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next())
                    adocao.setAdo_cod(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar adoção " + e.getMessage(), e);
        }
        return adocao;
    }
}
