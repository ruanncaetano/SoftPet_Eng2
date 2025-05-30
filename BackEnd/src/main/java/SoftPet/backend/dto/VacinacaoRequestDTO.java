package SoftPet.backend.dto;

import java.util.Date;
// Se for usar anotações de validação do Spring, descomente os imports abaixo
// e adicione a dependência 'spring-boot-starter-validation' ao seu pom.xml ou build.gradle.
// import jakarta.validation.constraints.NotNull;  // Para Spring Boot 3+
// import jakarta.validation.constraints.PastOrPresent; // Para Spring Boot 3+
// import jakarta.validation.constraints.Positive; // Para Spring Boot 3+
// Se estiver usando Spring Boot 2.x, seria javax.validation.constraints.*

public class VacinacaoRequestDTO {

    // @NotNull(message = "O código do voluntário que registra é obrigatório.")
    private Long voluntarioRegistrandoCod; // Quem está inserindo o registro no sistema

    // @NotNull(message = "O código do animal é obrigatório.")
    // @Positive(message = "O código do animal deve ser um número positivo.")
    private Long animalCod;

    // @NotNull(message = "O código da vacina é obrigatório.")
    // @Positive(message = "O código da vacina deve ser um número positivo.")
    private Long vacinaCod;

    // @NotNull(message = "O código do voluntário aplicador é obrigatório.")
    // @Positive(message = "O código do voluntário aplicador deve ser um número positivo.")
    private Long voluntarioAplicadorCod; // Quem efetivamente aplicou a vacina

    // @NotNull(message = "A data de aplicação é obrigatória.")
    // @PastOrPresent(message = "A data de aplicação não pode ser no futuro.")
    private Date dataAplicacaoAnimal; // Data em que a vacina foi aplicada no animal

    // @Positive(message = "A dose numérica deve ser positiva, se informada.")
    private Double doseNumericaAplicada; // Dose numérica (ex: 0.5, 1.0). Pode ser nulo.

    private String observacaoDaAplicacao; // Observações sobre a aplicação

    /**
     * Construtor padrão.
     */
    public VacinacaoRequestDTO() {
    }

    /**
     * Construtor com todos os campos.
     * voluntarioRegistrandoCod: ID do voluntário que está fazendo o registro.
     * animalCod: ID do animal que recebeu a vacina.
     * vacinaCod: ID da vacina aplicada.
     * voluntarioAplicadorCod: ID do voluntário/veterinário que aplicou a vacina.
     * dataAplicacaoAnimal: Data da aplicação da vacina no animal.
     * doseNumericaAplicada: Dose numérica da vacina.
     * observacaoDaAplicacao: Observações ou motivo da vacinação.
     */
    public VacinacaoRequestDTO(Long voluntarioRegistrandoCod, Long animalCod, Long vacinaCod,
                               Long voluntarioAplicadorCod, Date dataAplicacaoAnimal,
                               Double doseNumericaAplicada, String observacaoDaAplicacao) {
        this.voluntarioRegistrandoCod = voluntarioRegistrandoCod;
        this.animalCod = animalCod;
        this.vacinaCod = vacinaCod;
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
        this.dataAplicacaoAnimal = dataAplicacaoAnimal;
        this.doseNumericaAplicada = doseNumericaAplicada;
        this.observacaoDaAplicacao = observacaoDaAplicacao;
    }

    // Getters e Setters

    public Long getVoluntarioRegistrandoCod() {
        return voluntarioRegistrandoCod;
    }

    public void setVoluntarioRegistrandoCod(Long voluntarioRegistrandoCod) {
        this.voluntarioRegistrandoCod = voluntarioRegistrandoCod;
    }

    public Long getAnimalCod() {
        return animalCod;
    }

    public void setAnimalCod(Long animalCod) {
        this.animalCod = animalCod;
    }

    public Long getVacinaCod() {
        return vacinaCod;
    }

    public void setVacinaCod(Long vacinaCod) {
        this.vacinaCod = vacinaCod;
    }

    public Long getVoluntarioAplicadorCod() {
        return voluntarioAplicadorCod;
    }

    public void setVoluntarioAplicadorCod(Long voluntarioAplicadorCod) {
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
    }

    public Date getDataAplicacaoAnimal() {
        return dataAplicacaoAnimal;
    }

    public void setDataAplicacaoAnimal(Date dataAplicacaoAnimal) {
        this.dataAplicacaoAnimal = dataAplicacaoAnimal;
    }

    public Double getDoseNumericaAplicada() {
        return doseNumericaAplicada;
    }

    public void setDoseNumericaAplicada(Double doseNumericaAplicada) {
        this.doseNumericaAplicada = doseNumericaAplicada;
    }

    public String getObservacaoDaAplicacao() {
        return observacaoDaAplicacao;
    }

    public void setObservacaoDaAplicacao(String observacaoDaAplicacao) {
        this.observacaoDaAplicacao = observacaoDaAplicacao;
    }
}
