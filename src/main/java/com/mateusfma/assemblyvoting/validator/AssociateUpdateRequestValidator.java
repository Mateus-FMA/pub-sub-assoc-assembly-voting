package com.mateusfma.assemblyvoting.validator;

import com.mateusfma.assemblyvoting.router.rest.request.AssociateUpdateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssociateUpdateRequestValidator implements Validator {
    private static final Pattern CPF_PATTERN;
    private static final Pattern NAME_PATTERN;

    static {
        CPF_PATTERN = Pattern.compile("[\\d]{11}");
        NAME_PATTERN = Pattern.compile("[\\w\\s]+");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AssociateUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AssociateUpdateRequest request = (AssociateUpdateRequest) target;

        if (request.getId() == null)
            errors.rejectValue("id", "field.required", "ID do associado não foi informado.");

        if (request.getCpf() != null) {
            Matcher cpfMatcher = CPF_PATTERN.matcher(request.getCpf());
            if (!cpfMatcher.matches())
                errors.rejectValue("cpf", "field.format", "Formato do CPF inválido.");
        }

        if (request.getName() != null) {
            Matcher nameMatcher = NAME_PATTERN.matcher(request.getName());
            if (request.getName().length() > 50)
                errors.rejectValue("name", "field.min.length",
                        "Nome do associado possui mais de 50 caracteres.");

            if (!nameMatcher.matches())
                errors.rejectValue("name", "field.format",
                        "Nome do associado aceita apenas caracteres do alfabeto.");
        }

        if (request.getAge() != null && request.getAge() <= 0)
            errors.rejectValue("age", "field.invalid", "Idade não pode ser negativa.");
    }
}
