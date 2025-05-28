package SoftPet.backend.model;

import java.util.Date;

public class AnimalXVacinacaoModel {

    private Long animalCod;                // Mapeia para ANIMAIS_AN_COD
    private Long vacinaCod;                // Mapeia para VACINAS_VA_COD
    private Integer doseAplicadaNumero;   // Mapeia para VAC_DOSE INTEGER (Integer é bom aqui, pois não é um ID de entidade e pode ser nulo)
    private Date dataAplicacaoEfetiva;    // Mapeia para AXV_DT_APLICACAO DATE
    private Long voluntarioAplicadorCod; // Mapeia para AXV_VOL_COD_APLICADOR
    private String observacao;            // Mapeia para AXV_OBSERVACAO VARCHAR

    /**
     * Construtor padrão.
     */
    public AnimalXVacinacaoModel() {
    }

    /**
     * Construtor com todos os campos.
     * animalCod: Código do animal.
     * vacinaCod: Código da vacina.
     * doseAplicadaNumero: Dose numérica da vacina aplicada.
     * dataAplicacaoEfetiva: Data em que a vacina foi efetivamente aplicada no animal.
     * voluntarioAplicadorCod: Código do voluntário que aplicou a vacina.
     * observacao: Observações ou motivo da aplicação.
     */
    public AnimalXVacinacaoModel(Long animalCod, Long vacinaCod, Integer doseAplicadaNumero,
                                 Date dataAplicacaoEfetiva, Long voluntarioAplicadorCod, String observacao) {
        this.animalCod = animalCod;
        this.vacinaCod = vacinaCod;
        this.doseAplicadaNumero = doseAplicadaNumero;
        this.dataAplicacaoEfetiva = dataAplicacaoEfetiva;
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
        this.observacao = observacao;
    }

    // Getters e Setters consistentes com Long para os IDs

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

    public Integer getDoseAplicadaNumero() {
        return doseAplicadaNumero;
    }

    public void setDoseAplicadaNumero(Integer doseAplicadaNumero) {
        this.doseAplicadaNumero = doseAplicadaNumero;
    }

    public Date getDataAplicacaoEfetiva() {
        return dataAplicacaoEfetiva;
    }

    public void setDataAplicacaoEfetiva(Date dataAplicacaoEfetiva) {
        this.dataAplicacaoEfetiva = dataAplicacaoEfetiva;
    }

    public Long getVoluntarioAplicadorCod() {
        return voluntarioAplicadorCod;
    }

    public void setVoluntarioAplicadorCod(Long voluntarioAplicadorCod) {
        this.voluntarioAplicadorCod = voluntarioAplicadorCod;
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
                "animalCod=" + animalCod +
                ", vacinaCod=" + vacinaCod +
                ", doseAplicadaNumero=" + doseAplicadaNumero +
                ", dataAplicacaoEfetiva=" + dataAplicacaoEfetiva +
                ", voluntarioAplicadorCod=" + voluntarioAplicadorCod +
                ", observacao='" + (observacao != null ? observacao : "N/A") + '\'' +
                '}';
    }
}