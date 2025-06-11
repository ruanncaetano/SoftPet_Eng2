package SoftPet.backend.service;

import SoftPet.backend.dal.AnimalDAL;
import SoftPet.backend.model.AnimalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {
    @Autowired

    private AnimalDAL animalDAL;


    // Cadastrar novo animal
    public AnimalModel cadastrarAnimal(AnimalModel animal) {
        AnimalModel novoAnimal = new AnimalModel();
        novoAnimal.setNome(animal.getNome());
        novoAnimal.setIdade(animal.getIdade());
        novoAnimal.setTipo(animal.getTipo());
        novoAnimal.setSexo(animal.getSexo());
        novoAnimal.setPorte(animal.getPorte());
        novoAnimal.setRaca(animal.getRaca());
        novoAnimal.setPelagem(animal.getPelagem());
        novoAnimal.setPeso(animal.getPeso());
        novoAnimal.setBaia(animal.getBaia());
        novoAnimal.setDt_resgate(animal.getDt_resgate());
        novoAnimal.setDisp_adocao(animal.getDisp_adocao());
        novoAnimal.setAtivo(animal.getAtivo());
        novoAnimal.setCastrado(animal.getCastrado());
        novoAnimal.setObservacao(animal.getObservacao());
        novoAnimal.setFoto(animal.getFoto());
// poderia ter utilizado o construtor
        return animalDAL.Adicionar(novoAnimal);
    }

    public List<AnimalModel> buscarAnimais(String nome, String tipo, String porte, String sexo, boolean status) {
        return animalDAL.consultarComFiltros(nome, tipo, porte, sexo, status);
    }

    public List<AnimalModel> listarTodos() {
        return animalDAL.listarTodos();
    }


    public byte[] getFoto(Long id) {
        AnimalModel animal = animalDAL.buscarIdComFoto(id);
        if (animal != null && animal.getFoto() != null) {
            return animal.getFoto();
        }
        return null;
    }

    public AnimalModel buscarPorCod(int cod) {
        AnimalModel animal = animalDAL.buscarPorCod(cod);

        if (animal != null && animal.getAtivo() && animal.getDisp_adocao()) {
            return animal;
        }
        return null;
    }

    public AnimalModel atualizarAnimal(AnimalModel animal) {
        animalDAL.atualizar(animal);
        return animalDAL.buscarPorCod(animal.getCod());
    }

    // Buscar animal por ID
//    public Optional<AnimalModel> buscarPorId(int cod) {
//        return Optional.ofNullable(animalDAL.findById(cod));
//    }
//
//    // Listar todos os animais
//    public List<AnimalModel> listarTodos() {
//        return animalDAL.getAll();
//    }
//
//    // Atualizar status de adoção
//    public void atualizarStatusAdocao(int cod, boolean novoStatus) {
//        animalDAL.updateAdoptionStatus(cod, novoStatus);
//    }
//
//    // Remover animal
//    public boolean removerAnimal(int cod) {
//        return animalDAL.delete(cod);
//    }
//
//    // Buscar animais disponíveis para adoção
//    public List<AnimalModel> buscarDisponiveisAdocao() {
//        return animalDAL.findByAdoptionStatus(true);
//    }
//
//    // Buscar animais por tipo (cachorro, gato, etc.)
//    public List<AnimalModel> buscarPorTipo(String tipo) {
//        return animalDAL.findByType(tipo);
//    }
}