package SoftPet.backend.dto;

import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.EnderecoModel;
import SoftPet.backend.model.PessoaModel;

public class AdotanteCompletoDTO {
    private PessoaModel adotante;
    private ContatoModel contato;
    private EnderecoModel endereco;

    public AdotanteCompletoDTO() {
    }

    public AdotanteCompletoDTO(PessoaModel adotante, ContatoModel contato, EnderecoModel endereco)
    {
        this.adotante = adotante;
        this.adotante = adotante;
        this.contato = contato;
        this.endereco = endereco;
    }

    public PessoaModel getPessoa()
    {
        return adotante;
    }
    public void setDoador(PessoaModel adotante) {
        this.adotante = adotante;
    }

    public ContatoModel getContato() {
        return contato;
    }
    public void setContato(ContatoModel contato)
    {
        this.contato = contato;
    }

    public EnderecoModel getEndereco()
    {
        return endereco;
    }
    public void setEndereco(EnderecoModel endereco)
    {
        this.endereco = endereco;
    }
}
