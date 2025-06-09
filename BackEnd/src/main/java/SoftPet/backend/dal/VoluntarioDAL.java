package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CargoModel;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.CredenciaisModel;
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
        if (voluntario == null) {
            throw new IllegalArgumentException("Objeto VoluntarioModel não pode ser nulo para criação.");
        }
        if (voluntario.getCredenciais() == null || voluntario.getCredenciais().getLogin() == null) { // Verifica o login dentro de credenciais
            throw new IllegalArgumentException("Dados de credenciais (com login) são obrigatórios para o voluntário.");
        }
        if (voluntario.getContato() == null) {
            throw new IllegalArgumentException("Dados de contato são obrigatórios para o voluntário.");
        }

        // --- MODIFICAÇÃO AQUI ---
        // Buscar ou criar credenciais para evitar duplicatas pelo login
        Long credenciaisId = credenciaisDAL.buscarOuCriar(voluntario.getCredenciais());

        // Criar contato (vamos assumir que contato pode ser duplicado ou que addContato lida com isso)
        ContatoModel contatoSalvo = contatoDAL.addContato(voluntario.getContato());

        if (credenciaisId == null || credenciaisId <= 0) { // buscarOuCriar pode retornar -1L em falha
            throw new RuntimeException("Falha ao buscar ou criar ID das credenciais.");
        }
        if (contatoSalvo == null || contatoSalvo.getId() == null) {
            throw new RuntimeException("Falha ao criar ou obter ID do contato.");
        }

        voluntario.setCredenciaisCod(credenciaisId);
        voluntario.setContatoCod(contatoSalvo.getId());

        Long cargoId;
        if (voluntario.getCargoCod() != null && voluntario.getCargoCod() > 0) {
            cargoId = voluntario.getCargoCod();
        } else if (voluntario.getCargoNome() != null && !voluntario.getCargoNome().isBlank()) {
            CargoModel cargoParaBuscarOuCriar = new CargoModel(null, voluntario.getCargoNome());
            cargoId = cargoDAL.buscarOuCriar(cargoParaBuscarOuCriar);
        } else {
            throw new IllegalArgumentException("ID do Cargo ou Nome do Cargo é obrigatório.");
        }
        if (cargoId == null || cargoId <= 0) {
            throw new RuntimeException("Falha ao obter ID do cargo.");
        }
        voluntario.setCargoCod(cargoId);

        String sql = "INSERT INTO voluntario (vol_cpf, vol_nome, car_cod, con_cod, cre_cod) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, voluntario.getCpf());
            stmt.setString(2, voluntario.getNome());
            stmt.setInt(3, voluntario.getCargoCod().intValue());
            stmt.setInt(4, voluntario.getContatoCod().intValue());
            stmt.setInt(5, voluntario.getCredenciaisCod().intValue());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        voluntario.setId(rs.getLong(1));
                    } else {
                        throw new SQLException("Falha ao criar voluntário, nenhum ID gerado retornado.");
                    }
                }
            } else {
                throw new SQLException("Falha ao criar voluntário, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar voluntário no banco: " + e.getMessage(), e);
        }
        return voluntario;
    }

    // ... (restante dos métodos do VoluntarioDAL como buscarPorId, atualizar, findById, findByCPF, getAll, update, deleteByCPF) ...
    // Eles devem permanecer como estavam na última versão do Canvas, pois o problema era no 'create'.
    // Cole o restante dos métodos aqui da versão anterior do Canvas "voluntario_dal_npe_fix"

    public static VoluntarioModel buscarPorId(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM voluntario WHERE vol_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // VOL_COD é INTEGER
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
        if (voluntario == null || voluntario.getId() == null) {
            throw new IllegalArgumentException("Voluntário ou ID do voluntário não pode ser nulo para atualização.");
        }
        try {
            Long cargoId;
            if (voluntario.getCargoCod() != null && voluntario.getCargoCod() > 0) {
                cargoId = voluntario.getCargoCod();
            } else if (voluntario.getCargoNome() != null && !voluntario.getCargoNome().isBlank()){
                CargoModel cargoParaBuscarOuCriar = new CargoModel(null, voluntario.getCargoNome());
                cargoId = cargoDAL.buscarOuCriar(cargoParaBuscarOuCriar);
            } else {
                throw new IllegalArgumentException("Nome ou ID do cargo é necessário para atualizar voluntário.");
            }
            if (cargoId == null || cargoId <= 0) {
                throw new RuntimeException("Falha ao obter ID do cargo para atualização.");
            }
            voluntario.setCargoCod(cargoId);

            if (voluntario.getContato() != null && voluntario.getContato().getId() != null) {
                contatoDAL.updateContato(voluntario.getContato());
            } else if (voluntario.getContatoCod() != null && voluntario.getContato() != null) {
                voluntario.getContato().setId(voluntario.getContatoCod());
                contatoDAL.updateContato(voluntario.getContato());
            }

            if (voluntario.getCredenciais() != null && voluntario.getCredenciaisCod() != null) {
                credenciaisDAL.atualizar(voluntario.getCredenciais(), voluntario.getCredenciaisCod());
            }

            String sql = "UPDATE voluntario SET vol_nome = ?, vol_cpf = ?, car_cod = ? WHERE vol_cod = ?";
            try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
                stmt.setString(1, voluntario.getNome());
                stmt.setString(2, voluntario.getCpf());
                stmt.setInt(3, voluntario.getCargoCod().intValue());
                stmt.setInt(4, voluntario.getId().intValue());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public VoluntarioModel findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID para buscar o voluntário não pode ser nulo.");
        }
        VoluntarioModel voluntario = null;
        String sql = "SELECT vol_cod, vol_cpf, vol_nome, car_cod, con_cod, cre_cod FROM voluntario WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    voluntario = new VoluntarioModel();
                    voluntario.setId(rs.getLong("vol_cod"));
                    voluntario.setCpf(rs.getString("vol_cpf"));
                    voluntario.setNome(rs.getString("vol_nome"));
                    voluntario.setCargoCod(rs.getLong("car_cod"));
                    voluntario.setContatoCod(rs.getLong("con_cod"));
                    voluntario.setCredenciaisCod(rs.getLong("cre_cod"));

                    if (voluntario.getCredenciaisCod() != null) {
                        CredenciaisModel cred = credenciaisDAL.findById(voluntario.getCredenciaisCod());
                        voluntario.setCredenciais(cred);
                    }
                    if (voluntario.getContatoCod() != null) {
                        ContatoModel cont = contatoDAL.FindById(voluntario.getContatoCod());
                        voluntario.setContato(cont);
                    }
                    if (voluntario.getCargoCod() != null) {
                        CargoModel cargo = cargoDAL.buscarPorId(voluntario.getCargoCod());
                        if (cargo != null) {
                            voluntario.setCargoNome(cargo.getNome());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro SQL ao buscar voluntário por ID " + id + ": " + e.getMessage(), e);
        }
        return voluntario;
    }

    public VoluntarioModel findByCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null;
        }
        VoluntarioModel voluntario = null;
        String sql = "SELECT vol_cod, vol_cpf, vol_nome, car_cod, con_cod, cre_cod FROM voluntario WHERE vol_cpf = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    voluntario = new VoluntarioModel();
                    voluntario.setId(rs.getLong("vol_cod"));
                    voluntario.setCpf(rs.getString("vol_cpf"));
                    voluntario.setNome(rs.getString("vol_nome"));
                    voluntario.setCargoCod(rs.getLong("car_cod"));
                    voluntario.setContatoCod(rs.getLong("con_cod"));
                    voluntario.setCredenciaisCod(rs.getLong("cre_cod"));

                    if (voluntario.getCredenciaisCod() != null) {
                        voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                    }
                    if (voluntario.getContatoCod() != null) {
                        voluntario.setContato(contatoDAL.FindById(voluntario.getContatoCod()));
                    }
                    if (voluntario.getCargoCod() != null) {
                        CargoModel cargo = cargoDAL.buscarPorId(voluntario.getCargoCod());
                        if (cargo != null) {
                            voluntario.setCargoNome(cargo.getNome());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voluntario;
    }

    public List<VoluntarioModel> getAll() {
        List<VoluntarioModel> lista = new ArrayList<>();
        String sql = "SELECT vol_cod, vol_cpf, vol_nome, car_cod, con_cod, cre_cod FROM voluntario ORDER BY vol_nome";

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            if (rs != null) {
                while (rs.next()) {
                    VoluntarioModel voluntario = new VoluntarioModel();
                    voluntario.setId(rs.getLong("vol_cod"));
                    voluntario.setCpf(rs.getString("vol_cpf"));
                    voluntario.setNome(rs.getString("vol_nome"));
                    voluntario.setCargoCod(rs.getLong("car_cod"));
                    voluntario.setContatoCod(rs.getLong("con_cod"));
                    voluntario.setCredenciaisCod(rs.getLong("cre_cod"));

                    if (voluntario.getCredenciaisCod() != null) {
                        voluntario.setCredenciais(credenciaisDAL.findById(voluntario.getCredenciaisCod()));
                    }
                    if (voluntario.getContatoCod() != null) {
                        voluntario.setContato(contatoDAL.FindById(voluntario.getContatoCod()));
                    }
                    if (voluntario.getCargoCod() != null) {
                        CargoModel cargo = cargoDAL.buscarPorId(voluntario.getCargoCod());
                        if (cargo != null) {
                            voluntario.setCargoNome(cargo.getNome());
                        }
                    }
                    lista.add(voluntario);
                }
            } else {
                System.err.println("Falha ao consultar voluntários: ResultSet nulo retornado por SingletonDB.consultar().");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean update(VoluntarioModel voluntario) {
        if (voluntario == null || voluntario.getId() == null) {
            throw new IllegalArgumentException("Dados do voluntário ou ID inválidos para atualização.");
        }
        boolean credAtualizado = true;
        if (voluntario.getCredenciais() != null && voluntario.getCredenciaisCod() != null) {
            credAtualizado = credenciaisDAL.atualizar(voluntario.getCredenciais(), voluntario.getCredenciaisCod());
        }

        boolean contAtualizado = true;
        if (voluntario.getContato() != null && voluntario.getContatoCod() != null) {
            voluntario.getContato().setId(voluntario.getContatoCod());
            contAtualizado = contatoDAL.updateContato(voluntario.getContato());
        }

        if (!credAtualizado || !contAtualizado) {
            System.err.println("Falha ao atualizar credenciais ou contato associado ao voluntário.");
            return false;
        }

        Long cargoId;
        if (voluntario.getCargoCod() != null && voluntario.getCargoCod() > 0) {
            cargoId = voluntario.getCargoCod();
        } else if (voluntario.getCargoNome() != null && !voluntario.getCargoNome().isBlank()) {
            CargoModel cargoParaBuscarOuCriar = new CargoModel(null, voluntario.getCargoNome());
            cargoId = cargoDAL.buscarOuCriar(cargoParaBuscarOuCriar);
        } else {
            throw new IllegalArgumentException("Nome ou ID do cargo é necessário para atualizar voluntário.");
        }
        if (cargoId == null || cargoId <= 0) {
            throw new RuntimeException("Falha ao obter ID do cargo para atualização do voluntário.");
        }
        voluntario.setCargoCod(cargoId);

        String sql = "UPDATE voluntario SET vol_cpf = ?, vol_nome = ?, car_cod = ?, con_cod = ?, cre_cod = ? WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, voluntario.getCpf());
            stmt.setString(2, voluntario.getNome());
            stmt.setInt(3, voluntario.getCargoCod().intValue());
            stmt.setInt(4, voluntario.getContatoCod().intValue());
            stmt.setInt(5, voluntario.getCredenciaisCod().intValue());
            stmt.setInt(6, voluntario.getId().intValue());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio para deleção.");
        }
        VoluntarioModel voluntario = findByCPF(cpf);
        if (voluntario == null) {
            System.out.println("Nenhum voluntário encontrado com o CPF " + cpf + " para deletar.");
            return false;
        }

        boolean credDeletado = true;
        if (voluntario.getCredenciaisCod() != null) {
            credDeletado = credenciaisDAL.deletar(voluntario.getCredenciaisCod());
        }

        boolean contDeletado = true;
        if (voluntario.getContatoCod() != null) {
            contDeletado = contatoDAL.deleteByContato(voluntario.getContatoCod());
        }

        if (!credDeletado || !contDeletado) {
            System.err.println("Falha ao deletar credenciais ou contato associado ao voluntário CPF: " + cpf);
        }

        String sql = "DELETE FROM voluntario WHERE vol_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, voluntario.getId().intValue());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar voluntário: " + e.getMessage(), e);
        }
    }
}