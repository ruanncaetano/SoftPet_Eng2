package SoftPet.backend.service;

import SoftPet.backend.dal.AdocaoDAL;
import SoftPet.backend.dal.AnimalDAL;
import SoftPet.backend.dal.PessoaDAL;
import SoftPet.backend.dto.AdocaoDTO;
import SoftPet.backend.dto.PessoaCompletoDTO;
import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SoftPet.backend.util.Validation;

@Service
public class AdocaoService {
    @Autowired
    private AdocaoDAL adocaoDAL;
    @Autowired
    private AnimalDAL animalDAL;
    @Autowired
    private PessoaDAL pessoaDAL;

    public AdocaoModel addAdocao(AdocaoDTO adocaoDTO) {
        // Validação básica do DTO
        System.out.println("Dados recebidos: servise");
        System.out.println("Data: " + adocaoDTO.getAdocao().getAdo_dt());
        System.out.println("ID Pessoa: " + adocaoDTO.getPessoa().getId());
        System.out.println("ID Animal: " + adocaoDTO.getAnimal().getCod());
        if (adocaoDTO == null || adocaoDTO.getPessoa() == null || adocaoDTO.getAnimal() == null) {
            throw new IllegalArgumentException("Dados de adoção inválidos");
        }
        System.out.println(adocaoDTO.getAnimal().getCod());
        // Busca animal - verifica se existe
        AnimalModel animal = animalDAL.buscarPorCod(adocaoDTO.getAnimal().getCod());
        if (animal == null) {
            System.out.println(adocaoDTO.getAnimal().getCod());
            throw new IllegalArgumentException("Animal não encontrado!");
        }
        if (!Validation.ValidarIdade(animal.getIdade())) {

            throw new IllegalArgumentException("Idade do animal negativa!");
        }

        // Busca adotante - usando pe_cod do JSON
        PessoaCompletoDTO pessoa = pessoaDAL.findById(adocaoDTO.getPessoa().getId());
        if (pessoa == null) {
            throw new IllegalArgumentException("Adotante não encontrado!");
        }

        // Cria nova adoção
        AdocaoModel novaAdocao = new AdocaoModel();
        novaAdocao.setAdo_dt(adocaoDTO.getAdocao().getAdo_dt());
        novaAdocao.setAn_cod(animal.getCod());
        novaAdocao.setPe_cod(pessoa.getPessoa().getId());

        return adocaoDAL.NovaAdocao(novaAdocao);
    }

    public AdocaoDTO getAdocao(Long pessoaId) {
        PessoaCompletoDTO pessoa = pessoaDAL.findById(pessoaId);
        if (pessoa == null) {
            throw new IllegalArgumentException("Adotante não encontrado!");
        }

        return adocaoDAL.buscarAdocao(pessoa.getPessoa());
    }

    public AdocaoDTO getAdocao(String CPF)
    {
//        if(!cpfValidator.isCpfValido(CPF))
//            throw new IllegalArgumentException("CPF inválido!");

        PessoaCompletoDTO adotante = PessoaDAL.findByDoador(CPF);
        if(adotante == null)
            throw new IllegalArgumentException("Adotante não Localizado!");

        return adocaoDAL.buscarAdocao(adotante.getPessoa());
    }

}
