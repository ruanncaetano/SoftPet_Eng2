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
    public AdocaoDTO(LocalDate adoDt, byte[] adoContratoes, AnimalModel animal, PessoaModel adotanteBusca) {}
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
