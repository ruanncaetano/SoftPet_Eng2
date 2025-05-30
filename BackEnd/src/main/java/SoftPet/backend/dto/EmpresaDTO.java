package SoftPet.backend.dto;

import SoftPet.backend.model.EmpresaModel;

import java.time.LocalDateTime;

public class EmpresaDTO {
    private Long id;
    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String logoPequena;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String diretor;
    private String site;
    private String telefone;

    public EmpresaDTO() {}

    public EmpresaDTO(EmpresaModel model) {
        this.id = model.getId();
        this.nome = model.getNome();
        this.razaoSocial = model.getRazaoSocial();
        this.cnpj = model.getCnpj();
        this.logoPequena = model.getLogoPequena();
        this.endereco = model.getEndereco();
        this.bairro = model.getBairro();
        this.cidade = model.getCidade();
        this.uf = model.getUf();
        this.diretor = model.getDiretor();
        this.site = model.getSite();
        this.telefone = model.getTelefone();
    }

    public EmpresaDTO(Long id, String nome, String razaoSocial, String cnpj, String logoPequena,
                      String endereco, String bairro, String cidade, String uf, String diretor, String site, String telefone) {
        this.id = id;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.logoPequena = logoPequena;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.diretor = diretor;
        this.site = site;
        this.telefone = telefone;
    }


    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getLogoPequena() { return logoPequena; }
    public void setLogoPequena(String logoPequena) { this.logoPequena = logoPequena; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getDiretor() { return diretor; }
    public void setDiretor(String diretor) { this.diretor = diretor; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
