package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.AnimalXVacinacaoModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types; // Para java.sql.Types.INTEGER e java.sql.Types.DATE

@Repository
public class AnimalXVacinacaoDAL {

    public boolean registrarAplicacaoVacina(AnimalXVacinacaoModel aplicacao) {
        String sql = "INSERT INTO ANIMAL_X_VAC " +
                "(ANIMAIS_AN_COD, VACINAS_VA_COD, VAC_DOSE, " +
                "AXV_DT_APLICACAO, AXV_VOL_COD_APLICADOR, AXV_OBSERVACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {

            // Para ANIMAIS_AN_COD (INTEGER no banco, Long no modelo)
            if (aplicacao.getAnimalCod() != null) {
                stmt.setLong(1, aplicacao.getAnimalCod()); // Convertendo Long para int
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            // Para VACINAS_VA_COD (INTEGER no banco, Long no modelo)
            if (aplicacao.getVacinaCod() != null) {
                stmt.setLong(2, aplicacao.getVacinaCod()); // Convertendo Long para int
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            // Para VAC_DOSE (INTEGER no banco, Integer no modelo - está OK)
            if (aplicacao.getDoseAplicadaNumero() != null) {
                stmt.setInt(3, aplicacao.getDoseAplicadaNumero());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            // Para AXV_DT_APLICACAO (DATE no banco, Date no modelo - está OK)
            if (aplicacao.getDataAplicacaoEfetiva() != null) {
                stmt.setDate(4, new java.sql.Date(aplicacao.getDataAplicacaoEfetiva().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            // Para AXV_VOL_COD_APLICADOR (INTEGER no banco, Long no modelo)
            if (aplicacao.getVoluntarioAplicadorCod() != null) { // Agora esta condição é válida
                stmt.setInt(5, aplicacao.getVoluntarioAplicadorCod().intValue()); // Convertendo Long para int
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setString(6, aplicacao.getObservacao());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) { // Códigos de erro SQL Server para PK/Unique violation
                throw new RuntimeException("Erro ao registrar aplicação da vacina: Combinação única violada (ex: vacina já registrada para este animal). " + e.getMessage(), e);
            }
            throw new RuntimeException("Erro ao registrar aplicação da vacina no banco: " + e.getMessage(), e);
        }
    }
}