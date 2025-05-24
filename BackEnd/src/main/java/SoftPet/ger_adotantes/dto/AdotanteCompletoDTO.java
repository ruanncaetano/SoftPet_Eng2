package SoftPet.ger_adotantes.dto;

import SoftPet.ger_adotantes.model.ContatoModel;
import SoftPet.ger_adotantes.model.AdotanteModel;
import SoftPet.ger_adotantes.model.EnderecoModel;

public class AdotanteCompletoDTO
{

    private AdotanteModel adotante;
    private ContatoModel contato;
    private EnderecoModel endereco;

    public AdotanteCompletoDTO() {
    }

    public AdotanteCompletoDTO(AdotanteModel adotante, ContatoModel contato, EnderecoModel endereco)
    {
        this.adotante = adotante;
        this.contato = contato;
        this.endereco = endereco;
    }

    public AdotanteModel getAdotante()
    {
        return adotante;
    }
    public void setAdotante(AdotanteModel adotante) {
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
