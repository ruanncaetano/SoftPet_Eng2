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
                return new CargoModel(rs.getInt("car_cod"), rs.getString("car_nome"));
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
                return new CargoModel(rs.getInt("car_cod"), rs.getString("car_nome"));
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
                lista.add(new CargoModel(rs.getInt("car_cod"), rs.getString("car_nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int criar(CargoModel cargo) {
        String sql = "INSERT INTO cargo (car_nome) VALUES (?)";
        int idGerado = -1;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cargo.getNome());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
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

    public boolean update(CargoModel cargo) {
        String sql = "UPDATE cargo SET car_nome = ? WHERE car_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cargo.getNome());
            stmt.setInt(2, cargo.getId());

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

    public int buscarOuCriar(CargoModel cargo) {
        CargoModel existente = buscarPorNome(cargo.getNome());
        if (existente != null) {
            return existente.getId();
        } else {
            return criar(cargo);
        }
    }

}
