package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.model.AnimalModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class AnimalDAL {

    public AnimalModel Adicionar(AnimalModel animal)
    {
        String sql = "INSERT INTO animais(" +
                " an_nome, an_idade, an_tipo, an_sexo, an_raca, an_pelagem, an_baia, an_dt_resgate, an_disp_adocao, an_foto)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql,
                Statement.RETURN_GENERATED_KEYS))
        {

            // vai vai colocar os valores nos espaços vazios (?)
            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getIdade());
            stmt.setString(3, animal.getTipo());         // tipo (cachorro, gato, etc.)
            stmt.setString(4, animal.getSexo());         // sexo
            stmt.setString(5, animal.getRaca());         // raça
            stmt.setString(6, animal.getPelagem());      // pelagem
            stmt.setString(7, animal.getBaia());         // baia
            stmt.setDate(8, new java.sql.Date(animal.getDt_resgate().getTime())); // data
            stmt.setBoolean(9, animal.isDisp_adocao());  // disponível para adoção
            stmt.setBytes(10, animal.getFoto());         // foto em bytes
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
}
