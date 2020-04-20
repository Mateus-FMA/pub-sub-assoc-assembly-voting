package com.mateusfma.assemblyvoting.validator;

import com.mateusfma.assemblyvoting.router.rest.request.AssociateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssociateRequestValidator implements Validator {

    private static final Pattern CPF_PATTERN;
    private static final Pattern NAME_PATTERN;

    static {
        CPF_PATTERN = Pattern.compile("[\\d]{11}");
        NAME_PATTERN = Pattern.compile("[\\w\\s]+");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AssociateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "cpf", "field.required");
        ValidationUtils.rejectIfEmpty(errors, "name", "field.required");
        ValidationUtils.rejectIfEmpty(errors, "age", "field.required");

        AssociateRequest request = (AssociateRequest) target;

        Matcher cpfMatcher = CPF_PATTERN.matcher(request.getCpf());
        if (!cpfMatcher.matches())
            errors.rejectValue("cpf", "field.format", "Formato do CPF inválido.");

        Matcher nameMatcher = NAME_PATTERN.matcher(request.getName());
        if (request.getName().length() > 50)
            errors.rejectValue("name", "field.min.length",
                    "Nome do associado possui mais de 50 caracteres.");

        if (!nameMatcher.matches())
            errors.rejectValue("name", "field.format",
                    "Nome do associado aceita apenas caracteres do alfabeto.");

        if (request.getAge() == null)
            errors.rejectValue("age", "field.required", "Idade deve ser informada.");

        if (request.getAge() <= 0)
            errors.rejectValue("age", "field.invalid", "Idade não pode ser negativa.");
    }
}
