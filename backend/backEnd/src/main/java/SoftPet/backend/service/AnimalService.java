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
        novoAnimal.setDisp_adocao(animal.isDisp_adocao());
        novoAnimal.setFoto(animal.getFoto());

        return animalDAL.Adicionar(novoAnimal);
    }

    public List<AnimalModel> buscarAnimais(String nome, String tipo, String porte, String sexo, boolean status) {
        return animalDAL.consultarComFiltros(nome, tipo, porte, sexo, status);
    }

//    public byte[] getFoto(Long id) {
//        return animalDAL.buscarId(id);
//    }
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
//    // Atualizar informações do animal
//    public AnimalModel atualizarAnimal(AnimalModel animal) {
//        return animalDAL.update(animal);
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