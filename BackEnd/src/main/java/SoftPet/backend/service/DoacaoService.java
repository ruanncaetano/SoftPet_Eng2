package SoftPet.backend.service;

import SoftPet.backend.dal.DoacaoDAL;
import SoftPet.backend.dal.PessoaDAL;
import SoftPet.backend.dto.DoacaoDTO;
import SoftPet.backend.dto.PessoaCompletoDTO;
import SoftPet.backend.model.DoacaoModel;
import SoftPet.backend.util.Validation;
import SoftPet.backend.util.cpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoacaoService
{
    @Autowired
    private DoacaoDAL doacaoDAL;
    @Autowired
    private PessoaDAL pessoaDAL;

    public DoacaoModel addDoacao(DoacaoDTO doacaoDTO) throws Exception
    {
        PessoaCompletoDTO doador = pessoaDAL.findById(doacaoDTO.getDoador().getId());

        if(doador == null)
            throw new IllegalArgumentException("Doador não encontrado!");

        if(!cpfValidator.isCpfValido(doador.getPessoa().getCpf()))
            throw new IllegalArgumentException("CPF inválido!");

        if(!Validation.numNegativo(doacaoDTO.getDoacao().getQtde()))
            throw new IllegalArgumentException("Quantidade do produto inválida!");

        if(!Validation.validarStringDocao(doacaoDTO.getDoacao().getTipo()))
            throw new IllegalArgumentException("Tipo do produto inválido!");

        if(!Validation.validarStringDocao(doacaoDTO.getDoacao().getNome()))
            throw new IllegalArgumentException("Nome do produto inválido!");

        if(!Validation.isDataValidade(doacaoDTO.getDoacao().getDataValidade()))
            throw new IllegalArgumentException("Data de validade invalida!");

        DoacaoModel novaDoacao = doacaoDTO.getDoacao();
        novaDoacao.setId_doador(doador.getPessoa().getId());

        return doacaoDAL.addDoacao(novaDoacao);
    }

    public DoacaoDTO getDoacao(Long id)
    {
        if(doacaoDAL.findByDoacao(id) != null)
            return doacaoDAL.findByDoacao(id);
        return null;
    }

    public void updateDoacao(Long id, DoacaoModel doacao) throws Exception
    {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("ID da doação inválido!");

        if(!Validation.validarStringDocao(doacao.getTipo()))
            throw new IllegalArgumentException("Tipo do produto inválido!");

        if(!Validation.validarStringDocao(doacao.getNome()))
            throw new IllegalArgumentException("Nome do produto inválido!");

        if(!Validation.numNegativo(doacao.getQtde()))
            throw new IllegalArgumentException("Quantidade da doação inválida!");

        if(!Validation.isDataValidade(doacao.getDataValidade()))
            throw new IllegalArgumentException("Data de validade invalida!");

        DoacaoDTO doacaoExistente = doacaoDAL.findByDoacao(id);
        if(doacaoExistente == null)
            throw new Exception("Não existe uma doação com esse ID!");


        doacao.setId(id);
        doacao.setId_doador(doacaoExistente.getDoador().getId());

        doacaoDAL.updateDoacao(doacao);
    }

    public void deleteDoacao(Long id) throws Exception
    {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("ID da doação inválido!");

        DoacaoDTO doacaoDelete = doacaoDAL.findByDoacao(id);

        if(doacaoDelete == null)
            throw new Exception("Não existe uma doação com esse ID!");

        if(!doacaoDAL.deleteByDoacao(id))
            throw new Exception("Erro ao deletar a doação!");
    }

    public List<DoacaoDTO> getAllDoacao()
    {
        return doacaoDAL.getAllDoacoes();
    }

    public void consumirDoacao(Long id, int qtdeConsumida) throws Exception
    {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("ID da doação inválido!");

        if(!Validation.numNegativo(qtdeConsumida))
            throw new IllegalArgumentException("Quantidade da doação inválida!");

        DoacaoDTO doacaoExistente = doacaoDAL.findByDoacao(id);
        if(doacaoExistente == null)
            throw new Exception("Não existe uma doação com esse ID!");

        int estoqueAtual = doacaoExistente.getDoacao().getQtde();

        if(qtdeConsumida > estoqueAtual)
            throw new IllegalArgumentException("Quantidade a consumir maior que o estoque atual!");

        int novoEstoque = estoqueAtual - qtdeConsumida;
        DoacaoModel doacaoAtualizada = doacaoExistente.getDoacao();
        doacaoAtualizada.setQtde(novoEstoque);

        doacaoDAL.updateDoacao(doacaoAtualizada);
    }
}