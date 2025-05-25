package SoftPet.backend.service;

import SoftPet.backend.dal.ContatoDAL;
import SoftPet.backend.dal.AdotanteDAL;
import SoftPet.backend.dal.EnderecoDAL;
import SoftPet.backend.dto.AdotanteCompletoDTO;
import SoftPet.backend.model.ContatoModel;
import SoftPet.backend.model.AdotanteModel;
import SoftPet.backend.model.EnderecoModel;
import SoftPet.backend.util.cpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SoftPet.backend.util.Validation;

import java.util.List;

@Service
public class AdotanteService
{
    @Autowired
    private AdotanteDAL adotanteDAL;
    @Autowired
    private ContatoDAL contatoDAL;
    @Autowired
    private EnderecoDAL enderecoDAL;

    public AdotanteModel addAdotante(AdotanteCompletoDTO adotante) throws Exception
    {
        if(!cpfValidator.isCpfValido(adotante.getAdotante().getCpf()))
            throw new Exception("CPF inválido!");

        if(adotanteDAL.findByAdotante(adotante.getAdotante().getCpf()) != null)
            throw new Exception("Usuário já cadastrado!");

        if(!Validation.validarNomeCompleto(adotante.getAdotante().getNome()))
            throw new Exception("Digite o nome completo corretamente!");

        if(!Validation.validarTelefone(adotante.getContato().getTelefone()))
            throw new Exception("Telefone inválido! Ex: (11) 91234-5678");

        if(!Validation.validarRG(adotante.getAdotante().getRg()))
            throw new Exception("RG inválido!");

        if(!Validation.validarCEP(adotante.getEndereco().getCep()))
            throw new Exception("CEP inválido! Ex: 12345-678");

        ContatoModel novoContato = contatoDAL.addContato(adotante.getContato());
        EnderecoModel novoEndereco = enderecoDAL.addEndereco(adotante.getEndereco());

        AdotanteModel novoAdotante = adotante.getAdotante();
        novoAdotante.setId_contato(novoContato.getId());
        novoAdotante.setId_endereco(novoEndereco.getId());

        AdotanteModel adotanteFinal = adotanteDAL.addAdotante(novoAdotante);
        return adotanteFinal;
    }


    public AdotanteCompletoDTO getAdotanteCpf(String cpf)
    {
        if(adotanteDAL.findByAdotante(cpf) != null)
            return adotanteDAL.findByAdotante(cpf);
        return null;
    }

    public void updateAdotante(String cpf, AdotanteModel addotante, ContatoModel contato, EnderecoModel endereco) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        if(!Validation.validarNomeCompleto(addotante.getNome()))
            throw new Exception("Digite o nome completo corretamente!");

        if(!Validation.validarTelefone(contato.getTelefone()))
            throw new Exception("Telefone inválido! Ex: (11) 91234-5678");

        if(!Validation.validarRG(addotante.getRg()))
            throw new Exception("RG inválido!");

        if(!Validation.validarCEP(endereco.getCep()))
            throw new Exception("CEP inválido! Ex: 12345-678");

        AdotanteCompletoDTO adotanteExistente = adotanteDAL.findByAdotante(cpf);
        if(adotanteExistente == null)
            throw new Exception("Não existe esse usuário!");

        addotante.setId_contato(adotanteExistente.getContato().getId());
        addotante.setId_endereco(adotanteExistente.getEndereco().getId());

        adotanteDAL.updateAdotante(cpf, addotante);
        contatoDAL.updateContato(addotante.getId_contato(),contato.getTelefone());
        enderecoDAL.updateEndereco(addotante.getId_endereco(),endereco);
    }

    public void deleteAdotante(String cpf) throws Exception
    {
        if(!cpfValidator.isCpfValido(cpf))
            throw new Exception("CPF inválido!");

        AdotanteCompletoDTO adotanteDelete = adotanteDAL.findByAdotante(cpf);

        if(adotanteDelete == null)
            throw new Exception("Não existe esse usuário!");

        if(!adotanteDAL.deleteByAdotante(cpf))
            throw new Exception("Erro ao deletar um adotante!");

        contatoDAL.deleteByContato(adotanteDelete.getContato().getId());
        enderecoDAL.deleteByEndereco(adotanteDelete.getEndereco().getId());
    }

    public List<AdotanteCompletoDTO> getAllAdotante()
    {
        return adotanteDAL.getAll();
    }
}