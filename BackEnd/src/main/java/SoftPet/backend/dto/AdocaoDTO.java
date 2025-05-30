package SoftPet.backend.dto;

import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;

import java.time.LocalDate;

public class AdocaoDTO {

    private AnimalModel animal;
    private PessoaModel pessoa;
    private AdocaoModel adocao;

    public AdocaoDTO() {}
    public AdocaoDTO(Long id,LocalDate adoDt, byte[] adoContratoes, AnimalModel animal, PessoaModel adotanteBusca) {
        this.adocao = new AdocaoModel(); // Instancia o objeto antes de usar
        this.adocao.setAdo_dt(adoDt);
        this.adocao.setAdo_contrato(adoContratoes);
        this.adocao.setAdo_cod(id);
        this.animal = animal;
        this.pessoa = adotanteBusca; // Usa o parâmetro correto
    }
    public AdocaoDTO(AdocaoModel adocao, AnimalModel animal, PessoaModel pessoa) {
        this.adocao = adocao;
        this.animal = animal;
        this.pessoa = pessoa;
    }

    public AnimalModel getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalModel animal) {
        this.animal = animal;
    }

    public PessoaModel getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaModel pessoa) {
        this.pessoa = pessoa;
    }

    public AdocaoModel getAdocao() {
        return adocao;
    }

    public void setAdocao(AdocaoModel adocao) {
        this.adocao = adocao;
    }
}
