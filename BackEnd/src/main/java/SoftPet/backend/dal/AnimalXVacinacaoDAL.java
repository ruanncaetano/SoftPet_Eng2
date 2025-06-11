package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.AnimalXVacinacaoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class AnimalXVacinacaoDAL {

    /**
     * Registra uma nova aplicação de vacina no banco de dados.
     * aplicacao: O objeto AnimalXVacinacaoModel contendo os dados da aplicação.
     * Retorna o objeto AnimalXVacinacaoModel com o axvCod (ID da aplicação) preenchido.
     * Lança RuntimeException em caso de erro.
     */
    public AnimalXVacinacaoModel registrarAplicacaoVacina(AnimalXVacinacaoModel aplicacao) {
        // Assumindo a estrutura da tabela ANIMAL_X_VAC com AXV_COD como PK auto-incremental
        String sql = "INSERT INTO ANIMAL_X_VAC " +
                "(ANIMAIS_AN_COD, VACINAS_VA_COD, AXV_VOL_APLICADOR_COD, " +
                "AXV_DATA_APLICACAO, AXV_DOSE_NUMERICA, AXV_OBSERVACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Usando try-with-resources para PreparedStatement
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // ANIMAIS_AN_COD (INTEGER no banco, Long no modelo)
            if (aplicacao.getAnimalCod() != null) {
                stmt.setInt(1, aplicacao.getAnimalCod().intValue());
            } else {
                // ANIMAIS_AN_COD é NOT NULL na definição da tabela que sugeri
                throw new IllegalArgumentException("O código do animal é obrigatório para o registro de vacinação.");
            }

            // VACINAS_VA_COD (INTEGER no banco, Long no modelo)
            if (aplicacao.getVacinaCod() != null) {
                stmt.setInt(2, aplicacao.getVacinaCod().intValue());
            } else {
                // VACINAS_VA_COD é NOT NULL
                throw new IllegalArgumentException("O código da vacina é obrigatório para o registro de vacinação.");
            }

            // AXV_VOL_APLICADOR_COD (INTEGER no banco, Long no modelo)
            if (aplicacao.getVoluntarioAplicadorCod() != null) {
                stmt.setInt(3, aplicacao.getVoluntarioAplicadorCod().intValue());
            } else {
                // AXV_VOL_APLICADOR_COD é NOT NULL
                throw new IllegalArgumentException("O código do voluntário aplicador é obrigatório.");
            }

            // AXV_DATA_APLICACAO (DATE no banco, java.util.Date no modelo)
            if (aplicacao.getDataAplicacao() != null) {
                stmt.setDate(4, new java.sql.Date(aplicacao.getDataAplicacao().getTime()));
            } else {
                // AXV_DATA_APLICACAO é NOT NULL
                throw new IllegalArgumentException("A data de aplicação da vacina é obrigatória.");
            }

            // AXV_DOSE_NUMERICA (NUMERIC ou INTEGER no banco, Double no modelo)
            if (aplicacao.getDoseNumerica() != null) {
                stmt.setDouble(5, aplicacao.getDoseNumerica()); // Ou setInt se a coluna for INTEGER
            } else {
                stmt.setNull(5, Types.NUMERIC); // Ou Types.INTEGER
            }

            // AXV_OBSERVACAO (VARCHAR no banco, String no modelo)
            stmt.setString(6, aplicacao.getObservacao());


            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao registrar aplicação da vacina, nenhuma linha afetada.");
            }

            // Obter o ID (AXV_COD) gerado pelo banco
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    aplicacao.setAxvCod(generatedKeys.getLong(1)); // Assume que AXV_COD é SERIAL/IDENTITY e pode ser lido como Long
//                } else {
//                    throw new SQLException("Falha ao registrar aplicação da vacina, nenhum ID (AXV_COD) obtido.");
//                }
//            }
            return aplicacao;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao registrar aplicação de vacina no banco: " + e.getMessage(), e);
        }
        // ClassNotFoundException não precisa ser capturada aqui se os métodos do SingletonDB
        // não a declararem mais (se o driver já foi carregado uma vez).
    }


    
    // Outros métodos DAL para AnimalXVacinacaoModel podem ser adicionados aqui se necessário
    // Por exemplo: buscarAplicacoesPorAnimal(Long animalCod), buscarAplicacaoPorId(Long axvCod), etc.
    // Mas para a funcionalidade "Efetuar Vacinação", o método de criar é o principal.
}
