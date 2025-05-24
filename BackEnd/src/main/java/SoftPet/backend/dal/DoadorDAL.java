package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.DoadorCompletoDTO;
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
    public DoadorCompletoDTO findByDoador(String cpf)
    {
        DoadorCompletoDTO doadorDTO = null;
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

                //relacionando os objetos completos
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

                doadorDTO = new DoadorCompletoDTO(doador, contato, endereco);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return doadorDTO;
    }

    public DoadorCompletoDTO findById(long id)
    {
        DoadorCompletoDTO doadorDTO = null;
        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_telefone, " +
                "e.en_cep, e.en_rua, e.en_numero, e.en_bairro, e.en_cidade, e.en_uf, e.en_complemento " +
                "FROM pessoa p " +
                "JOIN contato c ON p.con_cod = c.con_cod " +
                "JOIN endereco e ON p.en_id = e.en_id " +
                "WHERE p.pe_cod = ?";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
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

                doadorDTO = new DoadorCompletoDTO(doador, contato, endereco);
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Erro ao buscar doador por ID: " + e.getMessage(), e);
        }
        return doadorDTO;
    }


    public DoadorModel addDoador(DoadorModel doador)
    {
        String sql = "INSERT INTO pessoa (pe_cpf, pe_nome, pe_profissao, con_cod, en_id, pe_rg) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, doador.getCpf());
            stmt.setString(2, doador.getNome());
            stmt.setString(3, doador.getProfissao());

            if (doador.getId_contato() != null)
                stmt.setLong(4, doador.getId_contato());
            else
                stmt.setNull(4, java.sql.Types.INTEGER);

            if (doador.getId_endereco() != null)
                stmt.setLong(5, doador.getId_endereco());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);

            stmt.setString(6, doador.getRg());

            int linhasMod = stmt.executeUpdate();
            if (linhasMod > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                    doador.setId(rs.getLong(1));
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Erro ao adicionar doador: " + e.getMessage(), e);
        }
        return doador;
    }


    public Boolean updateDoador(String cpf, DoadorModel doador)
    {
        if(!cpf.equals(doador.getCpf()))
            throw new IllegalArgumentException("O CPF não pode ser alterado.");

        String sql = "UPDATE pessoa SET pe_nome = ?, pe_status = ?, pe_profissao = ?, con_cod = ?, en_id = ?, pe_rg = ? WHERE pe_cpf = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, doador.getNome());
            stmt.setBoolean(2, doador.getStatus());
            stmt.setString(3, doador.getProfissao());
            if(doador.getId_contato() != null)
                stmt.setLong(4, doador.getId_contato());
            else
                stmt.setNull(4, java.sql.Types.BIGINT);
            if(doador.getId_endereco() != null)
                stmt.setLong(5, doador.getId_endereco());
            else
                stmt.setNull(5, java.sql.Types.BIGINT);
            stmt.setString(6, doador.getRg());
            stmt.setString(7, cpf);

            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteByDoador(String cpf)
    {
        String sql = "DELETE FROM pessoa WHERE pe_cpf = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, cpf);
            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public List<DoadorCompletoDTO> getAll()
    {
        List<DoadorCompletoDTO> list = new ArrayList<>();

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

                //relacionando os objetos completo
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

                DoadorCompletoDTO doadorDTO = new DoadorCompletoDTO(doador, contato, endereco);
                list.add(doadorDTO);
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