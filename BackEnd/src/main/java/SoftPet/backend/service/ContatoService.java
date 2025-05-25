package SoftPet.backend.service;

import SoftPet.backend.dal.ContatoDAL;
import SoftPet.backend.model.ContatoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {

    @Autowired
    private ContatoDAL contatoDAL;

    public ContatoModel criarContato(ContatoModel contato) {
        return contatoDAL.addContato(contato);
    }

    public ContatoModel buscarPorId(Long id) {
        return contatoDAL.FindById(id);
    }

    public boolean atualizarContato(ContatoModel contato) {
        return contatoDAL.updateContato(contato);
    }

    public boolean deletarContato(Long id) {
        return contatoDAL.deleteByContato(id);
    }
}
