package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CargoModel;
import SoftPet.backend.model.ContatoModel;
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
        Long credId = credenciaisDAL.criar(voluntario.getCredenciais());
        ContatoModel contId = contatoDAL.addContato(voluntario.getContato());

        voluntario.setCredenciaisCod(credId);
        voluntario.setContatoCod(contId.getId());

        // Buscar ou criar o cargo somente se o ID não estiver presente
        Long cargoId;
        if (voluntario.getCargoCod() > 0) {
            cargoId = voluntario.getCargoCod(); // Já veio com cargo existente
        } else {
            if (voluntario.getCargoNome() == null || voluntario.getCargoNome().isBlank()) {
                throw new IllegalArgumentException("Nome do cargo é obrigatório se o ID não for fornecido.");
            }
            cargoId = cargoDAL.buscarOuCriar(new CargoModel(0L, voluntario.getCargoNome()));
        }
        voluntario.setCargoCod(cargoId);

        String sql = "INSERT INTO voluntario (vol_cpf, vol_nome, car_cod, con_cod, cre_cod) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, voluntario.getCpf());
            stmt.setString(2, voluntario.getNome());
            stmt.setLong(3, voluntario.getCargoCod());
            stmt.setLong(4, voluntario.getContatoCod());
            stmt.setLong(5, voluntario.getCredenciaisCod());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    voluntario.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voluntario;
    }
    public static VoluntarioModel buscarPorId(Long id) {
        String sql = "SELECT * FROM voluntario WHERE vol_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                VoluntarioModel voluntario = new VoluntarioModel();
                voluntario.setId(rs.getLong("vol_cod"));
                voluntario.setNome(rs.getString("vol_nome"));
                voluntario.setCpf(rs.getString("vol_cpf"));
                voluntario.setCargoCod(rs.getLong("car_cod"));
                voluntario.setContatoCod(rs.getLong("con_cod"));
                voluntario.setCredenciaisCod(rs.getLong("cre_cod"));
                return voluntario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public boolean atualizar(VoluntarioModel voluntario) {
        try {
            // Atualizar cargo (reaproveitar se já existir)
            Long cargoId = cargoDAL.buscarOuCriar(new CargoModel(0L, voluntario.getCargoNome()));
            voluntario.setCargoCod(cargoId);

            // Atualizar contato e credenciais
            contatoDAL.updateContato(voluntario.getContato());
            credenciaisDAL.atualizar(voluntario.getCredenciais(), voluntario.getCredenciaisCod());

            // Atualizar voluntário
            String sql = "UPDATE voluntario SET vol_nome = ?, vol_cpf = ?, car_cod = ? WHERE vol_cod = ?";
            try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
                stmt.setString(1, voluntario.getNome());
                stmt.setString(2, voluntario.getCpf());
                stmt.setLong(3, voluntario.getCargoCod());
                stmt.setLong(4, voluntario.getId());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public VoluntarioModel findById(Long id) {
        VoluntarioModel voluntario = null;
        String sql = "SELECT * FROM voluntario WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                voluntario = new VoluntarioModel(
                        rs.getLong("vol_cod"),
                        rs.getString("vol_cpf"),
                        rs.getString("vol_nome"),
                        rs.getLong("car_cod"),
                        rs.getLong("con_cod"),
                        rs.getLong("cre_cod")
                );

                voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                voluntario.setContato(contatoDAL.FindById(voluntario.getContato().getId()));

                // Buscar e setar o nome do cargo
                CargoModel cargo = cargoDAL.buscarPorId(voluntario.getCargoCod());
                if (cargo != null) {
                    voluntario.setCargoNome(cargo.getNome());
                }
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
                        rs.getLong("vol_cod"),
                        rs.getString("vol_cpf"),
                        rs.getString("vol_nome"),
                        rs.getLong("car_cod"),
                        rs.getLong("con_cod"),
                        rs.getLong("cre_cod")
                );

                voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                voluntario.setContato(contatoDAL.FindById(voluntario.getContato().getId()));
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
                        rs.getLong("vol_cod"),
                        rs.getString("vol_cpf"),
                        rs.getString("vol_nome"),
                        rs.getLong("car_cod"),
                        rs.getLong("con_cod"),
                        rs.getLong("cre_cod")
                );

                voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                voluntario.setContato(contatoDAL.FindById(voluntario.getContato().getId()));

                lista.add(voluntario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean update(VoluntarioModel voluntario) {
        boolean credAtualizado = credenciaisDAL.atualizar(voluntario.getCredenciais());
        boolean contAtualizado = contatoDAL.updateContato(voluntario.getContato());
        if (!credAtualizado || !contAtualizado) return false;

        String sql = "UPDATE voluntario SET vol_cpf = ?, vol_nome = ?, car_cod = ?, con_cod = ?, cre_cod = ? WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, voluntario.getCpf());
            stmt.setString(2, voluntario.getNome());
            stmt.setLong(3, voluntario.getCargoCod());
            stmt.setLong(4, voluntario.getContatoCod());
            stmt.setLong(5, voluntario.getCredenciaisCod());
            stmt.setLong(6, voluntario.getId());

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
        contatoDAL.deleteByContato(voluntario.getContato().getId());

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
