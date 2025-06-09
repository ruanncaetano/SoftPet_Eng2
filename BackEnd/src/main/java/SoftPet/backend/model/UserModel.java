package SoftPet.backend.model;

public class UserModel
{
    private Long id;
    private String cpf;
    private String senha;

    public UserModel(Long id, String cpf, String senha)
    {
        this.id = id;
        this.cpf = cpf;
        this.senha = senha;
    }

    public UserModel(String cpf, String senha)
    {
        this.cpf = cpf;
        this.senha = senha;
    }

    public UserModel()
    {
        this(0L, "", "");
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

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
