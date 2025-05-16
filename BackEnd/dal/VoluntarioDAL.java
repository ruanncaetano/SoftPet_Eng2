package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.VoluntarioModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VoluntarioDAL {

    public VoluntarioModel create(VoluntarioModel voluntario) {
        String sql = "INSERT INTO voluntario (vol_cpf, vol_nome, car_cod, con_cod, cre_cod) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, voluntario.getCpf());
            stmt.setString(2, voluntario.getNome());
            stmt.setInt(3, voluntario.getCargoCod());
            stmt.setInt(4, voluntario.getContatoCod());
            stmt.setInt(5, voluntario.getCredenciaisCod());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    voluntario.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voluntario;
    }

    public VoluntarioModel findById(int id) {
        VoluntarioModel voluntario = null;
        String sql = "SELECT * FROM voluntario WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                voluntario = new VoluntarioModel(
                        rs.getInt("vol_cod"),
                        rs.getString("vol_cpf"),
                        rs.getString("vol_nome"),
                        rs.getInt("car_cod"),
                        rs.getInt("con_cod"),
                        rs.getInt("cre_cod")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voluntario;
    }

    public List<VoluntarioModel> getAll() {
        List<VoluntarioModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM voluntario";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);

        try {
            while (rs.next()) {
                VoluntarioModel voluntario = new VoluntarioModel(
                        rs.getInt("vol_cod"),
                        rs.getString("vol_cpf"),
                        rs.getString("vol_nome"),
                        rs.getInt("car_cod"),
                        rs.getInt("con_cod"),
                        rs.getInt("cre_cod")
                );
                lista.add(voluntario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
