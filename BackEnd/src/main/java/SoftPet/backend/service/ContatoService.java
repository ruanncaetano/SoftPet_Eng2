package SoftPet.backend.service;

import SoftPet.backend.dal.ContatoDAL;
import SoftPet.backend.model.ContatoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {

    @Autowired
    private ContatoDAL contatoDAL;

    public int criarContato(ContatoModel contato) {
        return contatoDAL.criar(contato);
    }

    public ContatoModel buscarPorId(int id) {
        return contatoDAL.findById(id);
    }

    public boolean atualizarContato(ContatoModel contato) {
        return contatoDAL.atualizar(contato);
    }

    public boolean deletarContato(int id) {
        return contatoDAL.deletar(id);
    }
}
