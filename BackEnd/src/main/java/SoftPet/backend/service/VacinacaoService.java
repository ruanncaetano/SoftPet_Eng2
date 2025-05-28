package SoftPet.backend.service;

import SoftPet.backend.dal.AnimalDAL;
import SoftPet.backend.dal.AnimalXVacinacaoDAL;
import SoftPet.backend.dal.VacinaDAL;
import SoftPet.backend.dal.VoluntarioDAL;
import SoftPet.backend.dto.VacinacaoRequestDTO;
import SoftPet.backend.model.AnimalModel;
import SoftPet.backend.model.AnimalXVacinacaoModel;
import SoftPet.backend.model.VacinaModel;
import SoftPet.backend.model.VoluntarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VacinacaoService {

    @Autowired
    private VoluntarioDAL voluntarioDAL; // Espera-se que findById(Long id) e VoluntarioModel.getId() usem Long
    @Autowired
    private AnimalDAL animalDAL;         // Espera-se que buscarPorCod(Long cod) e AnimalModel.getCod() usem Long
    @Autowired
    private VacinaDAL vacinaDAL;         // Espera-se que buscarPorCod(Long cod) e VacinaModel.getCod() usem Long
    @Autowired
    private AnimalXVacinacaoDAL animalXVacinacaoDAL;

    public AnimalXVacinacaoModel efetuarVacinacao(VacinacaoRequestDTO request) throws Exception {

        VoluntarioModel voluntarioRegistrando = voluntarioDAL.findById(request.getVoluntarioRegistrandoCod());
        if (voluntarioRegistrando == null) {
            throw new Exception("Voluntário (registrando) com código " + request.getVoluntarioRegistrandoCod() + " não encontrado.");
        }

        AnimalModel animal = animalDAL.buscarPorCod(request.getAnimalCod());
        if (animal == null) {
            throw new Exception("Animal com código " + request.getAnimalCod() + " não encontrado.");
        }

        VacinaModel vacina = vacinaDAL.buscarPorCod(request.getVacinaCod());
        if (vacina == null) {
            throw new Exception("Vacina com código " + request.getVacinaCod() + " não encontrada.");
        }

        VoluntarioModel voluntarioAplicador = voluntarioDAL.findById(request.getVoluntarioAplicadorCod());
        if (voluntarioAplicador == null) {
            throw new Exception("Voluntário (aplicador) com código " + request.getVoluntarioAplicadorCod() + " não encontrado.");
        }

        if (request.getDataAplicacaoAnimal() == null) {
            throw new Exception("A data de aplicação da vacina no animal é obrigatória.");
        }

        AnimalXVacinacaoModel aplicacao = new AnimalXVacinacaoModel();

        // Atribuições diretas, assumindo que getCod()/getId() dos modelos retornam Long
        aplicacao.setAnimalCod(animal.getCod());
        aplicacao.setVacinaCod(vacina.getCod());
        aplicacao.setDoseAplicadaNumero(request.getDoseNumericaAplicada());
        aplicacao.setDataAplicacaoEfetiva(request.getDataAplicacaoAnimal());
        aplicacao.setVoluntarioAplicadorCod(voluntarioAplicador.getId());

        String observacaoParaRegistro = construirObservacaoRelatorio(
                animal, vacina, voluntarioAplicador, voluntarioRegistrando, request
        );
        aplicacao.setObservacao(observacaoParaRegistro);

        boolean sucesso = animalXVacinacaoDAL.registrarAplicacaoVacina(aplicacao); // O DAL receberá o modelo com Longs
        if (!sucesso) {
            throw new Exception("Falha ao registrar a aplicação da vacina.");
        }
        return aplicacao;
    }

    private String construirObservacaoRelatorio(AnimalModel animal, VacinaModel vacina,
                                                VoluntarioModel aplicador, VoluntarioModel registrador,
                                                VacinacaoRequestDTO request) {
        StringBuilder sb = new StringBuilder();
        if (request.getObservacaoDaAplicacao() != null && !request.getObservacaoDaAplicacao().trim().isEmpty()) {
            sb.append("Observação da Aplicação: ").append(request.getObservacaoDaAplicacao()).append(". ");
        }
        sb.append(String.format("Relatório: Vacina '%s' (Cod: %d) aplicada no animal '%s' (Cod: %d) ",
                vacina.getNome(), vacina.getCod(),
                animal.getNome(), animal.getCod()));
        sb.append(String.format("em %s. ", request.getDataAplicacaoAnimal().toString()));
        if (request.getDoseNumericaAplicada() != null) {
            sb.append(String.format("Dose Aplicada: %d. ", request.getDoseNumericaAplicada()));
        }
        sb.append(String.format("Tipo Dose Padrão da Vacina: '%s'. ", vacina.getTipoDosePadrao()));
        sb.append(String.format("Aplicada por Vol/Vet: '%s' (Cod: %d). ",
                aplicador.getNome(), aplicador.getId()));
        sb.append(String.format("Registrado por Vol: '%s' (Cod: %d).",
                registrador.getNome(), registrador.getId()));
        return sb.toString();
    }
}