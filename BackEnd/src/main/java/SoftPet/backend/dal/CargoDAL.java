package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CargoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CargoDAL {

    public CargoModel buscarPorNome(String nome) {
        String sql = "SELECT * FROM cargo WHERE car_nome = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new CargoModel(rs.getLong("car_cod"), rs.getString("car_nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CargoModel findById(int id) {
        String sql = "SELECT * FROM cargo WHERE car_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new CargoModel(rs.getLong("car_cod"), rs.getString("car_nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CargoModel> getAll() {
        List<CargoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM cargo";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new CargoModel(rs.getLong("car_cod"), rs.getString("car_nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Long criar(CargoModel cargo) {
        String sql = "INSERT INTO cargo (car_nome) VALUES (?)";
        Long idGerado = (long) -1;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cargo.getNome());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGerado;
    }

    public boolean update(CargoModel cargo) {
        String sql = "UPDATE cargo SET car_nome = ? WHERE car_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cargo.getNome());
            stmt.setLong(2, cargo.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM cargo WHERE car_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long buscarOuCriar(CargoModel cargo) {
        String buscarSql = "SELECT car_cod FROM cargo WHERE LOWER(car_nome) = LOWER(?)";
        String inserirSql = "INSERT INTO cargo (car_nome) VALUES (?)";

        try (
                PreparedStatement buscarStmt = SingletonDB.getConexao().getPreparedStatement(buscarSql);
                PreparedStatement inserirStmt = SingletonDB.getConexao().getPreparedStatement(inserirSql, Statement.RETURN_GENERATED_KEYS)
        ) {
            // Primeiro tenta encontrar o cargo existente
            buscarStmt.setString(1, cargo.getNome());
            ResultSet rs = buscarStmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("car_cod"); // Cargo já existe
            }

            // Se não encontrar, insere o novo
            inserirStmt.setString(1, cargo.getNome());
            int linhasAfetadas = inserirStmt.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet generatedKeys = inserirStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Retorna o ID gerado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (long) -1; // Falha ao inserir ou buscar
    }


    public CargoModel buscarPorId(Long id) {
        String sql = "SELECT * FROM cargo WHERE car_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new CargoModel(
                        rs.getLong("car_cod"),
                        rs.getString("car_nome")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Se não encontrar ou ocorrer erro
    }

}
