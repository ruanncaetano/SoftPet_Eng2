package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class AdocaoDAL {
    public AdocaoModel NovaAdocao(AdocaoModel adocao) {

        String sql = "INSERT INTO adocao(ado_dt, ado_contrato, pe_cod, an_cod) VALUES (?, ?, ?, ?)";
        String sqlAtualizarAnimal = "UPDATE animais SET an_ativo = false WHERE an_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtAtualizaAnimal = SingletonDB.getConexao().getPreparedStatement(sqlAtualizarAnimal)) {
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
            // Atualizar status do animal
            stmtAtualizaAnimal.setLong(1, adocao.getAn_cod());
            stmtAtualizaAnimal.executeUpdate();
            return adocao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar adoção: " + e.getMessage(), e);
        }
    }

    public List<AdocaoDTO> buscarAdocoes(String cpf, LocalDate dataInicio, LocalDate dataFim) {
        List<AdocaoDTO> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT a.ado_cod, a.ado_dt, a.ado_contrato, a.pe_cod, " +
                        "a.an_cod, an.an_cod, an.an_nome, an.an_idade, an.an_tipo, an.an_sexo, an.an_porte, an.an_raca, an.an_pelagem, an.an_peso, an.an_baia, an.an_dt_resgate, an.an_disp_adocao, an.an_castrado, an.an_obs, an.an_ativo, " +
                        "p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg " +
                        "FROM adocao a " +
                        "INNER JOIN animais an ON a.an_cod = an.an_cod " +
                        "INNER JOIN pessoa p ON a.pe_cod = p.pe_cod " +
                        "WHERE 1=1 ");

        // Parâmetros dinâmicos
        if (cpf != null && !cpf.isEmpty()) {
            sql.append("AND p.pe_cpf = ? ");
        }
        if (dataInicio != null) {
            sql.append("AND a.ado_dt >= ? ");
        }
        if (dataFim != null) {
            sql.append("AND a.ado_dt <= ? ");
        }

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql.toString())) {
            int index = 1;

            if (cpf != null && !cpf.isEmpty()) {
                stmt.setString(index++, cpf);
            }
            if (dataInicio != null) {
                stmt.setDate(index++, Date.valueOf(dataInicio));
            }
            if (dataFim != null) {
                stmt.setDate(index++, Date.valueOf(dataFim));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AdocaoModel adocao = new AdocaoModel(
                        rs.getLong("ado_cod"),
                        rs.getDate("ado_dt").toLocalDate(),
                        rs.getBytes("ado_contrato"),
                        rs.getLong("pe_cod"),
                        rs.getInt("an_cod")
                );

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
                        rs.getBoolean("an_ativo")
                );

                PessoaModel pessoa = new PessoaModel(
                        rs.getLong("pe_cod"),
                        rs.getString("pe_cpf"),
                        rs.getString("pe_nome"),
                        rs.getBoolean("pe_status"),
                        rs.getString("pe_profissao"),
                        rs.getLong("con_cod"),
                        rs.getLong("en_id"),
                        rs.getString("pe_rg")
                );

                lista.add(new AdocaoDTO(adocao.getAdo_cod(),adocao.getAdo_dt(), adocao.getContrato(), animal, pessoa));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar adoções: " + e.getMessage(), e);
        }

        return lista;
    }
    public AdocaoDTO buscarAdocaoPorId(Long idAdocao) {
        String sql = "SELECT a.ado_cod, a.ado_dt, a.ado_contrato, a.pe_cod, " +
                "a.an_cod, an.an_cod, an.an_nome, an.an_idade, an.an_tipo, an.an_sexo, an.an_porte, " +
                "an.an_raca, an.an_pelagem, an.an_peso, an.an_baia, an.an_dt_resgate, an.an_disp_adocao, " +
                "an.an_castrado, an.an_obs, an.an_ativo, " +
                "p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg " +
                "FROM adocao a " +
                "INNER JOIN animais an ON a.an_cod = an.an_cod " +
                "INNER JOIN pessoa p ON a.pe_cod = p.pe_cod " +
                "WHERE a.ado_cod = ?"; // Busca pelo ID da adoção

        AdocaoDTO adocaoEncontrada = null;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, idAdocao); // Define o ID da adoção como parâmetro
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Mapeia os dados da adoção
                AdocaoModel adocao = new AdocaoModel(
                        rs.getLong("ado_cod"),
                        rs.getDate("ado_dt").toLocalDate(),
                        rs.getBytes("ado_contrato"),
                        rs.getLong("pe_cod"),
                        rs.getInt("an_cod")
                );

                // Mapeia os dados do animal
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
                        rs.getBoolean("an_ativo")
                );

                // Mapeia os dados do adotante (pessoa)
                PessoaModel adotante = new PessoaModel(
                        rs.getLong("pe_cod"),
                        rs.getString("pe_cpf"),
                        rs.getString("pe_nome"),
                        rs.getBoolean("pe_status"),
                        rs.getString("pe_profissao"),
                        rs.getLong("con_cod"),
                        rs.getLong("en_id"),
                        rs.getString("pe_rg")
                );

                adocaoEncontrada = new AdocaoDTO(
                        adocao.getAdo_cod(),
                        adocao.getAdo_dt(),
                        adocao.getContrato(),
                        animal,
                        adotante
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar adoção por ID: " + e.getMessage(), e);
        }

        return adocaoEncontrada; // Retorna null se não encontrar
    }
    public boolean atualizarContratoAdocao(Long ado_cod, byte[] novoContrato) {
        String sql = "UPDATE adocao SET ado_contrato = ? WHERE ado_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setBytes(1, novoContrato);
            stmt.setLong(2, ado_cod);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se pelo menos 1 linha foi atualizada

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar contrato de adoção: " + e.getMessage(), e);
        }
    }
    public byte[] buscarContratoPorIdAdocao(Long idAdocao) {
        String sql = "SELECT ado_contrato FROM adocao WHERE ado_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, idAdocao);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("ado_contrato");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar contrato de adoção: " + e.getMessage(), e);
        }
    }
}
