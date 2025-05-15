package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.DoadorModel;
import SoftPet.backend.model.EnderecoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoadorDAL
{
    public DoadorModel findByDoador(String cpf)
    {
        DoadorModel doador = null;
        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_telefone, " +
                "e.en_cep, e.en_rua, e.en_numero, e.en_bairro, e.en_cidade, e.en_uf, e.en_complemento " +
                "FROM pessoa p " +
                "JOIN contato c ON p.con_cod = c.con_cod " +
                "JOIN endereco e ON p.en_id = e.en_id " +
                "WHERE p.pe_cpf = ?";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                doador = new DoadorModel(
                        rs.getLong("pe_cod"),
                        rs.getString("pe_cpf"),
                        rs.getString("pe_nome"),
                        rs.getBoolean("pe_status"),
                        rs.getString("pe_profissao"),
                        rs.getLong("con_cod"),
                        rs.getLong("en_id"),
                        rs.getString("pe_rg")
                );
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return doador;
    }

    public DoadorModel addDoador(DoadorModel doador)
    {
        String sql = "INSERT INTO pessoa (pe_cpf, pe_nome, pe_status, pe_profissao, con_cod, en_id, pe_rg) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, doador.getCpf());
            stmt.setString(2, doador.getNome());
            stmt.setBoolean(3, doador.getStatus());
            stmt.setString(4, doador.getProfissao());
            if(doador.getContato() != null)
                stmt.setLong(5, doador.getContato());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);
            if(doador.getEndereco() != null)
                stmt.setLong(6, doador.getEndereco());
            else
                stmt.setNull(6, java.sql.Types.INTEGER);
            stmt.setString(7, doador.getRg());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0)
            {
                try(ResultSet rs = stmt.getGeneratedKeys())
                {
                    if(rs.next())
                        doador.setId(rs.getLong(1));
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return doador;
    }

    public Boolean updateDoador(Long id, DoadorModel doador)
    {
        String sql = "UPDATE pessoa SET pe_cpf = ?, pe_nome = ?, pe_status = ?, pe_profissao = ?, con_cod = ?, en_id = ?, pe_rg = ? WHERE pe_cod = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, doador.getCpf());
            stmt.setString(2, doador.getNome());
            stmt.setBoolean(3, doador.getStatus());
            stmt.setString(4, doador.getProfissao());
            if(doador.getContato() != null)
                stmt.setLong(5, doador.getContato());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);
            if(doador.getEndereco() != null)
                stmt.setLong(6, doador.getEndereco());
            else
                stmt.setNull(6, java.sql.Types.INTEGER);
            stmt.setString(7, doador.getRg());
            stmt.setLong(8, id);

            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteByDoador(Long id)
    {
        String sql = "DELETE FROM pessoa WHERE pe_cod = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public List<DoadorModel> listarTodos()
    {
        List<DoadorModel> list = new ArrayList<>();

        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_cod, c.con_telefone, " +
                "e.en_id, e.en_cep, e.en_rua, e.en_numero, e.en_bairro, e.en_cidade, e.en_uf, e.en_complemento " +
                "FROM pessoa p " +
                "JOIN contato c ON p.con_cod = c.con_cod " +
                "JOIN endereco e ON p.en_id = e.en_id";

        try(ResultSet rs = SingletonDB.getConexao().consultar(sql))
        {
            while(rs.next())
            {
                DoadorModel doador = new DoadorModel(
                        rs.getLong("pe_cod"),
                        rs.getString("pe_cpf"),
                        rs.getString("pe_nome"),
                        rs.getBoolean("pe_status"),
                        rs.getString("pe_profissao"),
                        rs.getLong("con_cod"),
                        rs.getLong("en_id"),
                        rs.getString("pe_rg")
                );

                //relacionando o objeto completo
                ContatoModel contato = new ContatoModel(
                        rs.getLong("con_cod"),
                        rs.getString("con_telefone")
                );
                EnderecoModel endereco = new EnderecoModel(
                        rs.getLong("en_id"),
                        rs.getString("en_cep"),
                        rs.getString("en_rua"),
                        rs.getInt("en_numero"),
                        rs.getString("en_bairro"),
                        rs.getString("en_cidade"),
                        rs.getString("en_uf"),
                        rs.getString("en_complemento")
                );
                list.add(doador);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Erro ao listar doadores:");
            e.printStackTrace();
        }
        return list;
    }
}