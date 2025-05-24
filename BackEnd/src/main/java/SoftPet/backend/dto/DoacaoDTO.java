package SoftPet.backend.dto;

import SoftPet.backend.model.DoacaoModel;
import SoftPet.backend.model.DoadorModel;

public class DoacaoDTO
{
    private DoacaoModel doacao;
    private DoadorModel doador;

    public DoacaoDTO()
    {

    }
    public DoacaoDTO(DoacaoModel doacao, DoadorModel doador)
    {
        this.doacao = doacao;
        this.doador = doador;
    }

    public DoacaoModel getDoacao() {
        return doacao;
    }
    public void setDoacao(DoacaoModel doacao) {
        this.doacao = doacao;
    }

    public DoadorModel getDoador() {
        return doador;
    }
    public void setDoador(DoadorModel doador) {
        this.doador = doador;
    }
}
