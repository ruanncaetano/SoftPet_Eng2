package SoftPet.backend.model;

public class VoluntarioModel {
    private Long id;
    private String cpf;
    private String nome;
    private Long cargoCod;
    private Long contatoCod;
    private Long credenciaisCod;
    private CredenciaisModel credenciais;
    private ContatoModel contato;
    private CargoModel cargo;


    // Construtor completo com objetos (útil para retorno com JOINs)
    public VoluntarioModel(Long id, String cpf, String nome, Long cargoCod, Long contatoCod, CredenciaisModel credenciais, ContatoModel contato) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.cargoCod = cargoCod;
        this.contatoCod = contatoCod;
        this.credenciais = credenciais;
        this.contato = contato;
    }

    public VoluntarioModel() {

    }

    // Construtor com apenas os códigos (usado nas queries SQL do DAL)
    public VoluntarioModel(Long id, String cpf, String nome, Long cargoCod, Long contatoCod, Long credenciaisCod) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.cargoCod = cargoCod;
        this.contatoCod = contatoCod;
        this.credenciaisCod = credenciaisCod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getCargoCod() {
        return cargoCod != 0 ? cargoCod : (cargo != null ? cargo.getId() : 0);
    }

    public void setCargo(CargoModel cargo) {
        this.cargo = cargo;
        if (cargo != null) {
            this.cargoCod = cargo.getId();
        }
    }

    public Long getContatoCod() {
        return contatoCod;
    }

    public void setContatoCod(Long contatoCod) {
        this.contatoCod = contatoCod;
    }

    public Long getCredenciaisCod() {
        return credenciaisCod;
    }

    public void setCredenciaisCod(Long credenciaisCod) {
        this.credenciaisCod = credenciaisCod;
    }

    public CredenciaisModel getCredenciais() {
        return credenciais;
    }

    public void setCredenciais(CredenciaisModel credenciais) {
        this.credenciais = credenciais;
    }

    public ContatoModel getContato() {
        return contato;
    }

    public void setContato(ContatoModel contato) {
        this.contato = contato;
    }
    public String getCargoNome() {
        if (cargo != null) {
            return cargo.getNome();
        }
        return null; // ou "" dependendo do que preferir
    }

    public void setCargoNome(String nome) {
        if (this.cargo == null) {
            this.cargo = new CargoModel();
        }
        this.cargo.setNome(nome);
    }
    public void setCargoCod(Long cargoCod) {
        this.cargoCod = cargoCod;
        if (this.cargo == null) {
            this.cargo = new CargoModel();
        }
        this.cargo.setId(cargoCod);
    }

}
