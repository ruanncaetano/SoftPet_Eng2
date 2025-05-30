package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.EmpresaDTO;
import SoftPet.backend.model.EmpresaModel;
import SoftPet.backend.service.EmpresaService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmpresaDAL {

    public EmpresaDTO findByEmpresa(Long id) {
        EmpresaDTO empresaDTO = null;
        String sql = "SELECT * FROM empresa WHERE id = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EmpresaModel empresa = mapResultSetToEmpresa(rs);
                empresaDTO = new EmpresaDTO(empresa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresaDTO;
    }

    public List<EmpresaDTO> getAllEmpresas() {
        List<EmpresaDTO> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa";

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs.next()) {
                EmpresaModel empresa = mapResultSetToEmpresa(rs);
                empresas.add(new EmpresaDTO(empresa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    public EmpresaModel addEmpresa(EmpresaModel empresa) {
        String sql = """
            INSERT INTO empresa
            (nome, razao_social, cnpj, logo_pequena, endereco, bairro,
             cidade, uf, diretor, site, telefone)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(empresa, stmt, false);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    empresa.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar empresa: " + e.getMessage(), e);
        }

        return empresa;
    }

    public boolean updateEmpresa(EmpresaModel empresa) {
        String sql = """
        UPDATE empresa SET
        nome = ?, razao_social = ?, cnpj = ?, logo_pequena = ?,
        endereco = ?, bairro = ?, cidade = ?, uf = ?, diretor = ?, site = ?, telefone = ?
        WHERE id = ?
    """;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            preencherStatement(empresa, stmt, true);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new EmpresaService.EmpresaNotFoundException("Empresa com ID " + empresa.getId() + " não encontrada para atualização.");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar empresa: " + e.getMessage(), e);
        }
    }


    public boolean deleteByEmpresa(Long id) {
        String sql = "DELETE FROM empresa WHERE id = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private EmpresaModel mapResultSetToEmpresa(ResultSet rs) throws SQLException {
        return new EmpresaModel(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("razao_social"),
                rs.getString("cnpj"),
                rs.getString("logo_pequena"),
                rs.getString("endereco"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("uf"),
                rs.getString("diretor"),
                rs.getString("site"),
                rs.getString("telefone")
        );
    }

    private void preencherStatement(EmpresaModel empresa, PreparedStatement stmt, boolean isUpdate) throws SQLException {
        stmt.setString(1, empresa.getNome());
        stmt.setString(2, empresa.getRazaoSocial());
        stmt.setString(3, empresa.getCnpj());
        stmt.setString(4, empresa.getLogoPequena());
        stmt.setString(5, empresa.getEndereco());
        stmt.setString(6, empresa.getBairro());
        stmt.setString(7, empresa.getCidade());
        stmt.setString(8, empresa.getUf());
        stmt.setString(9, empresa.getDiretor());
        stmt.setString(10, empresa.getSite());
        stmt.setString(11, empresa.getTelefone());

        if (isUpdate) {
            stmt.setLong(12, empresa.getId());
        }
    }
}
