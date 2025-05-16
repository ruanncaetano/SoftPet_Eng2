package SoftPet.backend.service;

import SoftPet.backend.dal.VoluntarioDAL;
import SoftPet.backend.model.VoluntarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioDAL voluntarioDAL;

    public VoluntarioModel cadastrarVoluntario(VoluntarioModel voluntario) {
        VoluntarioModel existente = voluntarioDAL.findById(voluntario.getId());
        if (existente != null) {
            throw new IllegalArgumentException("ID já cadastrado para outro voluntário.");
        }

        return voluntarioDAL.create(voluntario);
    }

    public List<VoluntarioModel> listarTodos() {
        return voluntarioDAL.getAll();
    }

    public VoluntarioModel buscarPorCPF(String cpf) {
        return voluntarioDAL.findByCPF(cpf);
    }

    public boolean removerPorCPF(String cpf) {
        return voluntarioDAL.deleteByCPF(cpf);
    }

    public boolean atualizarVoluntario(VoluntarioModel voluntario) {
        return voluntarioDAL.update(voluntario);
    }
}
