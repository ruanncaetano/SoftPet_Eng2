package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAL {

    public UserModel findByCPF(String cpf)
    {
        UserModel user = null;
        String sql = "SELECT * FROM credenciais WHERE cre_login = ?";
        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                user = new UserModel(
                        rs.getLong("cre_cod"),
                        rs.getString("cre_login"),
                        rs.getString("cre_senha")
                );
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return user;
    }

    public UserModel create(UserModel user) {
        String sql = "INSERT INTO credenciais (cre_login, cre_senha) VALUES (?, ?)";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getCpf());
            stmt.setString(2, user.getSenha());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updateSenha(String cpf, String novaSenha) {
        String sql = "UPDATE credenciais SET cre_senha = ? WHERE cre_login = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, novaSenha);
            stmt.setString(2, cpf);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByCPF(String cpf) {
        String sql = "DELETE FROM credenciais WHERE cre_login = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cpf);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos antigos opcionais (caso você ainda use a interface IDAL)
    public List<UserModel> getAll() {
        List<UserModel> list = new ArrayList<>();
        String sql = "SELECT * FROM credenciais";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                UserModel user = new UserModel(
                        rs.getLong("cre_cod"),
                        rs.getString("cre_login"),
                        rs.getString("cre_senha")
                );
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
