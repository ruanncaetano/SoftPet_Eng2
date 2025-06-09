package SoftPet.backend.model;

import java.time.LocalDate; // Usaremos LocalDate para ado_dt
// Remova import java.util.Date; se não for mais necessário
import java.util.Arrays; // Para o toString do byte[]

public class AdocaoModel {
    private Long ado_cod;           // Para ADO_COD INTEGER
    private LocalDate ado_dt;       // Para ADO_DT DATE, usando LocalDate consistentemente
    private byte[] ado_contrato;    // Para ADO_CONTRATO BYTEA/VARBINARY(MAX)
    private Long pe_cod;            // Para PE_COD INTEGER (FK Pessoa)
    private Long an_cod;            // Para AN_COD INTEGER (FK Animal)

    /**
     * Construtor padrão.
     */
    public AdocaoModel() {
    }

    /**
     * Construtor principal com todos os campos usando os tipos corretos da classe.
     * ado_cod: Código da adoção.
     * ado_dt: Data da adoção (LocalDate).
     * ado_contrato: Arquivo do contrato em bytes.
     * pe_cod: Código da pessoa (adotante).
     * an_cod: Código do animal adotado.
     */
    public AdocaoModel(Long ado_cod, LocalDate ado_dt, byte[] ado_contrato, Long pe_cod, Long an_cod) {
        this.ado_cod = ado_cod;
        this.ado_dt = ado_dt; // Atribuição direta de LocalDate
        this.ado_contrato = ado_contrato;
        this.pe_cod = pe_cod;
        this.an_cod = an_cod;
    }

    /**
     * Construtor alternativo.
     * Nota: Os nomes dos parâmetros foram mantidos como no seu exemplo, mas idealmente
     * seguiriam o padrão da classe (ex: ado_dt em vez de adoDt).
     * Os tipos foram ajustados para corresponder aos campos da classe.
     * Se anCod do banco é INTEGER, e no modelo PessoaModel o id é Long, etc.
     */


    // --- Getters e Setters ---

    public Long getAdo_cod() {
        return ado_cod;
    }

    public void setAdo_cod(Long ado_cod) {
        this.ado_cod = ado_cod;
    }

    // Getter e Setter para ado_dt usando LocalDate consistentemente
    public LocalDate getAdo_dt() {
        return ado_dt;
    }

    public void setAdo_dt(LocalDate ado_dt) {
        this.ado_dt = ado_dt;
    }

    public byte[] getAdo_contrato() {
        return ado_contrato;
    }

    public void setAdo_contrato(byte[] ado_contrato) {
        this.ado_contrato = ado_contrato;
    }

    public Long getPe_cod() {
        return pe_cod;
    }

    public void setPe_cod(Long pe_cod) {
        this.pe_cod = pe_cod;
    }

    public Long getAn_cod() {
        return an_cod;
    }

    public void setAn_cod(Long an_cod) {
        this.an_cod = an_cod;
    }

    @Override
    public String toString() {
        return "AdocaoModel{" +
                "ado_cod=" + ado_cod +
                ", ado_dt=" + ado_dt +
                ", ado_contrato=" + (ado_contrato != null ? "Presente (" + ado_contrato.length + " bytes)" : "Nulo") +
                ", pe_cod=" + pe_cod +
                ", an_cod=" + an_cod +
                '}';
    }
}