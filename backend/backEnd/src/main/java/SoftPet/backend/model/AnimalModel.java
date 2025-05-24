package SoftPet.backend.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class AnimalModel {
    private int cod;
    private boolean ativo;
    private String nome;
    private int idade;
    private String tipo;
    private String sexo;
    private String porte;
    private String raca;
    private String pelagem;
    private int peso;
    private String baia;
    private Date dt_resgate;
    private boolean disp_adocao;
    private boolean castrado;
    private byte[] foto;
    private String observacao;

    public AnimalModel(){}
    public AnimalModel (int cod,String nome, int idade, String tipo, String sexo,String porte, String raca,
                        String pelagem, int peso,String baia, Date dt_resgate,
                        boolean disp_adocao,boolean castrado, String observacao, boolean ativo)
    {
        this.cod = cod;
        this.nome = nome;
        this.idade = idade;
        this.tipo = tipo;
        this.sexo = sexo;
        this.porte = porte;
        this.raca = raca;
        this.pelagem = pelagem;
        this.baia = baia;
        this.dt_resgate = dt_resgate;
        this.disp_adocao = disp_adocao;
        this.foto = foto; // esta sendo definida separedamente
        this.peso=peso;
        this.castrado=castrado;
        this.observacao=observacao;
        this.ativo=ativo;

    }
    public AnimalModel( String nome, int idade, String tipo, String sexo,String porte, String raca,
                       String pelagem, int peso,String baia, Date dt_resgate,
                        boolean disp_adocao,boolean castrado, String observacao, boolean ativo) {

        this.nome = nome;
        this.idade = idade;
        this.tipo = tipo;
        this.sexo = sexo;
        this.porte = porte;
        this.raca = raca;
        this.pelagem = pelagem;
        this.baia = baia;
        this.dt_resgate = dt_resgate;
        this.disp_adocao = disp_adocao;
        this.foto = foto; // esta sendo definida separedamente
        this.peso=peso;
        this.castrado=castrado;
        this.observacao=observacao;
        this.ativo=ativo;

    }

    public AnimalModel(int cod,String nome,int idade, int peso, String baia, boolean adocao, boolean castrado, String obs, boolean ativo) {
        this.cod=cod;
        this.idade=idade;
        this.nome=nome;
    this.peso=peso;
    this.baia=baia;
    this.ativo=ativo;
    this.disp_adocao=adocao;
    this.foto=new byte[0];
    this.castrado=castrado;
    this.observacao=obs;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    public String getPelagem() {
        return pelagem;
    }

    public void setPelagem(String pelagem) {
        this.pelagem = pelagem;
    }

    public String getBaia() {
        return baia;
    }

    public void setBaia(String baia) {
        this.baia = baia;
    }

    public boolean getDisp_adocao() {
        return disp_adocao;
    }

    public void setDisp_adocao(boolean disp_adocao) {
        this.disp_adocao = disp_adocao;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Date getDt_resgate() {
        return dt_resgate;
    }

    public void setDt_resgate(Date dt_resgate) {
        this.dt_resgate = dt_resgate;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean getCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }
}
