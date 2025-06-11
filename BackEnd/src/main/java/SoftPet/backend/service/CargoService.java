package SoftPet.backend.service;

import SoftPet.backend.dal.CargoDAL;
import SoftPet.backend.model.CargoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    @Autowired
    private CargoDAL cargoDAL;

    public CargoModel findById(Long id) {
        return cargoDAL.buscarPorId(id);
    }

    public CargoModel findByNome(String nome) {
        return cargoDAL.buscarPorNome(nome);
    }

    public List<CargoModel> getAll() {
        return cargoDAL.getAll();
    }

    public CargoModel create(CargoModel cargo) {
        Long id = cargoDAL.criar(cargo).getId();
        if (id != -1) {
            cargo.setId(id);
            return cargo;
        }
        return null;
    }

    public boolean update(CargoModel cargo) {
        return cargoDAL.update(cargo);
    }

    public boolean delete(Long id) {
        return cargoDAL.delete(id);
    }

    /**
     * Busca um cargo pelo nome e cria se não existir.
     * Retorna o objeto cargo com id preenchido.
     */
    public CargoModel buscarOuCriar(CargoModel cargo) {
        Long id = cargoDAL.buscarOuCriar(cargo);
        cargo.setId(id);
        return cargo;
    }
}
