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
                        rs.getString("con_telefone"),
                        rs.getString("con_email")
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
        String sql = "INSERT INTO contato (con_telefone, con_email) VALUES (?, ?)";
        Long idGerado = 0L;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, contato.getTelefone());
            stmt.setString(2, contato.getEmail());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next())
                {
                    idGerado = rs.getLong(1);
                    contato.setId(idGerado);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return contato;
    }



    public boolean updateContato(ContatoModel contato)
    {
        String sql = "UPDATE contato SET con_telefone = ?, con_email = ? WHERE con_cod = ?";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, contato.getTelefone());
            stmt.setString(2, contato.getEmail());
            stmt.setLong(3, contato.getId());
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
                        rs.getString("con_telefone"),
                        rs.getString("con_email")
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