package SoftPet.ger_adotantes.dal;

import SoftPet.ger_adotantes.config.SingletonDB;
import SoftPet.ger_adotantes.model.ContatoModel;
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

    private Long gerarNovoIdContato() {
        String sql = "SELECT COALESCE(MAX(con_cod), 0) + 1 AS novo_id FROM contato";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("novo_id");
            } else {
                throw new SQLException("Não foi possível gerar novo ID para contato.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar novo ID para contato: " + e.getMessage(), e);
        }
    }


    public ContatoModel addContato(ContatoModel contato) {
        String sql = "INSERT INTO contato (con_cod, con_telefone) VALUES (?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            // Gerar ID manualmente — por exemplo, pegar o último + 1
            Long novoId = gerarNovoIdContato(); // você precisará implementar isso
            stmt.setLong(1, novoId);
            stmt.setString(2, contato.getTelefone());

            int linhasMod = stmt.executeUpdate();
            if (linhasMod > 0) {
                contato.setId(novoId);
            }
        } catch (SQLException e) {
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
