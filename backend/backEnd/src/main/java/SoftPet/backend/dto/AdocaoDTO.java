package SoftPet.backend.dto;

import SoftPet.backend.model.AdocaoModel;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.PessoaModel;

public class AdocaoDTO {
    private AdocaoModel adocao;
    private AnimalModel animal;
    private PessoaModel pessoa;

    public AdocaoDTO() {}
    public AdocaoDTO(AdocaoModel adocao, AnimalModel animal) {
        this.adocao = adocao;
        this.animal = animal;
    }

    public AdocaoModel getAdocao() {
        return adocao;
    }

    public void setAdocao(AdocaoModel adocao) {
        this.adocao = adocao;
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
}
