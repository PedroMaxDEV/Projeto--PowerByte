package org.example;

public class Verificar {
    public static boolean estoqueNegativo(int estoque) {
        return estoque < 0;
    }
    public static boolean CPFInvalido(String CPF) {
        if (CPF.length() != 16) 
            return false;
        if (CPF.charAt(3) != '.' || CPF.charAt(7) != '.' || CPF.charAt(11) != '-')
            return false;
        for(int i = 0; CPF.charAt(i) != '\n'; i++) {
            if(i == 3 || i == 7 || i == 11) 
                continue;
            if(CPF.charAt(i) < '0' && CPF.charAt(i) > '9')
                return false;
        }
        return true;
    }
}
