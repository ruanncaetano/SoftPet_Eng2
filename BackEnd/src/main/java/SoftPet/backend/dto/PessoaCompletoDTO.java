package SoftPet.backend.dto;

import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.PessoaModel;
import SoftPet.backend.model.EnderecoModel;

public class PessoaCompletoDTO
{

    private PessoaModel pessoa;
    private ContatoModel contato;
    private EnderecoModel endereco;

    public PessoaCompletoDTO() {
    }

    public PessoaCompletoDTO(PessoaModel pessoa, ContatoModel contato, EnderecoModel endereco)
    {
        this.pessoa = pessoa;
        this.contato = contato;
        this.endereco = endereco;
    }

    public PessoaModel getPessoa()
    {
        return pessoa;
    }
    public void setPessoa(PessoaModel pessoa) {
        this.pessoa = pessoa;
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
