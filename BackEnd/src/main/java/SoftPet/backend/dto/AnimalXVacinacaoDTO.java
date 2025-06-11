package SoftPet.backend.dto;

import java.time.LocalDate;
import java.util.Date;

public class AnimalXVacinacaoDTO {
    private Long codAnimal;
    private Long codVacina;
    private Date dataAplicacao;
    private Long codAplicdor;
    private Double dose;
    private String descricao;
    // getters e setters


    public AnimalXVacinacaoDTO() {
    }

    public AnimalXVacinacaoDTO(Long codAnimal, Long codVacina, Date dataAplicacao, Long codAplicdor, String descricao) {
        this.codAnimal = codAnimal;
        this.codVacina = codVacina;
        this.dataAplicacao = dataAplicacao;
        this.codAplicdor = codAplicdor;
        this.descricao = descricao;
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public Long getCodAnimal() {
        return codAnimal;
    }

    public void setCodAnimal(Long codAnimal) {
        this.codAnimal = codAnimal;
    }

    public Long getCodVacina() {
        return codVacina;
    }

    public void setCodVacina(Long codVacina) {
        this.codVacina = codVacina;
    }

    public Date getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(Date dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public Long getCodAplicdor() {
        return codAplicdor;
    }

    public void setCodAplicdor(Long codAplicdor) {
        this.codAplicdor = codAplicdor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
