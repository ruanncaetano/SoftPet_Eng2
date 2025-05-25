package SoftPet.backend.model;

public class VoluntarioModel {
    private int id;
    private String cpf;
    private String nome;
    private int cargoCod;
    private int contatoCod;
    private int credenciaisCod;
    private CredenciaisModel credenciais;
    private ContatoModel contato;
    private CargoModel cargo;


    // Construtor completo com objetos (útil para retorno com JOINs)
    public VoluntarioModel(int id, String cpf, String nome, int cargoCod, int contatoCod, CredenciaisModel credenciais, ContatoModel contato) {
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
    public VoluntarioModel(int id, String cpf, String nome, int cargoCod, int contatoCod, int credenciaisCod) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.cargoCod = cargoCod;
        this.contatoCod = contatoCod;
        this.credenciaisCod = credenciaisCod;
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

    public int getCargoCod() {
        return cargoCod != 0 ? cargoCod : (cargo != null ? cargo.getId() : 0);
    }

    public void setCargo(CargoModel cargo) {
        this.cargo = cargo;
        if (cargo != null) {
            this.cargoCod = cargo.getId();
        }
    }

    public int getContatoCod() {
        return contatoCod;
    }

    public void setContatoCod(int contatoCod) {
        this.contatoCod = contatoCod;
    }

    public int getCredenciaisCod() {
        return credenciaisCod;
    }

    public void setCredenciaisCod(int credenciaisCod) {
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
    public void setCargoCod(int cargoCod) {
        this.cargoCod = cargoCod;
        if (this.cargo == null) {
            this.cargo = new CargoModel();
        }
        this.cargo.setId(cargoCod);
    }

}
