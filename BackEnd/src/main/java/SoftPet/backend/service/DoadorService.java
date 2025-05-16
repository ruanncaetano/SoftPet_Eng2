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
        if (!cpfValidator.isCpfValido(doador.getDoador().getCpf()))
            throw new Exception("CPF inválido!");

        if (doadorDAL.findByDoador(doador.getDoador().getCpf()) != null)
            throw new Exception("Usuário já cadastrado!");

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

        DoadorCompletoDTO doadorExistente = doadorDAL.findByDoador(cpf);
        if(doadorExistente == null)
            throw new Exception("Não existe esse usuário!");

        doador.setId_contato(doadorExistente.getContato().getId());
        doador.setId_endereco(doadorExistente.getEndereco().getId());

        doadorDAL.updateDoador(cpf,doador);
        contatoDAL.updateContato(doador.getId_contato(),contato.getTelefone());
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