package com.mateusfma.assemblyvoting.validator;

import com.mateusfma.assemblyvoting.router.rest.request.CreateTopicRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CreateTopicRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateTopicRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");

        CreateTopicRequest request = (CreateTopicRequest) target;
        if (request.getName().length() > 30)
            errors.rejectValue("name", "field.min.length",
                    "Nome do tópico não pode ultrapassar 30 caracteres.");
    }
}
