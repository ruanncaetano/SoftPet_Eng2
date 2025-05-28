package SoftPet.backend.dto;

import java.util.Date;

public class VacinacaoRequestDTO {

    private Long voluntarioRegistrandoCod;
    private Long animalCod;
    private Long vacinaCod;
    private Date dataAplicacaoAnimal;
    private Integer doseNumericaAplicada;
    private Long voluntarioAplicadorCod;
    private String observacaoDaAplicacao;

    public VacinacaoRequestDTO() {
    }

    public VacinacaoRequestDTO(Long voluntarioRegistrandoCod, Long animalCod, Long vacinaCod,
                               Date dataAplicacaoAnimal, Integer doseNumericaAplicada,
                               Long voluntarioAplicadorCod, String observacaoDaAplicacao) {
        this.voluntarioRegistrandoCod = voluntarioRegistrandoCod;
        this.animalCod = animalCod;
        this.vacinaCod = vacinaCod;
        this.dataAplicacaoAnimal = dataAplicacaoAnimal;
        this.doseNumericaAplicada = doseNumericaAplicada;
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
        this.observacaoDaAplicacao = observacaoDaAplicacao;
    }

    // Getters e Setters para Long e outros tipos
    public Long getVoluntarioRegistrandoCod() {
        return voluntarioRegistrandoCod;
    }

    public void setVoluntarioRegistrandoCod(Long voluntarioRegistrandoCod) {
        this.voluntarioRegistrandoCod = voluntarioRegistrandoCod;
    }

    public int getAnimalCod() {
        return Math.toIntExact(animalCod);
    }

    public void setAnimalCod(Long animalCod) {
        this.animalCod = animalCod;
    }

    public int getVacinaCod() {
        return Math.toIntExact(vacinaCod);
    }

    public void setVacinaCod(Long vacinaCod) {
        this.vacinaCod = vacinaCod;
    }

    public Date getDataAplicacaoAnimal() {
        return dataAplicacaoAnimal;
    }

    public void setDataAplicacaoAnimal(Date dataAplicacaoAnimal) {
        this.dataAplicacaoAnimal = dataAplicacaoAnimal;
    }

    public Integer getDoseNumericaAplicada() {
        return doseNumericaAplicada;
    }

    public void setDoseNumericaAplicada(Integer doseNumericaAplicada) {
        this.doseNumericaAplicada = doseNumericaAplicada;
    }

    public Long getVoluntarioAplicadorCod() {
        return voluntarioAplicadorCod;
    }

    public void setVoluntarioAplicadorCod(Long voluntarioAplicadorCod) {
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
    }

    public String getObservacaoDaAplicacao() {
        return observacaoDaAplicacao;
    }

    public void setObservacaoDaAplicacao(String observacaoDaAplicacao) {
        this.observacaoDaAplicacao = observacaoDaAplicacao;
    }
}