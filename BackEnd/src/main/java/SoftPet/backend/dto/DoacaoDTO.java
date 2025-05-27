package SoftPet.backend.dto;

import SoftPet.backend.model.DoacaoModel;
import SoftPet.backend.model.PessoaModel;

public class DoacaoDTO
{
    private DoacaoModel doacao;
    private PessoaModel doador;

    public DoacaoDTO()
    {

    }
    public DoacaoDTO(DoacaoModel doacao, PessoaModel doador)
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

    public PessoaModel getDoador() {
        return doador;
    }
    public void setDoador(PessoaModel doador) {
        this.doador = doador;
    }
}
