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

@Repository
public class AnimalDAL {

    public AnimalModel Adicionar(AnimalModel animal)
    {
        String sql = "INSERT INTO animais(" +
                " an_nome, an_idade, an_tipo, an_sexo,an_porte, an_raca, an_pelagem,an_peso, an_baia," +
                " an_dt_resgate, an_disp_adocao, an_foto,an_castrado,an_obs,an_ativo)" +
                "VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?,?,?,?);";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql,
                Statement.RETURN_GENERATED_KEYS))
        {

            // vai vai colocar os valores nos espaços vazios (?)
            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getIdade());
            stmt.setString(3, animal.getTipo());         // tipo (cachorro, gato, etc.)
            stmt.setString(4, animal.getSexo());         // sexo
            stmt.setString(5, animal.getPorte());
            stmt.setString(6, animal.getRaca());         // raça
            stmt.setString(7, animal.getPelagem());      // pelagem
            stmt.setInt(8, animal.getPeso());
            stmt.setString(9, animal.getBaia());         // baia
            stmt.setDate(10, new java.sql.Date(animal.getDt_resgate().getTime())); // data
            stmt.setBoolean(11, animal.getDisp_adocao());  // disponível para adoção
            stmt.setBytes(12, animal.getFoto());         // foto em bytes
            stmt.setBoolean(13,animal.getCastrado());
            stmt.setString(14,animal.getObservacao());
            stmt.setBoolean(15, animal.getAtivo());
            // esse campo de cima esta preenchendo a string SQL

            int linhasMod = stmt.executeUpdate(); // informa quantas linhas foram modificadas

            if(linhasMod > 0) // verifica se ao menos uma linha foi mudada
            {
                ResultSet rs = stmt.getGeneratedKeys(); // pega o id gerado
                if(rs.next())
                {
                    animal.setCod(rs.getInt(1)); // pega o cod gerado
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animal;
    }

    public AnimalModel buscarPorCod(int cod) {
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setInt(1, cod);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                AnimalModel animal = new AnimalModel();
                animal.setCod(rs.getInt("an_cod"));
                animal.setNome(rs.getString("an_nome"));
                animal.setIdade(rs.getInt("an_idade"));
                animal.setPeso(rs.getInt("an_peso"));
                animal.setBaia(rs.getString("an_baia"));
                animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                animal.setFoto(rs.getBytes("an_foto"));
                animal.setCastrado(rs.getBoolean("an_castrado"));
                animal.setObservacao(rs.getString("an_obs"));
                animal.setAtivo(rs.getBoolean("an_ativo"));
                return animal;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animal por código: " + e.getMessage(), e);
        }
    }

    public void atualizar(AnimalModel animal) {
        String sql = "UPDATE animais SET an_nome = ?, an_idade = ?, an_peso = ?, an_baia = ?, "
                + "an_disp_adocao = ?, an_foto = ?, an_castrado = ?, an_obs = ?, an_ativo = ? "
                + "WHERE an_cod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getIdade());
            stmt.setInt(3, animal.getPeso());
            stmt.setString(4, animal.getBaia());
            stmt.setBoolean(5, animal.getDisp_adocao());
            stmt.setBytes(6, animal.getFoto());
            stmt.setBoolean(7, animal.getCastrado());
            stmt.setString(8, animal.getObservacao());
            stmt.setBoolean(9, animal.getAtivo());
            stmt.setInt(10, animal.getCod());
            System.out.println("Atualizando animal com COD: " + animal.getCod());

            int rows = stmt.executeUpdate();
            System.out.println("Linhas afetadas: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar animal: " + e.getMessage(), e);
        }
    }

    // vai buscar os animais de acordo com os filtro, com os if e else, vai montar o comando de SELECT e trazer as inormações para mim

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
        if (dispAdocao) {
            sql.append(" AND an_disp_adocao = ?");
            params.add(dispAdocao);
        }
        sql.append(" ORDER BY an_cod");
        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AnimalModel animal = new AnimalModel();
                animal.setCod(rs.getInt("an_cod"));
                animal.setNome(rs.getString("an_nome"));
                animal.setIdade(rs.getInt("an_idade"));
                animal.setTipo(rs.getString("an_tipo"));
                animal.setSexo(rs.getString("an_sexo"));
                animal.setRaca(rs.getString("an_raca"));
                animal.setPelagem(rs.getString("an_pelagem"));
                animal.setPeso(rs.getInt("an_peso"));
                animal.setPorte(rs.getString("an_porte"));
                animal.setBaia(rs.getString("an_baia"));
                animal.setDt_resgate(rs.getDate("an_dt_resgate"));
                animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                animal.setAtivo(rs.getBoolean("an_ativo"));
                animal.setCastrado(rs.getBoolean("an_castrado"));
                animal.setObservacao(rs.getString("an_obs"));                animal.setFoto(rs.getBytes("an_foto"));
                lista.add(animal);

            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar animais: " + e.getMessage(), e);
        }

        return lista;
    }
    public AnimalModel buscarId(Long id) {
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        AnimalModel animal = null;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                animal = new AnimalModel();
                animal.setCod(rs.getInt("an_cod"));
                animal.setNome(rs.getString("an_nome"));
                animal.setIdade(rs.getInt("an_idade"));
                animal.setTipo(rs.getString("an_tipo"));
                animal.setSexo(rs.getString("an_sexo"));
                animal.setRaca(rs.getString("an_raca"));
                animal.setPelagem(rs.getString("an_pelagem"));
                animal.setPeso(rs.getInt("an_peso"));
                animal.setPorte(rs.getString("an_porte"));
                animal.setBaia(rs.getString("an_baia"));
                animal.setDt_resgate(rs.getDate("an_dt_resgate"));
                animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                animal.setAtivo(rs.getBoolean("an_ativo"));
                animal.setCastrado(rs.getBoolean("an_castrado"));
                animal.setObservacao(rs.getString("an_obs"));
                animal.setFoto(rs.getBytes("an_foto")); // campo bytea (foto)
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animal por ID: " + e.getMessage(), e);
        }

        return animal;
    }
    public AnimalModel buscarIdComFoto(Long id) {
        String sql = "SELECT * FROM animais WHERE an_cod = ? AND an_foto IS NOT NULL";
        AnimalModel animal = null;

        try (PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                animal = new AnimalModel();
                animal.setCod(rs.getInt("an_cod"));
                animal.setNome(rs.getString("an_nome"));
                animal.setIdade(rs.getInt("an_idade"));
                animal.setTipo(rs.getString("an_tipo"));
                animal.setSexo(rs.getString("an_sexo"));
                animal.setRaca(rs.getString("an_raca"));
                animal.setPelagem(rs.getString("an_pelagem"));
                animal.setPeso(rs.getInt("an_peso"));
                animal.setPorte(rs.getString("an_porte"));
                animal.setBaia(rs.getString("an_baia"));
                animal.setDt_resgate(rs.getDate("an_dt_resgate"));
                animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                animal.setAtivo(rs.getBoolean("an_ativo"));
                animal.setCastrado(rs.getBoolean("an_castrado"));
                animal.setObservacao(rs.getString("an_obs"));
                animal.setFoto(rs.getBytes("an_foto")); // campo bytea (foto)
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animal por ID com foto: " + e.getMessage(), e);
        }

        return animal;
    }
}
