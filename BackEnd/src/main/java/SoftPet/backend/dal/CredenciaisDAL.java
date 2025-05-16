package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CredenciaisModel;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class CredenciaisDAL {

    public int criar(CredenciaisModel credenciais) {
        String sql = "INSERT INTO credenciais (cre_login, cre_senha) VALUES (?, ?)";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, credenciais.getLogin());
            stmt.setString(2, credenciais.getSenha()); // Aconselhável guardar hash da senha
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean atualizar(CredenciaisModel credenciais) {
        String sql = "UPDATE credenciais SET cre_login = ?, cre_senha = ? WHERE cre_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, credenciais.getLogin());
            stmt.setString(2, credenciais.getSenha());
            stmt.setInt(3, credenciais.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM credenciais WHERE cre_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CredenciaisModel findById(int credenciaisCod) {
        CredenciaisModel credenciais = null;
        String sql = "SELECT * FROM credenciais WHERE cre_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, credenciaisCod);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                credenciais = new CredenciaisModel(
                        rs.getInt("cre_cod"),
                        rs.getString("cre_login"),
                        rs.getString("cre_senha")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return credenciais;
    }
}

