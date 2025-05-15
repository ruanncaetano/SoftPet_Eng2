package SoftPet.backend.service;

import SoftPet.backend.dal.ContatoDAL;
import SoftPet.backend.dal.DoadorDAL;
import SoftPet.backend.dal.EnderecoDAL;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.DoadorModel;
import SoftPet.backend.model.EnderecoModel;
import SoftPet.backend.util.cpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        ContatoModel novoContato = contatoDAL.addContato(new ContatoModel(contato.getTelefone()));
        EnderecoModel novoEndereco = enderecoDAL.addEndereco(new EnderecoModel(endereco.getCep(),endereco.getRua(),endereco.getNumero(),endereco.getBairro(),endereco.getCidade(),endereco.getUf(),endereco.getComplemento()));
        DoadorModel novoDoador = doadorDAL.addDoador(new DoadorModel(doador.getCpf(),doador.getNome(),doador.getStatus(),doador.getProfissao(),novoContato.getId(),novoEndereco,doador.getRg()));
        return novoDoador;
    }
}
