package SoftPet.backend.service;

import SoftPet.backend.dal.AdocaoDAL;
import SoftPet.backend.dal.AnimalDAL;
import SoftPet.backend.dal.PessoaDAL;
import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.dto.AdotanteCompletoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SoftPet.backend.util.Validation;
import SoftPet.backend.util.cpfValidator;

@Service
public class AdocaoService {
    @Autowired
    private AdocaoDAL adocaoDAL;
    @Autowired
    private AnimalDAL animalDAL;
    @Autowired
    private PessoaDAL pessoaDAL;

    public AdocaoModel addAdocao(AdocaoDTO adocaoDTO)
    {
        AnimalModel animal = animalDAL.buscarPorCod(adocaoDTO.getAnimal().getCod());
        AdotanteCompletoDTO  adotante = pessoaDAL.findByDoador(adocaoDTO.getPessoa().getCpf());

        if(adotante == null)
            throw new IllegalArgumentException("Adotante não encontrado!");
//        if(!cpfValidator.isCpfValido(adotante.getPessoa().getCpf()))
//            throw new IllegalArgumentException("CPF inválido!");
//        if(!Validation.validarTelefone(adotante.getContato().getTelefone()))
//            throw new IllegalArgumentException("Telefone inválido!");
//        if(!Validation.validarNomeCompleto(adotante.getPessoa().getNome()))
//            throw new IllegalArgumentException("Nome inválido!");

        if(animal == null)
            throw new IllegalArgumentException("Animal não encontrado!");
        if(!Validation.ValidarIdae(animal.getIdade()))
            throw new IllegalArgumentException("Idade do animal negativa!");

        AdocaoModel novaAdocao= adocaoDTO.getAdocao();
        novaAdocao.setAn_cod(animal.getCod());
        novaAdocao.setAdo_cod(adotante.getPessoa().getId());
        return adocaoDAL.NovaAdocao(novaAdocao);
    }

}
