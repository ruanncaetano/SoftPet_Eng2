package SoftPet.backend.dto;

import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.AdotanteModel;
import SoftPet.backend.model.EnderecoModel;

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

    public SoftPet.backend.model.ContatoModel getContato() {
        return contato;
    }
    public void setContato(ContatoModel contato)
    {
        this.contato = contato;
    }

    public SoftPet.backend.model.EnderecoModel getEndereco()
    {
        return endereco;
    }
    public void setEndereco(EnderecoModel endereco)
    {
        this.endereco = endereco;
    }
}
