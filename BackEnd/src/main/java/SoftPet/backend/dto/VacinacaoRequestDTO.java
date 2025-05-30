package SoftPet.backend.dto;

import java.util.Date;
// Se for usar anotações de validação do Spring, descomente os imports abaixo
// e adicione a dependência 'spring-boot-starter-validation' ao seu pom.xml ou build.gradle.
// import jakarta.validation.constraints.NotNull;  // Para Spring Boot 3+
// import jakarta.validation.constraints.PastOrPresent; // Para Spring Boot 3+
// import jakarta.validation.constraints.Positive; // Para Spring Boot 3+
// Se estiver usando Spring Boot 2.x, seria javax.validation.constraints.*

public class VacinacaoRequestDTO {

    private Long voluntarioRegistrandoCod;
    private Long animalCod;
    private Long vacinaCod;
    private Long voluntarioAplicadorCod;
    private Date dataAplicacaoAnimal;
    private Double doseNumericaAplicada;
    private String observacaoDaAplicacao;

    public VacinacaoRequestDTO() {
    }

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
