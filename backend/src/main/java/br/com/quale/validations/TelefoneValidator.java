package br.com.quale.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<Telefone, String> {

    @Override
    public void initialize(Telefone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 1. Se for nulo, consideramos válido.
        // A responsabilidade de checar nulidade deve ser da anotação @NotNull.
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // 2. Remove tudo que não for dígito (parênteses, traços, espaços)
        String numero = value.replaceAll("\\D", "");

        // 3. Verifica o tamanho (10 para fixo, 11 para celular)
        if (numero.length() < 10 || numero.length() > 11) {
            return false;
        }

        // 4. Verifica se o DDD é válido (11 a 99)
        // Pegamos os 2 primeiros dígitos
        int ddd = Integer.parseInt(numero.substring(0, 2));
        if (ddd < 11 || ddd > 99) {
            return false;
        }

        // 5. Validação Específica
        if (numero.length() == 11) {
            // É Celular? Deve começar com 9 após o DDD
            // Ex: 11 9 1234 5678
            return numero.charAt(2) == '9';
        } else {
            // É Fixo? O primeiro dígito após DDD deve ser entre 2 e 8
            // Ex: 11 3234 5678
            char primeiroDigito = numero.charAt(2);
            return primeiroDigito >= '2' && primeiroDigito <= '8';
        }
    }
}