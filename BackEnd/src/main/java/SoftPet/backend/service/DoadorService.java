package SoftPet.backend.service;

import SoftPet.backend.dal.ContatoDAL;
import SoftPet.backend.dal.DoadorDAL;
import SoftPet.backend.dal.EnderecoDAL;
import SoftPet.backend.dto.DoadorCompletoDTO;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.DoadorModel;
import SoftPet.backend.model.EnderecoModel;
import SoftPet.backend.util.cpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SoftPet.backend.util.Validation;

import java.util.List;
import java.util.Optional;

@Service
public class DoadorService
{
    @Autowired
    private DoadorDAL doadorDAL;
    @Autowired
    private ContatoDAL contatoDAL;
    @Autowired
    private EnderecoDAL enderecoDAL;

    public DoadorModel addDoador(DoadorCompletoDTO doador) throws Exception
    {
        if(!cpfValidator.isCpfValido(doador.getDoador().getCpf()))
            throw new Exception("CPF inválido!");

        if(doadorDAL.findByDoador(doador.getDoador().getCpf()) != null)
            throw new Exception("Usuário já cadastrado!");

        if(!Validation.validarNomeCompleto(doador.getDoador().getNome()))
            throw new Exception("Digite o nome completo corretamente!");

        if(!Validation.validarTelefone(doador.getContato().getTelefone()))
            throw new Exception("Telefone inválido! Ex: (11) 91234-5678");

        if(!Validation.validarRG(doador.getDoador().getRg()))
            throw new Exception("RG inválido!");

        if(!Validation.validarCEP(doador.getEndereco().getCep()))
            throw new Exception("CEP inválido! Ex: 12345-678");

        ContatoModel novoContato = contatoDAL.addContato(doador.getContato());
        EnderecoModel novoEndereco = enderecoDAL.addEndereco(doador.getEndereco());

        DoadorModel novoDoador = doador.getDoador();
        novoDoador.setId_contato(novoContato.getId());
        novoDoador.setId_endereco(novoEndereco.getId());

        DoadorModel doadorFinal = doadorDAL.addDoador(novoDoador);
        return doadorFinal;
    }


    public DoadorCompletoDTO getDoadorCpf(String cpf)
    {
        if(doadorDAL.findByDoador(cpf) != null)
            return doadorDAL.findByDoador(cpf);
        return null;
    }

    public void updateDoador(String cpf, DoadorModel doador, ContatoModel contato, EnderecoModel endereco) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        if(!Validation.validarNomeCompleto(doador.getNome()))
            throw new Exception("Digite o nome completo corretamente!");

        if(!Validation.validarTelefone(contato.getTelefone()))
            throw new Exception("Telefone inválido! Ex: (11) 91234-5678");

        if(!Validation.validarRG(doador.getRg()))
            throw new Exception("RG inválido!");

        if(!Validation.validarCEP(endereco.getCep()))
            throw new Exception("CEP inválido! Ex: 12345-678");

        DoadorCompletoDTO doadorExistente = doadorDAL.findByDoador(cpf);
        if(doadorExistente == null)
            throw new Exception("Não existe esse usuário!");

        doador.setId_contato(doadorExistente.getContato().getId());
        doador.setId_endereco(doadorExistente.getEndereco().getId());

        doadorDAL.updateDoador(cpf,doador);
        ContatoModel contatoAtualizado = new ContatoModel();
        contatoAtualizado.setId(doador.getId_contato());
        contatoAtualizado.setTelefone(contato.getTelefone());
        contatoAtualizado.setEmail(contato.getEmail());

        contatoDAL.updateContato(contatoAtualizado);
        enderecoDAL.updateEndereco(doador.getId_endereco(),endereco);
    }

    public void deleteDoador(String cpf) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        DoadorCompletoDTO doadorDelete = doadorDAL.findByDoador(cpf);

        if(doadorDelete == null)
            throw new Exception("Não existe esse usuário!");

        if(!doadorDAL.deleteByDoador(cpf))
            throw new Exception("Erro ao deletar um doador!");

        contatoDAL.deleteByContato(doadorDelete.getContato().getId());
        enderecoDAL.deleteByEndereco(doadorDelete.getEndereco().getId());
    }

    public List<DoadorCompletoDTO> getAllDoador()
    {
        return doadorDAL.getAll();
    }
}