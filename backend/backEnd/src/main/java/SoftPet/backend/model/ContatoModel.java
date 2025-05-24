package SoftPet.backend.model;

public class ContatoModel
{
    private Long id;
    private String telefone;

    public ContatoModel(Long id, String telefone)
    {
        this.id = id;
        this.telefone = telefone;
    }
    public ContatoModel(String telefone)
    {
        this.telefone = telefone;
    }
    public ContatoModel()
    {
        this(0L,"");
    }

    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTelefone()
    {
        return telefone;
    }
    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }
}