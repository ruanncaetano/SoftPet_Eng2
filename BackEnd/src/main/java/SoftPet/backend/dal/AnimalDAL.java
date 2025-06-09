package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.AnimalModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types; // Para java.sql.Types
import java.util.Date; // Para conversão de java.sql.Date para java.util.Date

@Repository
public class AnimalDAL {

    public AnimalModel Adicionar(AnimalModel animal) {
        String sql = "INSERT INTO animais(" +
                " an_nome, an_idade, an_tipo, an_sexo, an_porte, an_raca, an_pelagem, an_peso, an_baia," +
                " an_dt_resgate, an_disp_adocao, an_foto, an_castrado, an_obs, an_ativo)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getIdade());
            stmt.setString(3, animal.getTipo());
            stmt.setString(4, animal.getSexo());
            stmt.setString(5, animal.getPorte());
            stmt.setString(6, animal.getRaca());
            stmt.setString(7, animal.getPelagem());
            stmt.setInt(8, animal.getPeso());
            stmt.setString(9, animal.getBaia());
            if (animal.getDt_resgate() != null) {
                stmt.setDate(10, new java.sql.Date(animal.getDt_resgate().getTime()));
            } else {
                stmt.setNull(10, Types.DATE);
            }
            stmt.setBoolean(11, animal.getDisp_adocao());
            stmt.setBytes(12, animal.getFoto());
            stmt.setBoolean(13, animal.getCastrado());
            stmt.setString(14, animal.getObservacao());
            stmt.setBoolean(15, animal.getAtivo());

            int linhasMod = stmt.executeUpdate();

            if (linhasMod > 0) {
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Assumindo que AnimalModel.setCod(Long cod) existe
                        animal.setCod(rs.getLong(1));
                    } else {
                        throw new SQLException("Falha ao adicionar animal, nenhum ID gerado retornado.");
                    }
                }
            } else {
                throw new SQLException("Falha ao adicionar animal, nenhuma linha afetada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar animal: " + e.getMessage(), e);
        }
        return animal;
    }

    public AnimalModel buscarPorCod(int cod) {
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, cod);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AnimalModel animal = new AnimalModel();
                    // Assumindo que AnimalModel.setCod(Long cod) existe
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    if (rs.getDate("an_dt_resgate") != null) {
                        animal.setDt_resgate(new java.util.Date(rs.getDate("an_dt_resgate").getTime()));
                    }
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setFoto(rs.getBytes("an_foto"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                    return animal;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar animal por código (int): " + e.getMessage(), e);
        }
        return null;
    }

    public void atualizar(AnimalModel animal) {
        // CORREÇÃO: Se animal.getCod() retorna 'long', não pode ser comparado com 'null'.
        // A verificação de 'animal == null' é suficiente para o objeto.
        // Se 'cod' sendo 0 ou negativo significa um ID inválido, essa seria a verificação para o ID.
        // Para a convenção "tudo Long", AnimalModel.getCod() deveria retornar Long.
        if (animal == null) {
            throw new IllegalArgumentException("Objeto Animal não pode ser nulo para atualização.");
        }
        // Se AnimalModel.getCod() retorna long, e um ID não definido é 0L ou < 0
        // if (animal.getCod() <= 0) { // Exemplo de verificação para um ID long inválido
        //    throw new IllegalArgumentException("ID do animal inválido para atualização.");
        // }
        // Se AnimalModel.getCod() fosse Long (recomendado), a verificação original seria:
        // if (animal == null || animal.getCod() == null) {
        //    throw new IllegalArgumentException("Animal ou ID do animal não pode ser nulo para atualização.");
        // }


        String sql = "UPDATE animais SET an_nome = ?, an_idade = ?, an_tipo = ?, an_sexo = ?, an_porte = ?, an_raca = ?, an_pelagem = ?, an_peso = ?, an_baia = ?, "
                + "an_dt_resgate = ?, an_disp_adocao = ?, an_foto = ?, an_castrado = ?, an_obs = ?, an_ativo = ? "
                + "WHERE an_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getIdade());
            stmt.setString(3, animal.getTipo());
            stmt.setString(4, animal.getSexo());
            stmt.setString(5, animal.getPorte());
            stmt.setString(6, animal.getRaca());
            stmt.setString(7, animal.getPelagem());
            stmt.setInt(8, animal.getPeso());
            stmt.setString(9, animal.getBaia());
            if (animal.getDt_resgate() != null) {
                stmt.setDate(10, new java.sql.Date(animal.getDt_resgate().getTime()));
            } else {
                stmt.setNull(10, Types.DATE);
            }
            stmt.setBoolean(11, animal.getDisp_adocao());
            stmt.setBytes(12, animal.getFoto());
            stmt.setBoolean(13, animal.getCastrado());
            stmt.setString(14, animal.getObservacao());
            stmt.setBoolean(15, animal.getAtivo());

            // Assumindo que AN_COD é INTEGER no banco e animal.getCod() retorna long.
            // Se animal.getCod() retornasse Long, seria animal.getCod().intValue() após verificação de nulo.
            stmt.setInt(16, (int) animal.getCod());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                System.out.println("Nenhuma linha atualizada para o animal com COD: " + animal.getCod() + ". O animal pode não existir ou os dados são idênticos.");
            } else {
                System.out.println("Linhas afetadas na atualização do animal: " + rows);
            }
        } catch (NullPointerException e) {
            // Este catch pode ocorrer se animal.getCod() tentar desempacotar um Long nulo para long
            // DENTRO do AnimalModel, ou se outro getter retornar null e for usado indevidamente.
            e.printStackTrace();
            throw new IllegalArgumentException("Erro ao acessar dados do animal para atualização (possível ID nulo no modelo): " + e.getMessage(), e);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar animal: " + e.getMessage(), e);
        }
    }

    public List<AnimalModel> consultarComFiltros(String nome, String porte, String tipo, String sexo, boolean dispAdocao) {
        List<AnimalModel> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM animais WHERE an_ativo = true");
        List<Object> params = new ArrayList<>();

        if (nome != null && !nome.isEmpty()) {
            sql.append(" AND an_nome ILIKE ?");
            params.add("%" + nome + "%");
        }
        if (porte != null && !porte.isEmpty()) {
            sql.append(" AND an_porte = ?");
            params.add(porte);
        }
        if (tipo != null && !tipo.isEmpty()) {
            sql.append(" AND an_tipo = ?");
            params.add(tipo);
        }
        if (sexo != null && !sexo.isEmpty()) {
            sql.append(" AND an_sexo = ?");
            params.add(sexo);
        }
        sql.append(" AND an_disp_adocao = ?");
        params.add(dispAdocao);
        sql.append(" ORDER BY an_nome");

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AnimalModel animal = new AnimalModel();
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    if (rs.getDate("an_dt_resgate") != null) {
                        animal.setDt_resgate(new java.util.Date(rs.getDate("an_dt_resgate").getTime()));
                    }
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setFoto(rs.getBytes("an_foto"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                    lista.add(animal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar animais com filtros: " + e.getMessage(), e);
        }
        return lista;
    }

    public AnimalModel buscarPorCod(Long cod) {
        if (cod == null) {
            return null;
        }
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, cod.intValue());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AnimalModel animal = new AnimalModel();
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    if (rs.getDate("an_dt_resgate") != null) {
                        animal.setDt_resgate(new java.util.Date(rs.getDate("an_dt_resgate").getTime()));
                    }
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setFoto(rs.getBytes("an_foto"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                    return animal;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar animal por código (Long): " + e.getMessage(), e);
        }
        return null;
    }

    public AnimalModel buscarId(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        AnimalModel animal = null;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue());
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    animal = new AnimalModel();
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    if (rs.getDate("an_dt_resgate") != null) {
                        animal.setDt_resgate(new java.util.Date(rs.getDate("an_dt_resgate").getTime()));
                    }
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setFoto(rs.getBytes("an_foto"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar animal por ID (buscarId): " + e.getMessage(), e);
        }
        return animal;
    }

    public AnimalModel buscarIdComFoto(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        AnimalModel animal = null;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, id.intValue());
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    animal = new AnimalModel();
                    animal.setCod(rs.getLong("an_cod"));
                    animal.setNome(rs.getString("an_nome"));
                    animal.setIdade(rs.getInt("an_idade"));
                    animal.setTipo(rs.getString("an_tipo"));
                    animal.setSexo(rs.getString("an_sexo"));
                    animal.setPorte(rs.getString("an_porte"));
                    animal.setRaca(rs.getString("an_raca"));
                    animal.setPelagem(rs.getString("an_pelagem"));
                    animal.setPeso(rs.getInt("an_peso"));
                    animal.setBaia(rs.getString("an_baia"));
                    if (rs.getDate("an_dt_resgate") != null) {
                        animal.setDt_resgate(new java.util.Date(rs.getDate("an_dt_resgate").getTime()));
                    }
                    animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                    animal.setFoto(rs.getBytes("an_foto"));
                    animal.setCastrado(rs.getBoolean("an_castrado"));
                    animal.setObservacao(rs.getString("an_obs"));
                    animal.setAtivo(rs.getBoolean("an_ativo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar animal por ID com foto: " + e.getMessage(), e);
        }
        return animal;
    }
}
