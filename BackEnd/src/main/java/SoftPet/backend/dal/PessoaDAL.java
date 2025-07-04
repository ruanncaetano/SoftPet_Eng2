package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.PessoaCompletoDTO;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.PessoaModel;
import SoftPet.backend.model.EnderecoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class    PessoaDAL
{
    public static PessoaCompletoDTO findByDoador(String cpf)
    {
        PessoaCompletoDTO doadorDTO = null;
        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_telefone, c.con_email, " +
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
                PessoaModel doador = new PessoaModel(
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
                        rs.getString("con_telefone"),
                        rs.getString("con_email")
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

                doadorDTO = new PessoaCompletoDTO(doador, contato, endereco);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return doadorDTO;
    }

    public PessoaCompletoDTO findById(long id)
    {
        PessoaCompletoDTO doadorDTO = null;
        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_telefone, c.con_email, " +
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
                PessoaModel doador = new PessoaModel(
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
                        rs.getString("con_telefone"),
                        rs.getString("con_email")
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

                doadorDTO = new PessoaCompletoDTO(doador, contato, endereco);
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException("Erro ao buscar doador por ID: " + e.getMessage(), e);
        }
        return doadorDTO;
    }


    public PessoaModel addDoador(PessoaModel doador) {
        String sql = "INSERT INTO pessoa (pe_cpf, pe_nome, pe_profissao, con_cod, en_id, pe_rg, pe_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

            // Aqui está o novo campo: pe_status = true
            stmt.setBoolean(7, true);

            int linhasMod = stmt.executeUpdate();
            if (linhasMod > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                    doador.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar doador: " + e.getMessage(), e);
        }
        return doador;
    }



    public Boolean updateDoador(String cpf, PessoaModel doador)
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

    public Boolean deleteFisicoByDoador(String cpf)
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

    public Boolean deleteLogicoByDoador(String cpf) {
        String sql = "UPDATE pessoa SET pe_status = false WHERE pe_cpf = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cpf);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean reativarDoador(String cpf) {
        String sql = "UPDATE pessoa SET pe_status = true WHERE pe_cpf = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cpf);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public List<PessoaCompletoDTO> getAll()
    {
        List<PessoaCompletoDTO> list = new ArrayList<>();

        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_cod, c.con_telefone, c.con_email, " +
                "e.en_id, e.en_cep, e.en_rua, e.en_numero, e.en_bairro, e.en_cidade, e.en_uf, e.en_complemento " +
                "FROM pessoa p " +
                "JOIN contato c ON p.con_cod = c.con_cod " +
                "JOIN endereco e ON p.en_id = e.en_id " +
                "WHERE p.pe_status = true"; // <-- filtro aplicado aqui

        try(ResultSet rs = SingletonDB.getConexao().consultar(sql))
        {
            while(rs.next())
            {
                PessoaModel doador = new PessoaModel(
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
                        rs.getString("con_telefone"),
                        rs.getString("con_email")
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

                PessoaCompletoDTO doadorDTO = new PessoaCompletoDTO(doador, contato, endereco);
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