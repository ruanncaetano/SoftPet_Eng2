package SoftPet.backend.dal;

import SoftPet.backend.config.SingletonDB;
import SoftPet.backend.dto.AdotanteCompletoDTO;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.EnderecoModel;
import SoftPet.backend.model.PessoaModel;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Repository
public class PessoaDAL {
    public AdotanteCompletoDTO findByDoador(String cpf)
    {
        AdotanteCompletoDTO doadorDTO = null;
        String sql = "SELECT p.pe_cod, p.pe_cpf, p.pe_nome, p.pe_status, p.pe_profissao, p.con_cod, p.en_id, p.pe_rg, " +
                "c.con_telefone, " +
                "e.en_cep, e.en_rua, e.en_numero, e.en_bairro, e.en_cidade, e.en_uf, e.en_complemento " +
                "FROM pessoa p " +
                "JOIN contato c ON p.con_cod = c.con_cod " +
                "JOIN endereco e ON p.en_id = e.en_id " +
                "WHERE p.pe_cpf = ?";

        try(PreparedStatement stmt = SingletonDB.getConexao().getPreparedStatement(sql))
        {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                PessoaModel doador = new PessoaModel(
                        rs.getInt("pe_cod"),
                        rs.getString("pe_cpf"),
                        rs.getString("pe_nome"),
                        rs.getBoolean("pe_status"),
                        rs.getString("pe_profissao"),
                        rs.getLong("con_cod"),
                        rs.getLong("en_id"),
                        rs.getString("pe_rg")
                );

                //relacionando os objetos completos
                ContatoModel contato = new ContatoModel(
                        rs.getLong("con_cod"),
                        rs.getString("con_telefone")
                );
                EnderecoModel endereco = new EnderecoModel(
                        rs.getLong("en_id"),
                        rs.getString("en_cep"),
                        rs.getString("en_rua"),
                        rs.getInt("en_numero"),
                        rs.getString("en_bairro"),
                        rs.getString("en_cidade"),
                        rs.getString("en_uf"),
                        rs.getString("en_complemento")
                );

                doadorDTO = new AdotanteCompletoDTO(doador, contato, endereco);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return doadorDTO;
    }

}
