package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.ContatoModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContatoDAL
{
    public ContatoModel FindById(Long id)
    {
        ContatoModel contato = null;
        String sql = "SELECT * FROM contato WHERE con_cod = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                contato = new ContatoModel(
                        rs.getLong("con_cod"),
                        rs.getString("con_telefone")
                );
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return contato;
    }

    public ContatoModel addContato(ContatoModel contato)
    {
        String sql = "INSERT INTO contato (con_telefone) VALUES (?)";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, contato.getTelefone());
            int linhasMod = stmt.executeUpdate();
            if (linhasMod > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                    contato.setId(rs.getLong(1));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Erro ao adicionar contato: " + e.getMessage(), e);
        }
        return contato;
    }


    public Boolean updateContato(Long id, String novoTelefone)
    {
        String sql = "UPDATE contato SET con_telefone = ? WHERE con_cod = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, novoTelefone);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteByContato(Long id)
    {
        String sql = "DELETE FROM contato WHERE con_cod = ?";
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

    public List<ContatoModel> getAll()
    {
        List<ContatoModel> list = new ArrayList<>();
        String sql = "SELECT * FROM contato";
        try
        {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while(rs.next())
            {
                ContatoModel contato = new ContatoModel(
                        rs.getLong("con_cod"),
                        rs.getString("con_telefone")
                );
                list.add(contato);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
