package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
// import java.sql.PreparedStatement; // Duplicado, já coberto por java.sql.*
// import java.sql.ResultSet; // Duplicado
// import java.sql.SQLException; // Duplicado
// import java.sql.Statement; // Duplicado
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class AdocaoDAL {
    public AdocaoModel NovaAdocao(AdocaoModel adocao) {

        String sql = "INSERT INTO adocao(ado_dt, ado_contrato, pe_cod, an_cod) VALUES (?, ?, ?, ?)";
        String sqlAtualizarAnimal = "UPDATE animais SET an_disp_adocao = false, an_ativo = false WHERE an_cod = ?"; // Atualiza também an_disp_adocao
        
        Connection conn = null; // Para controlo de transação manual
        try {
            conn = SingletonDB.getConexao().getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // Inserir na tabela adocao
            try (PreparedStatement stmtAdocao = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (adocao.getAdo_dt() != null) {
                    stmtAdocao.setDate(1, Date.valueOf(adocao.getAdo_dt()));
                } else {
                    stmtAdocao.setNull(1, Types.DATE); // Trata data nula se permitido pelo AdocaoModel
                }
                stmtAdocao.setBytes(2, adocao.getAdo_contrato()); 
                
                // Assumindo que pe_cod e an_cod são INTEGER no banco e Long no modelo
                if (adocao.getPe_cod() != null) {
                    stmtAdocao.setInt(3, adocao.getPe_cod().intValue());
                } else {
                     throw new SQLException("PE_COD (ID da Pessoa) não pode ser nulo para adoção.");
                }
                if (adocao.getAn_cod() != null) {
                    stmtAdocao.setInt(4, adocao.getAn_cod().intValue());
                } else {
                    throw new SQLException("AN_COD (ID do Animal) não pode ser nulo para adoção.");
                }


                int linhasAfetadas = stmtAdocao.executeUpdate();
                if (linhasAfetadas > 0) {
                    try (ResultSet rs = stmtAdocao.getGeneratedKeys()) {
                        if (rs.next()) {
                            adocao.setAdo_cod(rs.getLong(1)); // ADO_COD é INTEGER, mas setAdo_cod espera Long
                        } else {
                            throw new SQLException("Falha ao registar adoção, nenhum ID gerado retornado.");
                        }
                    }
                } else {
                    throw new SQLException("Falha ao registar adoção, nenhuma linha afetada.");
                }
            }

            // Atualizar status do animal
            try (PreparedStatement stmtAtualizaAnimal = conn.prepareStatement(sqlAtualizarAnimal)) {
                if (adocao.getAn_cod() != null) {
                    stmtAtualizaAnimal.setInt(1, adocao.getAn_cod().intValue());
                } else {
                     // Esta situação não deveria ocorrer se a inserção acima foi bem-sucedida
                    throw new SQLException("AN_COD do animal é nulo para atualização de status.");
                }
                stmtAtualizaAnimal.executeUpdate();
            }
            
            conn.commit(); // Comita a transação se tudo correu bem
            return adocao;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Logar erro no rollback
                }
            }
            throw new RuntimeException("Erro ao registar adoção: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura auto-commit
                    // Não fechar a conexão aqui, pois é a do SingletonDB
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<AdocaoDTO> buscarAdocoes(String cpf, LocalDate dataInicio, LocalDate dataFim) {
        List<AdocaoDTO> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT a.ado_cod, a.ado_dt, a.ado_contrato, a.pe_cod AS adocao_pe_cod, " + // Alias para evitar ambiguidade
                "a.an_cod AS adocao_an_cod, an.an_cod, an.an_nome, an.an_idade, an.an_tipo, an.an_sexo, an.an_porte, an.an_raca, an.an_pelagem, an.an_peso, an.an_baia, an.an_dt_resgate, an.an_disp_adocao, an.an_castrado, an.an_obs, an.an_ativo, " +
                "p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg " +
                "FROM adocao a " +
                "INNER JOIN animais an ON a.an_cod = an.an_cod " +
                "INNER JOIN pessoa p ON a.pe_cod = p.pe_cod " +
                "WHERE 1=1 ");

        List<Object> parametros = new ArrayList<>();

        if (cpf != null && !cpf.isEmpty()) {
            sql.append("AND p.pe_cpf = ? ");
            parametros.add(cpf);
        }
        if (dataInicio != null) {
            sql.append("AND a.ado_dt >= ? ");
            parametros.add(Date.valueOf(dataInicio));
        }
        if (dataFim != null) {
            sql.append("AND a.ado_dt <= ? ");
            parametros.add(Date.valueOf(dataFim));
        }
        sql.append("ORDER BY a.ado_dt DESC"); // Ordenar por data de adoção, por exemplo

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql.toString())) {
            int index = 1;
            for (Object param : parametros) {
                stmt.setObject(index++, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AdocaoModel adocao = new AdocaoModel(
                            rs.getLong("ado_cod"),
                            rs.getDate("ado_dt") != null ? rs.getDate("ado_dt").toLocalDate() : null,
                            rs.getBytes("ado_contrato"),
                            rs.getLong("adocao_pe_cod"), // Usando alias
                            rs.getLong("adocao_an_cod")  // Usando alias e getLong conforme alteração de Pedro
                    );

                    AnimalModel animal = new AnimalModel();
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    animal.setDt_resgate(rs.getDate("an_dt_resgate"));
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                    // animal.setFoto(rs.getBytes("an_foto")); // Se a coluna an_foto estiver no SELECT

                    PessoaModel pessoa = new PessoaModel();
                    pessoa.setId(rs.getLong("pe_cod"));
                    pessoa.setCpf(rs.getString("pe_cpf"));
                    pessoa.setNome(rs.getString("pe_nome"));
                    pessoa.setStatus(rs.getBoolean("pe_status"));
                    pessoa.setProfissao(rs.getString("pe_profissao"));
                    pessoa.setId_contato(rs.getLong("con_cod"));
                    pessoa.setId_endereco(rs.getLong("en_id"));
                    pessoa.setRg(rs.getString("pe_rg"));
                    // Popular ContatoModel e EnderecoModel se necessário para o DTO

                    lista.add(new AdocaoDTO(adocao.getAdo_cod(), adocao.getAdo_dt(), adocao.getAdo_contrato(), animal, pessoa));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar adoções: " + e.getMessage(), e);
        }
        return lista;
    }

    public AdocaoDTO buscarAdocaoPorId(Long idAdocao) {
        String sql = "SELECT a.ado_cod, a.ado_dt, a.ado_contrato, a.pe_cod AS adocao_pe_cod, " +
                "a.an_cod AS adocao_an_cod, an.an_cod, an.an_nome, an.an_idade, an.an_tipo, an.an_sexo, an.an_porte, " +
                "an.an_raca, an.an_pelagem, an.an_peso, an.an_baia, an.an_dt_resgate, an.an_disp_adocao, " +
                "an.an_castrado, an.an_obs, an.an_ativo, " +
                "p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg " +
                "FROM adocao a " +
                "INNER JOIN animais an ON a.an_cod = an.an_cod " +
                "INNER JOIN pessoa p ON a.pe_cod = p.pe_cod " +
                "WHERE a.ado_cod = ?"; 

        AdocaoDTO adocaoEncontrada = null;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, idAdocao.intValue()); // ADO_COD é INTEGER no banco
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AdocaoModel adocao = new AdocaoModel(
                            rs.getLong("ado_cod"),
                            rs.getDate("ado_dt") != null ? rs.getDate("ado_dt").toLocalDate() : null,
                            rs.getBytes("ado_contrato"),
                            rs.getLong("adocao_pe_cod"), // Usando alias
                            rs.getLong("adocao_an_cod")  // Mantendo a alteração de Pedro (getLong)
                    );

                    AnimalModel animal = new AnimalModel();
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    animal.setDt_resgate(rs.getDate("an_dt_resgate"));
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                    // animal.setFoto(rs.getBytes("an_foto")); // Se a coluna an_foto estiver no SELECT

                    PessoaModel adotante = new PessoaModel();
                    adotante.setId(rs.getLong("pe_cod"));
                    adotante.setCpf(rs.getString("pe_cpf"));
                    adotante.setNome(rs.getString("pe_nome"));
                    adotante.setStatus(rs.getBoolean("pe_status"));
                    adotante.setProfissao(rs.getString("pe_profissao"));
                    adotante.setId_contato(rs.getLong("con_cod"));
                    adotante.setId_endereco(rs.getLong("en_id"));
                    adotante.setRg(rs.getString("pe_rg"));
                    // Popular ContatoModel e EnderecoModel se necessário para o DTO

                    adocaoEncontrada = new AdocaoDTO(
                            adocao.getAdo_cod(),
                            adocao.getAdo_dt(),
                            adocao.getAdo_contrato(),
                            animal,
                            adotante
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar adoção por ID: " + e.getMessage(), e);
        }
        return adocaoEncontrada; 
    }

    public boolean atualizarContratoAdocao(Long ado_cod, byte[] novoContrato) {
        if (ado_cod == null) {
            throw new IllegalArgumentException("ID da adoção não pode ser nulo para atualizar contrato.");
        }
        String sql = "UPDATE adocao SET ado_contrato = ? WHERE ado_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setBytes(1, novoContrato);
            stmt.setInt(2, ado_cod.intValue()); // ADO_COD é INTEGER no banco

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; 

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar contrato de adoção: " + e.getMessage(), e);
        }
    }

    public byte[] buscarContratoPorIdAdocao(Long idAdocao) {
        if (idAdocao == null) {
             throw new IllegalArgumentException("ID da adoção não pode ser nulo para buscar contrato.");
        }
        String sql = "SELECT ado_contrato FROM adocao WHERE ado_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, idAdocao.intValue()); // ADO_COD é INTEGER no banco
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("ado_contrato");
                }
            }
            return null; 
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar contrato de adoção: " + e.getMessage(), e);
        }
    }
}
