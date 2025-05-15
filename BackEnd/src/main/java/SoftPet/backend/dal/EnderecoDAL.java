package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.EnderecoModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnderecoDAL
{
    public EnderecoModel FindById(Long id)
    {
        EnderecoModel endereco = null;
        String sql = "SELECT * FROM endereco WHERE end_id = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                endereco = new EnderecoModel(
                        rs.getLong("en_id"),
                        rs.getString("en_cep"),
                        rs.getString("en_rua"),
                        rs.getInt("en_numero"),
                        rs.getString("en_bairro"),
                        rs.getString("en_cidade"),
                        rs.getString("en_uf"),
                        rs.getString("en_complemento")
                );
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return endereco;
    }

    public EnderecoModel addEndereco(EnderecoModel endereco)
    {
        String sql = "INSERT INTO endereco (en_cep,en_rua,en_numero,en_bairro,en_cidade,en_uf,en_complemento) VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getRua());
            stmt.setInt(3, endereco.getNumero());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getUf());
            stmt.setString(7, endereco.getComplemento());

            int AffectedRows = stmt.executeUpdate();
            if(AffectedRows > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next())
                    endereco.setId(rs.getLong(1));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return endereco;
    }

    public Boolean updateEndereco(Long id, EnderecoModel endereco)
    {
        String sql = "UPDATE endereco SET en_cep = ?, en_rua = ?, en_numero = ?, en_bairro = ?, en_cidade = ?, en_uf = ?, en_complemento = ? WHERE en_id = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getRua());
            stmt.setInt(3, endereco.getNumero());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getUf());
            stmt.setString(7, endereco.getComplemento());
            stmt.setLong(8, id);
            return stmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteByEndereco(Long id)
    {
        String sql = "DELETE FROM endereco WHERE en_id = ?";
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

}
