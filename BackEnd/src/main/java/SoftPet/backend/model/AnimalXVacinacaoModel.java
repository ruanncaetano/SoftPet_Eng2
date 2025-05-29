package SoftPet.backend.model;

import java.util.Date; // Usaremos java.util.Date para alinhar com o tipo DATE do SQL via JDBC

public class AnimalXVacinacaoModel {
    private Long axvCod; // Chave primária do registro da aplicação (AXV_COD)
    private Long animalCod; // FK para ANIMAIS(AN_COD)
    private Long vacinaCod; // FK para VACINAS(VA_COD)
    // private Long voluntarioRegistrandoCod; // Opcional: FK para VOLUNTARIO(VOL_COD) - quem registrou
    private Long voluntarioAplicadorCod; // FK para VOLUNTARIO(VOL_COD) - quem aplicou
    private Date dataAplicacao; // Data da aplicação (AXV_DATA_APLICACAO)
    private Double doseNumerica; // Dose numérica, se aplicável (AXV_DOSE_NUMERICA). Use Double para flexibilidade (ex: 0.5ml)
    private String observacao; // Observações (AXV_OBSERVACAO)

    public AnimalXVacinacaoModel() {
    }

    public AnimalXVacinacaoModel(Long axvCod, Long animalCod, Long vacinaCod, Long voluntarioAplicadorCod, Date dataAplicacao, Double doseNumerica, String observacao) {
        this.axvCod = axvCod;
        this.animalCod = animalCod;
        this.vacinaCod = vacinaCod;
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
        this.dataAplicacao = dataAplicacao;
        this.doseNumerica = doseNumerica;
        this.observacao = observacao;
    }

    // Getters e Setters
    public Long getAxvCod() {
        return axvCod;
    }

    public void setAxvCod(Long axvCod) {
        this.axvCod = axvCod;
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

    public Date getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(Date dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public Double getDoseNumerica() {
        return doseNumerica;
    }

    public void setDoseNumerica(Double doseNumerica) {
        this.doseNumerica = doseNumerica;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "AnimalXVacinacaoModel{" +
                "axvCod=" + axvCod +
                ", animalCod=" + animalCod +
                ", vacinaCod=" + vacinaCod +
                ", voluntarioAplicadorCod=" + voluntarioAplicadorCod +
                ", dataAplicacao=" + dataAplicacao +
                ", doseNumerica=" + doseNumerica +
                ", observacao='" + observacao + '\'' +
                '}';
    }
}
