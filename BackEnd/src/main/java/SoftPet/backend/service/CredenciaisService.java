package SoftPet.backend.service;

import SoftPet.backend.dal.CredenciaisDAL;
import SoftPet.backend.model.CredenciaisModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredenciaisService {

    @Autowired
    private CredenciaisDAL credenciaisDAL;

    public int criarCredenciais(CredenciaisModel credenciais) {
        return credenciaisDAL.criar(credenciais);
    }

    public CredenciaisModel buscarPorId(int id) {
        return credenciaisDAL.findById(id);
    }

    public boolean atualizarCredenciais(CredenciaisModel credenciais) {
        return credenciaisDAL.atualizar(credenciais);
    }

    public boolean deletarCredenciais(int id) {
        return credenciaisDAL.deletar(id);
    }
}
