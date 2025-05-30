package SoftPet.backend.model;

import java.util.Date; // Para alinhar com java.sql.Date do JDBC

public class VacinaModel {
    private Long cod;                // Mapeia para VA_COD INTEGER (usando Long na camada Java)
    private String nome;            // Mapeia para VA_NOME VARCHAR(100)
    private String descricao;       // Mapeia para VA_DESC VARCHAR(255)
    private Date dataReferenciaLote; // Mapeia para VA_DT_APLICACAO DATE (data referente à vacina)
    private String tipoDosePadrao;  // Mapeia para VA_DOSE VARCHAR(20) (tipo/dose padrão da vacina)

    /**
     * Construtor padrão.
     */
    public VacinaModel() {
    }

    /**
     * Construtor com todos os campos.
     * cod: Código da vacina (VA_COD).
     * nome: Nome da vacina (VA_NOME).
     * descricao: Descrição da vacina (VA_DESC).
     * dataReferenciaLote: Data referente ao lote ou tipo da vacina (VA_DT_APLICACAO).
     * tipoDosePadrao: Tipo ou dose padrão da vacina (VA_DOSE).
     */
    public VacinaModel(Long cod, String nome, String descricao, Date dataReferenciaLote, String tipoDosePadrao) {
        this.cod = cod;
        this.nome = nome;
        this.descricao = descricao;
        this.dataReferenciaLote = dataReferenciaLote;
        this.tipoDosePadrao = tipoDosePadrao;
    }

    // Getters e Setters
    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

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

    @Override
    public String toString() {
        return "VacinaModel{" +
                "cod=" + cod +
                ", nome='" + nome + '\'' +
                ", descricao='" + (descricao != null ? descricao : "N/A") + '\'' +
                ", dataReferenciaLote=" + dataReferenciaLote +
                ", tipoDosePadrao='" + tipoDosePadrao + '\'' +
                '}';
    }
}
