package br.com.quale.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TelefoneValidator.class) // Aponta para a classe lógica
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Telefone { // Note o @ antes de interface
    String message() default "Número inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}