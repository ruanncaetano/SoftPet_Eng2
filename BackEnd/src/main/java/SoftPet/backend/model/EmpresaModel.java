package SoftPet.backend.model;

import java.time.LocalDateTime;
import java.util.InputMismatchException;

public class EmpresaModel {
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

    public EmpresaModel() {
        this(null, "", "", "", "", "", "", "", "", "", "", "");
    }

    public EmpresaModel(String nome, String razaoSocial, String cnpj, String logoPequena,
                        String endereco, String bairro, String cidade, String uf,
                        String diretor, String site, String telefone) {
        this(null, nome, razaoSocial, cnpj, logoPequena, endereco, bairro, cidade, uf, diretor, site, telefone);
    }

    public EmpresaModel(Long id, String nome, String razaoSocial, String cnpj, String logoPequena,
                        String endereco, String bairro, String cidade, String uf,
                        String diretor, String site, String telefone) {
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

    public static boolean isCNPJ(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return (false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char) ((11 - r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String imprimeCNPJ(String CNPJ) {
        // máscara do CNPJ: 99.999.999.9999-99
        try {
            return (CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." +
                    CNPJ.substring(5, 8) + "." + CNPJ.substring(8, 12) + "-" +
                    CNPJ.substring(12, 14));
        } catch (Exception e) {
            return "false";
        }

    }
}
