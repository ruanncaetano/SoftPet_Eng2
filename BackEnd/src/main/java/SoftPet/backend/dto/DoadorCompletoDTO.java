package SoftPet.backend.dto;

import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.DoadorModel;
import SoftPet.backend.model.EnderecoModel;

public class DoadorCompletoDTO
{

    private DoadorModel doador;
    private ContatoModel contato;
    private EnderecoModel endereco;

    public DoadorCompletoDTO() {
    }

    public DoadorCompletoDTO(DoadorModel doador, ContatoModel contato, EnderecoModel endereco)
    {
        this.doador = doador;
        this.contato = contato;
        this.endereco = endereco;
    }

    public DoadorModel getDoador()
    {
        return doador;
    }
    public void setDoador(DoadorModel doador) {
        this.doador = doador;
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
