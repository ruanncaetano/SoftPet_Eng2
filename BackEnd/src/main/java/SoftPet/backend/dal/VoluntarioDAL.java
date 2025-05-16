package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CargoModel;
import SoftPet.backend.model.VoluntarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VoluntarioDAL {

    @Autowired
    private CargoDAL cargoDAL;

    @Autowired
    private CredenciaisDAL credenciaisDAL;

    @Autowired
    private ContatoDAL contatoDAL;

    public VoluntarioModel create(VoluntarioModel voluntario) {
        // Criar credenciais e contato
        int credId = credenciaisDAL.criar(voluntario.getCredenciais());
        int contId = contatoDAL.criar(voluntario.getContato());

        voluntario.setCredenciaisCod(credId);
        voluntario.setContatoCod(contId);

        // Buscar ou criar o cargo, e pegar o id
        int cargoId = cargoDAL.buscarOuCriar(new CargoModel(voluntario.getCargoCod(), voluntario.getCargoNome()));
        voluntario.setCargoCod(cargoId);

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

                voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                voluntario.setContato(contatoDAL.findById(voluntario.getContatoCod()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voluntario;
    }

    public VoluntarioModel findByCPF(String cpf) {
        VoluntarioModel voluntario = null;
        String sql = "SELECT * FROM voluntario WHERE vol_cpf = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cpf);
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

                voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                voluntario.setContato(contatoDAL.findById(voluntario.getContatoCod()));
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

                voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                voluntario.setContato(contatoDAL.findById(voluntario.getContatoCod()));

                lista.add(voluntario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean update(VoluntarioModel voluntario) {
        boolean credAtualizado = credenciaisDAL.atualizar(voluntario.getCredenciais());
        boolean contAtualizado = contatoDAL.atualizar(voluntario.getContato());
        if (!credAtualizado || !contAtualizado) return false;

        String sql = "UPDATE voluntario SET vol_cpf = ?, vol_nome = ?, car_cod = ?, con_cod = ?, cre_cod = ? WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, voluntario.getCpf());
            stmt.setString(2, voluntario.getNome());
            stmt.setInt(3, voluntario.getCargoCod());
            stmt.setInt(4, voluntario.getContatoCod());
            stmt.setInt(5, voluntario.getCredenciaisCod());
            stmt.setInt(6, voluntario.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByCPF(String cpf) {
        VoluntarioModel voluntario = findByCPF(cpf);
        if (voluntario == null) return false;

        credenciaisDAL.deletar(voluntario.getCredenciaisCod());
        contatoDAL.deletar(voluntario.getContatoCod());

        String sql = "DELETE FROM voluntario WHERE vol_cpf = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cpf);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
