package SoftPet.backend.dto;

import java.util.Date;
// Se for usar anotações de validação do Spring, descomente os imports abaixo
// e adicione a dependência 'spring-boot-starter-validation' ao seu pom.xml ou build.gradle.
// import jakarta.validation.constraints.NotBlank; // Para Spring Boot 3+
// import jakarta.validation.constraints.NotNull;  // Para Spring Boot 3+
// import jakarta.validation.constraints.Size;     // Para Spring Boot 3+
// Se estiver usando Spring Boot 2.x, seria javax.validation.constraints.*

public class VacinaRequestDTO {

    // @NotBlank(message = "O nome da vacina é obrigatório.")
    // @Size(max = 100, message = "O nome da vacina não pode exceder 100 caracteres.")
    private String nome;

    // @Size(max = 255, message = "A descrição da vacina não pode exceder 255 caracteres.")
    private String descricao; // Descrição é opcional

    // @NotNull(message = "A data de referência do lote é obrigatória.")
    private Date dataReferenciaLote; // Corresponde a VA_DT_APLICACAO no banco

    // @NotBlank(message = "O tipo/dose padrão da vacina é obrigatório.")
    // @Size(max = 20, message = "O tipo/dose padrão não pode exceder 20 caracteres.")
    private String tipoDosePadrao;  // Corresponde a VA_DOSE no banco

    /**
     * Construtor padrão.
     */
    public VacinaRequestDTO() {
    }

    /**
     * Construtor com todos os campos.
     * nome: Nome da vacina.
     * descricao: Descrição da vacina.
     * dataReferenciaLote: Data referente ao lote ou tipo da vacina.
     * tipoDosePadrao: Tipo ou dose padrão da vacina.
     */
    public VacinaRequestDTO(String nome, String descricao, Date dataReferenciaLote, String tipoDosePadrao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataReferenciaLote = dataReferenciaLote;
        this.tipoDosePadrao = tipoDosePadrao;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataReferenciaLote() {
        return dataReferenciaLote;
    }

    public void setDataReferenciaLote(Date dataReferenciaLote) {
        this.dataReferenciaLote = dataReferenciaLote;
    }

    public String getTipoDosePadrao() {
        return tipoDosePadrao;
    }

    public void setTipoDosePadrao(String tipoDosePadrao) {
        this.tipoDosePadrao = tipoDosePadrao;
    }
}