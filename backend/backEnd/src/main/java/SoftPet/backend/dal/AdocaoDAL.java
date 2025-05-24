package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.dto.AdotanteCompletoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;
import SoftPet.backend.service.AnimalService;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Repository
public class AdocaoDAL {
    public AdocaoModel NovaAdocao(AdocaoModel adocao) {

        String sql = "INSERT INTO adocao(ado_dt, ado_contrato, pe_cod, an_cod) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(adocao.getAdo_dt()));
            stmt.setBytes(2, adocao.getContrato());
            stmt.setLong(3, adocao.getPe_cod());
            stmt.setLong(4, adocao.getAn_cod());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        adocao.setAdo_cod(rs.getLong(1));
                    }
                }
            }
            return adocao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar adoção: " + e.getMessage(), e);
        }
    }

    public AdocaoDTO buscarAdocao(PessoaModel adotante) {
        String sql = "SELECT a.ado_cod, a.ado_dt, a.ado_contrato, a.pe_cod, " +
                "a.an_cod, an.an_cod, an.an_nome, an.an_idade, an.an_tipo, an.an_sexo, an.an_porte, an.an_raca, an.an_pelagem, an.an_peso, an.an_baia, an.an_dt_resgate, an.an_disp_adocao, an.an_castrado, an.an_obs, an.an_ativo, " +
                "p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg " +
                "FROM adocao a INNER JOIN animais an ON a.an_cod = an.an_cod " +
                "INNER JOIN pessoa p ON a.pe_cod = p.pe_cod WHERE a.ado_cod = ?";
        AdocaoDTO procura = null;
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, adotante.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                AdocaoModel adocao = new AdocaoModel(
                        rs.getLong("ado_cod"),
                        rs.getDate("ado_dt").toLocalDate(),
                        rs.getBytes("ado_contrato"),
                        rs.getLong("pe_cod"),
                        rs.getInt("an_cod") );

                AnimalModel animal = new AnimalModel(
                        rs.getInt("an_cod"),
                        rs.getString("an_nome"),
                        rs.getInt("an_idade"),
                        rs.getString("an_tipo"),
                        rs.getString("an_sexo"),
                        rs.getString("an_porte"),
                        rs.getString("an_raca"),
                        rs.getString("an_pelagem"),
                        rs.getInt("an_peso"),
                        rs.getString("an_baia"),
                        rs.getDate("an_dt_resgate"),
                        rs.getBoolean("an_disp_adocao"),
                        rs.getBoolean("an_castrado"),
                        rs.getString("an_obs"),
                        rs.getBoolean("an_ativo"));
                PessoaModel adotanteBusca = new PessoaModel(
                        rs.getLong("pe_cod"),
                        rs.getString("pe_cpf"),
                        rs.getString("pe_nome"),
                        rs.getBoolean("pe_status"),
                        rs.getString("pe_profissao"),
                        rs.getLong("con_cod"),
                        rs.getLong("en_id"),
                        rs.getString("pe_rg"));
                procura = new AdocaoDTO(rs.getDate("ado_dt").toLocalDate(),rs.getBytes("ado_contrato"), animal, adotanteBusca);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return procura;
    }

}
