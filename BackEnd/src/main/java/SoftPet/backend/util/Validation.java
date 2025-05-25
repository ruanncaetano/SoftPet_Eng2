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

    // Verifica se a data de validade é futura
    public static boolean validarDataValidade(LocalDate dataValidade) {
        if (dataValidade == null)
            return false;
        return dataValidade.isAfter(LocalDate.now());
    }

    // Valida unidade de medida (ex: kg, litros, pacotes etc.)
    public static boolean validarUnidadeMedida(String unidade) {
        return unidade != null && !unidade.trim().isEmpty();
    }



}
