package SoftPet.backend.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validation {

    // Valida campos de texto como tipo, nome ou descrição do produto
    public static boolean validarTextoProduto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    // Verifica se a quantidade é válida (zero ou positiva)
    public static boolean validarQuantidadeEstoque(int quantidade) {
        return quantidade >= 0;
    }

<<<<<<< HEAD
    // Verifica se a data de validade é futura
    public static boolean validarDataValidade(LocalDate dataValidade) {
=======
    public static boolean validarRG(String rg)
    {
        if(rg == null)
            return false;
        rg = rg.replaceAll("\\D", "");
        return rg.length() == 9;
    }

    public static boolean validarNomeCompleto(String nome)
    {
        if(nome == null || nome.trim().isEmpty())
            return false;

        String[] partes = nome.trim().split("\\s+");

        if(partes.length < 2)
            return false;

        for(String parte : partes)
        {
            if (!parte.matches("^[A-Za-zÀ-ÿ]+$"))
                return false;
        }
        return true;
    }

    public static boolean ValidarIdade(int idade) { return idade >=0;}

    public static boolean numNegativo(int num)
    {
        return num >= 0;
    }

    public static boolean validarStringDocao(String str)
    {
        if(str == null || str.trim().isEmpty())
            return false;
        return true;
    }

    public static boolean isDataValidade(LocalDate dataValidade)
    {
>>>>>>> main
        if (dataValidade == null)
            return false;
        return dataValidade.isAfter(LocalDate.now());
    }

    // Valida unidade de medida (ex: kg, litros, pacotes etc.)
    public static boolean validarUnidadeMedida(String unidade) {
        return unidade != null && !unidade.trim().isEmpty();
    }



}
