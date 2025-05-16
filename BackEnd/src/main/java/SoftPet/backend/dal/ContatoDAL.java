package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.ContatoModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ContatoDAL {

    public int criar(ContatoModel contato) {
        String sql = "INSERT INTO contato (con_telefone, con_email) VALUES (?, ?)";
        int idGerado = 0;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, contato.getTelefone());
            stmt.setString(2, contato.getEmail());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGerado;
    }

    public ContatoModel findById(int id) {
        ContatoModel contato = null;
        String sql = "SELECT * FROM contato WHERE con_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                contato = new ContatoModel(
                        rs.getInt("con_cod"),
                        rs.getLong("con_telefone"),
                        rs.getString("con_email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contato;
    }

    public boolean atualizar(ContatoModel contato) {
        String sql = "UPDATE contato SET con_telefone = ?, con_email = ? WHERE con_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, contato.getTelefone());
            stmt.setString(2, contato.getEmail());
            stmt.setInt(3, contato.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM contato WHERE con_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
