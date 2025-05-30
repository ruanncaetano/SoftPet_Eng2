package SoftPet.backend.service;

import SoftPet.backend.dal.AnimalDAL;
import SoftPet.backend.dal.AnimalXVacinacaoDAL;
import SoftPet.backend.dal.VacinaDAL;
import SoftPet.backend.dal.VoluntarioDAL; // Importação adicionada
import SoftPet.backend.dto.VacinaRequestDTO;
import SoftPet.backend.dto.VacinacaoRequestDTO; // Importação adicionada
import SoftPet.backend.model.AnimalModel;       // Importação adicionada
import SoftPet.backend.model.AnimalXVacinacaoModel; // Importação adicionada
import SoftPet.backend.model.VacinaModel;
import SoftPet.backend.model.VoluntarioModel; // Importação adicionada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date; // Importação adicionada
import java.util.List;

@Service
public class VacinaService {

    private final VacinaDAL vacinaDAL;
    // Adicionando as outras dependências DAL necessárias
    private final AnimalDAL animalDAL;
    private final VoluntarioDAL voluntarioDAL;
    private final AnimalXVacinacaoDAL animalXVacinacaoDAL;

    @Autowired // Injeção de dependência via construtor atualizada
    public VacinaService(VacinaDAL vacinaDAL, AnimalDAL animalDAL, VoluntarioDAL voluntarioDAL, AnimalXVacinacaoDAL animalXVacinacaoDAL) {
        this.vacinaDAL = vacinaDAL;
        this.animalDAL = animalDAL; // Atribuição adicionada
        this.voluntarioDAL = voluntarioDAL; // Atribuição adicionada
        this.animalXVacinacaoDAL = animalXVacinacaoDAL; // Atribuição adicionada
    }

    /**
     * Adiciona uma nova vacina (entidade) ao sistema.
     * dto: DTO contendo os dados da vacina a ser criada.
     * Retorna o VacinaModel da vacina criada, incluindo seu ID gerado.
     * Lança IllegalArgumentException se os dados obrigatórios não forem fornecidos.
     */
    public VacinaModel adicionarVacina(VacinaRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da vacina é obrigatório.");
        }
        if (dto.getDataReferenciaLote() == null) {
            throw new IllegalArgumentException("A data de referência do lote é obrigatória.");
        }
        if (dto.getTipoDosePadrao() == null || dto.getTipoDosePadrao().trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo/dose padrão da vacina é obrigatório.");
        }

        VacinaModel novaVacina = new VacinaModel();
        novaVacina.setNome(dto.getNome());
        novaVacina.setDescricao(dto.getDescricao());
        novaVacina.setDataReferenciaLote(dto.getDataReferenciaLote());
        novaVacina.setTipoDosePadrao(dto.getTipoDosePadrao());

        return vacinaDAL.criar(novaVacina);
    }

    /**
     * Busca uma vacina pelo seu ID.
     * id: O ID (VA_COD) da vacina.
     * Retorna o VacinaModel encontrado.
     * Lança RuntimeException se não encontrada.
     */
    public VacinaModel buscarVacinaPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da vacina para busca não pode ser nulo.");
        }
        VacinaModel vacina = vacinaDAL.buscarPorCod(id);
        if (vacina == null) {
            throw new RuntimeException("Vacina com ID " + id + " não encontrada.");
        }
        return vacina;
    }

    /**
     * Lista todas as vacinas cadastradas.
     * Retorna uma lista de VacinaModel.
     */
    public List<VacinaModel> listarTodasVacinas() {
        return vacinaDAL.listarTodas();
    }

    /**
     * Atualiza os dados de uma vacina existente.
     * id: O ID da vacina a ser atualizada.
     * dto: DTO contendo os novos dados para a vacina.
     * Retorna o VacinaModel atualizado.
     * Lança RuntimeException se a vacina não for encontrada ou IllegalArgumentException para dados inválidos.
     */
    public VacinaModel atualizarVacina(Long id, VacinaRequestDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da vacina para atualização não pode ser nulo.");
        }
        VacinaModel vacinaExistente = buscarVacinaPorId(id);

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da vacina é obrigatório para atualização.");
        }
        if (dto.getDataReferenciaLote() == null) {
            throw new IllegalArgumentException("A data de referência do lote é obrigatória para atualização.");
        }
        if (dto.getTipoDosePadrao() == null || dto.getTipoDosePadrao().trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo/dose padrão é obrigatório para atualização.");
        }

        vacinaExistente.setNome(dto.getNome());
        vacinaExistente.setDescricao(dto.getDescricao());
        vacinaExistente.setDataReferenciaLote(dto.getDataReferenciaLote());
        vacinaExistente.setTipoDosePadrao(dto.getTipoDosePadrao());

        return vacinaDAL.atualizar(vacinaExistente);
    }

    /**
     * Deleta uma vacina pelo seu ID.
     * id: O ID da vacina a ser deletada.
     * Lança RuntimeException se a vacina não for encontrada ou se não puder ser deletada.
     */
    public void deletarVacina(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da vacina para deleção não pode ser nulo.");
        }
        buscarVacinaPorId(id); // Garante que existe

        boolean deletado = vacinaDAL.deletar(id);
        if (!deletado) {
            // O DAL.deletar já lança exceção se não puder deletar por dependência.
            // Esta condição pode ser para outros casos de falha não cobertos por exceção no DAL.
            System.err.println("Vacina com ID " + id + " não foi deletada, mas existia. Verifique a lógica do DAL.deletar().");
            // throw new RuntimeException("Falha ao deletar vacina com ID " + id + ", mas a vacina foi encontrada inicialmente.");
        }
    }

    // --- Método para registrar a APLICAÇÃO de uma vacina ---
    /**
     * Processa o registro da aplicação de uma vacina em um animal.
     * request: DTO contendo os dados para o registro da vacinação (VacinacaoRequestDTO).
     * Retorna o modelo AnimalXVacinacaoModel representando o registro salvo.
     * Lança Exception em caso de falhas de validação ou erros de persistência.
     */
    public AnimalXVacinacaoModel efetuarVacinacao(VacinacaoRequestDTO request) throws Exception {
        // Validações dos IDs e da data de aplicação
        if (request.getVoluntarioRegistrandoCod() == null) {
            throw new IllegalArgumentException("Código do voluntário registrando não pode ser nulo.");
        }
        if (request.getAnimalCod() == null) {
            throw new IllegalArgumentException("Código do animal não pode ser nulo.");
        }
        if (request.getVacinaCod() == null) {
            throw new IllegalArgumentException("Código da vacina não pode ser nulo.");
        }
        if (request.getVoluntarioAplicadorCod() == null) {
            throw new IllegalArgumentException("Código do voluntário aplicador não pode ser nulo.");
        }
        if (request.getDataAplicacaoAnimal() == null) {
            throw new Exception("A data de aplicação da vacina no animal é obrigatória.");
        }

        VoluntarioModel voluntarioRegistrando = voluntarioDAL.findById(request.getVoluntarioRegistrandoCod());
        if (voluntarioRegistrando == null) {
            throw new Exception("Voluntário (registrando) com código " + request.getVoluntarioRegistrandoCod() + " não encontrado.");
        }

        // Usando o método buscarPorCod(Long cod) do seu AnimalDAL
        AnimalModel animal = animalDAL.buscarPorCod(request.getAnimalCod());
        if (animal == null) {
            throw new Exception("Animal com código " + request.getAnimalCod() + " não encontrado.");
        }

        // Busca a VacinaModel (entidade vacina) usando o vacinaCod do request
        VacinaModel vacinaEntidade = this.buscarVacinaPorId(request.getVacinaCod()); // Reutiliza o método de busca desta classe
        // A linha acima já lança exceção se a vacina não for encontrada.

        VoluntarioModel voluntarioAplicador = voluntarioDAL.findById(request.getVoluntarioAplicadorCod());
        if (voluntarioAplicador == null) {
            throw new Exception("Voluntário (aplicador) com código " + request.getVoluntarioAplicadorCod() + " não encontrado.");
        }

        AnimalXVacinacaoModel aplicacao = new AnimalXVacinacaoModel();
        // Assumindo que AnimalModel.getCod() retorna long, e VacinaModel.getCod() retorna Long
        aplicacao.setAnimalCod(animal.getCod()); // Se AnimalModel.getCod() retorna long, será auto-boxed para Long
        aplicacao.setVacinaCod(vacinaEntidade.getCod());
        aplicacao.setDoseNumerica(request.getDoseNumericaAplicada());
        aplicacao.setDataAplicacao(request.getDataAplicacaoAnimal());
        aplicacao.setVoluntarioAplicadorCod(voluntarioAplicador.getId()); // Assumindo que VoluntarioModel.getId() retorna Long

        String observacaoParaRegistro = construirObservacaoRelatorioParaAplicacao(
                animal, vacinaEntidade, voluntarioAplicador, voluntarioRegistrando, request
        );
        aplicacao.setObservacao(observacaoParaRegistro);

        return animalXVacinacaoDAL.registrarAplicacaoVacina(aplicacao);
    }

    private String construirObservacaoRelatorioParaAplicacao(AnimalModel animal, VacinaModel vacina,
                                                             VoluntarioModel aplicador, VoluntarioModel registrador,
                                                             VacinacaoRequestDTO request) {
        StringBuilder sb = new StringBuilder();
        if (request.getObservacaoDaAplicacao() != null && !request.getObservacaoDaAplicacao().trim().isEmpty()) {
            sb.append("Observação da Aplicação: ").append(request.getObservacaoDaAplicacao()).append(". ");
        }
        // Assumindo que getCod() e getId() retornam Long (ou tipos compatíveis com %d em String.format)
        sb.append(String.format("Relatório: Vacina '%s' (Cod: %d) aplicada no animal '%s' (Cod: %d) ",
                vacina.getNome(), vacina.getCod(),
                animal.getNome(), animal.getCod()));
        sb.append(String.format("em %s. ", request.getDataAplicacaoAnimal().toString()));
        if (request.getDoseNumericaAplicada() != null) {
            sb.append(String.format("Dose Aplicada: %s. ", request.getDoseNumericaAplicada().toString())); // Usar toString() para Double
        }
        sb.append(String.format("Tipo Dose Padrão da Vacina: '%s'. ", vacina.getTipoDosePadrao()));
        sb.append(String.format("Aplicada por Vol/Vet: '%s' (Cod: %d). ",
                aplicador.getNome(), aplicador.getId()));
        sb.append(String.format("Registrado por Vol: '%s' (Cod: %d).",
                registrador.getNome(), registrador.getId()));
        return sb.toString();
    }
}
