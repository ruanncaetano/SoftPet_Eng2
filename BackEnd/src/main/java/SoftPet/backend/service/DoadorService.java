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

    public DoadorModel addDoador(DoadorModel doador, ContatoModel contato, EnderecoModel endereco) throws Exception
    {
        if(!cpfValidator.isCpfValido(doador.getCpf()))
            throw new Exception("CPF inválido!");

        if(doadorDAL.findByDoador(doador.getCpf()) != null)
            throw new Exception("Usuário já cadastrado!");

        ContatoModel novoContato = contatoDAL.addContato(contato);
        EnderecoModel novoEndereco = enderecoDAL.addEndereco(endereco);

        doador.setId_contato(novoContato.getId());
        doador.setId_endereco(novoEndereco.getId());

        DoadorModel novoDoador = doadorDAL.addDoador(doador);
        return novoDoador;
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

        if(doadorDAL.findByDoador(cpf) == null)
            throw new Exception("Não existe esse usuário!");

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