package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.CargoModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CargoDAL {

    /**
     * Busca um cargo pelo nome.
     * nome: O nome do cargo a ser buscado.
     * Retorna o CargoModel encontrado ou null se não existir.
     */
    public CargoModel buscarPorNome(String nome) {
        String sql = "SELECT car_cod, car_nome FROM cargo WHERE car_nome = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CargoModel(rs.getLong("car_cod"), rs.getString("car_nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Considerar lançar uma RuntimeException para melhor tratamento de erro na camada de serviço
            // throw new RuntimeException("Erro ao buscar cargo por nome: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Busca um cargo pelo seu ID.
     * id: O ID (Long) do cargo a ser buscado.
     * Retorna o CargoModel encontrado ou null se não existir.
     */
    public CargoModel buscarPorId(Long id) {
        if (id == null) {
            // throw new IllegalArgumentException("ID do cargo não pode ser nulo.");
            return null; // Ou lançar exceção
        }
        String sql = "SELECT car_cod, car_nome FROM cargo WHERE car_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // CAR_COD é INTEGER no banco, converter Long para int
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CargoModel(
                            rs.getLong("car_cod"), // Lê como Long para o modelo
                            rs.getString("car_nome")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao buscar cargo por ID: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Lista todos os cargos cadastrados.
     * Retorna uma lista de CargoModel.
     */
    public List<CargoModel> getAll() {
        List<CargoModel> lista = new ArrayList<>();
        String sql = "SELECT car_cod, car_nome FROM cargo ORDER BY car_nome";

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) { // Usa o consultar do SingletonDB
            if (rs != null) {
                while (rs.next()) {
                    lista.add(new CargoModel(rs.getLong("car_cod"), rs.getString("car_nome")));
                }
            } else {
                System.err.println("Falha ao consultar cargos: ResultSet nulo retornado por SingletonDB.consultar().");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao listar todos os cargos: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Cria um novo cargo no banco de dados.
     * O ID do cargo (car_cod) deve ser gerado automaticamente pelo banco.
     * cargo: O objeto CargoModel contendo o nome do cargo. O ID no objeto será ignorado e preenchido.
     * Retorna o CargoModel criado com o ID preenchido.
     */
    public CargoModel criar(CargoModel cargo) {
        if (cargo == null || cargo.getNome() == null || cargo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo é obrigatório para criação.");
        }
        String sql = "INSERT INTO cargo (car_nome) VALUES (?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cargo.getNome());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cargo.setId(rs.getLong(1)); // Define o ID gerado no modelo
                        return cargo; // Retorna o objeto completo
                    } else {
                        throw new SQLException("Falha ao criar cargo, nenhum ID gerado retornado.");
                    }
                }
            } else {
                throw new SQLException("Falha ao criar cargo, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar cargo: " + e.getMessage(), e);
        }
        // Não deve chegar aqui se tudo correr bem ou se uma exceção for lançada
        // Ou lançar exceção se o ID não foi setado
    }

    /**
     * Atualiza o nome de um cargo existente.
     * cargo: O objeto CargoModel com o ID do cargo a ser atualizado e o novo nome.
     * Retorna true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean update(CargoModel cargo) {
        if (cargo == null || cargo.getId() == null || cargo.getNome() == null || cargo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("ID e nome do cargo são obrigatórios para atualização.");
        }
        String sql = "UPDATE cargo SET car_nome = ? WHERE car_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, cargo.getNome());
            stmt.setInt(2, cargo.getId().intValue()); // CAR_COD é INTEGER, converter Long para int

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao atualizar cargo: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deleta um cargo pelo seu ID.
     * id: O ID (Long) do cargo a ser deletado.
     * Retorna true se a deleção for bem-sucedida, false caso contrário.
     */
    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do cargo não pode ser nulo para deleção.");
        }
        // ATENÇÃO: Antes de deletar um cargo, verifique se ele não está sendo usado
        // na tabela VOLUNTARIO (coluna CAR_COD). Se estiver, a deleção falhará
        // devido à restrição de chave estrangeira, ou você pode causar inconsistência
        // se a restrição não estiver configurada para ON DELETE RESTRICT/CASCADE.
        // Adicionar uma verificação aqui é uma boa prática.
        // Ex:
        // String sqlCheck = "SELECT COUNT(*) FROM VOLUNTARIO WHERE CAR_COD = ?";
        // ... (executar e verificar se count > 0) ...

        String sql = "DELETE FROM cargo WHERE car_cod = ?";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue()); // CAR_COD é INTEGER, converter Long para int
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro ao deletar cargo: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Busca um cargo pelo nome. Se não existir, cria um novo e retorna o ID.
     * cargo: CargoModel contendo o nome do cargo.
     * Retorna o ID (Long) do cargo existente ou recém-criado.
     * Retorna -1L em caso de falha grave.
     */
    public Long buscarOuCriar(CargoModel cargo) {
        if (cargo == null || cargo.getNome() == null || cargo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo é obrigatório para buscar ou criar.");
        }

        CargoModel existente = buscarPorNome(cargo.getNome());
        if (existente != null && existente.getId() != null) {
            return existente.getId(); // Cargo já existe, retorna seu ID
        }

        // Se não encontrar, insere o novo
        String inserirSql = "INSERT INTO cargo (car_nome) VALUES (?)";
        try (PreparedStatement inserirStmt = SingletonDB.getConexao().getPreparedStatement(inserirSql, Statement.RETURN_GENERATED_KEYS)) {
            inserirStmt.setString(1, cargo.getNome());
            int linhasAfetadas = inserirStmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = inserirStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1); // Retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("Erro no processo de buscar ou criar cargo: " + e.getMessage(), e);
        }
        return -1L; // Indica falha
    }
}
