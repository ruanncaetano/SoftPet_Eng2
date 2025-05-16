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
                " an_nome, an_idade, an_tipo, an_sexo,an_porte, an_raca, an_pelagem, an_baia, an_dt_resgate, an_disp_adocao, an_foto)" +
                "VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?);";

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
            stmt.setString(8, animal.getBaia());         // baia
            stmt.setDate(9, new java.sql.Date(animal.getDt_resgate().getTime())); // data
            stmt.setBoolean(10, animal.isDisp_adocao());  // disponível para adoção
            stmt.setBytes(11, animal.getFoto());         // foto em bytes
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

    // vai buscar os animais de acordo com os filtro, com os if e else, vai montar o comando de SELECT e trazer as inormações para mim

    public List<AnimalModel> consultarComFiltros(String nome, String porte, String tipo, String sexo, Boolean dispAdocao) {
        List<AnimalModel> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM animais WHERE 1=1");
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
        if (dispAdocao != null) {
            sql.append(" AND an_disp_adocao = ?");
            params.add(dispAdocao);
        }

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
                animal.setPorte(rs.getString("an_porte"));
                animal.setBaia(rs.getString("an_baia"));
                animal.setDt_resgate(rs.getDate("an_dt_resgate"));
                animal.setDisp_adocao(rs.getBoolean("an_disp_adocao"));
                animal.setFoto(rs.getBytes("an_foto"));

                lista.add(animal);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar animais: " + e.getMessage(), e);
        }

        return lista;
    }
    public AnimalModel buscarId(int id)
    {
        String sql = "SELECT * FROM animais WHERE an_cod = ?";
        List<Object> params = new ArrayList<>();
    }
}
