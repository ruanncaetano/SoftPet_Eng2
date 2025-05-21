package SoftPet.backend.model;

public class PessoaModel
{
    private int id;
    private String cpf;
    private String nome;
    private Boolean status;
    private String profissao;
    private Long id_contato;
    private Long id_endereco;
    private String rg;

    public PessoaModel(int id, String cpf, String nome, Boolean status, String profissao, Long id_contato, Long id_endereco, String rg)
    {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.status = status;
        this.profissao = profissao;
        this.id_contato = id_contato;
        this.id_endereco = id_endereco;
        this.rg = rg;
    }
    public PessoaModel(String cpf, String nome, Boolean status, String profissao, Long id_contato, Long id_endereco, String rg)
    {
        this.cpf = cpf;
        this.nome = nome;
        this.status = status;
        this.profissao = profissao;
        this.id_contato = id_contato;
        this.id_endereco = id_endereco;
        this.rg = rg;
    }
    public PessoaModel()
    {
        this(0,"","",false,"",null,null,"");
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getProfissao() {
        return profissao;
    }
    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public Long getId_contato() {
        return id_contato;
    }
    public void setId_contato(Long id_contato) {
        this.id_contato = id_contato;
    }

    public Long getId_endereco() {
        return id_endereco;
    }
    public void setId_endereco(Long id_endereco) {
        this.id_endereco = id_endereco;
    }

    public String getRg() {
        return rg;
    }
    public void setRg(String rg) {
        this.rg = rg;
    }
}