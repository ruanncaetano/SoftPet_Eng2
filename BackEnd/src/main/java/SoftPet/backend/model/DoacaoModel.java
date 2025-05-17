package SoftPet.backend.model;

public class DoacaoModel
{
    private Long id;
    private String tipo;
    private int qtde;
    private String nome;
    private Long id_doador;

    public DoacaoModel(Long id, String tipo, int qtde, String nome, Long id_doador)
    {
        this.id = id;
        this.tipo = tipo;
        this.qtde = qtde;
        this.nome = nome;
        this.id_doador = id_doador;
    }
    public DoacaoModel(String tipo, int qtde, String nome, Long id_doador)
    {
        this.tipo = tipo;
        this.qtde = qtde;
        this.nome = nome;
        this.id_doador = id_doador;
    }
    public DoacaoModel()
    {
        this(0L,"",0,"",null);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQtde() {
        return qtde;
    }
    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId_doador() {
        return id_doador;
    }
    public void setId_doador(Long id_doador) {
        this.id_doador = id_doador;
    }
}
