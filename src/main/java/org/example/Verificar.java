package org.example;

public class Verificar {
    public static boolean CPFInvalido(String CPF) {
        if (CPF.length() != 14) 
            return true;
        if (CPF.charAt(3) != '.' || CPF.charAt(7) != '.' || CPF.charAt(11) != '-')
            return true;
        for(int i = 0; i < CPF.length(); i++) {
            if(i == 3 || i == 7 || i == 11) 
                continue;
            if(CPF.charAt(i) < '0' && CPF.charAt(i) > '9')
                return true;
        }
        return false;
    }
}
