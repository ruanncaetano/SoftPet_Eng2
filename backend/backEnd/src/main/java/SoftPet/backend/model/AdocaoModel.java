package SoftPet.backend.model;

import org.springframework.stereotype.Service;

import java.util.Date;

public class AdocaoModel {
  private int ado_cod;
  private Date ado_dt;
  private byte[] contrato;
  private Long pe_cod;
  private int an_cod;

    public AdocaoModel() {
    }

    public AdocaoModel(int ado_cod, Date ado_dt, byte[] contrato, Long pe_cod, int an_cod) {
        this.ado_cod = ado_cod;
        this.ado_dt = ado_dt;
        this.contrato = contrato;
        this.pe_cod = pe_cod;
        this.an_cod = an_cod;
    }

    public int getAdo_cod() {
        return ado_cod;
    }

    public void setAdo_cod(int ado_cod) {
        this.ado_cod = ado_cod;
    }

    public int getAn_cod() {
        return an_cod;
    }

    public void setAn_cod(int an_cod) {
        this.an_cod = an_cod;
    }

    public Long getPe_cod() {
        return pe_cod;
    }

    public void setPe_cod(Long pe_cod) {
        this.pe_cod = pe_cod;
    }

    public Date getAdo_dt() {
        return ado_dt;
    }

    public void setAdo_dt(Date ado_dt) {
        this.ado_dt = ado_dt;
    }

    public byte[] getContrato() {
        return contrato;
    }

    public void setContrato(byte[] contrato) {
        this.contrato = contrato;
    }
}
