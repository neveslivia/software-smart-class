package org.example.smart.Validador;

import java.util.regex.Pattern;

public class ValidadorGERALALUNO {

    public static boolean validarEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        return Pattern.matches(regex, email);
    }

    public static boolean validarTelefone(String telefone) {
        String regex = "\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}";
        return Pattern.matches(regex, telefone);
    }

    public static boolean validarCPF(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.matches("(\\d)\\1{10}")) return false;

        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0, digito1, digito2;

        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * pesos1[i];
        digito1 = 11 - (soma % 11);
        if (digito1 > 9) digito1 = 0;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * pesos2[i];
        digito2 = 11 - (soma % 11);
        if (digito2 > 9) digito2 = 0;

        return cpf.charAt(9) - '0' == digito1 && cpf.charAt(10) - '0' == digito2;
    }
}